package plugin

import com.google.auto.service.AutoService
import org.gradle.api.Project
import org.gradle.api.tasks.compile.AbstractCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonOptions
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinGradleSubplugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

@AutoService(KotlinGradleSubplugin::class)
class LibLSGradleSubplugin : KotlinGradleSubplugin<AbstractCompile> {

    override fun apply(
        project: Project,
        kotlinCompile: AbstractCompile,
        javaCompile: AbstractCompile?,
        variantData: Any?,
        androidProjectHandler: Any?,
        kotlinCompilation: KotlinCompilation<KotlinCommonOptions>?
    ): List<SubpluginOption> {
        val extension = project.extensions.findByType(LibSLGradleExtension::class.java) ?: LibSLGradleExtension()
        return listOf(SubpluginOption(key = "enabled", value = extension.enabled.toString()))
    }

    override fun getCompilerPluginId(): String = "libls"

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        groupId = "libls", artifactId = "kotlin-plugin", version = "0.0.1"
    )

    override fun isApplicable(project: Project, task: AbstractCompile) =
        project.plugins.hasPlugin(LibSLGradlePlugin::class.java)
}
