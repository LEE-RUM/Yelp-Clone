package com.example.hw5_yelpclone

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val BASE_URL = "https://api.yelp.com/v3/"

    private val TAG = "MainActivity"

    private val API_KEY = "XIeO43Ewzau_ugWZfnIUd8hRKRvXbW4-We4NV9z0uWjbBRY13O_9grN4h8ZhY2J7UUNBr8XwhxQP5onuMppFSA4JQMAGJPnFFQE-oeY80-s7SDaD5TCCg4lf9uhkYnYx"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val search = findViewById<Button>(R.id.searchBtn)
        val location = findViewById<TextView>(R.id.locationSearch).text
        val food = findViewById<TextView>(R.id.foodSearch).text

        search.setOnClickListener {

            foodSearch.hideKeyboard()
            if (food.isEmpty()) {
                dialogMessage("Search term cannot be empty. Please enter a search term.", "Search term missing")
            }
            else if (location.isEmpty()){
                dialogMessage("Location cannot be empty. Please enter a location term.", "Location missing")
            }
            else{
                yelpSearch()
            }
        }
    }

    private fun yelpSearch(){
        val restaurantList = ArrayList<Restaurant>()
        val adapter = YelpAdapter(this,restaurantList)

        val recyclerView = findViewById<RecyclerView>(R.id.yelpRecycler)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val yelpApi = retrofit.create(YelpService::class.java)
        yelpApi.searchRestraunts("Bearer $API_KEY", foodSearch.text.toString(), locationSearch.text.toString()).enqueue(object : Callback<RestaurantData> {

            override fun onFailure(call: Call<RestaurantData>, t: Throwable) {
                return
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<RestaurantData>,
                response: Response<RestaurantData>
            ) {
                Log.d(TAG, "onResponse $response")

                val body = response.body() ?: return

                restaurantList.addAll(body.businesses)
                adapter.notifyDataSetChanged()
            }

        })
    }

    private fun dialogMessage(prompt: String, title: String){
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(prompt)
            .setCancelable(false)
            .setNegativeButton("OKAY", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle(title)
        alert.show()

    }

    private fun View.hideKeyboard() {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
}
