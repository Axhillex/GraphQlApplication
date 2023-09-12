package com.example.graphqlapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.graphqlapplication.data.UsersRequestBody
import com.example.graphqlapplication.data.GetUsersResponseBody
import com.example.graphqlapplication.data.UserData
import com.example.graphqlapplication.databinding.ActivityMainBinding
import com.example.graphqlapplication.utils.constants.client
import com.example.graphqlapplication.utils.ui.UsersAdapter
import com.example.graphqlapplication.utils.ui.UsersInterface
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), UsersInterface {

    private lateinit var binding: ActivityMainBinding
    private val adapter = UsersAdapter()
    private val timer = mutableStateOf(0)
    private val userData = mutableStateListOf(
        UserData("1", "First Name", "firstUserName", "firstEmail", "phone"),
        UserData("2", "Second Name", "secondUserName", "secondEmail", "phone"),
        UserData("3", "Third Name", "thirdUserName", "thirdEmail", "phone"),
        UserData("4", "Fourth Name", "fourthUserName", "fourthEmail", "phone"),
        UserData("5", "Fifth Name", "fifthUserName", "fifthEmail", "phone"),
        UserData("6", "Sixth Name", "sixthUserName", "sixthEmail", "phone"),
        UserData("7", "Seventh Name", "seventhUserName", "seventhEmail", "phone"),
        UserData("8", "Eighth Name", "eighthUserName", "eighthEmail", "phone"),
        UserData("9", "Ninth Name", "ninthUserName", "ninthEmail", "phone"),
        UserData("10", "Tenth Name", "tenthUserName", "tenthEmail", "phone"),
        UserData("11", "Eleventh Name", "tenthUserName", "tenthEmail", "phone"),
        UserData("12", "Twelfth Name", "tenthUserName", "tenthEmail", "phone"),
        UserData("13", "Thirteenth Name", "tenthUserName", "tenthEmail", "phone"),
        UserData("14", "Fourteenth Name", "tenthUserName", "tenthEmail", "phone"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            adapter.callBack = this@MainActivity
            binding = ActivityMainBinding.inflate(layoutInflater)
            getUsersList()
            setUpView()
            onButtonClicks()
            setContentView(binding.root)
        }
    }

    private suspend fun onButtonClicks() = withContext(Dispatchers.IO) {
        binding.addData.setOnClickListener {
            userData.add(0, UserData("0", "0", "0", "0", "0"))
        }
        binding.removeData.setOnClickListener {
            userData.removeAt(0)
        }
    }

    private suspend fun getUsersList() = withContext(Dispatchers.IO) {
        val request = client().graphQlUser(
            UsersRequestBody(requiredData = "id  name username email phone email").createGetQuery()
        )

        if (request.isSuccessful) {
            val data = Gson().fromJson(request.body(), GetUsersResponseBody::class.java)
            adapter.addUsers(data.data?.users?.data)
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    private suspend fun setUpView() = withContext(Dispatchers.Main) {
        binding.usersRecyclerView.adapter = adapter
        userData.addAll(userData)
        binding.composeView.setContent {
            LazyColumn(
                Modifier.fillMaxSize(),
                rememberLazyListState()
            ) {
                itemsIndexed(userData) { index, user ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "$user",
                            Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = "Timer -> ${timer.value}",
                            Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally)
                        )
//                        LaunchedEffect(key1 = null, block = {
//                            launch(Dispatchers.IO) {
//                                countDownTimer(30)
//                                    .onStart {
//                                        user.timer.value = 0
//                                    }
//                                    .onEach {
//                                        user.timer.value = it
//                                        if (it == 1) {
//                                            cancel()
//                                        }
//                                    }
//                                    .onCompletion {
//                                        userData.remove(user)
//                                    }
//                                    .launchIn(this)
//                            }
//                        })
                    }
                    if (index != userData.size - 1) {
                        Divider(Modifier.padding(horizontal = 8.dp))
                    }
                }
            }
        }
        delay(2000)
        Toast.makeText(baseContext, "Timer started to add other views", Toast.LENGTH_SHORT).show()
        countDownTimer(30)
            .onStart {
                userData.add(0, UserData("0", "0", "0", "0", "0"))
            }
            .onEach {
                timer.value = it
                if (it.mod(3) == 0) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(baseContext, "added new user", Toast.LENGTH_SHORT).show()
                    }
                    userData.add(0, UserData("0", "0", "0", "0", "0"))
                }
                if (it == 1) {
                    delay(1000)
                    timer.value = 0
                    delay(1000)
                    userData.clear()
                }
            }
            .onCompletion {
                withContext(Dispatchers.Main) {
                    Toast.makeText(baseContext, "removed first user", Toast.LENGTH_SHORT).show()
                }
                userData.removeAt(0)
            }
            .launchIn(CoroutineScope(Dispatchers.IO))
    }

    private suspend fun countDownTimer(duration: Int): Flow<Int> {
        return callbackFlow {
            repeat(duration) {
                send(duration - it)
                delay(1000)
            }

            awaitClose {
                cancel()
            }
        }
    }

    override fun onRemove(id: String?) {
        lifecycleScope.launch(Dispatchers.IO) {
            val request = client().graphQlUser(
                UsersRequestBody(requiredData = "$id").createRemoveQuery()
            )

            if (request.isSuccessful) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(baseContext, "Removed $id", Toast.LENGTH_SHORT).show()
                }
                getUsersList()
            }
        }
    }
}