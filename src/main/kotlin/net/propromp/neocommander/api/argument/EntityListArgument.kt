package net.propromp.neocommander.api.argument

import org.bukkit.entity.Entity

class EntityListArgument(name: String) :
    AbstractEntityArgument<List<Entity>>(name, SEVERAL_ENTITIES)