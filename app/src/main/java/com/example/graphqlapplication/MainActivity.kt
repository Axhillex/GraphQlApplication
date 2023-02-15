package com.example.graphqlapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.graphqlapplication.data.UsersRequestBody
import com.example.graphqlapplication.data.GetUsersResponseBody
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

    private suspend fun setUpView() = withContext(Dispatchers.Main) {
        binding.usersRecyclerView.adapter = adapter
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