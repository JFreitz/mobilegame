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

class playground_map : AppCompatActivity(), OnInitListener {

    private lateinit var questions: Array<String>
    private lateinit var answers: Array<Button>
    private lateinit var backButton: ImageButton
    private lateinit var textView: TextView
    private lateinit var textToSpeech: TextToSpeech
    private var currentQuestionIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playground_map)

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

        backButton = findViewById(R.id.back_button)  // Assuming there is a back button in your playground_map layout
        textView = findViewById(R.id.playground_popup)  // Assuming there is a TextView in your playground_map layout

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
            "Where is the tree with a swing?",
            "Where is the red see-saw?",
            "Where is the grass with flowers?",
            "Where is the sandpit?"
        )
    }

    private fun initializeViews() {
        answers = arrayOf(
            findViewById(R.id.playground_tree_with_swing),
            findViewById(R.id.playground_redsee_saw),
            findViewById(R.id.playground_grass_with_flowers),
            findViewById(R.id.playground_sand_box)
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
            0 -> "playground_tree_with_swing"
            1 -> "playground_redsee_saw"
            2 -> "playground_grass_with_flowers"
            3 -> "playground_sand_box"
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
