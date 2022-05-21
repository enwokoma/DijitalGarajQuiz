package com.emmanuel.dijitalgaraj.quiz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.emmanuel.dijitalgaraj.quiz.databinding.ActivityHiddenEmailBinding
import com.emmanuel.dijitalgaraj.quiz.databinding.ActivityShowInformationBinding

class HiddenEmailActivity : AppCompatActivity() {
    private lateinit var hiddenEmailBinding: ActivityHiddenEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hiddenEmailBinding = ActivityHiddenEmailBinding.inflate(layoutInflater)
        setContentView(hiddenEmailBinding.root)

        val results = intent.getStringExtra("json_results")

    }
}