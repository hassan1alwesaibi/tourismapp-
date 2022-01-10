package com.example.tourism.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.tourism.Model.Dto.Users
import com.example.tourism.R
import com.example.tourism.ViewModel.UsersViewModel
import com.example.tourism.adapters.UsersRecyclerAdapter
import com.example.tourism.databinding.FragmentUserlistBinding
import com.example.tourism.databinding.MainFragmentBinding
import com.google.firebase.firestore.auth.User


class userlistFragment : Fragment() {
    private val usersList = mutableListOf<Users>()
    private val usersViewModel:UsersViewModel by activityViewModels()
    private lateinit var binding:FragmentUserlistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentUserlistBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userAdapter = UsersRecyclerAdapter(usersList,usersViewModel,requireContext())
        binding.UsersViewRecyclerView.adapter = userAdapter
        usersViewModel.getlistUsers()
        usersViewModel.getlistUserLiveData.observe(viewLifecycleOwner,{
            it?.let{
                usersList.addAll(it)
                userAdapter.notifyDataSetChanged()
            }
        })

    }
}