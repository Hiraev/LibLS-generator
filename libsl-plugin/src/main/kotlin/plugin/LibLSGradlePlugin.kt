package plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

// https://youtu.be/w-GMlaziIyo?t=1330

// TopLevel Gradle Plugin with configurable extansion
class LibSLGradlePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.extensions.create(
            "libsl",
            LibSLGradleExtension::class.java
        )
    }

}

open class LibSLGradleExtension {
    var enabled = true
}
