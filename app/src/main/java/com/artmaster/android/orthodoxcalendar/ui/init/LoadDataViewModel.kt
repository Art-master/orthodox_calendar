package com.artmaster.android.orthodoxcalendar.ui.init

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.common.Settings
import com.artmaster.android.orthodoxcalendar.common.Settings.Name.FIRST_LOAD_APP
import com.artmaster.android.orthodoxcalendar.common.Settings.Name.SPEED_UP_START_ANIMATION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoadDataViewModel : ViewModel() {
    private val preferences = App.appComponent.getPreferences()
    private val repository = App.appComponent.getRepository()
    private val fileParser = App.appComponent.getFileParser()

    private val isOffAnimation = preferences.get(Settings.Name.OFF_START_ANIMATION)
    val animationTime = getLoadingAnimTime()

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

    fun loadData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (isAppFirstLoad()) {
                    val data = fileParser.getData()
                    repository.insertHolidays(data)
                    preferences.set(FIRST_LOAD_APP, Settings.TRUE)
                }
            }
        }
    }

    private fun isAppFirstLoad() = preferences.get(FIRST_LOAD_APP) == Settings.FALSE
}