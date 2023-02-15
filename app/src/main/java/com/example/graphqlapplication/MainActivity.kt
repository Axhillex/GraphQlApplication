package com.example.graphqlapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.graphqlapplication.data.GetUsersRequestBody
import com.example.graphqlapplication.data.GetUsersResponseBody
import com.example.graphqlapplication.databinding.ActivityMainBinding
import com.example.graphqlapplication.utils.constants.client
import com.example.graphqlapplication.utils.ui.UsersAdapter
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter = UsersAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setUpView()
            getUsersList()
            setContentView(binding.root)
        }
    }

    private suspend fun getUsersList() = withContext(Dispatchers.IO) {
        val request = client().getUsers(
            GetUsersRequestBody(requiredData = "id  name username email phone email").createQuery()
        )

        if (request.isSuccessful) {
            val data = Gson().fromJson(request.body(), GetUsersResponseBody::class.java)
            adapter.addUsers(data.data?.users?.data)
        }
    }

    private fun setUpView() {
        binding.usersRecyclerView.adapter = adapter
    }
}