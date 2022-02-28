package com.ejamdar.instagram

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.parse.Parse
import com.parse.ParseObject

class ParseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //configure and init parse at launch
        ParseObject.registerSubclass(PostClass::class.java) //using the class
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
    }

}

