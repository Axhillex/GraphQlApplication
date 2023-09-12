package com.example.graphqlapplication.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class UserData(
    val id: String? = null,
    val name: String? = null,
    val username: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val timer: MutableState<Int> = mutableStateOf(0),
)
