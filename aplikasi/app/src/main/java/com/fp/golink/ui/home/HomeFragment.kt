package com.fp.golink.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.fp.golink.PostWithAuthor
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
    private lateinit var listView: ListView
    private lateinit var historyArray: ArrayList<HistoryItem>
    var result : String = "0"

    // This property is only valid between onCreateView and
    // onDestroyView.
//    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        urlInput = view.findViewById(R.id.urlInput)
        resultText = view.findViewById(R.id.resultText)
        submitButton = view.findViewById(R.id.submitButton)
        listView = view.findViewById(R.id.lv_history)

        historyArray = arrayListOf<HistoryItem>()
        val historyAdapter = HistoryAdapter(requireContext(), historyArray)
        listView.adapter = historyAdapter

        submitButton.setOnClickListener {
            val url = urlInput.text.toString()
            val inputData = preprocessURL(url)
            classifyURL(inputData)


            Log.d("history", "${historyArray}")

            historyAdapter.notifyDataSetChanged()
            listView.smoothScrollToPosition(historyArray.size - 1)
        }

        setupUrlInputListener() // If input url edited, we reset the result
    }

    private fun setupUrlInputListener() {
        urlInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Not needed in this case
            }
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // Reset resultText to empty when urlInput text changes
                resultText.text = ""
            }
            override fun afterTextChanged(p0: Editable?) {
                // Not needed in this case
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
    }

    private fun preprocessURL(url: String): String {
        return url.replace(",", "/")
    }

    private fun classifyURL(inputData: String) {
//        val urlString = "http://10.0.2.2:8000/predict/{feature}?features=$inputData"
        val urlString = "https://luthfiyyah23.pythonanywhere.com/predict?features=$inputData"
        GlobalScope.launch(Dispatchers.IO) {
            val response = makeHttpRequest(urlString)
            Log.d("ML", "response = ${response}")
            val dataHistory = HistoryItem(inputData, response)
            historyArray.add(dataHistory)
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
                result = "0"
            } else {
                resultText.text = "This site is safe."
                result = "1"
            }
        }
    }
}