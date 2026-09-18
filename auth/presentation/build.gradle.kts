plugins {

     alias (libs.plugins.runapp.android.library.feature.ui)
}

android {
    namespace = "com.example.auth.presentation"
}

dependencies {
   implementation(projects.auth.domain)
   implementation(projects.core.domain)
}