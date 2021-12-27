package com.example.tourism.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tourism.Model.Dto.Users
import com.example.tourism.R

class UsersRecyclerAdapter(private val list: List<Users>) :
    RecyclerView.Adapter<UsersRecyclerAdapter.UsersViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UsersRecyclerAdapter.UsersViewHolder {
        return UsersViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.itemuser_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val item = list[position]
        TODO("bind view with data")
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
}
