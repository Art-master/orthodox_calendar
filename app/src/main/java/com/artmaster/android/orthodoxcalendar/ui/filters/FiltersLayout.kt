package com.artmaster.android.orthodoxcalendar.ui.filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.domain.Filter
import com.artmaster.android.orthodoxcalendar.ui.CalendarViewModel
import com.artmaster.android.orthodoxcalendar.ui.common.CheckBox
import com.artmaster.android.orthodoxcalendar.ui.common.Divider
import com.artmaster.android.orthodoxcalendar.ui.common.Header
import com.artmaster.android.orthodoxcalendar.ui.common.StyledButton

@Preview
@Composable
fun FiltersLayoutPreview() {
    val onFilterChange = remember { { _: Filter, _: Boolean -> } }
    val clearAllFilters = remember { {} }

    val filters = remember {
        mutableStateOf(Filter.values())
    }

    FiltersLayout(
        filters = filters,
        onFilterChange = onFilterChange,
        clearAllFilters = clearAllFilters
    )
}

@Composable
fun FiltersLayoutWrapper(viewModel: CalendarViewModel) {

    val onFilterChange = remember {
        { filter: Filter, enabled: Boolean ->
            if (enabled) {
                viewModel.addFilter(filter)
            } else {
                viewModel.removeFilter(filter)
            }
        }
    }

    val clearAllFilters = remember {
        {
            viewModel.clearAllFilters()
        }
    }

    val filters = remember {
        mutableStateOf(Filter.values())
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