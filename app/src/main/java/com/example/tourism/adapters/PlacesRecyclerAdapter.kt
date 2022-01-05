package com.example.tourism.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas

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
import androidx.navigation.Navigation.findNavController


import com.example.tourism.Model.Dto.CollectionModel
import com.example.tourism.Model.Dto.DetailsModel



class PlacesRecyclerAdapter(val viewMode: PlaceViewModel,
                            val fileContext: Context,val fragmentManager:FragmentManager, val view:View) :
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

        if (item.photos != null) {
            imgLink = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photo_reference=${item.photos[0].photoReference}&key=AIzaSyAswnygZzJMdw9uEJ21KM5ZTLiAFj7Fogc"
        }
        val collection =
            CollectionModel(imgLink, item.name!!, "", item.geometry.location)

                if (item.photos != null) {
                    Glide.with(fileContext)
                        .load(imgLink)
                        .centerCrop()
                        .into(holder.pictureOfPlace)
                }
            holder.nameOfPlace.text = item.name
//-------------------------------------------------------------when press in pic show details and comment
            holder.pictureOfPlace.setOnClickListener {
                val bundle = Bundle()
                bundle.putParcelable("bitmap", getBitmapFromView(holder.pictureOfPlace))
                bundle.putParcelable("Location",collection)
                bundle.putParcelable("details",details(item,imgLink))//ask
                findNavController(view).navigate(R.id.action_mainFragment_to_detailsFragment,bundle)
            }
  //--------------------------------------------------------------- when press in 3dots open bottom sheet
        holder.Dots_Button.setOnClickListener {
            val bundle = Bundle()

            //pass information to another fragment
            bundle.putParcelable("bitmap", getBitmapFromView(holder.pictureOfPlace))
            bundle.putParcelable("Location",collection)
            bundle.putParcelable("details",details(item,imgLink))


            bottomsheetDialog.arguments = bundle
            bottomsheetDialog.show(fragmentManager,"")

   }

    }
//---------------------------------------------------------------------
    fun getBitmapFromView(view: ImageView): Bitmap? { //View //1
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
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
    open fun details(item:Result,imgLink:String) = DetailsModel(
            imgLink,
            item.name!!,
            item.businessStatus.toString(),
            item.vicinity.toString(),
            item.rating ?: 0.0,
            item.placeId!!
        )




}


