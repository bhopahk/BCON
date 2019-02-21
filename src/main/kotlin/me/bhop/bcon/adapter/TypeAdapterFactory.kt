package me.bhop.bcon.adapter

import me.bhop.bcon.adapter.adapters.DefaultTypeAdapter

interface TypeAdapterFactory {
    fun <T> register(adapter: TypeAdapter<T>, override: Boolean = false)

    fun <T> unregister(type: Class<T>): TypeAdapter<T>?

    fun <T> getTypeAdapter(type: Class<T>, orGlobal: Boolean = true): TypeAdapter<T>?

    fun getDefaultTypeAdapter(): TypeAdapter<Any> = DefaultTypeAdapter
}