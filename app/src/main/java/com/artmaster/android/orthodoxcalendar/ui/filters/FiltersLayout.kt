package com.artmaster.android.orthodoxcalendar.ui.filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.domain.Filter
import com.artmaster.android.orthodoxcalendar.ui.CalendarViewModel
import com.artmaster.android.orthodoxcalendar.ui.common.CheckBox
import com.artmaster.android.orthodoxcalendar.ui.common.Divider
import com.artmaster.android.orthodoxcalendar.ui.common.Header

@Preview
@Composable
fun FiltersLayoutPreview() {
    val onFilterChange = remember { { _: Filter, _: Boolean -> } }

    val filters = remember {
        mutableStateOf(Filter.values())
    }

    FiltersLayout(filters = filters, onFilterChange = onFilterChange)
}

@Composable
fun FiltersLayoutWrapper(viewModel: CalendarViewModel) {

    val onFilterChange = remember {
        { filter: Filter, enabled: Boolean ->
            if (enabled) {
                viewModel.removeFilter(filter)
            } else {
                viewModel.addFilter(filter)
            }

        }
    }

    val filters = remember {
        mutableStateOf(Filter.values())
    }
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        FiltersLayout(
            filters = filters,
            onFilterChange = onFilterChange
        )
    }
}

@Composable
fun FiltersLayout(
    filters: MutableState<Array<Filter>>,
    onFilterChange: (Filter, Boolean) -> Unit
) {
    val scroll = rememberScrollState(0)

    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(scroll),
        verticalArrangement = Arrangement.Bottom
    ) {
        Header(title = stringResource(id = R.string.filters_title))

        Divider()

        filters.value.forEach { filter ->
            FilterCheckBox(
                filter = filter,
                onFilterChange = onFilterChange
            )
        }
    }
}

@Composable
fun FilterCheckBox(
    filter: Filter,
    onFilterChange: (filter: Filter, enabled: Boolean) -> Unit
) {
    var state by remember { mutableStateOf(filter.enabled) }
    val onCheck = remember {
        { flag: Boolean ->
            onFilterChange(filter, flag)
            state = flag
        }
    }
    val title = stringResource(id = filter.resId)
    CheckBox(title = title, state = state, onCheck = onCheck)
}