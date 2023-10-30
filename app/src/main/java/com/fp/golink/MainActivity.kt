package com.fp.golink

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

private const val INPUT_SIZE = 224 // Sesuaikan dengan panjang input yang dibutuhkan oleh model
private const val THRESHOLD = 0.5 // Misalnya, output lebih besar dari 0.5 menandakan URL phishing

class MainActivity : AppCompatActivity() {

    private lateinit var urlInput: EditText
    private lateinit var resultText: TextView
    private lateinit var submitButton: Button

    private lateinit var tflite: Interpreter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Gantilah dengan layout yang sesuai

        // Inisialisasi elemen UI
        urlInput = findViewById(R.id.urlInput)
        resultText = findViewById(R.id.resultText)
        submitButton = findViewById(R.id.submitButton)

        // Hubungkan dengan model TensorFlow Lite
//        tflite = Interpreter(loadModelFile())

        submitButton.setOnClickListener {
            // Tindakan yang akan dijalankan saat tombol diklik
            val url = urlInput.text.toString()
//            val input = processInput(url)
            val output = Array(1) { FloatArray(1) }
//            tflite.run(input, output)

//            val isPhishing = processOutput(output)
            val isPhishing = true

            if (isPhishing) {
                resultText.text = "Situs tersebut tidak aman -- Kemungkinan situs ini merupakan phishing"
            } else {
                resultText.text = "Ini situs yang aman"
            }
        }
    }

//    private fun loadModelFile(): MappedByteBuffer {
//        val modelFilename = "model.tflite" // Pastikan file model ada dalam direktori assets
//        val fileDescriptor = assets.openFd(modelFilename)
//        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
//        val fileChannel = inputStream.channel
//        val startOffset = fileDescriptor.startOffset
//        val declaredLength = fileDescriptor.declaredLength
//        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
//    }
//
//    private fun processInput(url: String): FloatArray {
//        val inputArray = FloatArray(INPUT_SIZE)
//        // Isi array input sesuai dengan data yang diperlukan
//        return inputArray
//    }
//
//    private fun processOutput(output: Array<FloatArray>): Boolean {
//        val outputValue = output[0][0]
//        return outputValue > THRESHOLD
//    }
}
