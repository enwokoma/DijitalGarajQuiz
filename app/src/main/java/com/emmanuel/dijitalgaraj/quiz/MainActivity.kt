package com.emmanuel.dijitalgaraj.quiz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.emmanuel.dijitalgaraj.quiz.interfaces.InterfaceAPI
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit

import com.emmanuel.dijitalgaraj.quiz.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.putButton.setOnClickListener { fetchHash() }
    }

    private fun fetchHash() {
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

        CoroutineScope(Dispatchers.IO).launch {

            // Do the PUT request and get response
            val response = service.updateRecord(requestBody)

            withContext(Dispatchers.Main) {
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

                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())

                }
            }
        }
    }
}