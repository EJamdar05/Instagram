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
import com.parse.*
import java.io.File

//let user make post by taking photo with camera

class MainActivity : AppCompatActivity() {
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    val photoFileName = "photo.jpg"
    var photoFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //set desc of post
        //button to launch camera
        //image view to show picture
        findViewById<Button>(R.id.cameraButton).setOnClickListener{
            onLaunchCamera()
        }
        //button to send and save post
        findViewById<Button>(R.id.postButton).setOnClickListener{
            //send post without image
            //get desc that user inputted
            val desc = findViewById<EditText>(R.id.descriptionTextField).text.toString()
            val user = ParseUser.getCurrentUser()
            submitPost(desc, user, photoFile)
        }

        queryPosts()
        findViewById<Button>(R.id.SignOutButton).setOnClickListener{
            ParseUser.logOut()
            val currentUser = ParseUser.getCurrentUser()
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }

    }

    private fun submitPost(desc: String, user: ParseUser, file: File?) {
        //send post obj to parse server
        //create post obj
        val post = PostClass()
        post.setDesc(desc)
        post.setUser(user)
        post.setImage(ParseFile(file))
        post.saveInBackground{ exception ->
            if (exception != null){
                Log.e(TAG, "Error posting the post")
                exception.printStackTrace()
                Toast.makeText(this,"Something went wrong", Toast.LENGTH_SHORT).show()
            }else{
                Log.i(TAG, "Successful post creation")
                Toast.makeText(this,"Post successfully made", Toast.LENGTH_SHORT).show()
                val textField = findViewById<EditText>(R.id.descriptionTextField).setText("")
                val imageField = findViewById<ImageView>(R.id.postImageField).setImageResource(0)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) { //code is same
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath) //change to bmp
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                val ivPreview: ImageView = findViewById(R.id.postImageField) //set to image view
                ivPreview.setImageBitmap(takenImage)
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE) //open the camera app
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName) //specify where top save the photo

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) //start camera app, take photo, save a file with the code
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
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