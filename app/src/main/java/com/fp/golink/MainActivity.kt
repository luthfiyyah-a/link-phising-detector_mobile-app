package com.fp.golink

import android.content.res.AssetManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import org.tensorflow.lite.support.metadata.MetadataExtractor

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
        tflite = Interpreter(loadModelFile())
        submitButton.setOnClickListener {
            // Tindakan yang akan dijalankan saat tombol diklik
            val url = urlInput.text.toString()
            val inputData = preprocessURL(url, tflite)
            Log.i("URL: ", url)
            Log.i("input: ", inputData.toString())
            val prediction = classifyURL(inputData, tflite)

            if (prediction) {
                resultText.text = "This site is not safe. It may be a phishing site."
            } else {
                resultText.text = "This site is safe."
            }
        }
    }
    private fun loadModelFile(): MappedByteBuffer {
        val modelFilename = "model.tflite" // Pastikan file model ada dalam direktori assets
        val fileDescriptor = assets.openFd(modelFilename)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }
    private fun preprocessURL(url: String, tfliteModel: Interpreter): FloatArray {
        val maxInputLength = getMaxInputLength(tfliteModel);
        // Tokenize the URL into characters or subunits, e.g., characters or words
        val tokens = tokenizeURL(url)
        // Initialize the input data as an array of floats with the maximum length
        val inputData = FloatArray(maxInputLength)
        // Map tokens to numerical values and fill the input data
        for (i in tokens.indices) {
            if (i < maxInputLength) {
                // Map the token to a numerical value
                val numericValue = mapTokenToNumeric(tokens[i])
                inputData[i] = numericValue
            }
        }
        return inputData
    }
    private fun tokenizeURL(url: String): List<String> {
        // Implement your tokenization logic here
        // You can split the URL into characters, words, or other subunits
        // and return them as a list of strings
        // For simplicity, let's tokenize by characters in this example
        return url.split("").filter { it.isNotBlank() }
    }

    private fun mapTokenToNumeric(token: String): Float {
        // Implement the mapping of tokens to numerical values here
        // Convert the token to a numerical representation based on your model's requirements
        // This could be one-hot encoding, character embeddings, etc.
        // For simplicity, let's use a basic example
        val numericValue = token.toCharArray().first().toInt().toFloat()
        return numericValue
    }
    private fun getMaxInputLength(tfliteModel: Interpreter): Int {
        val inputTensor = tfliteModel.getInputTensor(0)
        val inputShape = inputTensor.shape()

        return inputShape[1]
    }
    private fun classifyURL(inputData: FloatArray, tfliteModel: Interpreter): Boolean {
        // Implement your TFLite model inference logic here
        // Use the 'tflite' interpreter to make predictions
        // Replace this logic with your actual TFLite model inference code

        // For example, if the model predicts that the input is safe:
        val outputData = Array(1) { FloatArray(1) }
        // Run the inference
        tfliteModel.run(inputData, outputData)
        Log.i("Output: ", outputData[0][0].toString())
        // Assuming your model outputs a single value (0 or 1), you can interpret the result like this:
        val predictedClass = if (outputData[0][0] >= 0.5) 1 else 0
        // Return true if the predicted class is 1 (safe), otherwise false
        return predictedClass == 1
    }
}
