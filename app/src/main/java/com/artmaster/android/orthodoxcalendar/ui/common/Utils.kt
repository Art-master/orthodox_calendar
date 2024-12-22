package com.artmaster.android.orthodoxcalendar.ui.common

import android.app.ActivityManager
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun isLowPerformanceDevice(): Boolean {
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
        return true
    }
    val context = LocalContext.current
    val activityManager = context.getSystemService(ActivityManager::class.java)

    return remember {
        val isLowMemory = activityManager.isLowRamDevice
        val isLowDensity = context.resources.displayMetrics.densityDpi < 240 // Low DPI screens

        isLowMemory || isLowDensity
    }
}