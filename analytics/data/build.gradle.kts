import org.gradle.kotlin.dsl.invoke

plugins {
    alias(libs.plugins.runapp.android.library)
    alias(libs.plugins.runapp.android.room)
}

android {
    namespace = "com.example.analytics.data"

}

dependencies {
    implementation(libs.kotlinx.coroutines.core)

    implementation(projects.core.database)
    implementation(projects.core.domain)
    implementation(projects.analytics.domain)

    implementation(libs.bundles.koin)
}