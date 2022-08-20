package com.example.test

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.test.databinding.ActivityMainBinding
import com.example.test.viewModel.MainViewModel
import com.example.test.viewModel.MainViewModelFactory
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class MainActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    var img: String? = null
    var educations: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel =
            ViewModelProvider(this, MainViewModelFactory(this)).get(MainViewModel::class.java)
        viewModel.loadData()?.let { parsJson(it) }
        binding.imgBtn.setOnClickListener {
            var intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
    }

    private fun parsJson(s: String) {
        var builder: StringBuilder = StringBuilder()
        try {
            var root = JSONObject(s)
            var develop: JSONObject = root.getJSONObject("user")
            img = develop.getString("photo")
            educations = develop.getInt("education")
            builder.append("").append(develop.getString("first_name")).append("\n")
            builder.append("").append(develop.getString("second_name")).append("\n")
            builder.append(when (educations) {
                1 -> "high_school"
                2 -> "bachelor"
                3 -> "master"
                4 -> "doctoral"
                else -> "unknown"
            }).append("\n")
            var company: JSONArray = develop.getJSONArray("company")
            for (i in 0 until company.length()) {
                val compan: JSONObject = company.getJSONObject(i)

                builder.append(compan.getString("name")).append(": ")
                    .append(compan.getString("position")).append("\n")
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        binding.textView.setText(builder)
        Glide.with(this)
            .load(img)
            .into(binding.image)
    }

}