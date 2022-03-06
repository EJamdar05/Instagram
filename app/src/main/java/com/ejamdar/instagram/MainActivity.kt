package com.ejamdar.instagram

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ejamdar.instagram.fragments.ComposeFragment
import com.ejamdar.instagram.fragments.HomeFragment
import com.ejamdar.instagram.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.*
import java.io.File

//let user make post by taking photo with camera

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //set desc of post
        //button to launch camera
        //image view to show picture

        //queryPosts()
        val fragmentManager: FragmentManager = supportFragmentManager


        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener {
            item ->
            var fragment : Fragment? = null
            when(item.itemId){
                R.id.action_home -> {
                    //navigate to home screen
                    fragment = HomeFragment()
                }
                R.id.action_compose ->{
                    fragment = ComposeFragment()
                }
                R.id.action_profile ->{
                    fragment = ProfileFragment()
                }
            }

            if(fragment != null){
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit()
            }
            true //user interaction handled
        }
        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.action_home

    }

    private fun queryPosts() {
        val query: ParseQuery<PostClass> = ParseQuery.getQuery(PostClass::class.java)
        query.include(PostClass.KEY_USR)
        query.findInBackground(object : FindCallback<PostClass>{
            //tell parse to find all post objs in server and return them
            override fun done(posts: MutableList<PostClass>?, e: ParseException?) {
                if (e != null){
                    Log.e(TAG, "Error getting posts")
                }else{
                    if(posts != null){
                        for(post in posts){
                            Log.i(TAG, "Post: "+post.getDesc() + "UserName: "+post.getUser()?.username)
                        }
                    }
                }
            }

        })
    }


    companion object{
        const val TAG = "MainActivity"
    }
}