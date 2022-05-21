package com.emmanuel.dijitalgaraj.quiz

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.emmanuel.dijitalgaraj.quiz.databinding.ActivityHiddenEmailBinding

class HiddenEmailActivity : AppCompatActivity() {
    private lateinit var hiddenEmailBinding: ActivityHiddenEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hiddenEmailBinding = ActivityHiddenEmailBinding.inflate(layoutInflater)
        setContentView(hiddenEmailBinding.root)

        val results = intent.getStringExtra("testEmail")

        hiddenEmailBinding.hiddenEmailText.text = results
        Log.d("Found Hidden Email: ", results.toString())
    }
}