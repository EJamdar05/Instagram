package com.ejamdar.instagram.fragments

import android.util.Log
import com.ejamdar.instagram.PostClass
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

class ProfileFragment: HomeFragment() {
    override fun querryPosts(){
        val query: ParseQuery<PostClass> = ParseQuery.getQuery(PostClass::class.java)
        query.include(PostClass.KEY_USR) //findpost obj
        query.whereEqualTo(PostClass.KEY_USR, ParseUser.getCurrentUser()) //only for profile currently signed in
        query.findInBackground(object : FindCallback<PostClass> {
            //tell parse to find all post objs in server and return them
            override fun done(posts: MutableList<PostClass>?, e: ParseException?) {
                if (e != null){
                    Log.e(TAG, "Error getting posts")
                }else{
                    if(posts != null){
                        for(post in posts){
                            if(ParseUser.getCurrentUser().toString() == PostClass.KEY_USR){
                                Log.i(TAG, "Post: "+post.getDesc() + "UserName: "+post.getUser()?.username)
                            }
                        }
                        allPosts.addAll(posts)
                        allPosts.reverse()
                        adapterView.notifyDataSetChanged()
                    }
                }
            }

        })
    }
}