package com.example.hw4_1

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LifecycleViewModel : ViewModel() {
    private val _events = MutableStateFlow<List<LifecycleEvent>>(emptyList())
    val events: StateFlow<List<LifecycleEvent>> = _events.asStateFlow()

    private val _currentState = MutableStateFlow("Unknown")
    val currentState: StateFlow<String> = _currentState.asStateFlow()

    fun addEvent(eventName: String) {
        val newEvent = LifecycleEvent(eventName)
        _events.value = _events.value + newEvent
        _currentState.value = eventName
    }
}

