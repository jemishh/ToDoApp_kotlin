package com.example.todo.fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.FireBaseMethods
import com.example.todo.MainActivity
import com.example.todo.R
import com.example.todo.adapter.rvAdapterToDos
import com.example.todo.adapter.rvAdapterUsers
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.databinding.FragmentToDoBinding
import com.example.todo.models.ToDoModel
import com.example.todo.models.UserInfoModel
import com.google.firebase.database.*

class ToDoFragment : Fragment() {


    private lateinit var binding: FragmentToDoBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var todoList: ArrayList<ToDoModel>
    var firebaseMethods: FireBaseMethods? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentToDoBinding.inflate(inflater , container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvToDos.layoutManager = LinearLayoutManager(requireContext())
        firebaseMethods = FireBaseMethods(requireContext())
        binding.rvToDos.setHasFixedSize(true)

        todoList= arrayListOf<ToDoModel>()
        binding.progressBar.bringToFront()
        binding.progressBar.setVisibility(View.VISIBLE)
        getUserData()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getUserData() {
        dbRef = FirebaseDatabase.getInstance().getReference()
        firebaseMethods?.let {
            context?.let { it1 ->
                dbRef!!.child(requireContext().getString(R.string.dbname_users))
                    .child(it.userID).child(it1.getString(R.string.dbname_tasks))
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()){
                                todoList.clear()
                                for (todoSnapshot in snapshot.children){
                                    //Log.d(TAG, "onDataChange: $todoSnapshot")
                                    val todo = todoSnapshot.getValue(ToDoModel :: class.java)
                                    todoList.add(todo!!)
                                }
                            }
                            binding.rvToDos.adapter = rvAdapterToDos(todoList)
                            binding.progressBar.setVisibility(View.GONE)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                            binding.progressBar.setVisibility(View.VISIBLE)
                        }

                    })
            }

        }

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    // Leave empty do disable back press or
                    (activity as MainActivity?)?.loadFragment(HomeFragment())
                    (activity as MainActivity?)?.bottomNavigation?.show(1, true)

                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }
}