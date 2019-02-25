package me.bhop.bcon.adapter

import me.bhop.bcon.adapter.adapters.BooleanTypeAdapter
import me.bhop.bcon.adapter.adapters.CollectionTypeAdapter
import me.bhop.bcon.adapter.adapters.NumberTypeAdapter
import me.bhop.bcon.adapter.adapters.StringTypeAdapter
import me.bhop.bcon.exception.TypeAdapterRegisteredException

@Suppress("UNCHECKED_CAST")
object GlobalTypeAdapterFactory : TypeAdapterFactory {
    private val adapters: MutableMap<Class<*>, TypeAdapter<*>> = mutableMapOf()

    init {
        adapters[String::class.java] = StringTypeAdapter()
        adapters[Boolean::class.java] = BooleanTypeAdapter()
        adapters[Class.forName("java.lang.Boolean")] = BooleanTypeAdapter()
        adapters[Int::class.java] = NumberTypeAdapter()
        adapters[Class.forName("java.lang.Integer")] = NumberTypeAdapter()
        adapters[Double::class.java] = NumberTypeAdapter()
        adapters[Class.forName("java.lang.Double")] = NumberTypeAdapter()
        adapters[Long::class.java] = NumberTypeAdapter()
        adapters[Class.forName("java.lang.Long")] = NumberTypeAdapter()
        adapters[Short::class.java] = NumberTypeAdapter()
        adapters[Class.forName("java.lang.Short")] = NumberTypeAdapter()
        adapters[Float::class.java] = NumberTypeAdapter()
        adapters[Class.forName("java.lang.Float")] = NumberTypeAdapter()
        adapters[Collection::class.java] = CollectionTypeAdapter()
    }

    override fun <T> register(adapter: TypeAdapter<T>, override: Boolean) = if (!adapters.containsKey(adapter.type)) adapters[adapter.type] = adapter else //todo perhaps use reflection to just get the generic type instead of having the getType method. would be cleaner
        throw TypeAdapterRegisteredException("Unable to register adapter for type '${adapter.type.name}' because there is already a registered adapter and override is not enabled!")

    override fun <T> unregister(type: Class<T>): TypeAdapter<T>? = adapters.remove(type) as? TypeAdapter<T>?

    override fun <T> getTypeAdapter(type: Class<T>, orSuper: Boolean, orGlobal: Boolean): TypeAdapter<T>? {
        val adapter = adapters[type]
        if (adapter != null)
            return adapter as? TypeAdapter<T>
        else {
            for (iface in type.interfaces) {
                val iAdapter = getTypeAdapter(iface, orSuper, orGlobal)
                if (iAdapter != null)
                    return iAdapter as? TypeAdapter<T>
            }
            if (type.superclass != null) {
                val sAdapter = getTypeAdapter(type.superclass)
                if (sAdapter != null)
                    return sAdapter as? TypeAdapter<T>
            }
            return adapter
        }
    }
}