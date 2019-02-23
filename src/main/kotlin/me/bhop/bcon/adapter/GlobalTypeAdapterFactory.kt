package me.bhop.bcon.adapter

import me.bhop.bcon.adapter.adapters.BooleanTypeAdapter
import me.bhop.bcon.adapter.adapters.StringTypeAdapter
import me.bhop.bcon.exception.TypeAdapterRegisteredException

@Suppress("UNCHECKED_CAST")
object GlobalTypeAdapterFactory : TypeAdapterFactory {
    private val adapters: MutableMap<Class<*>, TypeAdapter<*>> = mutableMapOf()

    init {
        adapters[String::class.java] = StringTypeAdapter()
        adapters[Boolean::class.java] = BooleanTypeAdapter()
    }

    override fun <T> register(adapter: TypeAdapter<T>, override: Boolean) = if (!adapters.containsKey(adapter.getType())) adapters[adapter.getType()] = adapter else //todo perhaps use reflection to just get the generic type instead of having the getType method. would be cleaner
        throw TypeAdapterRegisteredException("Unable to register adapter for type '${adapter.getType().name}' because there is already a registered adapter and override is not enabled!")

    override fun <T> unregister(type: Class<T>): TypeAdapter<T>? = adapters.remove(type) as? TypeAdapter<T>?

    override fun <T> getTypeAdapter(type: Class<T>, orGlobal: Boolean): TypeAdapter<T>? = adapters[type] as? TypeAdapter<T>?
}