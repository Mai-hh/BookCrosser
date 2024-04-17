package com.huaihao.bookcrosser.util

import android.content.Context
import android.os.Parcelable
import com.google.gson.Gson
import com.tencent.mmkv.MMKV

const val USER_TOKEN = "USER_TOKEN"

object MMKVUtil {
    lateinit var mmkv: MMKV

    fun initialize(context: Context) {
        MMKV.initialize(context)
        mmkv = MMKV.defaultMMKV()
    }

    fun put(key: String, value: Any?) {
        when (value) {
            is String -> mmkv.encode(key, value)
            is Int -> mmkv.encode(key, value)
            is Boolean -> mmkv.encode(key, value)
            is Float -> mmkv.encode(key, value)
            is Long -> mmkv.encode(key, value)
            is Double -> mmkv.encode(key, value)
            is ByteArray -> mmkv.encode(key, value)
            is Set<*> -> mmkv.encode(key, value as Set<String>)
            is Parcelable -> mmkv.encode(key, value)
            else -> throw IllegalArgumentException("Unsupported type ${value?.javaClass?.simpleName}")
        }
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return mmkv.decodeInt(key, defaultValue)
    }

    fun getDouble(key: String, defaultValue: Double = 0.0): Double {
        return mmkv.decodeDouble(key, defaultValue)
    }

    fun getLong(key: String, defaultValue: Long = 0L): Long {
        return mmkv.decodeLong(key, defaultValue)
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return mmkv.decodeBool(key, defaultValue)
    }

    fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return mmkv.decodeFloat(key, defaultValue)
    }

    fun getBytes(key: String, defaultValue: ByteArray? = null): ByteArray? {
        return mmkv.decodeBytes(key, defaultValue)
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return mmkv.decodeString(key, defaultValue) ?: defaultValue
    }

    inline fun <reified T> putArray(key: String, list: List<T>?) {
        if (list.isNullOrEmpty()) {
            mmkv.remove(key)
        } else {
            val json = Gson().toJson(list.takeLast(20))
            mmkv.encode(key, json)
        }
    }

    inline fun <reified T> getArray(key: String): List<T> {
        val json = mmkv.decodeString(key, null) ?: return emptyList()
        return Gson().fromJson(json, Array<T>::class.java).toList()
    }

    fun getStringSet(key: String, defaultValue: Set<String> = emptySet()): Set<String> {
        return mmkv.decodeStringSet(key, defaultValue) ?: defaultValue
    }

    inline fun <reified T : Parcelable> getParcelable(key: String): T? {
        return mmkv.decodeParcelable(key, T::class.java)
    }

    fun removeKey(key: String) {
        mmkv.removeValueForKey(key)
    }

    fun clear(key: String) {
        mmkv.removeValueForKey(key)
    }

    fun clearAll() {
        mmkv.clearAll()
    }
}