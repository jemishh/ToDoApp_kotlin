package com.example.todo.adapter

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.databinding.FragmentRegistrationBinding
import com.example.todo.databinding.ItemOtherUserBinding
import com.example.todo.databinding.ItemUserBinding
import com.example.todo.models.UserInfoModel
import com.google.firebase.auth.UserInfo
import kotlin.math.log

class rvAdapterUsers(private val userList: ArrayList<UserInfoModel>) : RecyclerView.Adapter<rvAdapterUsers.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOtherUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: ItemOtherUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(userList[position]){
                binding.tvUsername.text = this.name
                binding.tvEmail.text = this.email
                binding.tvDob.text = this.dob
                binding.tvAge.text = this.age
            }
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
   /* class myViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }
*/
}