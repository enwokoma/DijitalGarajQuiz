package com.emmanuel.dijitalgaraj.quiz

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.emmanuel.dijitalgaraj.quiz.databinding.ActivityShowInformationBinding
import com.emmanuel.dijitalgaraj.quiz.utils.TinyDB
import com.github.razir.progressbutton.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class ShowInformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val results = intent.getStringExtra("json_results")
        val hash = intent.getStringExtra("hashKey")

        binding.jsonResultsTextview.text = results
        // Automatically copy hash to device clipboard
        copyToClipboard(hash)

        // bind button to activity lifecycle
        bindProgressButton(binding.getEmailButton)
        val button = binding.getEmailButton
        button.attachTextChangeAnimator()
        button.setOnClickListener {
            showButtonProgress(button)
        }
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

    private fun showButtonProgress(button: Button) {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        executor.execute {
            //Background work here
            handler.post {
                //UI Thread work here
                button.showProgress {
                    buttonTextRes = R.string.loading
                    progressColor = Color.WHITE
                    gravity = DrawableButton.GRAVITY_TEXT_START
                }
                getEmailFromHash()
                button.isEnabled = true
            }
        }
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

                val executor: ExecutorService = Executors.newSingleThreadExecutor()
                val handler = Handler(Looper.getMainLooper())

                executor.execute {
                    //Background work here
                    val hiddenEmail = searchHash(hashKey, myEmail)
                    handler.post {
                        //UI Thread work here
                        val intent = Intent(applicationContext, HiddenEmailActivity::class.java)
                        intent.putExtra("testEmail", hiddenEmail)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        val button = binding.getEmailButton
                        button.hideProgress(R.string.get_email_btn_text)
                        applicationContext.startActivity(intent)
                    }
                }

                /*Thread {
                    val hiddenEmail = searchHash(hashKey, myEmail)
                    val intent = Intent(applicationContext, HiddenEmailActivity::class.java)
                    intent.putExtra("testEmail", hiddenEmail)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    applicationContext.startActivity(intent)
                }.start()*/
            }
        }
    }
}