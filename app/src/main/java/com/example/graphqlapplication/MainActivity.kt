package com.example.graphqlapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateListOf
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), UsersInterface {

    private lateinit var binding: ActivityMainBinding
    private val adapter = UsersAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            adapter.callBack = this@MainActivity
            binding = ActivityMainBinding.inflate(layoutInflater)
            getUsersList()
            setUpView()
            setContentView(binding.root)
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
        val data = mutableStateListOf(
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
        binding.composeView.setContent {
            LazyColumn(
                Modifier
                    .fillMaxSize()
            ) {
                itemsIndexed(data) { index, user ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .animateItemPlacement(tween(250))
                    ) {
                        Text(text = "$user")
                    }
                    if (index != data.size - 1) {
                        Divider(Modifier.padding(horizontal = 8.dp))
                    }
                }
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
                    Toast.makeText(applicationContext, "Removed $id", Toast.LENGTH_SHORT).show()
                }
                getUsersList()
            }
        }
    }
}