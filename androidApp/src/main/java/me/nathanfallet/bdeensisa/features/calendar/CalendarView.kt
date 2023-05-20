package me.nathanfallet.bdeensisa.features.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.nathanfallet.bdeensisa.R
import me.nathanfallet.bdeensisa.extensions.renderedDate
import me.nathanfallet.bdeensisa.features.MainViewModel
import kotlin.time.Duration.Companion.days

@Composable
fun CalendarView(
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel,
    mainViewModel: MainViewModel,
    owner: LifecycleOwner
) {

    val localDensity = LocalDensity.current

    val day by viewModel.getDay().observeAsState()
    val todayEvents by viewModel.getTodayEvents().observeAsState()

    val scrollState = rememberScrollState(with(localDensity) {
        (11 + 7 * 44).dp.roundToPx()
    })
    val coroutineScope = rememberCoroutineScope()

    val localDate =
        (day ?: Clock.System.now()).toLocalDateTime(TimeZone.currentSystemDefault()).date
    val localTomorrow =
        Clock.System.now().plus(1.days).toLocalDateTime(TimeZone.currentSystemDefault()).date

    viewModel.getDay().observe(owner) {
        with(localDensity) {
            coroutineScope.launch {
                scrollState.scrollTo((11 + 7 * 44).dp.roundToPx())
            }
        }
    }

    Column(modifier) {
        TopAppBar {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                IconButton(
                    onClick = viewModel::previous,
                    enabled = localDate >= localTomorrow
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_arrow_back_24),
                        contentDescription = "Précédent"
                    )
                }
                Text(text = day?.renderedDate ?: "Calendrier")
                IconButton(onClick = viewModel::next) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_arrow_forward_24),
                        contentDescription = "Suivant"
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(scrollState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                for (i in 0..23) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "${i}h",
                            textAlign = TextAlign.End,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .width(28.dp)
                                .height(20.dp)
                        )
                        Divider()
                    }
                }
                Row {
                    Spacer(modifier = Modifier.height(0.dp))
                }
            }
            for (event in todayEvents ?: emptyList()) {
                CalendarEventView(
                    event = event,
                    day = day ?: Clock.System.now()
                )
            }
        }
    }

}