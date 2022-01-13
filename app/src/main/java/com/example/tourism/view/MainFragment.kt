package com.example.tourism.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseAuth
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.checkSelfPermission
import com.example.tourism.view.Activity.LOCATION_PERMISSION_REQ_CODE
import com.example.tourism.view.Activity.LoginActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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
    lateinit var sharedPreferences: SharedPreferences
    lateinit var sharedPreferencesEditor: SharedPreferences.Editor

  //-------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
       Log.d(TAG,"onCreate")
        super.onCreate(savedInstanceState)
      fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    getCurrentLocation()

      arguments?.let {

        }
        setHasOptionsMenu(true)
    }
//-----------------------------------------------------------------------------------
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
    Log.d(TAG,"onCreateView")
        binding =  MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

//--------------------------------------------------------------------------------------
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    Log.d(TAG,"onViewCreated")
    sharedPreferences = requireActivity().getSharedPreferences("Settings", AppCompatActivity.MODE_PRIVATE)
    PlaceAdapter = PlacesRecyclerAdapter(placeViewModel,requireContext(),requireFragmentManager(),requireView())
    binding.MainViewRecyclerView.adapter = PlaceAdapter
   // fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
//    getCurrentLocation() // call
//    PlaceAdapter.notifyDataSetChanged()
//    val get = sharedPreferences.getInt("seek",1500)
//    placeViewModel.callPlace(26.394579, 50.194706,get)
   observers()// call
//--------------------------------------------------------------------------------------
    // for refresh page
    binding.swiperefreshlayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener(){
        getCurrentLocation()
        PlaceAdapter.notifyDataSetChanged()
        binding.swiperefreshlayout.setRefreshing(false)
})
//-------------------------------------------------------------set up toolbar like actionbar
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = ""

// for paging
//    binding.MainViewRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener()
//    {
//        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//            super.onScrollStateChanged(recyclerView, newState)
//            if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE){
//
//
//                getCurrentLocation()
//                Log.d(TAG, "fuc")
//            }
//        }
//
//    })

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        Log.d(TAG,"onViewStateRestored")
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onStart() {
        Log.d(TAG,"onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG,"onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG,"onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG,"onStop")
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG,"onSaveInstanceState")
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        Log.d(TAG,"onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d(TAG,"onDestroy")
        super.onDestroy()
    }
    //-------------------------------------------------------------------------------


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        requireActivity().menuInflater.inflate(R.menu.toolbar, menu)
        val searchItem = menu.findItem(R.id.app_bar_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                placeViewModel.searchPlace(query)
                searchView.clearFocus()
//

                return true

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        } )

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                getCurrentLocation()

                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    //----------------------------------------------------------------------------------------------
override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // for select logout will show you dialog and remove sharedperfernces
    if (item.itemId == R.id.logout) {
        val alertDialog = android.app.AlertDialog.Builder(context).setTitle("Logout")
            .setMessage("Are you sure you want to logout ?")
        alertDialog.setPositiveButton("Logout") { _, _ ->
            Log.i(TAG, "logout")
            FirebaseAuth.getInstance().signOut()
            this?.let {
                val intent = Intent(it.requireActivity(), LoginActivity::class.java)
                it.startActivity(intent)
            }
            sharedPreferences = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE)
            sharedPreferencesEditor = sharedPreferences.edit()

           sharedPreferencesEditor.putBoolean("isUserLogin",false)
            sharedPreferencesEditor.commit()

        }
        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        alertDialog.show()

    }
    //----------------------
        // when select profile open profileFragment
        if(item.itemId == R.id.profile){
            findNavController().navigate(R.id.action_mainFragment_to_profileFragment)
        }

 //-----------------------------------------
        if(item.itemId == R.id.filter){
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setTitle("")
            val view: View = layoutInflater.inflate(R.layout.dialog_seekbar_layout, null)
           val valueradius:SeekBar =view.findViewById(R.id.valueSeekBar)

            var startpoint = 0
            var endpoint = 0

            valueradius.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {


                    Log.d("SeekBarValue", seekBar?.progress.toString())
                    if(seekBar != null){
                        startpoint = seekBar.progress
                    }
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {
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
              // when select value will refresh page
                getCurrentLocation()
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

          //  Log.d(TAG, it.toString())
        })


    }
//----------------------------------------------------------------------------------------------------
     private fun getCurrentLocation() {
     //   Log.d("value current lan", "${latitude}")
        //Log.d("result current lon", "${longitude}")
        println(latitude)
        println(longitude)
        if (checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
         //   Log.d("MainActivityFragment", "sdfsdfsdfsdfsdfds")

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                // getting the last known or current location
                latitude = location.latitude
                longitude = location.longitude

             val get = sharedPreferences.getInt("seek",1500)

                placeViewModel.callPlace(latitude, longitude,get)

//------------------------------------------------------------------------------------------
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
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), LOCATION_PERMISSION_REQ_CODE
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        getCurrentLocation()
    }

}