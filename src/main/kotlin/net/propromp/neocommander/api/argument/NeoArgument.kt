package net.propromp.neocommander.api.argument

import com.mojang.brigadier.arguments.ArgumentType
import net.propromp.neocommander.api.NeoCommandContext
import net.propromp.neocommander.api.NeoSuggestionProvider

/**
 * argument
 *
 * @param T type used by brigadier argument
 * @param B type parsed
 * @property name name of this argument
 * @property suggestionProvider suggestion provider
 */
abstract class NeoArgument<out T, out B>(
    val name: String,
    open val suggestionProvider: NeoSuggestionProvider? = null
) {
    /**
     * get Brigadier's ArgumentType
     *
     * @return
     */
    abstract fun asBrigadier(): ArgumentType<out Any>
    abstract fun parse(context: NeoCommandContext, t: @UnsafeVariance T): B
}