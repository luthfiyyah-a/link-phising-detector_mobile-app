package com.fp.golink

import android.icu.util.Output
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.fp.golink.databinding.ActivityMainBinding
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

private val INPUT_SIZE = 224 // Sesuaikan dengan panjang input yang dibutuhkan oleh model
private val THRESHOLD = 0.5 // Misalnya, output lebih besar dari 0.5 menandakan URL phishing

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var urlInput: EditText
    private lateinit var resultText: TextView
    private lateinit var submitButton: Button


    private lateinit var tflite: Interpreter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* Navigasi Bar */
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        /* End of Navigasi Bar */

        /* hubungkan dengan tf lite (model ML) */
//        tflite = Interpreter(loadModelFile())

        urlInput = findViewById(R.id.urlInput)
        resultText = findViewById(R.id.resultText)
        submitButton = findViewById<Button>(R.id.submitButton)


        submitButton.setOnClickListener {
            // Tindakan yang akan dijalankan saat tombol diklik
            val isPhising = true

            if (isPhising) {
                resultText.text = "Situs tersebut tidak aman -- Kemungkinan situs ini merupakan phising"
            } else {
                resultText.text = "Ini situs yang aman"
            }
        }
    }

//    private fun loadModelFile(): MappedByteBuffer {
//        val fileDescriptor = assets.openFd("model.tflite")
//        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
//        val fileChannel = inputStream.channel
//        val startOffset = fileDescriptor.startOffset
//        val declaredLength = fileDescriptor.declaredLength
//        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
//    }
//
//    private fun processInput(url: String): FloatArray {
//        val inputArray = FloatArray(INPUT_SIZE)
//        return inputArray
//    }
//
//    private fun processOutput(output: Array<FloatArray>): Boolean {
//        val outputValue = output[0][0]
//        return outputValue > THRESHOLD
//    }

    fun onSubmitButtonClicked(view: View) {
//        val url = urlInput.text.toString()
//        val input = processInput(url)
//        val output = Array(1) { FloatArray(1)}
//        tflite.run(input, output)

//        val isPhising = processOutput(output)
        val isPhising = true

        if(isPhising) {
            resultText.setText("Situs tersebut tidak aman -- Kemungkinan situs ini merupakan phising")
        }
        else {
            resultText.setText("Ini situs yang aman")
        }
    }
}