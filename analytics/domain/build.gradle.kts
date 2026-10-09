plugins {

    alias(libs.plugins.runapp.jvm.library)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
dependencies{
    implementation(libs.kotlinx.coroutines.core)
}
