package com.artmaster.android.orthodoxcalendar.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class CurrentTime(
    val year: Int,
    val month: Int,
    val dayOfMonth: Int,
)
