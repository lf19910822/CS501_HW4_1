package com.example.hw4_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.hw4_1.ui.theme.HW4_1Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: LifecycleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                viewModel.addEvent("onCreate")
            }

            override fun onStart(owner: LifecycleOwner) {
                viewModel.addEvent("onStart")
            }

            override fun onResume(owner: LifecycleOwner) {
                viewModel.addEvent("onResume")
            }

            override fun onPause(owner: LifecycleOwner) {
                viewModel.addEvent("onPause")
            }

            override fun onStop(owner: LifecycleOwner) {
                viewModel.addEvent("onStop")
            }

            override fun onDestroy(owner: LifecycleOwner) {
                viewModel.addEvent("onDestroy")
            }
        })

        enableEdgeToEdge()
        setContent {
            HW4_1Theme {
                LifeTrackerApp(viewModel)
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.addEvent("onRestart")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LifeTrackerApp(viewModel: LifecycleViewModel) {
    val events by viewModel.events.collectAsState()
    val currentState by viewModel.currentState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(events.size) {
        if (events.isNotEmpty()) {
            val latestEvent = events.last()
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Lifecycle: ${latestEvent.eventName}",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("LifeTracker") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CurrentStateCard(currentState)

            EventList(events)
        }
    }
}


@Composable
fun CurrentStateCard(currentState: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = getColorForEvent(currentState).copy(alpha = 0.2f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(getColorForEvent(currentState))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Current State: $currentState",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = getColorForEvent(currentState)
            )
        }
    }
}

@Composable
fun EventList(events: List<LifecycleEvent>) {
    val listState = rememberLazyListState()

    LaunchedEffect(events.size) {
        if (events.isNotEmpty()) {
            listState.animateScrollToItem(events.size - 1)
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(events) { event ->
            EventItem(event)
        }
    }
}

@Composable
fun EventItem(event: LifecycleEvent) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = event.color.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(event.color)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.eventName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = event.color
                )
                Text(
                    text = event.getFormattedTime(),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}