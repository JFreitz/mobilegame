package com.firstapp.final_app

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.speech.tts.TextToSpeech.QUEUE_FLUSH
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.final_app.ImageAdapter
import com.example.final_app.R
import com.example.final_app.bathroom_map
import com.example.final_app.bedroom_map
import com.example.final_app.classroom_map
import com.example.final_app.kitchen_map
import com.example.final_app.library_map
import com.example.final_app.living_room_map
import com.example.final_app.office_map
import com.example.final_app.playground_map
import kotlin.math.abs
import java.util.Locale

class MainActivity : AppCompatActivity(), OnInitListener {

    private lateinit var viewPager2: ViewPager2
    private lateinit var imageList: ArrayList<Int>
    private lateinit var adapter: ImageAdapter
    private lateinit var textToSpeech: TextToSpeech
    private var lastPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        init()
        setUpTransformer()
        setUpPageChangeListener()
        setUpImageClickListeners()

        // Initialize TextToSpeech
        textToSpeech = TextToSpeech(this, this)
    }

    private fun setUpTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }

        viewPager2.setPageTransformer(transformer)
    }

    private fun init() {
        viewPager2 = findViewById(R.id.viewPager2)
        imageList = ArrayList()

        imageList.add(R.drawable.bedroom)
        imageList.add(R.drawable.bathroom)
        imageList.add(R.drawable.classroom)
        imageList.add(R.drawable.kitchen)
        imageList.add(R.drawable.library)
        imageList.add(R.drawable.living_room)
        imageList.add(R.drawable.office)
        imageList.add(R.drawable.playground)

        adapter = ImageAdapter(imageList, viewPager2)

        viewPager2.adapter = adapter
        viewPager2.offscreenPageLimit = 1
        viewPager2.clipToPadding = false
        viewPager2.clipChildren = false
        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }

    private fun setUpPageChangeListener() {
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                lastPage = position
            }
        })
    }

    private fun setUpImageClickListeners() {
        adapter.setOnItemClickListener(object : ImageAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> {
                        // Handle click for bedroom
                        speakOut("Welcome to the Bedroom")
                        startActivity(Intent(this@MainActivity, bedroom_map::class.java))
                    }
                    1 -> {
                        // Handle click for bathroom
                        speakOut("Welcome to the Bathroom")
                        startActivity(Intent(this@MainActivity, bathroom_map::class.java))
                    }
                    2 -> {
                        // Handle click for classroom
                        speakOut("Welcome to the Classroom")
                        startActivity(Intent(this@MainActivity, classroom_map::class.java))
                    }
                    3 -> {
                        // Handle click for kitchen
                        speakOut("Welcome to the Kitchen")
                        startActivity(Intent(this@MainActivity, kitchen_map::class.java))
                    }
                    4 -> {
                        // Handle click for library
                        speakOut("Welcome to the Library")
                        startActivity(Intent(this@MainActivity, library_map::class.java))
                    }
                    5 -> {
                        // Handle click for living room
                        speakOut("Welcome to the Living Room")
                        startActivity(Intent(this@MainActivity, living_room_map::class.java))
                    }
                    6 -> {
                        // Handle click for office
                        speakOut("Welcome to the Office")
                        startActivity(Intent(this@MainActivity, office_map::class.java))
                    }
                    7 -> {
                        // Handle click for playground
                        speakOut("Welcome to the Playground")
                        startActivity(Intent(this@MainActivity, playground_map::class.java))
                    }
                }
            }
        })
    }

    private fun speakOut(text: String) {
        textToSpeech.speak(text, QUEUE_FLUSH, null, null)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        viewPager2.isUserInputEnabled = true
    }

    override fun onBackPressed() {
        if (lastPage == imageList.size - 1) {
            viewPager2.isUserInputEnabled = false
        } else {
            super.onBackPressed()
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Set language (you may want to customize this based on your app's requirements)
            val languageResult = textToSpeech.setLanguage(Locale.US)

            if (languageResult == TextToSpeech.LANG_MISSING_DATA || languageResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                showToast("Text-to-Speech language is not supported.")
            }
        } else {
            showToast("Text-to-Speech initialization failed.")
        }
    }

    override fun onDestroy() {
        // Shutdown TextToSpeech when the activity is destroyed
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }
}
