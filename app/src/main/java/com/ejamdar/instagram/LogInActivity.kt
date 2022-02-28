package com.ejamdar.instagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.Parse;
import com.parse.ParseUser

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        //check userlogin and if it exists, go to main activity
        if(ParseUser.getCurrentUser() != null){
            goToMainActivity()
        }

        findViewById<Button>(R.id.signInButton).setOnClickListener{
            val userName = findViewById<EditText>(R.id.userNameField).text.toString();
            val password = findViewById<EditText>(R.id.passwordField).text.toString();
            loginUser(userName, password);
        }

        findViewById<Button>(R.id.signUpButton).setOnClickListener{
            val userName = findViewById<EditText>(R.id.userNameField).text.toString();
            val password = findViewById<EditText>(R.id.passwordField).text.toString();
            signUpUser(userName, password);
        }

    }

    private fun loginUser(userName: String, password: String){
        ParseUser.logInInBackground(userName, password, ({ user, e ->
            if (user != null) {
                Log.i(TAG, "Successful login")
                goToMainActivity()
            } else {
                Log.e(TAG, "Failed")
                e.printStackTrace()
                Toast.makeText(this,"Error logging in", Toast.LENGTH_SHORT).show()
            }})
        )
    }

    private fun signUpUser(userName: String, password: String){
        // Create the ParseUser
        val user = ParseUser()

        // Set fields for the user to be created
        user.setUsername(userName)
        user.setPassword(password)

        user.signUpInBackground { e ->
            if (e == null) {
                Log.i(TAG, "Successful login")
                goToMainActivity()
            } else {
                Log.e(TAG, "Failed")
                e.printStackTrace()
                Toast.makeText(this,"Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() //once successful login, closes login activity
    }

    companion object{
        val TAG = "LoginActivity"
    }
}