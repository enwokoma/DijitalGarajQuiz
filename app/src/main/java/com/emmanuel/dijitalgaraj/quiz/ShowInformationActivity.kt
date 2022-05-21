package com.emmanuel.dijitalgaraj.quiz

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.emmanuel.dijitalgaraj.quiz.databinding.ActivityShowInformationBinding
import com.emmanuel.dijitalgaraj.quiz.utils.TinyDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        binding.getEmailButton.setOnClickListener { getEmailFromHash() }
    }

    private fun copyToClipboard(text: String?) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("HashKey", text)
        clipboard.setPrimaryClip(clip)

        val test = clip.getItemAt(0)
        val saveHash = TinyDB(applicationContext)
        saveHash.putString("savedHashKey", test.text.toString())

        Log.d("Test TinyDB", saveHash.getString("savedHashKey")!!)
        Toast.makeText(this, getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show();
    }

    private fun getEmailFromHash() {
        val myEmail = getString(R.string.application_email)

        // Get Hash from TinyDB
        val tinyDB = TinyDB(applicationContext)
        val hashKey = tinyDB.getString("savedHashKey")!!

        // Get the hidden Email Address from the Hash
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                //searchHash(hashKey, myEmail)

                Thread {
                    val hiddenEmail = searchHash(hashKey, myEmail)
                    val intent = Intent(applicationContext, HiddenEmailActivity::class.java)
                    intent.putExtra("testEmail", hiddenEmail)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    applicationContext.startActivity(intent)
                }.start()
            }
        }
    }
}