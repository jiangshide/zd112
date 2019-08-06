package com.android.network.codec

import android.text.TextUtils
import com.google.gson.JsonParseException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

/**
 * created by jiangshide on 2019-07-31.
 * email:18311271399@163.com
 */
class BooleanTypeAdapter: TypeAdapter<Boolean>(){

    override fun write(out: JsonWriter, value: Boolean?) {
        if(null == value){
            out.nullValue()
        }else{
            out.value(value)
        }
    }

    override fun read(`in`: JsonReader): Boolean? {
        val peek = `in`.peek()
        when(peek){
            JsonToken.BOOLEAN -> return `in`.nextBoolean()
            JsonToken.NULL -> {
                `in`.nextNull()
                return null
            }
            JsonToken.NUMBER -> return `in`.nextInt() != 0
            JsonToken.STRING -> return toBoolean(`in`.nextString())
            else -> throw JsonParseException("Excepted BOOLEAN or NUMBER but was $peek")
        }
    }

    companion object{
        fun toBoolean(name:String):Boolean{
            return !TextUtils.isEmpty(name)&&(name.equals("true",ignoreCase = true) || name != "0")
        }
    }
}