package com.example.tourism.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tourism.view.BottomSheetFragment
import com.example.tourism.Model.PlaceModel.Result
import com.example.tourism.R
import com.example.tourism.ViewModel.PlaceViewModel

import android.os.Bundle
import android.provider.MediaStore
import androidx.navigation.Navigation.findNavController
import com.bumptech.glide.load.engine.DiskCacheStrategy


import com.example.tourism.Model.Dto.DetailsModel
import java.io.ByteArrayOutputStream


class PlacesRecyclerAdapter(val viewMode: PlaceViewModel,
                            val fileContext: Context,
                            val fragmentManager:FragmentManager,
                            val view:View) :
    RecyclerView.Adapter<PlacesRecyclerAdapter.PlacesViewHolder>() {
    var bottomsheetDialog = BottomSheetFragment()
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.placeId == newItem.placeId
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)
    fun submitList(list: List<Result>) {
        differ.submitList(list)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlacesRecyclerAdapter.PlacesViewHolder {

        return PlacesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_main_layout,
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: PlacesViewHolder, position: Int) {
        val item = differ.currentList[position]
        var imgLink = ""
        val bundle = Bundle()
        if (item.photos != null) {
            imgLink = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photo_reference=${item.photos[0].photoReference}&key=AIzaSyAswnygZzJMdw9uEJ21KM5ZTLiAFj7Fogc"
        }
                  if (item.photos != null) {
                    Glide.with(fileContext)
                        .load(imgLink)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.pictureOfPlace)
                }
            holder.nameOfPlace.text = item.name
//-------------------------------------------------------------when press in pic show details and comment
            holder.pictureOfPlace.setOnClickListener {

                bundle.putParcelable("details",details(item,imgLink,coverBitmaptouri(fileContext,getBitmapFromView(holder.pictureOfPlace))))
                findNavController(view).navigate(R.id.action_mainFragment_to_detailsFragment,bundle)
            }
  //--------------------------------------------------------------- when press in 3dots open bottom sheet
        holder.Dots_Button.setOnClickListener {
            bundle.putParcelable("details",details(item,imgLink,coverBitmaptouri(fileContext,getBitmapFromView(holder.pictureOfPlace))))
            bottomsheetDialog.arguments = bundle
            bottomsheetDialog.show(fragmentManager,"")
        }

    }
    //--------------------------------------------------------------------------
    override fun getItemCount(): Int {
        return differ.currentList.size - 1
    }
//----------------------------------------------------------------------------
    class PlacesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var pictureOfPlace: ImageView = itemView.findViewById(R.id.PictureOfPlace)
        val nameOfPlace: TextView = itemView.findViewById(R.id.NameOfPlace)
        val Dots_Button:ImageButton = itemView.findViewById(R.id.Dots_Button1)

    }
//-----------------------------------------------------------------------------------
    // to pass data to anther forgment
    open fun details(item:Result,imgLink:String,bitmap:Uri) = DetailsModel(
            imgLink,
            item.name!!,
             bitmap!!,
            item.businessStatus.toString(),
            item.vicinity.toString(),
            item.rating ?: 0.0,
            item.placeId!!,
            item.geometry.location

        )
    //---------------------------------------------------------------------------------------
   fun getBitmapFromView(view: ImageView): Bitmap? { //View //1
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }


    fun coverBitmaptouri(context: Context, bitmap: Bitmap?): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path!!.toString())
    }




}


