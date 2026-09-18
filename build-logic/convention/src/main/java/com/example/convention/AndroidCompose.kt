package com.example.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureCompose(commonExtension: CommonExtension){
    commonExtension.apply {
        buildFeatures.apply {
            compose = true
        }
        composeOptions.apply {
            kotlinCompilerExtensionVersion = libs
                .findVersion("composeCompiler")
                .get()
                .toString()
        }
        dependencies {
            val bom = libs.findLibrary("androidx.compose.bom").get()
            "implementation"(platform(bom))
            "androidTestImplementation"(platform(bom))
            "debugImplementation"(libs.findLibrary("androidx.compose.ui.tooling.preview").get())
             }
    }
}