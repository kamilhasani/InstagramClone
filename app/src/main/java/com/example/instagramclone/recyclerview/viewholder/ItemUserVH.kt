package com.example.instagramclone.recyclerview.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.User
import com.example.instagramclone.databinding.ItemUserBinding

class ItemUserVH (private val binding : ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind (user : User){
        binding.run{
            usernameInSearch.text = user.username
            namaInSearch.text = user.fullname
            Glide.with(binding.root).load(user.image).into(binding.imageInSearch)
            btnFllwInSrch.setOnClickListener {
            }
        }

    }

}