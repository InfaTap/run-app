plugins {
    alias(libs.plugins.runapp.android.library)
    alias (libs.plugins.runapp.android.room)
}

android {
    namespace = "com.example.core.database"
}

dependencies {
     implementation(libs.org.mongodb.bson)
    implementation(projects.core.domain)
}