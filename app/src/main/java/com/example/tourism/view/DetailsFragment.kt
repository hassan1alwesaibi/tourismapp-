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
import com.example.tourism.Model.Dto.CollectionModel
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

        var bottomsheetDialog = BottomSheetFragment()
        val PictureOfPlace1:Bitmap? = requireArguments().getParcelable("bitmap")
        val collection: CollectionModel? = requireArguments().getParcelable("Location")
        val details: DetailsModel? = requireArguments().getParcelable("details")
        val PictureOfPlace: ImageView = view.findViewById(R.id.PictureOfPlace)
        val NameOfPlace: TextView = view.findViewById(R.id.NameOfPlace)
        val Business:TextView = view.findViewById(R.id.Business_status)
        val dots_Button:ImageView = view.findViewById(R.id.Dots_Button1)
        val Address:TextView = view.findViewById(R.id.Address)
        val commentRecyclerView: RecyclerView = view.findViewById(R.id.detailsRecyclerView)
        val commentAdapter = CommentRecyclerAdapter(commentList,requireContext())

        dots_Button.setOnClickListener{
           // requireArguments().clear()
            val bundle = Bundle()
            bundle.putParcelable("bitmap",PictureOfPlace1 )
            bundle.putParcelable("Location",collection)
            bundle.putParcelable("details",details)
            bottomsheetDialog.arguments = bundle
            bottomsheetDialog.show(childFragmentManager,"")
        }

        Glide.with(this)
            .load(details!!.placepicture)
            .centerCrop()
            .into(PictureOfPlace)


        NameOfPlace.setText(details!!.name)
        Business.setText(details!!.businessStatus)
        Address.setText(details!!.vicinity)
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

}