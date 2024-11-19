package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.model.ExchangeRateResponse
import com.example.myapplication.viewModel.ExchangeRateViewModel
import com.example.myapplication.viewModel.UIState

class MainActivity : AppCompatActivity() {
    private val viewModel: ExchangeRateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //set up default view
        val textViewData: TextView = findViewById(R.id.textViewData)
        val textTo = findViewById<EditText>(R.id.to)
        val textFrom = findViewById<EditText>(R.id.from)
        val amount = findViewById<EditText>(R.id.amount)
        val button = findViewById<Button>(R.id.button)
        val progressBar = findViewById<ProgressBar>(R.id.prges)
        progressBar.visibility = View.GONE



        //set up function for button
        button.setOnClickListener{
            button.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            textViewData.visibility = View.GONE
            if(amount.text.toString().toDoubleOrNull() == null || textTo.text.isEmpty() || textFrom.text.isEmpty())
            {
                Toast.makeText(applicationContext, "Please input valid number and currency", Toast.LENGTH_SHORT).show()
                button.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }else{
                //Fetch data
                viewModel.fetchExchangeRate(amount = amount.text.toString().toDoubleOrNull()!!, to = textTo.text.toString().lowercase(), from = textFrom.text.toString().lowercase())
            }
        }
        // Observer data
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is UIState.Success -> {
                    // Update UI with the exchange rate data
                    val   exchangeRate : ExchangeRateResponse = state.data as ExchangeRateResponse
                    // Handle exchange rate in UI
                    val displayText = """
                       Base Currency: ${exchangeRate.base_code}
                       Target Currency: ${exchangeRate.target_code}
                       Conversion Rate: ${exchangeRate.conversion_rate}
                       Conversion Result: ${exchangeRate.conversion_result}
                   """.trimIndent()
                    textViewData.text = displayText
                    textViewData.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    button.visibility = View.VISIBLE
                }
                is UIState.Error -> {
                    // Show error message
                    Toast.makeText(this,"Please check name of currency or your connectivity", Toast.LENGTH_LONG).show()
                    progressBar.visibility = View.GONE
                    button.visibility = View.VISIBLE
                }
            }
        }
    }
}
