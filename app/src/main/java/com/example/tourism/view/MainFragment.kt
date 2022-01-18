package com.example.tourism.view

import android.os.Bundle
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
       // var resultFragment = ResultFragment()
        val bundle = Bundle()

        binding.naerby.setOnClickListener{
            bundle.putString("query","nearby")
            setFragmentResult("CODE", bundle)
            //resultFragment.arguments = bundle
            findNavController().navigate(R.id.action_blankFragment_to_mainFragment)

        }
        binding.hotel.setOnClickListener{
            bundle.putString("query","hotel")
            setFragmentResult("CODE", bundle)

            findNavController().navigate(R.id.action_blankFragment_to_mainFragment)

        }
        binding.airport.setOnClickListener{
            bundle.putString("query","airport")
            setFragmentResult("CODE", bundle)

            findNavController().navigate(R.id.action_blankFragment_to_mainFragment)
        }
        binding.museum.setOnClickListener{
            bundle.putString("query","museum")
            setFragmentResult("CODE", bundle)

            findNavController().navigate(R.id.action_blankFragment_to_mainFragment)
        }
         binding.coffee.setOnClickListener{
             bundle.putString("query","coffee")
             setFragmentResult("CODE", bundle)

             findNavController().navigate(R.id.action_blankFragment_to_mainFragment)
         }
        super.onViewCreated(view, savedInstanceState)
    }

}