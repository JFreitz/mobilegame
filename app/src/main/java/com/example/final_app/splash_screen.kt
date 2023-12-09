package com.example.final_app

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.VideoView
import com.firstapp.final_app.MainActivity

class splash_screen : AppCompatActivity() {

    private val SPLASH_TIME: Long = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(R.layout.activity_splash_screen)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE


        val videoView =findViewById<VideoView>(R.id.loading_screen1)
        val packageName = "android.resource://" + getPackageName() + "/" + R.raw.loading_screen
        val uri = Uri.parse(packageName)
        videoView.setVideoURI(uri)
        videoView.start()

        Handler().postDelayed( {
            startActivity(Intent (this, MainActivity::class.java))
            finish()
        },SPLASH_TIME)
    }
}