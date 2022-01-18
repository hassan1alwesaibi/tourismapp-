package com.example.tourism.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide

import com.example.tourism.Model.Dto.CommentsModel
import com.example.tourism.Model.Dto.DetailsModel

import com.example.tourism.R
import com.example.tourism.ViewModel.CommentsViewModel

import com.example.tourism.adapters.CommentRecyclerAdapter
import com.example.tourism.databinding.FragmentDetailsBinding


class detailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val commentList = mutableListOf<CommentsModel>()
    private val commentsViewModel: CommentsViewModel by activityViewModels()
    lateinit var commentAdapter :CommentRecyclerAdapter


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
         commentAdapter = CommentRecyclerAdapter(commentList,requireContext())

      //-------------------------------------------when press to 3 dots will open bottom shut
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

        if(details.businessStatus == "null"){
          binding.BusinessStatus.setText("")
      }else {
          binding.BusinessStatus.setText(details!!.businessStatus)
      }
        binding.Address.setText(details!!.vicinity)
        binding.ratingBar.rating = details!!.rating.toFloat()

        commentRecyclerView.adapter = commentAdapter
        commentsViewModel.getlistComment(details!!.placeId)
        observe()
       // for refresh page
        binding.swiperefreshlayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener(){
            commentsViewModel.getlistComment(details!!.placeId)
            commentAdapter.notifyDataSetChanged()
            binding.swiperefreshlayout.setRefreshing(false)
        })

    }

    override fun onSaveInstanceState(oldInstanceState: Bundle) {
        super.onSaveInstanceState(oldInstanceState)
        oldInstanceState.clear()
    }
    fun observe() {
        commentsViewModel.getLiveData.observe(viewLifecycleOwner, {
            it?.let {
                commentList.clear()
                commentList.addAll(it)
                commentAdapter.notifyDataSetChanged()
                commentAdapter.submitList(it)
                commentsViewModel.getLiveData.postValue(null)
            }
        })
    }
}