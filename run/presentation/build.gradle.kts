plugins {
    alias(libs.plugins.runapp.android.library.feature.ui)
}

android {
    namespace = "com.example.run.presentation"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.google.maps.android.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.timber)

    implementation(projects.run.domain)
    implementation(projects.core.domain)

}