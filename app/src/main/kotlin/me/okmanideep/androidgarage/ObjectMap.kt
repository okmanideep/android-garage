package me.okmanideep.androidgarage

import java.util.*


object ObjectMap {
    private val objectMap = HashMap<String, Any>()

    fun add(key : String, any : Any) {
        objectMap[key] = any
    }

    fun get(key: String) : Any {
        return objectMap[key]!!
    }

    fun has(key: String) = objectMap.containsKey(key)

    fun remove(key: String) {
        objectMap.remove(key)
    }
}