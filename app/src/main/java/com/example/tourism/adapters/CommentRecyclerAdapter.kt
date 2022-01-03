package com.example.tourism.adapters

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.tourism.Model.Dto.CommentsModel
import com.example.tourism.Model.Dto.Users
import com.example.tourism.R
import com.example.tourism.Repostries.UsersRepository
import com.google.firebase.firestore.ktx.toObject

private const val TAG = "CommentRecyclerAdapter"
class CommentRecyclerAdapter(private val list: List<CommentsModel>) :
    RecyclerView.Adapter<CommentRecyclerAdapter.CommentViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentRecyclerAdapter.CommentViewHolder {
        return CommentViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_comment_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val item = list[position]
        holder.comment.text = item.comments


        val apiRepo = UsersRepository.get()


            // Get user details by comments
            val res = apiRepo.getUserDatabyId(item.UserId)

                res.addOnSuccessListener { document ->
                    val user = document.toObject<Users>(Users::class.java)
                    holder.fullname.text = user?.FirstName + " " + user?.LastName

                    Log.d(TAG, user.toString())
                    Log.d(TAG, ".............")
                }.addOnFailureListener(){
                    Log.d(TAG, it.message.toString())        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fullname : TextView =  itemView.findViewById(R.id.fullnameOfUser)
    //    val userpicture: ImageView = itemView.findViewById(R.id.picOfUser)
         val comment : TextView =  itemView.findViewById(R.id.comments_textview)
    }
}
