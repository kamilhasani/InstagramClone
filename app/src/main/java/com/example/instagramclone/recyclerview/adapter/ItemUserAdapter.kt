package com.example.instagramclone.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramclone.User
import com.example.instagramclone.databinding.ItemUserBinding
import com.example.instagramclone.recyclerview.viewholder.ItemUserVH

class ItemUserAdapter : RecyclerView.Adapter<ItemUserVH>() {

    private val listData= arrayListOf<User>()

    fun addData(userData: List<User>){
        listData.clear()
        listData.addAll(userData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemUserVH {
        val inflate = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflate)
        return ItemUserVH(
            binding
        )
    }

    override fun getItemCount(): Int {
        return listData.size

    }

    override fun onBindViewHolder(holder: ItemUserVH, position: Int) {
        val data = listData[position]
        holder.bind(data)

    }
}