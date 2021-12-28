package com.example.tourism.view


import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import com.example.tourism.ViewModel.PlaceViewModel
import com.example.tourism.adapters.PlacesRecyclerAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.ByteArrayOutputStream
import com.example.tourism.R
import android.content.Intent
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.tourism.Model.Dto.CollectionModel
import com.example.tourism.Model.PlaceModel.Location


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
        val getaddress: LinearLayout = view.findViewById(R.id.get_address)
        val users: LinearLayout = view.findViewById(R.id.users)
        val comments: LinearLayout = view.findViewById(R.id.comments)


        shared.setOnClickListener() {
            val bitMap: Bitmap? = requireArguments().getParcelable("bitmap")

            val bitmapUri: Uri = getImageUriFromBitmap(requireActivity(), bitMap)
            //val bitmapUri: Uri = getImageUri(requireContext(),bitMap!!)
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
            context?.startActivity(Intent.createChooser(intent, "Share"))
        }
        getaddress.setOnClickListener() {
            val address: CollectionModel? = requireArguments().getParcelable("Location")
            val location: Location = address!!.location
            val Uri = Uri.parse("google.streetview:cbll=${location.lat},${location.lng}")
            val mapIntent = Intent(Intent.ACTION_VIEW, Uri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
        users.setOnClickListener() {
            findNavController().navigate(R.id.action_mainFragment_to_userlistFragment)
        }
        comments.setOnClickListener() {
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Add Comments")

            val view: View = layoutInflater.inflate(R.layout.dialog_comments, null)
            val comments: EditText = view.findViewById(R.id.dialog_comment)
            builder.setView(view)
            builder.setPositiveButton("Add", { _, _ ->

            })
            builder.setNegativeButton("Cancel", { _, _ -> })
            builder.show()
        }
    }


    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap?): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }
}









