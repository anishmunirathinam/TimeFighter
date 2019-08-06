package com.anish.timefighter

import android.content.Context
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    internal lateinit var tapMe: Button
    internal lateinit var scoreTextView: TextView
    internal lateinit var timeLeftTextView: TextView
    internal lateinit var countDownTimer: CountDownTimer

    internal var score: Int = 0
    internal var gameStarted = false
    internal val initalCountDown: Long = 60000
    internal val countDownInterval: Long = 1000
    internal var timeLeftOnTimer: Long = 60000

    companion object {
        private val SCORE_KEY = "SCORE_KEY"
        private val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    internal val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "score: $score")
        tapMe = findViewById<Button>(R.id.tap_me_button)
        scoreTextView = findViewById<TextView>(R.id.your_score_text_view)
        timeLeftTextView = findViewById<TextView>(R.id.time_left_text_view)
        resetGame()

        tapMe.setOnClickListener {
            _ ->
            handleTap()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SCORE_KEY, score)
        outState.putLong(TIME_LEFT_KEY, timeLeftOnTimer)
        countDownTimer.cancel()
        Log.d(TAG, "onSaveInstanceState score: $score, time left: $timeLeftOnTimer" )
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy")
    }

    private fun resetGame() {
        score = 0
        scoreTextView.text = getString(R.string.your_score, score.toString())
        val initalTimeLeft = initalCountDown / countDownInterval
        timeLeftTextView.text = getString(R.string.time_left, initalTimeLeft.toString())
        countDownTimer = object : CountDownTimer(initalCountDown, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                val timeLeft = millisUntilFinished/1000
                timeLeftTextView.text = getString(R.string.time_left, timeLeft.toString())
            }

            override fun onFinish() {
                endGame()
            }
        }
        gameStarted = false
    }

    private fun startGame() {
        gameStarted = true
        countDownTimer.start()
    }

    private fun endGame() {
        Toast.makeText(this, getString(R.string.end_game_message, score.toString()), Toast.LENGTH_SHORT).show()
        resetGame()
    }

    private fun handleTap() {
        if (!gameStarted) {
            startGame()
        }
        score = score + 1
        val newScore = getString(R.string.your_score, score.toString())
        scoreTextView.text = newScore
    }
}
