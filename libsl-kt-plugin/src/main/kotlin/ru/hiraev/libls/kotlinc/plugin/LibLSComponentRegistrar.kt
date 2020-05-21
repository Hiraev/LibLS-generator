package ru.hiraev.libls.kotlinc.plugin

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.resolve.extensions.SyntheticResolveExtension
import org.jetbrains.kotlin.resolve.jvm.extensions.AnalysisHandlerExtension

@AutoService(ComponentRegistrar::class)
class LibLSComponentRegistrar : ComponentRegistrar {

    override fun registerProjectComponents(
        project: MockProject,
        configuration: CompilerConfiguration
    ) {
        SyntheticResolveExtension.registerExtension(
            project,
            LibLSSyntheticResolver()
        )
        AnalysisHandlerExtension.registerExtension(
            project,
            LibSLAnalysisHandlerExtension()
        )
    }

}
