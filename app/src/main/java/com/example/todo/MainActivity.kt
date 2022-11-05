package com.example.todo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.todo.databinding.ActivityMainBinding
import com.example.todo.fragments.AddFragment
import com.example.todo.fragments.HomeFragment
import com.example.todo.fragments.ToDoFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    public lateinit var bottomNavigation: MeowBottomNavigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setData()
        setClickListener()

    }
    private fun setClickListener(){
        binding.tvLogOut.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            finish()
            startActivity(Intent(this, StartActivity::class.java))
        }
        binding.scTheme.setOnCheckedChangeListener{ buttonView, isChecked ->
            if(isChecked){
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                binding.tvTheme.setText("DARK")
            } else{
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
                binding.tvTheme.setText("LIGHT")
            }

        }
    }
    private fun setData(){
    //add menu item to bottom navigation
        bottomNavigation= findViewById(R.id.bottomNavigation)
        binding.bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.ic_home))
        binding.bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.ic_plus))
        binding.bottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.ic_notes))
        binding.bottomNavigation.setOnShowListener { item: MeowBottomNavigation.Model ->
            //initialize fragment
            var fragment: Fragment? = null
            when (item.id) {
                1 -> fragment = HomeFragment()
                2 -> fragment = AddFragment()
                3 -> fragment = ToDoFragment()
            }
            //load fragment
            loadFragment(fragment)
        }
        binding.bottomNavigation.show(1, true)
        binding.bottomNavigation.setOnClickMenuListener { item: MeowBottomNavigation.Model? -> }
        binding.bottomNavigation.setOnReselectListener { item: MeowBottomNavigation.Model ->
            //initialize fragment
            var fragment: Fragment? = null
            when (item.id) {
                1 -> fragment = HomeFragment()
                2 -> fragment = AddFragment()
                3 -> fragment = ToDoFragment()
            }
            //load fragment
            //loadFragment(fragment)
        }
    }

    fun loadFragment(fragment: Fragment?) {
        //replace fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_main, fragment!!)
            .commit()
    }


}