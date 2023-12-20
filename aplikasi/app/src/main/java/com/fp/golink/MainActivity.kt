package com.fp.golink

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.fp.golink.ui.dashboard.DashboardFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.fp.golink.ui.home.HomeFragment
import com.fp.golink.ui.post.PostFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
//    private lateinit var urlInput: EditText
//    private lateinit var resultText: TextView
//    private lateinit var submitButton: Button
    private lateinit var nav_view: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        urlInput = findViewById(R.id.urlInput)
//        resultText = findViewById(R.id.resultText)
//        submitButton = findViewById(R.id.submitButton)

        nav_view = findViewById(R.id.nav_view)
        nav_view.setOnNavigationItemSelectedListener(onBottomNavListener)
        var fr = supportFragmentManager.beginTransaction()
        fr.replace(R.id.fl_fragment, HomeFragment())
        fr.commit()

//        submitButton.setOnClickListener {
//            val url = urlInput.text.toString()
//            val inputData = preprocessURL(url)
//            classifyURL(inputData)
//        }
    }

    private val onBottomNavListener = BottomNavigationView.OnNavigationItemSelectedListener { i ->
        var selectedFr: Fragment = HomeFragment()

        when(i.itemId) {
            R.id.navigation_home -> {
                selectedFr= HomeFragment()
                Log.d("MAIN", "kepanggil home nya")
            }
            R.id.navigation_post -> {
                selectedFr= PostFragment()
                Log.d("MAIN", "kepanggil post nya")
            }
        }

        var fr = supportFragmentManager.beginTransaction()
        fr.replace(R.id.fl_fragment, selectedFr)
        fr.commit()
        true
    }

//    private fun preprocessURL(url: String): String {
//        return url.replace(",", "/")
//    }
//
//    private fun classifyURL(inputData: String) {
//        val urlString = "http://10.0.2.2:8000/predict/{feature}?features=$inputData"
//        GlobalScope.launch(Dispatchers.IO) {
//            val response = makeHttpRequest(urlString)
//            handleResponse(response)
//        }
//    }
//
//    private suspend fun makeHttpRequest(urlString: String): String {
//        var result = ""
//        try {
//            val url = URL(urlString)
//            val connection = url.openConnection() as HttpURLConnection
//            val reader = BufferedReader(InputStreamReader(connection.inputStream))
//            val response = StringBuilder()
//            var line: String?
//            while (reader.readLine().also { line = it } != null) {
//                response.append(line)
//            }
//            reader.close()
//            result = response.toString()
//        } catch (e: Exception) {
//            Log.e("HTTP Request", "Exception: ${e.message}")
//        }
//        return result
//    }
//
//    private fun handleResponse(response: String) {
//        Log.i("RESPONSE", response)
//        runOnUiThread {
//            if (response.contains("This is a Phishing Site")) {
//                resultText.text = "This site is not safe. It may be a phishing site."
//            } else {
//                resultText.text = "This site is safe."
//            }
//        }
//    }
}
