package com.example.tourism.view



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.activityViewModels

import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import com.example.tourism.R
import android.content.Intent

import android.net.Uri


import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController


import com.example.tourism.Model.Dto.DetailsModel
import com.example.tourism.Model.PlaceModel.Location
import com.example.tourism.ViewModel.BottonSheetViewModel
import com.example.tourism.databinding.FragmentBottomSheetBinding



class BottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetBinding
    private val bottonsheetViewModel: BottonSheetViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       val detail: DetailsModel? = requireArguments().getParcelable("details")
//---------------------------------------------------------------------to share photo of places to anthor app
        binding.shared.setOnClickListener() {
            shared(detail!!.bitmap)
        }
 //----------------------------------------------------------------------------to open goole map
        binding.getAddress.setOnClickListener() {
            location(detail!!.location)
        }
 //----------------------------------------------------------------- // to see list of users
        binding.users.setOnClickListener() {
            findNavController().navigate(R.id.action_mainFragment_to_userlistFragment)
            dismiss()
        }

 //--------------------------------------------------------------------for add cooment
        // open custom dialog to writes comment
        binding.comments.setOnClickListener() {
             commentdialog(detail!!.placeId)
        }

    }
//------------------------------------------------------------------------------------
  fun shared(uri: Uri){
      val intent = Intent(Intent.ACTION_SEND)
      intent.type = "image/*"
      intent.putExtra(Intent.EXTRA_STREAM, uri)
      context?.startActivity(Intent.createChooser(intent, "Share"))
      dismiss()

  }
    //------------------------------------------------------------------------
    fun location(location: Location){
        requireArguments().clear()
        val Uri = Uri.parse("google.navigation:q=${location.lat},${location.lng}")
        val mapIntent = Intent(Intent.ACTION_VIEW, Uri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
        dismiss()
    }
 //-------------------------------------------------------------------------------
    fun commentdialog(placeId:String){
      val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
      builder.setTitle("Add Comments")
      builder.setCancelable(false)
      // receive
      val view: View = layoutInflater.inflate(R.layout.dialog_comments, null)
      val comments: EditText = view.findViewById(R.id.dialog_comment)

      builder.setView(view)

      builder.setPositiveButton("Add", { _, _ ->
          //save comment to commentsModel
          bottonsheetViewModel.save(placeId, comments.text.toString())
          this@BottomSheetFragment.dismiss()
      })
      builder.setNegativeButton("Cancel", { _, _ ->
          this@BottomSheetFragment.dismiss()
      })
      builder.show()
  }
//-----------------------------------------------------------------------
}









