package com.example.todo.fragments

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.todo.FireBaseMethods
import com.example.todo.MainActivity
import com.example.todo.R
import com.example.todo.databinding.FragmentAddBinding
import com.example.todo.databinding.FragmentHomeBinding


class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding
    //firebase
    var firebaseMethods: FireBaseMethods? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater , container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        firebaseMethods = FireBaseMethods(requireContext())
        super.onViewCreated(view, savedInstanceState)

        binding.btnAdd.setOnClickListener {
            closeKeyboard()
            val title = binding.etTitle.text.toString()
            val todo = binding.etToDo.text.toString()

            if(title.isNotEmpty() && todo.isNotEmpty()) {
                   firebaseMethods?.addTask(title, todo)
                        /*binding.etTitle.text.clear()
                        binding.etToDo.text.clear()*/
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

    private fun closeKeyboard(): Boolean {
        val view = activity?.currentFocus
        if (view != null) {
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        return true
    }
}