package me.bhop.bcon

import me.bhop.bcon.adapter.GlobalTypeAdapterFactory
import me.bhop.bcon.adapter.TypeAdapter
import me.bhop.bcon.adapter.TypeAdapterFactory
import me.bhop.bcon.exception.TypeAdapterRegisteredException

class Bcon : TypeAdapterFactory {
    private val adapters: MutableMap<Class<*>, TypeAdapter<*>> = mutableMapOf()

    override fun <T> register(adapter: TypeAdapter<T>, override: Boolean)  = if (!adapters.containsKey(adapter.getType())) adapters[adapter.getType()] = adapter else
        throw TypeAdapterRegisteredException("Unable to register adapter for type '${adapter.getType().name}' because there is already a registered adapter and override is not enabled!")

    @Suppress("UNCHECKED_CAST")
    override fun <T> unregister(type: Class<T>): TypeAdapter<T>? = adapters.remove(type) as? TypeAdapter<T>?

    @Suppress("UNCHECKED_CAST")
    override fun <T> getTypeAdapter(type: Class<T>, orGlobal: Boolean): TypeAdapter<T>? {
        val local = adapters[type]
        return when {
            local as? TypeAdapter<T>? != null -> local
            orGlobal -> GlobalTypeAdapterFactory.getTypeAdapter(type)
            else -> null
        }
    }
}