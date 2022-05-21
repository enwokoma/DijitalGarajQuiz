package com.emmanuel.dijitalgaraj.quiz

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.emmanuel.dijitalgaraj.quiz.databinding.ActivityShowInformationBinding
import com.emmanuel.dijitalgaraj.quiz.utils.TinyDB

class ShowInformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val results = intent.getStringExtra("json_results")
        val hash = intent.getStringExtra("hashKey")

        binding.jsonResultsTextview.text = results
        copyToClipboard(hash)

    }

    private fun copyToClipboard(text: String?) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("HashKey", text)
        clipboard.setPrimaryClip(clip)

        val test = clip.getItemAt(0)
        val saveHash = TinyDB(applicationContext)
        saveHash.putString("savedHashKey", test.text.toString())

        Log.d("Test TinyDB", saveHash.getString("savedHashKey")!!)
        Toast.makeText(this, getString(R.string.copied_to_clipboard), Toast.LENGTH_LONG).show();
    }
}