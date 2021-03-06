package net.propromp.neocommander.api.builder

import net.propromp.neocommander.api.NeoCommand
import net.propromp.neocommander.api.NeoCommandContext
import net.propromp.neocommander.api.NeoCommandSource
import net.propromp.neocommander.api.argument.NeoArgument
import org.bukkit.command.CommandSender

data class CommandBuilder(
    private val name: String,
    private val aliases: List<String> = listOf(),
    private val description: String = "",
    private val function: (NeoCommandContext) -> Int = { 0 },
    private val requirements: List<(NeoCommandSource) -> Boolean> = listOf(),
    private val arguments: List<NeoArgument<Any,Any>> = listOf(),
    private val children: List<NeoCommand> = listOf(),
    private val parallelCommands: List<NeoCommand> = listOf()
) {
    fun build() = NeoCommand(
        name,
        aliases,
        description,
        function,
        { source -> requirements.filterNot {
            it.invoke(source)
        }.isEmpty() },
        arguments.associateBy { it.name },
        children,
        parallelCommands
    )

    fun aliases(vararg aliases: String) = copy(aliases = aliases.toList())
    fun appendAliases(vararg aliases: String) = copy(
        name,
        this.aliases.toMutableList().apply { addAll(aliases) },
        description,
        function,
        requirements,
        arguments,
        children
    )

    fun description(description: String) = copy(description = description)
    fun executesWithReturn(function: (NeoCommandContext) -> Int) = copy(function = function)
    fun executes(function: (NeoCommandContext) -> Unit) = copy(function = { function.invoke(it);0 })
    fun requirement(requires: List<(NeoCommandSource) -> Boolean>) = copy(requirements = requires)
    fun appendRequirement(require: (NeoCommandSource) -> Boolean) =
        copy(requirements = requirements.toMutableList().apply { add(require) })

    fun requiresPermission(permission: String) = appendRequirement { it.sender.hasPermission(permission) }
    fun requiresSender(clazz: Class<out CommandSender>) = appendRequirement { clazz.isInstance(it.sender) }
    fun arguments(vararg arguments: NeoArgument<Any,Any>) = copy(arguments = arguments.toList())
    fun appendArguments(vararg arguments: NeoArgument<Any,Any>) =
        copy(arguments = this.arguments.toMutableList().apply { addAll(arguments) })

    fun children(vararg children: NeoCommand) = copy(children = children.toList())
    fun appendChildren(vararg children: NeoCommand) =
        copy(children = this.children.toMutableList().apply { addAll(children) })


    fun parallelCommands(vararg parallelCommand: NeoCommand) = copy(parallelCommands = parallelCommand.toList())
    fun appendParallelCommands(vararg parallelCommand: NeoCommand) =
        copy(parallelCommands = this.parallelCommands.toMutableList().apply { addAll(parallelCommand) })
}