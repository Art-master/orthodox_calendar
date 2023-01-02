package com.artmaster.android.orthodoxcalendar.ui.init_page.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.DATA_VERSION
import com.artmaster.android.orthodoxcalendar.common.Settings
import com.artmaster.android.orthodoxcalendar.common.Settings.Name.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoadDataViewModel : ViewModel() {

    private val preferences = App.appComponent.getPreferences()
    private val repository = App.appComponent.getRepository()
    private val fileParser = App.appComponent.getFileParser()

    private val isOffAnimation = preferences.get(OFF_START_ANIMATION)
    private val userDataVersion = preferences.get(USER_DATA_VERSION)
    val animationTime = getLoadingAnimTime()

    var isDatabasePrepared by mutableStateOf(false)

    private fun getLoadingAnimTime(): Long {
        if (isShowStartAnimation().not()) return 0
        var time = Constants.LOADING_ANIMATION_DURATION.toLong()
        val isSpeedUpEnabled = preferences.get(SPEED_UP_START_ANIMATION)
        if (isSpeedUpEnabled == Settings.TRUE) {
            time = Constants.LOADING_ANIMATION_SPEED_UP.toLong()
        }
        return time
    }

    private fun isShowStartAnimation(): Boolean {
        return isOffAnimation == Settings.FALSE
    }

    fun fillDatabaseIfNeed(callback: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (isAppFirstLoad()) {
                    val data = fileParser.getData()
                    repository.insertHolidays(data)
                    preferences.set(FIRST_LOAD_APP, Settings.TRUE)
                } else if (userDataVersion.toInt() != DATA_VERSION) {
                    val data = fileParser.getData()
                    repository.deleteCommonHolidays()
                    repository.insertHolidays(data)
                    preferences.set(USER_DATA_VERSION, userDataVersion.toInt().inc().toString())
                }
                isDatabasePrepared = true
                callback.invoke()
            }
        }
    }

    private fun isAppFirstLoad() = preferences.get(FIRST_LOAD_APP) == Settings.FALSE
}