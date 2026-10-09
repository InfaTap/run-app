package com.example.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.DynamicFeatureExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.Project

internal fun Project.configureBuildTypes(commonExtension: CommonExtension,
                                extensionType: ExtensionType) {


    commonExtension.apply {

        buildFeatures.apply {
            buildConfig = true
        }

        val apiKey = gradleLocalProperties(rootDir, providers = providers).getProperty("API_KEY")
            ?: error("API_KEY missing: add API_KEY=<your key> to local.properties")
        when(extensionType){
            ExtensionType.APPLICATION -> {
                (commonExtension as ApplicationExtension).apply {
                    buildTypes {
                        debug {
                            configureDebugBuildType(apiKey = apiKey)
                        }
                        release {
                            configureReleaseBuildType(apiKey = apiKey, commonExtension = commonExtension)
                        }
                    }
                }
            }
            ExtensionType.LIBRARY -> {
                (commonExtension as LibraryExtension).apply {
                    buildTypes {
                        debug {
                            configureDebugBuildType(apiKey = apiKey)
                        }
                        release {
                            configureReleaseBuildType(apiKey = apiKey, commonExtension = commonExtension)
                        }
                    }
                }
            }
            ExtensionType.DYNAMIC_FEATURE -> {
                (commonExtension as DynamicFeatureExtension).apply {
                    buildTypes {
                        debug {
                            configureDebugBuildType(apiKey = apiKey)
                        }
                        release {
                            configureReleaseBuildType(apiKey = apiKey, commonExtension = commonExtension)
                        }
                    }
                }
            }
        }

    }
}

private fun BuildType.configureDebugBuildType(apiKey: String) {
    buildConfigField("String", "API_KEY", "\"$apiKey\"")
    buildConfigField("String", "BASE_URL", "\"https://runique.pl-coding.com:8080\"")
}
private fun BuildType.configureReleaseBuildType(
    apiKey: String,
    commonExtension: CommonExtension) {

    buildConfigField("String", "API_KEY", "\"$apiKey\"")
    buildConfigField("String", "BASE_URL", "\"https://runique.pl-coding.com:8080\"")

    isMinifyEnabled = false
    proguardFiles(
        commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
    )
}
