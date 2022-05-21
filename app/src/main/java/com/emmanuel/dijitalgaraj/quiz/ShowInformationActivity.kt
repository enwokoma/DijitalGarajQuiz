package com.emmanuel.dijitalgaraj.quiz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.emmanuel.dijitalgaraj.quiz.databinding.ActivityMainBinding
import com.emmanuel.dijitalgaraj.quiz.databinding.ActivityShowInformationBinding

class ShowInformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val results = intent.getStringExtra("json_results")

        binding.jsonResultsTextview.text = results

    }
}