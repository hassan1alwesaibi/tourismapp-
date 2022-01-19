package com.example.tourism.view

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri


import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.tourism.Model.Dto.Users
import com.example.tourism.R
import com.example.tourism.ViewModel.UsersViewModel
import com.example.tourism.databinding.FragmentProfileBinding
import com.example.tourism.view.Activity.LoginActivity
import com.example.tourism.view.Activity.sharedPreferences
import com.example.tourism.view.Activity.sharedPreferencesEditor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy



class profileFragment : Fragment() {
    private val IMAGE_PICKER = 0
    private var users = Users()
    private val usersViewModel: UsersViewModel by activityViewModels()
    private lateinit var binding: FragmentProfileBinding
    private lateinit var progressDialog: ProgressDialog
    var imagePath:Uri = "".toUri()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        progressDialog = ProgressDialog(requireActivity())
        progressDialog.setTitle("Loading...")
        progressDialog.setCancelable(false)

        // Inflate the layout for this fragment
        binding =  FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.spinner.setEnabled(false)

  //--------------------------------------- // to close profile and go to main fragment
        binding.close.setOnClickListener {
            findNavController().popBackStack()
        }
   //-----------------------------------------// when press to edit let you allow to change information
        binding.edittoggleButton.setOnClickListener {
            if (binding.edittoggleButton.isChecked) {
                binding.deleteaccount.isVisible = true
                binding.profilPicture.isEnabled = true

                binding.profilPicture.setOnClickListener {
                    ImagePicker()
                }
                binding.firstnamepEdittext.isEnabled = true
                binding.lastnamepEdittext.isEnabled = true
                binding.spinner.setEnabled(true)
            } else {
                binding.deleteaccount.isVisible = false
                binding.profilPicture.isEnabled = false
                binding.firstnamepEdittext.isEnabled = false
                binding.lastnamepEdittext.isEnabled = false
                binding.spinner.setEnabled(false)
//                imagePath.let {
//                    usersViewModel.UploadPhoto(imagePath!!)
//
//                }
                saveaddite() // save change
            }
        }

//-----------------------------------------------------// to delet account
        binding.deleteaccount.setOnClickListener {
            deleteDialog()
        }
//-------------------------------------
        showPic("https://firebasestorage.googleapis.com/v0/b/tourism-1de93.appspot.com/o/${FirebaseAuth.getInstance().uid.toString()}?alt=media&token=052d7e39-d904-4029-ab56-1d39c7e64a69")//call
        observers()//call
        usersViewModel.getUser()//call
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICKER && resultCode == Activity.RESULT_OK) {
//            progressDialog.show()
            imagePath = Matisse.obtainResult(data)[0]
            Log.d("xxxxxxxxxxx", imagePath.toString())
            showPic(imagePath.toString())
//          usersViewModel.UploadPhoto(imagePath)
        }
    }

//-------------------------------------------------------------------------
    fun deleteDialog(){
        val alertDialog = android.app.AlertDialog.Builder(context).setTitle("Delete account")
            .setMessage(
                "Are you sure? All flights and information will be deleted." +
                        "That can't be undone")
            alertDialog.setPositiveButton("Delete") { _, _ ->
            Log.i(ContentValues.TAG, "Delete")
            usersViewModel.deleteuser()
                val user = Firebase.auth.currentUser!!
                user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User account deleted.")
                    }
                }
                FirebaseAuth.getInstance().signOut()
                this?.let {
                val intent = Intent(it.requireActivity(), LoginActivity::class.java)
                it.startActivity(intent)
            }
                requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE)
                sharedPreferencesEditor = sharedPreferences.edit()
                sharedPreferencesEditor.remove("isUserLogin")
                sharedPreferencesEditor.commit()
                requireActivity().finish()
        }
        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        alertDialog.show()
    }
    //-------------------------------------------------------------------for imge picker and show
    fun ImagePicker() {
        Matisse.from(this)
            .choose(MimeType.ofImage(), false)
            .capture(true)
            .captureStrategy(CaptureStrategy(true, "com.example.tourism"))
            .forResult(IMAGE_PICKER)
    }

    fun showPic(imagePath: String) {
        Glide.with(this)
            .load(imagePath)
            .centerCrop()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.userprofile)
            .into(binding.profilPicture)
    }


    //-------------------------------------------------------------------------save edit
    fun saveaddite() {
        users.apply {
            FirstName = binding.firstnamepEdittext.text.toString()
            LastName = binding.lastnamepEdittext.text.toString()
            Email =    binding.emailpEdittext.text.toString()
            Gender = binding.spinner.selectedItem.toString()
            usersViewModel.UploadPhoto(imagePath)
            usersViewModel.save(users)

        }
    }
    //---------------------------------------------------------------------------------
    fun observers() {
        usersViewModel.getUserLiveDate.observe(viewLifecycleOwner, {
            binding.firstnamepEdittext.setText(it.FirstName)
            binding.lastnamepEdittext.setText(it.LastName)
            binding.emailpEdittext.setText(it.Email)

            when(it.Gender) {    // to save Selection category when get out from app

                "Male" ->  binding.spinner.setSelection(1)
                "Female" -> binding.spinner.setSelection(2)
                "Other" -> binding.spinner.setSelection(3)
            }
            Log.d(ContentValues.TAG, it.toString())
        })
        usersViewModel.UploadPhotosersLiveDate.observe(viewLifecycleOwner, {
            showPic("https://firebasestorage.googleapis.com/v0/b/tourism-1de93.appspot.com/o/${FirebaseAuth.getInstance().uid.toString()}?alt=media&token=052d7e39-d904-4029-ab56-1d39c7e64a69")
            progressDialog.dismiss()
        })
        usersViewModel.saveusersLiveDate.observe(viewLifecycleOwner, {
        })
        usersViewModel.deleUserLiveDate.observe(viewLifecycleOwner, {
        })
    }


}
