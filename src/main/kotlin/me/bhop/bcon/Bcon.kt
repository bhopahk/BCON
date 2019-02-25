package me.bhop.bcon

import me.bhop.bcon.adapter.GlobalTypeAdapterFactory
import me.bhop.bcon.adapter.TypeAdapter
import me.bhop.bcon.adapter.TypeAdapterFactory
import me.bhop.bcon.exception.TypeAdapterRegisteredException

class Bcon : TypeAdapterFactory {
    private val adapters: MutableMap<Class<*>, TypeAdapter<*>> = mutableMapOf()

    override fun <T> register(adapter: TypeAdapter<T>, override: Boolean)  = if (!adapters.containsKey(adapter.type)) adapters[adapter.type] = adapter else
        throw TypeAdapterRegisteredException("Unable to register adapter for type '${adapter.type.name}' because there is already a registered adapter and override is not enabled!")

    @Suppress("UNCHECKED_CAST")
    override fun <T> unregister(type: Class<T>): TypeAdapter<T>? = adapters.remove(type) as? TypeAdapter<T>?

    @Suppress("UNCHECKED_CAST")
    override fun <T> getTypeAdapter(type: Class<T>, orSuper: Boolean, orGlobal: Boolean): TypeAdapter<T>? {
        val local = adapters[type]
        if (local != null)
            return local as? TypeAdapter<T>
        else if (orGlobal) {
            return GlobalTypeAdapterFactory.getTypeAdapter(type, orSuper = false, orGlobal = false)
        } else {
            for (iface in type.interfaces) {
                val iAdapter = getTypeAdapter(iface, orSuper, orGlobal)
                if (iAdapter != null)
                    return iAdapter as? TypeAdapter<T>
            }
            if (type.superclass != null) {
                val sAdapter = getTypeAdapter(type.superclass, orSuper, orGlobal)
                if (sAdapter != null)
                    return sAdapter as? TypeAdapter<T>
            } else
                return null
        }
        return null
    }
}