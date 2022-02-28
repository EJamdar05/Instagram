package com.ejamdar.instagram

import com.parse.*
import java.io.File

@ParseClassName("Post")

//Desc, image, and user
class PostClass : ParseObject() {
    fun getDesc() : String? {
        return getString(KEY_DESC)
    }

    fun setDesc(description : String){
        put(KEY_DESC, description)
    }

    fun setImage(file : ParseFile){
        put(KEY_IMAGE, file)
    }

    fun getImage() : ParseFile? {
        return getParseFile(KEY_IMAGE)
    }

    fun getUser(): ParseUser?{
        return getParseUser(KEY_USR)
    }

    fun setUser(user : ParseUser){
        put(KEY_USR, user)
    }

    companion object{
        const val KEY_DESC = "Description"
        const val KEY_IMAGE = "Image"
        const val KEY_USR = "User"
    }

}