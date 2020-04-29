package com.example.kebon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.kebon.sign.SignInActivity
import com.example.kebon.utils.Preferences

class SplashScreenActivity : AppCompatActivity() {
lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        preferences= Preferences(this)

        val handler = Handler()
        handler.postDelayed({
            if (preferences.getValues("status").equals("1")){
                val intent=Intent (this@SplashScreenActivity,MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this@SplashScreenActivity,
                    SignInActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 5000)
    }
}
