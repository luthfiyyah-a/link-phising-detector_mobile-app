package com.fp.golink.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.fp.golink.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HomeFragment : Fragment() {

//    private var _binding: FragmentHomeBinding? = null
    private lateinit var urlInput: EditText
    private lateinit var resultText: TextView
    private lateinit var submitButton: Button

    // This property is only valid between onCreateView and
    // onDestroyView.
//    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)
//
//        _binding = FragmentHomeBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
//        return root
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        urlInput = view.findViewById(R.id.urlInput)
        resultText = view.findViewById(R.id.resultText)
        submitButton = view.findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
            val url = urlInput.text.toString()
            val inputData = preprocessURL(url)
            classifyURL(inputData)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
    }

    private fun preprocessURL(url: String): String {
        return url.replace(",", "/")
    }

    private fun classifyURL(inputData: String) {
        val urlString = "http://10.0.2.2:8000/predict/{feature}?features=$inputData"
        GlobalScope.launch(Dispatchers.IO) {
            val response = makeHttpRequest(urlString)
            handleResponse(response)
        }
    }

    private suspend fun makeHttpRequest(urlString: String): String {
        var result = ""
        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()
            result = response.toString()
        } catch (e: Exception) {
            Log.e("HTTP Request", "Exception: ${e.message}")
        }
        return result
    }

    private fun handleResponse(response: String) {
        Log.i("RESPONSE", response)
        requireActivity().runOnUiThread {
            if (response.contains("This is a Phishing Site")) {
                resultText.text = "This site is not safe. It may be a phishing site."
            } else {
                resultText.text = "This site is safe."
            }
        }
    }
}