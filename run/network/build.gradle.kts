plugins {
    alias(libs.plugins.runapp.android.library)
    alias(libs.plugins.runapp.jvm.ktor)
}

android {
    namespace = "com.example.run.network"
}

dependencies {
implementation(projects.core.domain)
implementation(projects.core.data)
}