package me.nathanfallet.suitebde.ui.components.feed

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.features.scans.ScannerActivity
import me.nathanfallet.suitebde.models.associations.SubscriptionInAssociation
import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.ui.components.navigation.DefaultNavigationBar

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Suppress("FunctionName")
fun FeedRootView(
    subscriptions: List<SubscriptionInAssociation>,
    search: String,
    updateSearch: (String) -> Unit,
    events: List<Event>,
    sendNotificationVisible: Boolean,
    showScannerVisible: Boolean,
    onOpenURL: (Uri) -> Unit,
    users: List<User>,
    clubs: List<Club>,
    hasMoreUsers: Boolean,
    hasMoreClubs: Boolean,
    loadMoreUsers: () -> Unit,
    loadMoreClubs: () -> Unit,
    navigate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    var isMenuShown by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val barcodeLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        result.contents?.let { contents ->
            Uri.parse(contents)?.also {
                if (it.scheme != "bdeensisa" && it.scheme != "suitebde") {
                    Toast.makeText(context, "QR Code invalide !", Toast.LENGTH_SHORT).show()
                    return@let
                }
                if (it.host == "scan_history") {
                    navigate("feed/scan_history")
                    return@let
                }
                onOpenURL(it)
            } ?: run {
                Toast.makeText(context, "QR Code invalide !", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LazyColumn(modifier) {
        stickyHeader {
            DefaultNavigationBar(
                title = stringResource(R.string.feed_title),
                toolbar = {
                    Box {
                        IconButton(onClick = {
                            isMenuShown = true
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_add_24),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        DropdownMenu(
                            expanded = isMenuShown,
                            onDismissRequest = {
                                isMenuShown = false
                            }
                        ) {
                            DropdownMenuItem(onClick = {
                                navigate("feed/suggest_event")
                                isMenuShown = false
                            }, text = {
                                Text(stringResource(R.string.feed_events_suggest))
                            })
                            if (sendNotificationVisible) {
                                DropdownMenuItem(onClick = {
                                    navigate("feed/send_notification")
                                    isMenuShown = false
                                }, text = {
                                    Text(stringResource(R.string.feed_notifications_send))
                                })
                            }
                        }
                    }
                    if (showScannerVisible) {
                        IconButton(onClick = {
                            val options = ScanOptions()
                            options.captureActivity = ScannerActivity::class.java
                            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                            options.setOrientationLocked(false)
                            options.setBeepEnabled(false)
                            options.addExtra(Intents.Scan.SCAN_TYPE, Intents.Scan.MIXED_SCAN)
                            barcodeLauncher.launch(options)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_qr_code_scanner_24),
                                contentDescription = "Scanner un QR code",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    IconButton(onClick = {
                        navigate("feed/settings")
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_settings_24),
                            contentDescription = stringResource(R.string.settings_title),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            // TODO: reusable text field style
            TextField(
                value = search,
                onValueChange = updateSearch,
                placeholder = {
                    Text(
                        text = stringResource(R.string.app_search),
                        color = Color.LightGray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (search.trim().isNotEmpty()) FeedSearchView(
            users = users,
            clubs = clubs,
            hasMoreUsers = hasMoreUsers,
            hasMoreClubs = hasMoreClubs,
            loadMoreUsers = loadMoreUsers,
            loadMoreClubs = loadMoreClubs,
            navigate = navigate
        ) else FeedHomeView(
            subscriptions = subscriptions,
            events = events,
            navigate = navigate
        )

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }

}

@Preview
@Composable
@Suppress("FunctionName")
fun FeedRootViewPreview() {
    FeedRootView(
        search = "",
        updateSearch = {},
        subscriptions = listOf(
            SubscriptionInAssociation(
                "id",
                "associationId",
                "Cotisation pour la scolarité",
                "Cool",
                85.0,
                "1y",
                false
            ),
            SubscriptionInAssociation(
                "id2",
                "associationId",
                "Cotisation pour l'année",
                "Cool",
                35.0,
                "1y",
                false
            )
        ),
        events = listOf(
            Event(
                id = "id",
                associationId = "associationId",
                name = "Vente de crèpes",
                description = "A cool event",
                image = "https://images.unsplash.com/photo-1637036124732-cb0fab13bb15?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                startsAt = Clock.System.now(),
                endsAt = Clock.System.now(),
                validated = true
            ),
            Event(
                id = "id2",
                associationId = "associationId",
                name = "Assemblée générale",
                description = "A cool event",
                image = "https://images.unsplash.com/photo-1492538368677-f6e0afe31dcc?q=80&w=1740&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                startsAt = Clock.System.now(),
                endsAt = Clock.System.now(),
                validated = true
            ),
        ),
        sendNotificationVisible = true,
        showScannerVisible = true,
        onOpenURL = {},
        users = listOf(),
        clubs = listOf(),
        hasMoreUsers = true,
        hasMoreClubs = true,
        loadMoreUsers = {},
        loadMoreClubs = {},
        navigate = {}
    )
}
