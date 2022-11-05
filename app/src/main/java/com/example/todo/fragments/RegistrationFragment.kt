package com.example.todo.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todo.FireBaseMethods
import com.example.todo.R
import com.example.todo.databinding.FragmentRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class RegistrationFragment : Fragment() {


    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var binding: FragmentRegistrationBinding
    //vars
    private lateinit var email: String
    private lateinit var name: String
    private lateinit var dob: String
    private lateinit var ageString: String

    //firebase
    var firebaseMethods: FireBaseMethods? = null
    var fAuthListener: FirebaseAuth.AuthStateListener? = null
    var firebaseDatabase: FirebaseDatabase? = null
    var dbRef: DatabaseReference? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegistrationBinding.inflate(inflater , container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        firebaseMethods = FireBaseMethods(requireContext())

        onClickEvents()
        setupFirebaseAuth()

        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        super.onViewCreated(view, savedInstanceState)


    }
    private fun onClickEvents(){
        binding.tetDob.setShowSoftInputOnFocus(false)
        //date picker
        var cal = Calendar.getInstance()
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDate(cal)
            }
        }
        binding.tetDob.setOnClickListener {
            val dialog = DatePickerDialog(requireContext(), dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH))
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis())
            dialog.show()
        }

        binding.btnSignUp.setOnClickListener {
            closeKeyboard()
            email = binding.tetEmail.text.toString()
            name = binding.tetName.text.toString()
            val password = binding.tetPassword.text.toString()
            val confirmPass = binding.tetConfirmPass.text.toString()
            dob = binding.tetDob.text.toString()
            ageString = binding.tvAge.text.toString()

            if(email.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty() && confirmPass.isNotEmpty() && dob.isNotEmpty() ) {
                if(password == confirmPass && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.progressBar.setVisibility(View.VISIBLE)
                    firebaseMethods?.registerNewUser(email, password)
                }
            }
        }


        binding.tvToLogIn.setOnClickListener {
            navControl.navigate(R.id.action_registrationFragment_to_loginFragment)
        }

    }
    private fun updateDate(cal : Calendar){
        val format = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(format, Locale.US)
        val dob=sdf.format(cal.time).toString()
        binding.tetDob.setText(sdf.format(cal.time))
        var s = dob.subSequence(dob.length-4,dob.length).toString()
        val birthYear: Int = s.toInt()
        val age = Calendar.getInstance().get(Calendar.YEAR) - birthYear
        val ageString = age.toString()
        binding.tvAge.setText(ageString)
    }

    /*------------------------------------Firebase--------------------------------------------------*/
    private fun setupFirebaseAuth() {
        Log.d(ContentValues.TAG, "setupFirebaseAuth: Setting up firebase auth.")
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        dbRef = firebaseDatabase!!.reference
        fAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth: FirebaseAuth? ->
            val user = auth.currentUser
            if (user != null) {
                //user is signed in
                Log.d(ContentValues.TAG, "onAuthStateChanged: signed_in:" + user.uid)
                dbRef!!.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        firebaseMethods?.addNewUser(email, name, dob,ageString)
                        binding.progressBar.setVisibility(View.GONE)
                        auth.signOut()
                        navControl.navigate(R.id.action_registrationFragment_to_loginFragment)

                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
            } else {
                //user is signed out
            }
        }
    }
    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(fAuthListener!!)
        Log.d(ContentValues.TAG, "onStart: start")
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(fAuthListener!!)
        Log.d(ContentValues.TAG, "onStop: stop")
    }

    private fun closeKeyboard(): Boolean {
        val view = activity?.currentFocus
        if (view != null) {
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        return true
    }

    //Handling backpress
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    // Leave empty do disable back press or
                    navControl.navigate(R.id.action_registrationFragment_to_loginFragment)                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }
}