package com.example.todo.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.helper.widget.MotionEffect
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todo.R
import com.example.todo.databinding.FragmentLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater , container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onClickEvents()

        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun onClickEvents(){
        binding.tvToSignUp.setOnClickListener{
            navControl.navigate(R.id.action_loginFragment_to_registrationFragment)
        }
        binding.btnLogIn.setOnClickListener {

            val email = binding.tetEmail.text.toString()
            val password = binding.tetPassword.text.toString()

            if(email.isNotEmpty()&& password.isNotEmpty()) {
                binding.progressBar.setVisibility(View.VISIBLE)
                closeKeyboard()

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                    OnCompleteListener { task: Task<AuthResult?> ->
                        val user: FirebaseUser? = auth.getCurrentUser()
                        if (!task.isSuccessful) {
                            Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT)
                                .show()
                            binding.progressBar.setVisibility(View.INVISIBLE)
                        } else {
                            try {
                                if (user != null) {
                                    if (user.isEmailVerified) {
                                        navControl.navigate(R.id.action_loginFragment_to_mainActivity)
                                        /*val intent =
                                            Intent(activity, MainActivity::class.java)
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        startActivity(intent)
                                        Toast.makeText(activity, "welcome", Toast.LENGTH_SHORT)
                                            .show()
                                        activity?.finish()*/
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Email is not verified \n Check your mail inbox",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        user.sendEmailVerification()
                                        binding.progressBar.setVisibility(View.INVISIBLE)
                                        auth.signOut()
                                    }
                                }
                            } catch (e: NullPointerException) {
                                Log.e(
                                    MotionEffect.TAG,
                                    "onCreate: NullPointerException" + e.message
                                )
                            }
                        }
                    })
            }
        }

    }

    private fun closeKeyboard(): Boolean {
        val view = activity?.currentFocus
        if (view != null) {
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        return true
    }

    //Handle back press
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