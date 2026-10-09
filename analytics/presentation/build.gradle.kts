plugins {
    alias(libs.plugins.runapp.android.library.feature.ui)
}

android {
    namespace = "com.example.analytics.presentation"


}

dependencies {
    implementation(projects.analytics.domain)
}