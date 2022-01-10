package com.example.tourism.view

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.tourism.Model.Dto.CommentsModel
import com.example.tourism.Model.Dto.DetailsModel
import com.example.tourism.Model.Dto.Users
import com.example.tourism.R
import com.example.tourism.ViewModel.CommentsViewModel
import com.example.tourism.ViewModel.PlaceViewModel
import com.example.tourism.adapters.CommentRecyclerAdapter
import com.example.tourism.databinding.FragmentDetailsBinding
import com.example.tourism.databinding.FragmentProfileBinding


class detailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val commentList = mutableListOf<CommentsModel>()
    private val commentsViewModel: CommentsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var bottomsheetDialog = BottomSheetFragment()
        val details: DetailsModel? = requireArguments().getParcelable("details")

        val commentRecyclerView: RecyclerView = view.findViewById(R.id.detailsRecyclerView)
        val commentAdapter = CommentRecyclerAdapter(commentList,requireContext())
       binding.DotsButton1.setOnClickListener{
            val bundle = Bundle()
            bundle.putParcelable("details",details)
            bottomsheetDialog.arguments = bundle
            bottomsheetDialog.show(childFragmentManager,"")
        }

        Glide.with(this)
            .load(details!!.placepicture)
            .centerCrop()
            .into(binding.PictureOfPlace)
      //----------------------- take value from api
        binding.NameOfPlace.setText(details!!.name)
        binding.BusinessStatus.setText(details!!.businessStatus)
        binding.Address.setText(details!!.vicinity)
        binding.ratingBar.rating = details!!.rating.toFloat()
        commentRecyclerView.adapter = commentAdapter

//        commentsViewModel.getUserDatabyId(d)
        commentsViewModel.getlistComment(details!!.placeId)
        commentsViewModel.getLiveData.observe(viewLifecycleOwner, {
            it?.let {
                commentList.clear()
                commentList.addAll(it)
                commentAdapter.notifyDataSetChanged()
                commentsViewModel.getLiveData.postValue(null)
            }
        })
    }

    override fun onSaveInstanceState(oldInstanceState: Bundle) {
        super.onSaveInstanceState(oldInstanceState)
        oldInstanceState.clear()
    }
}