package com.example.todo.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.todo.FireBaseMethods
import com.example.todo.R
import com.example.todo.adapter.rvAdapterUsers
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.databinding.FragmentRegistrationBinding
import com.example.todo.models.UserInfoModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var navControl: NavController
    private lateinit var userArraylist: ArrayList<UserInfoModel>
    var firebaseMethods: FireBaseMethods? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater , container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val fAuth = FirebaseAuth.getInstance()
        val user = fAuth.currentUser
        if (user == null) {
            //go back to login
        }

        binding.rvUsers.layoutManager = LinearLayoutManager(requireContext())
        firebaseMethods = FireBaseMethods(requireContext())
        binding.rvUsers.setHasFixedSize(true)

        userArraylist= arrayListOf<UserInfoModel>()
        binding.progressBar.bringToFront()
        binding.progressBar.setVisibility(View.VISIBLE)
        getUserData()
        super.onViewCreated(view, savedInstanceState)
    }


    private fun getUserData() {
        dbRef = FirebaseDatabase.getInstance().getReference("users")
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    userArraylist.clear()
                    for (userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(UserInfoModel :: class.java)
                        if(userSnapshot.key == firebaseMethods?.userID) {
                            binding.include.tvThisUsername.text = user!!.name
                            binding.include.tvThisEmail.text = user.email
                            binding.include.tvThisDob.text = user.dob
                            binding.include.tvThisAge.text = user.age
                            binding.progressBar.setVisibility(View.GONE)
                        }else {
                            userArraylist.add(user!!)
                        }
                    }
                    binding.rvUsers.adapter = rvAdapterUsers(userArraylist)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
                binding.progressBar.setVisibility(View.VISIBLE)
            }

        })
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    // Leave empty do disable back press or
                    activity?.finish()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }
}

