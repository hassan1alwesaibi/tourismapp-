package com.example.tourism.view

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.tourism.Model.Dto.Users
import com.example.tourism.R
import com.example.tourism.ViewModel.UsersViewModel
import com.example.tourism.view.Activity.LoginActivity
import com.example.tourism.view.Activity.sharedPreferences
import com.example.tourism.view.Activity.sharedPreferencesEditor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy
import java.io.File


class profileFragment : Fragment() {

    private val IMAGE_PICKER = 0
    private var users = Users()
    private val usersViewModel: UsersViewModel by activityViewModels()
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var email: EditText
    private lateinit var imge: ImageView
    private lateinit var gender: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        progressDialog = ProgressDialog(requireActivity())
        progressDialog.setTitle("Loading...")
        progressDialog.setCancelable(false)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        firstName = view.findViewById(R.id.firstnamep_edittext)
        lastName = view.findViewById(R.id.lastnamep_edittext)
        email = view.findViewById(R.id.emailp_edittext)
        gender = view.findViewById(R.id.gender_EditText)
        var delete: LinearLayout = view.findViewById(R.id.deleteaccount)
        val cancel: ImageButton = view.findViewById(R.id.cancelBtn)
        var addittoggleButton: ToggleButton = view.findViewById(R.id.addittoggleButton)

        imge = view.findViewById(R.id.profil_picture)

        cancel.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_mainFragment)
        }
        addittoggleButton.setOnClickListener {
            if (addittoggleButton.isChecked) {
                delete.isVisible = true
                imge.isEnabled = true
                firstName.isEnabled = true
                lastName.isEnabled = true
                // email.isEnabled = true
                gender.isEnabled = true


            } else {
                delete.isVisible = false
                imge.isEnabled = false
                firstName.isEnabled = false
                lastName.isEnabled = false
                // email.isEnabled = false
                email.isEnabled = false
                gender.isEnabled = false
                saveaddite()
            }
        }

        delete.setOnClickListener {
            val alertDialog = android.app.AlertDialog.Builder(context).setTitle("Delete account")
                .setMessage(
                    "Are you sure? All flights and information will be deleted." +
                            "That can't be undone"
                )
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
        imge.setOnClickListener {
            ImagePicker()
        }


        showPic()

        observers()
        usersViewModel.getUser()





        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICKER && resultCode == Activity.RESULT_OK) {
            progressDialog.show()
            val imagePath = Matisse.obtainResult(data)[0]
            Log.d("xxxxxxxxxxx", imagePath.toString())
            usersViewModel.UploadPhoto(imagePath)
        }
    }

    //-------------------------------------------------------------------
    fun ImagePicker() {
        Matisse.from(this)
            .choose(MimeType.ofImage(), false)
            .capture(true)
            .captureStrategy(CaptureStrategy(true, "com.example.tourism"))
            .forResult(IMAGE_PICKER)
    }

    fun showPic() {
        Glide.with(this)
            .load("https://firebasestorage.googleapis.com/v0/b/tourism-1de93.appspot.com/o/${FirebaseAuth.getInstance().uid.toString()}?alt=media&token=052d7e39-d904-4029-ab56-1d39c7e64a69")
            .centerCrop()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.userprofile)
            .into(imge)
    }

    //----------------------------------------------------------------------------------
    fun saveaddite() {
        users.apply {
            FirstName = firstName.text.toString()
            LastName = lastName.text.toString()
            Email = email.text.toString()
            Gender = gender.text.toString()

            usersViewModel.save(users)
        }
    }

    //---------------------------------------------------------------------------------
    fun observers() {
        usersViewModel.getUserLiveDate.observe(viewLifecycleOwner, {
            firstName.setText(it.FirstName)
            lastName.setText(it.LastName)
            email.setText(it.Email)
            gender.setText(it.Gender)
            Log.d(ContentValues.TAG, it.toString())
        })
        usersViewModel.UploadPhotosersLiveDate.observe(viewLifecycleOwner, {
            showPic()
            progressDialog.dismiss()

        })
        usersViewModel.saveusersLiveDate.observe(viewLifecycleOwner, {


        })
        usersViewModel.deleUserLiveDate.observe(viewLifecycleOwner, {

        })
    }


}
