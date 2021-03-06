package net.propromp.neocommander.api.argument

import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.propromp.neocommander.api.NeoCommandContext
import net.propromp.neocommander.api.exception.ArgumentParseException

abstract class EnumArgument<T : Enum<*>>(name: String, val enumClass: Class<T>) :
    CustomArgument<T>(name) {
    final override fun parse(context: NeoCommandContext, t: String): T {
        return enumClass.enumConstants.filter { it.name.equals(t, true) }.getOrNull(0)
            ?: throw ArgumentParseException("Illegal input.")
    }

    final override fun suggest(input: String, context: NeoCommandContext, builder: SuggestionsBuilder) {
        enumClass.enumConstants.map { it.name }.filter { emptyOrStartsWith(input, it, true) }
            .forEach { builder.suggest(it) }
    }
}