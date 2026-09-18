import com.android.build.api.dsl.ApplicationExtension
import com.example.convention.ExtensionType
import com.example.convention.configureBuildTypes
import com.example.convention.configureKotlinAndroid
import com.example.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.android.application")
            }
            extensions.configure<ApplicationExtension>{
              configureKotlinAndroid(this)
              defaultConfig{
                  applicationId = libs.findVersion("projectApplicationId").get().toString()
                  versionName = libs.findVersion("projectVersionName").get().toString()
                  versionCode = libs.findVersion("projectVersionCode").get().toString().toInt()

                  targetSdk = libs.findVersion("projectTargetSdkVersion").get().toString().toInt()


              }

              configureBuildTypes(this, ExtensionType.APPLICATION)
            }

        }

    }


}