package com.example.test.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.test.R
import com.example.test.model.User
import com.example.test.repository.MainRepository
import com.google.gson.Gson
import java.io.InputStream
import java.util.*

class MainViewModel(val context:Context) : ViewModel() {
    private val repository = MainRepository()

    fun loadData():String? {
        val inputStream = context.resources.openRawResource(R.raw.develop)
        val scanner = Scanner(inputStream)
        val builder: StringBuilder = StringBuilder()
        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine())
        }
        return builder.toString()
    }
}