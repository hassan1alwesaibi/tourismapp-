package com.example.tourism.adapters

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.tourism.Model.Dto.Users
import com.example.tourism.R
import com.example.tourism.ViewModel.UsersViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.auth.User

class UsersRecyclerAdapter(
    val list: List<Users>,
    val viewModel: UsersViewModel,
    val recontext: Context) :
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val item = list[position]
        holder.fullname.text = item.FirstName +" "+ item.LastName
        holder.email.text = item.Email
       Glide.with(recontext).load("https://firebasestorage.googleapis.com/v0/b/tourism-1de93.appspot.com/o/${item.UserId}?alt=media&token=052d7e39-d904-4029-ab56-1d39c7e64a69")
           .centerCrop()
           .into(holder.userpicture)


    }

    override fun getItemCount(): Int {
        return list.size
    }

    class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fullname : TextView =  itemView.findViewById(R.id.fullname_textview)
        val email: TextView = itemView.findViewById(R.id.email_textview)
        val userpicture:ImageView = itemView.findViewById(R.id.userpicture_ImgeView)
    }

}
