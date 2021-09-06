package com.example.postdata_api

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    var editTextmobile: EditText? = null
    var editTextpassword: EditText? = null
    var buttonPostTOAPI: Button? = null
    var Response: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initializing our views
        editTextmobile = findViewById(R.id.ed_mobilenumber)
        editTextpassword = findViewById(R.id.ed_password)
        buttonPostTOAPI = findViewById(R.id.postdatabtn)
        Response = findViewById(R.id.idTVResponse)

        // adding on click listener to our button.

        buttonPostTOAPI!!.setOnClickListener {
            if (editTextmobile!!.length()==0 && editTextpassword!!.length()==0) {
                Toast.makeText(
                    this@MainActivity,
                    "Please enter both the values",
                    Toast.LENGTH_SHORT
                ).show()
               // return@OnClickListener
            }
            // calling a method to post the data and passing our name and job.
            postData(editTextmobile?.getText().toString(),
                editTextpassword?.getText().toString())
        })
        
    }
    
    private fun postData(mobile: String, password: String) {

             val retrofit = Retrofit.Builder()
            .baseUrl("https://api.mrmoasilha.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        // below line is to create an instance for our retrofit api class.
        val retrofitAPI: RestApi = retrofit.create(RestApi::class.java)

        // passing data from our text fields to our modal class.
        //val modal = UserInfo( val deviceType: String, val deviceModel: Int,val deviceId: Int, val deviceName: String,val language: String, val iPAddress: Int, val pushNotificationToken: String,val cabLatitude: Int,val cabLongitude: Int, val loginType: String )

        // calling a method to create a post and passing our modal class.
        val call: Call<UserInfo> = retrofitAPI.createPost(modal)

        // on below line we are executing our method.
        call.enqueue(object : Callback<UserInfo?> {
            override fun onResponse(call: Call<UserInfo?>, response: Response<UserInfo?>) {
                // this method is called when we get response from our api.
                Toast.makeText(this@MainActivity, "Data added to API", Toast.LENGTH_SHORT).show()

                // to our both edit text.
                editTextmobile?.setText("")
                editTextpassword?.setText("")

                // we are getting response from our body
                // and passing it to our modal class.
                val responseFromAPI: UserInfo? = response.body()

                // on below line we are getting our data from modal class and adding it to our string.
                val responseString = """
                    Response Code : ${response.code()}
                    timeStamp : ${responseFromAPI.toString()}
                    name : ${responseFromAPI.toString()}
                    type : ${responseFromAPI.toString()}
                    status : ${responseFromAPI.toString()}
                    token : ${responseFromAPI.toString()}
                    """.trimIndent()

                // below line we are setting our
                // string to our text view.
                Response!!.text = responseString
            }

            override fun onFailure(call: Call<UserInfo?>, t: Throwable) {
                // setting text to our text view when
                // we get error response from API.
                Response!!.text = "Error found is : " + t.message
            }
        })
    }
}