package com.ejamdar.instagram.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ejamdar.instagram.MainActivity
import com.ejamdar.instagram.PostAdapter
import com.ejamdar.instagram.PostClass
import com.ejamdar.instagram.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery


open class HomeFragment : Fragment() {

    lateinit var postRecyclerView : RecyclerView
    lateinit var adapterView : PostAdapter
    var allPosts: MutableList<PostClass> = mutableListOf()
    lateinit var swipeContainer: SwipeRefreshLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    fun clear() {
        allPosts.clear()
        adapterView.notifyDataSetChanged()
    }

    fun addAll(instaPost: List<PostClass>) {
        allPosts.addAll(instaPost)
        adapterView.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeContainer = view.findViewById(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener {
            // Your code to refresh the list here.
            // Make sure you call swipeContainer.setRefreshing(false)
            // once the network request has completed successfully.
            swipeContainer.setRefreshing(false)

        }

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light);

        //set up views
        postRecyclerView = view.findViewById<RecyclerView>(R.id.postRecycleView)
        adapterView = PostAdapter(requireContext(), allPosts)
        postRecyclerView.adapter = adapterView
        postRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        queryPosts()
    }


    private fun queryPosts() {
        val query: ParseQuery<PostClass> = ParseQuery.getQuery(PostClass::class.java)
        query.include(PostClass.KEY_USR)
        query.findInBackground(object : FindCallback<PostClass> {
            //tell parse to find all post objs in server and return them
            override fun done(posts: MutableList<PostClass>?, e: ParseException?) {
                if (e != null){
                    Log.e(TAG, "Error getting posts")
                }else{
                    if(posts != null){
                        for(post in posts){
                            Log.i(TAG, "Post: "+post.getDesc() + "UserName: "+post.getUser()?.username)
                        }
                        allPosts.addAll(posts)
                        allPosts.reverse()
                        adapterView.notifyDataSetChanged()
                    }
                }
            }

        })
    }



    companion object{
        var TAG = "HomeFrag"
    }


    open fun querryPosts() {}
}