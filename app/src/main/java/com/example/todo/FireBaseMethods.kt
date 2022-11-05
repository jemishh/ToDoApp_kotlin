package com.example.todo

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.todo.models.ToDoModel
import com.example.todo.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FireBaseMethods(context: Context) {

    private var fAuth: FirebaseAuth? = null
    private val fAuthListener: AuthStateListener? = null
    private var firebaseDatabase: FirebaseDatabase? = null
    private var storageReference: StorageReference? = null
    private var dbRef: DatabaseReference? = null

    private lateinit var context: Context
    public var userID = ""

    init {
        fAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        dbRef = firebaseDatabase!!.getReference()
        storageReference = FirebaseStorage.getInstance().reference
        this.context = context
        if (fAuth!!.getCurrentUser() != null) {
            userID = fAuth!!.getCurrentUser()!!.uid
            //Log.d(TAG, "FirebaseMethods: User ID- " + userID);
        } else {
            Log.d(ContentValues.TAG, "FirebaseMethods: didn't get the user ID")
        }
    }

    //register user to authentication
    fun registerNewUser(email: String?, password: String?) {
        fAuth!!.createUserWithEmailAndPassword(email!!, password!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Signup Successful", Toast.LENGTH_SHORT).show()
                userID = fAuth!!.currentUser!!.uid
                sendVerificationEmail()
            }
        }
    }

    //send email
    private fun sendVerificationEmail() {
        Log.d(TAG, "sendVerificationEmail: reached ")
        val user = FirebaseAuth.getInstance().currentUser
        Log.d(TAG, "sendVerificationEmail: $user")
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Please verify your email", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(context, "Couldn't send verification email", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    //add user details to database
    fun addNewUser(
        email: String,
        name: String,
        dob: String,
        age: String,
    ) {
        val user = UserModel(userID, email,name, dob, age)
        dbRef!!.child(context!!.getString(R.string.dbname_users))
            .child(userID)
            .setValue(user)

    }
    fun addTask(
        title: String,
        todo: String,
    ) {

        val todoModel = ToDoModel(title,todo)
        val newTaskKey = dbRef?.child(context.getString(R.string.dbname_tasks))?.push()?.getKey();

        if (newTaskKey != null) {
            dbRef!!.child(context.getString(R.string.dbname_users))
                .child(userID)
                .child(context.getString(R.string.dbname_tasks))
                .child(newTaskKey)
                .setValue(todoModel).addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Task Added", Toast.LENGTH_SHORT).show()
                    } else{
                        Toast.makeText(context, "Failed to add task", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }
}