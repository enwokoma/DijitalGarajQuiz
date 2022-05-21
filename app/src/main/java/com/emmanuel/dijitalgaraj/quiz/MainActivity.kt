package com.emmanuel.dijitalgaraj.quiz

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.emmanuel.dijitalgaraj.quiz.databinding.ActivityMainBinding
import com.emmanuel.dijitalgaraj.quiz.interfaces.InterfaceAPI
import com.github.razir.progressbutton.*
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Retrofit
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // bind button to activity lifecycle
        bindProgressButton(binding.putButton)
        val button = binding.putButton
        button.attachTextChangeAnimator()
        button.setOnClickListener {
            showButtonProgress(button)
        }
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
                fetchHash()
                button.isEnabled = true
            }
        }
    }

    private fun fetchHash() = GlobalScope.launch(Dispatchers.Main) {
        val url = "http://career.dijitalgaraj.com/hash/"
        val slug = "emmanuel-nwokoma-40866"
        val guid = "80e2c70f-996b-445d-95f5-2f1822145397"

        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("$url$slug/")
            .build()

        // Create Service
        val service = retrofit.create(InterfaceAPI::class.java)

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("GUID", guid)

        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        // Create RequestBody (I am not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        try {
            val response = withContext(Dispatchers.IO) {
                // Do the PUT request and get response
                service.updateRecord(requestBody)
            }

            //Do something with response e.g show to the UI.
            if (response.isSuccessful) {

                // Convert raw JSON to pretty JSON using GSON library
                val gson = GsonBuilder().setPrettyPrinting().create()
                val prettyJson = gson.toJson(
                    JsonParser.parseString(
                        response.body()
                            ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                    )
                )
                Log.d("Pretty Printed JSON :", prettyJson)

                val intent = Intent(this@MainActivity, ShowInformationActivity::class.java)
                intent.putExtra("json_results", prettyJson)

                // Get Hash from JSON
                val getHash = JSONObject(prettyJson).get("hash").toString()
                intent.putExtra("hashKey", getHash)
                val button = binding.putButton
                button.hideProgress(R.string.put_request_btn)
                this@MainActivity.startActivity(intent)

            } else {

                Log.e("RETROFIT_ERROR", response.code().toString())

            }
        }
        catch (e: HttpException) {
            Toast.makeText(this@MainActivity, "Exception ${e.message}", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            Toast.makeText(this@MainActivity, "No Internet Connection, try again", Toast.LENGTH_LONG).show()
        } catch (e: Throwable) {
            Toast.makeText(this@MainActivity, "Ooops: Something else went wrong", Toast.LENGTH_LONG).show()
        }
    }
}