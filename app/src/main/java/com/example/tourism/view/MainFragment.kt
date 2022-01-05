package com.example.tourism.view

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseAuth
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.checkSelfPermission
import com.example.tourism.view.Activity.LOCATION_PERMISSION_REQ_CODE
import com.example.tourism.view.Activity.LoginActivity
import com.example.tourism.view.Activity.sharedPreferences
import com.example.tourism.view.Activity.sharedPreferencesEditor
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tourism.Model.Dto.DetailsModel
import com.example.tourism.R
import com.example.tourism.ViewModel.PlaceViewModel
import com.example.tourism.adapters.PlacesRecyclerAdapter
import com.example.tourism.databinding.MainFragmentBinding
import com.google.android.gms.location.LocationServices


const val TAG = "MainFragment"

private var latitude: Double = 0.0
private var longitude: Double = 0.0



class MainFragment : Fragment() {
    private val placeViewModel: PlaceViewModel by activityViewModels()
    private lateinit var PlaceAdapter: PlacesRecyclerAdapter
    private lateinit var binding: MainFragmentBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val detailsModel = DetailsModel()
    lateinit var sharedPreferences: SharedPreferences
    lateinit var sharedPreferencesEditor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences("Settings",
            AppCompatActivity.MODE_PRIVATE
        )

        PlaceAdapter = PlacesRecyclerAdapter(placeViewModel,requireContext(),requireFragmentManager(),requireView()
        )
        binding.MainViewRecyclerView.adapter = PlaceAdapter
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        getCurrentLocation() // call
        observers() // cll
        //--------------------------------------------------------------- set up toolbar like actionbar
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = ""

    }
    //-------------------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        requireActivity().menuInflater.inflate(R.menu.toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    //------------------------------------------------------------------------
override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.logout) { // for select logout will show you dialog
        val alertDialog = android.app.AlertDialog.Builder(context).setTitle("Logout")
            .setMessage("Are you sure you want to logout ?")
        alertDialog.setPositiveButton("Logout") { _, _ ->
            Log.i(TAG, "logout")
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
        if(item.itemId == R.id.profile){ // when select profile open profileFragment
            findNavController().navigate(R.id.action_mainFragment_to_profileFragment)

        }
        if(item.itemId == R.id.filter){
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setTitle("")
            val view: View = layoutInflater.inflate(R.layout.dialog_seekbar_layout, null)
            val valueradius : SeekBar = view.findViewById(R.id.valueSeekBar)
            val value: TextView = view.findViewById(R.id.value)
            var startpoint = 0
            var endpoint = 0
            valueradius.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                  value.text = progress.toString()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    if(seekBar != null){
                        startpoint = seekBar.progress
                    }


                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    if(seekBar != null){
                        endpoint = seekBar.progress

                        sharedPreferencesEditor = sharedPreferences.edit()
                        sharedPreferencesEditor.putInt("seek", endpoint)
                        sharedPreferencesEditor.commit()
                    }

                }
            })

            builder.setView(view)
            builder.setPositiveButton("Submit", { _, _ ->
                valueradius.progress
            })
            builder.setNegativeButton("Close", { _, _ -> })
            builder.show()


        }


    return super.onOptionsItemSelected(item)

}
//------------------------------------------------------------------------------------------
    fun observers () {
        placeViewModel.placesLiveDate.observe(viewLifecycleOwner,{
            PlaceAdapter.submitList(it)
            Log.d(TAG, it.toString())
        })

    }
//----------------------------------------------------------------------------------------------------
     private fun getCurrentLocation() {
        Log.d("value current lan", "${latitude}")
        Log.d("result current lon", "${longitude}")
        println(latitude)
        println(longitude)
        if (checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("MainActivityFragment", "sdfsdfsdfsdfsdfds")

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                // getting the last known or current location
                latitude = location.latitude
                longitude = location.longitude

             val get = sharedPreferences.getInt("seek",1500)

                placeViewModel.callPlace(latitude, longitude,get)

                Log.d("return for location lan", "${location.latitude}")
                Log.d("result for location lon", "${location.longitude}")

                Log.d("return for lan", "${latitude}")
                Log.d("result for lon", "${longitude}")

            }
                .addOnFailureListener {
                    Toast.makeText(
                        requireContext(), "Failed on getting current location",
                        Toast.LENGTH_SHORT
                    ).show()
                }

        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), LOCATION_PERMISSION_REQ_CODE
            )
        }
    }
    // for paremissions--------------------------------------------------------
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionsResult")

    }

}