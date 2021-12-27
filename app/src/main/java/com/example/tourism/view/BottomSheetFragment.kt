package com.example.tourism.view

import android.Manifest
import android.R.attr
import android.content.ActivityNotFoundException
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import com.example.tourism.ViewModel.PlaceViewModel
import com.example.tourism.adapters.PlacesRecyclerAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

import com.example.tourism.R
import android.content.Intent

import android.R.attr.bitmap
import android.content.Context
import android.net.Uri

import android.provider.MediaStore
import java.net.URI


class BottomSheetFragment : BottomSheetDialogFragment() {
    private val placeViewModel: PlaceViewModel by activityViewModels()
    private lateinit var PlaceAdapter: PlacesRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val shared: LinearLayout = view.findViewById(R.id.shared)
        val getaddress :LinearLayout = view.findViewById(R.id.get_address)

        shared.setOnClickListener() {
            val bitMap : Bitmap? = requireArguments().getParcelable("bitmap")

           val bitmapUri: Uri = getImageUriFromBitmap(requireActivity(),bitMap)
            //val bitmapUri: Uri = getImageUri(requireContext(),bitMap!!)
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
           context?.startActivity(Intent.createChooser(intent, "Share"))
        }
        getaddress.setOnClickListener(){



        }


        }
        }

    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap?): Uri {
     val bytes = ByteArrayOutputStream()
     bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
     val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
     return Uri.parse(path.toString())
 }










