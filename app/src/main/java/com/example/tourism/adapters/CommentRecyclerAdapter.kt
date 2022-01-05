package com.example.tourism.adapters

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.tourism.Model.Dto.CommentsModel
import com.example.tourism.Model.Dto.Users
import com.example.tourism.R
import com.example.tourism.Repostries.FireRepository

private const val TAG = "CommentRecyclerAdapter"
class CommentRecyclerAdapter(val list: List<CommentsModel>,val picContext: Context) :
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
        val apiRepo = FireRepository.get()


            // Get user details by comments
            val res = apiRepo.getUserDatabyId(item.UserId)
                    res.addOnSuccessListener { document ->
                    val user = document.toObject<Users>(Users::class.java)
                    holder.fullname.text = user?.FirstName + " " + user?.LastName
                    var img = "https://firebasestorage.googleapis.com/v0/b/tourism-1de93.appspot.com/o/${item.UserId}?alt=media&token=b0f9dab3-5624-4026-bbae-9647629bf634"
                    Glide.with(picContext)

                            .load(img)
                            .centerCrop()
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.userpicture)



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
         val userpicture: ImageView = itemView.findViewById(R.id.picOfUser)
         val comment : TextView =  itemView.findViewById(R.id.comments_textview)
    }
}
