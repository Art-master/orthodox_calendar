package com.artmaster.android.orthodoxcalendar.ui.tools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.domain.Filter
import com.artmaster.android.orthodoxcalendar.ui.common.CheckBox
import com.artmaster.android.orthodoxcalendar.ui.common.Divider
import com.artmaster.android.orthodoxcalendar.ui.common.Header
import com.artmaster.android.orthodoxcalendar.ui.common.StyledButton
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.CalendarViewModelFake
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.ICalendarViewModel

@Preview
@Composable
fun FiltersLayoutPreview() {
    FiltersLayoutWrapper(CalendarViewModelFake())
}

@Composable
fun FiltersLayoutWrapper(viewModel: ICalendarViewModel) {

    val onFilterChange = remember {
        { filter: Filter, enabled: Boolean ->
            if (enabled) {
                viewModel.addActiveFilter(filter)
            } else {
                viewModel.removeActiveFilter(filter)
            }
        }
    }

    val clearAllFilters = remember {
        {
            viewModel.clearAllFilters()
        }
    }

    val filters = remember {
        mutableStateOf(Filter.entries.toTypedArray())
    }
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        FiltersLayout(
            filters = filters,
            onFilterChange = onFilterChange,
            clearAllFilters = clearAllFilters
        )
    }
}

@Composable
fun FiltersLayout(
    filters: MutableState<Array<Filter>>,
    onFilterChange: (Filter, Boolean) -> Unit,
    clearAllFilters: () -> Unit
) {
    val scroll = rememberScrollState(0)
    var updateFlag by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(scroll),
        verticalArrangement = Arrangement.Bottom
    ) {
        Header(title = stringResource(id = R.string.filters_title))

        Divider()

        key(updateFlag) {
            filters.value.forEach { filter ->
                FilterCheckBox(
                    filter = filter,
                    onFilterChange = {
                        onFilterChange(filter, it)
                        updateFlag = updateFlag.not()
                    }
                )
            }
        }

        StyledButton(
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(top = 50.dp),
            title = stringResource(id = R.string.filter_clear_all)
        ) {
            clearAllFilters()
            updateFlag = updateFlag.not()
        }
    }
}

@Composable
fun FilterCheckBox(
    filter: Filter,
    onFilterChange: (enabled: Boolean) -> Unit
) {
    val title = stringResource(id = filter.resId)
    CheckBox(title = title, state = filter.enabled, onCheck = onFilterChange)
}