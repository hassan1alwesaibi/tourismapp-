package com.example.tourism.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

import com.example.tourism.Model.Dto.Users
import com.example.tourism.R
import com.example.tourism.ViewModel.UsersViewModel
import com.example.tourism.adapters.UsersRecyclerAdapter
import com.example.tourism.databinding.FragmentUserlistBinding



class userlistFragment : Fragment() {
    private val usersList = mutableListOf<Users>()
    private val usersViewModel:UsersViewModel by activityViewModels()
    private lateinit var binding:FragmentUserlistBinding
    lateinit var userAdapter :UsersRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =  FragmentUserlistBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        userAdapter =  UsersRecyclerAdapter(usersList,usersViewModel,requireContext())
        binding.UsersViewRecyclerView.adapter = userAdapter
        usersViewModel.getlistUsers()

 //---------------------------------------------------when press in it will open mainFragment
        binding.close.setOnClickListener {
            findNavController().navigate(R.id.action_userlistFragment_to_mainFragment)
        }

    }
//--------------------------------------------------------------------------
    fun observe() {
        usersViewModel.getlistUserLiveData.observe(viewLifecycleOwner, {
            it?.let {
                usersList.addAll(it)
                userAdapter.notifyDataSetChanged()
            }
        })
    }
}