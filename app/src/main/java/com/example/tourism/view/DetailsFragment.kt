package com.example.tourism.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.tourism.Model.Dto.CommentsModel
import com.example.tourism.Model.Dto.DetailsModel
import com.example.tourism.Model.Dto.Users
import com.example.tourism.R
import com.example.tourism.ViewModel.CommentsViewModel
import com.example.tourism.ViewModel.PlaceViewModel
import com.example.tourism.adapters.CommentRecyclerAdapter


class detailsFragment : Fragment() {
    private val commentList = mutableListOf<CommentsModel>()
    private val commentsViewModel: CommentsViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
// received data from PlacesRecyclerAdapter
        val details : DetailsModel? = requireArguments().getParcelable("details")

        val PictureOfPlace:ImageView = view.findViewById(R.id.PictureOfPlace)

        val NameOfPlace:TextView = view.findViewById(R.id.NameOfPlace)
          NameOfPlace.setText(details!!.name)

        val commentRecyclerView: RecyclerView = view.findViewById(R.id.detailsRecyclerView)
        val commentAdapter = CommentRecyclerAdapter(commentList)
        commentRecyclerView.adapter = commentAdapter

//        commentsViewModel.getUserDatabyId(d)
        commentsViewModel.getlistComment(details!!.placeId)
        commentsViewModel.getLiveData.observe(viewLifecycleOwner,{
            it?.let{
                commentList.clear()
                commentList.addAll(it)
                commentAdapter.notifyDataSetChanged()
            }
        })
    }

}