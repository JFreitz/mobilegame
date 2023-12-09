package com.example.final_app

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.firstapp.final_app.MainActivity
import java.util.*

class living_room_map : AppCompatActivity(), OnInitListener {

    private lateinit var questions: Array<String>
    private lateinit var answers: Array<Button>
    private lateinit var backButton: ImageButton
    private lateinit var textView: TextView
    private lateinit var textToSpeech: TextToSpeech
    private var currentQuestionIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_living_room_map)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        initializeQuestions()
        initializeViews()

        // Set up Text-to-Speech
        textToSpeech = TextToSpeech(this, this)

        for (button in answers) {
            button.setOnClickListener {
                checkAnswer(button)
            }
        }

        backButton = findViewById(R.id.back_button)  // Assuming there is a back button in your living_room_map layout
        textView = findViewById(R.id.living_room_popup)  // Assuming there is a TextView in your living_room_map layout

        backButton.setOnClickListener {
            // Stop Text-to-Speech when leaving the activity
            textToSpeech.stop()
            textToSpeech.shutdown()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Add a delay before speaking the first question
        Handler(Looper.getMainLooper()).postDelayed({
            setQuestion()
        }, 2500) // Adjust the delay time as needed
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED
            ) {
                // Handle error if language data is missing or not supported
            }
        } else {
            // Handle initialization error
        }
    }

    private fun initializeQuestions() {
        questions = arrayOf(
            "Where is the TV?",
            "Where is the big frame?",
            "Where is the shelf?",
            "Where is the table?"
        )
    }

    private fun initializeViews() {
        answers = arrayOf(
            findViewById(R.id.living_room_tv),
            findViewById(R.id.living_room_bigframe),
            findViewById(R.id.living_room_shelf),
            findViewById(R.id.living_room_table)
        )
    }

    private fun setQuestion() {
        if (currentQuestionIndex < questions.size) {
            textView.text = questions[currentQuestionIndex]

            // Speak the current question with the modified text
            speakOut("Can you tell me ${questions[currentQuestionIndex]}")
        } else {
            // All questions answered correctly
            textView.text = "Congratulations! You answered all questions correctly."
            speakOut("Great job! Congratulations! You answered all questions correctly.")
        }
    }

    private fun checkAnswer(selectedButton: Button) {
        val correctAnswerText = when (currentQuestionIndex) {
            0 -> "living_room_tv"
            1 -> "living_room_bigframe"
            2 -> "living_room_shelf"
            3 -> "living_room_table"
            else -> ""
        }

        if (selectedButton.tag == correctAnswerText) {
            currentQuestionIndex++

            // Move to the next question and speak it
            setQuestion()
        } else {
            // Incorrect answer, display the current question in the message
            textView.text = "Wrong Answer, ${questions[currentQuestionIndex]}"
            speakOut("Oops! Wrong Answer. ${questions[currentQuestionIndex]}")
        }
    }

    private fun speakOut(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }
}
