package com.example.graphqlapplication.utils.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.graphqlapplication.R
import com.example.graphqlapplication.data.UserData
import com.example.graphqlapplication.databinding.UserItemViewBinding
import com.example.graphqlapplication.utils.ui.UsersAdapter.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsersAdapter : RecyclerView.Adapter<UserItemView>() {

    private val usersList: ArrayList<UserData?> = ArrayList()
    internal var callBack: UsersInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemView {
        return UserItemView(LayoutInflater.from(parent.context).inflate(R.layout.user_item_view, parent, false))
    }

    override fun onBindViewHolder(holder: UserItemView, position: Int) {
        holder.setUpView(usersList[position], onRemove = {
            callBack?.onRemove(it)
        })
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    internal suspend fun addUsers(data: ArrayList<UserData?>?) = withContext(Dispatchers.IO) {
        usersList.clear()
        data?.let { usersList.addAll(it) }
        withContext(Dispatchers.Main) {
            notifyItemRangeChanged(0, usersList.size)
        }
    }

    class UserItemView(itemView: View) : ViewHolder(itemView) {

        private val binding = UserItemViewBinding.bind(itemView)

        internal fun setUpView(userData: UserData?, onRemove: (id: String?) -> Unit) {
            "(${userData?.username})".also { binding.userName.text = it }
            binding.name.text = userData?.name
            "${userData?.email}  |  ${userData?.phone}".also { binding.emailAndPhone.text = it }

            binding.edit.setOnClickListener {

            }

            binding.remove.setOnClickListener {
                onRemove.invoke(userData?.id)
            }
        }
    }
}