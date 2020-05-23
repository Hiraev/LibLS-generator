package ru.hiraev.libls.kotlinc.plugin

import kastree.ast.Node
import kastree.ast.psi.Converter
import org.jetbrains.kotlin.analyzer.AnalysisResult
import org.jetbrains.kotlin.com.intellij.openapi.project.Project
import org.jetbrains.kotlin.container.ComponentProvider
import org.jetbrains.kotlin.context.ProjectContext
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.resolve.BindingTrace
import org.jetbrains.kotlin.resolve.jvm.extensions.AnalysisHandlerExtension

/**
 * Alternative solution is implementation PreprocessedVirtualFileFactoryExtension
 * and override createPreprocessedFile function. In function createPreprocessedFile use
 * "org.jetbrains.kotlin.com.intellij.psiPsiManager.getInstance(proj).findFile(it) as KtFile",
 * proj is a val like in kastree.ast.psi.Parser
 */
class LibSLAnalysisHandlerExtension : AnalysisHandlerExtension {

    override fun doAnalysis(
            project: Project,
            module: ModuleDescriptor,
            projectContext: ProjectContext,
            files: Collection<KtFile>,
            bindingTrace: BindingTrace,
            componentProvider: ComponentProvider
    ): AnalysisResult? {
        parseFiles(files)
        return super.doAnalysis(project, module, projectContext, files, bindingTrace, componentProvider)
    }

    override fun analysisCompleted(
            project: Project,
            module: ModuleDescriptor,
            bindingTrace: BindingTrace,
            files: Collection<KtFile>
    ): AnalysisResult? {
        LibSLProjectProcessor.onCompleteAnalysis()
        return super.analysisCompleted(project, module, bindingTrace, files)
    }

    private fun parseFiles(files: Collection<KtFile>) {
        files.mapNotNull(::parseKtFile).let(LibSLProjectProcessor::addAllFiles)
    }

    private fun parseKtFile(ktFile: KtFile): Node.File? = try {
        Converter.convertFile(ktFile)
    } catch (t: Throwable) {
        // Message with exception may be written in a log file
        null
    }

}
