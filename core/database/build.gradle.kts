plugins {
    alias(libs.plugins.runapp.android.library)
}

android {
    namespace = "com.example.core.database"
}

dependencies {
     implementation(libs.org.mongodb.bson)
    implementation(projects.core.domain)
}