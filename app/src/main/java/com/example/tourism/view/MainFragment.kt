package com.example.tourism.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.tourism.R
import com.example.tourism.databinding.FragmentMainBinding
import com.example.tourism.databinding.FragmentProfileBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.naerby.setOnClickListener{
            callresult("nearby")
        }
        binding.hotel.setOnClickListener{
            callresult("hotel")
        }
        binding.airport.setOnClickListener{
            callresult("airport")

        }
        binding.museum.setOnClickListener{
            callresult("museum")
        }
         binding.coffee.setOnClickListener{
             callresult("coffee")
         }
        binding.restaurants.setOnClickListener{
            callresult("restaurants")
        }
        binding.car.setOnClickListener{
            callresult("car rental")
        }
        binding.hospitals.setOnClickListener{
            callresult("hospitals")
        }
        binding.banks.setOnClickListener{
            callresult("banks")
        }
        binding.entertainments.setOnClickListener{
            callresult("entertainments")
        }
        super.onViewCreated(view, savedInstanceState)
    }

// this function open result fragment
    fun callresult(places:String){
        val bundle = Bundle()
        // pass query to athor fragment
        bundle.putString("query",places)
        setFragmentResult("CODE", bundle)

        findNavController().navigate(R.id.action_blankFragment_to_mainFragment)

    }

}