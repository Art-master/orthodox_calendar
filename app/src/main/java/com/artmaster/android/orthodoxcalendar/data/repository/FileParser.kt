package com.artmaster.android.orthodoxcalendar.data.repository

import android.content.Context
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.INIT_ASSETS_FILE_DIRECTORY
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.INIT_ASSETS_FILE_NAME
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.impl.AppFileParser
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.nio.charset.Charset

/**
 * Parse JSON file to the List entities
 */
class FileParser(private val ctx: Context) : AppFileParser {
    override fun getData(): List<Holiday> {
        val textFromFile = getTextFromFile(INIT_ASSETS_FILE_NAME)
        return Gson().fromJson<List<Holiday>>(textFromFile, getTypeClass())
    }

    private fun getTypeClass(): Type? {
        return object : TypeToken<List<Holiday>>() {}.type
    }

    private fun getTextFromFile(fileName: String): String {
        return ctx.assets.open(INIT_ASSETS_FILE_DIRECTORY + fileName)
                .reader(Charset.defaultCharset())
                .readText()
    }
}