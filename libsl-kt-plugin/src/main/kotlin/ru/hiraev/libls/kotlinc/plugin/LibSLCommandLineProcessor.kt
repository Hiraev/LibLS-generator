package ru.hiraev.libls.kotlinc.plugin

//@AutoService(CommandLineProcessor::class)
//class LibSLCommandLineProcessor : CommandLineProcessor {
//
//    override val pluginId = "libls"
//
//    override val pluginOptions: Collection<AbstractCliOption> = listOf(
//        CliOption("enabled", "<true|false>", "whether plugin is enabled")
//    )
//
//    override fun processOption(
//        option: AbstractCliOption,
//        value: String,
//        configuration: CompilerConfiguration
//    ) = when (option.optionName) {
//        "enabled" -> configuration.put(KEY_ENABLED, value.toBoolean())
//        else -> error("Unexpected configuration option ${option.optionName}")
//    }
//
//}
