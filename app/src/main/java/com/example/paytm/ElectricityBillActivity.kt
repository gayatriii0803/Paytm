package com.example.paytm

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.payment_sdk.PaymentCallback
import com.example.payment_sdk.PaymentResult
import com.example.payment_sdk.PaymentSDK
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText


class ElectricityBillActivity : AppCompatActivity() {
    private lateinit var etConsumerNumber: TextInputEditText
    private lateinit var etBillerName: TextInputEditText
    private lateinit var etAmount: TextInputEditText
    private lateinit var btnPayNow: MaterialButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (
                android.view.View.SYSTEM_UI_FLAG_FULLSCREEN or
                        android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                )
        actionBar?.hide() // For older ActionBar support
        supportActionBar?.hide() // For AppCompat support
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary)
        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = false
        setContentView(R.layout.activity_electricity_bill)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        etConsumerNumber = findViewById(R.id.etConsumerNumber)
        etBillerName = findViewById(R.id.etBillerName)
        etAmount = findViewById(R.id.etAmount)
        btnPayNow = findViewById(R.id.btnPayNow)
    }

    private fun setupListeners() {
        btnPayNow.setOnClickListener {
            processPayment()
        }
    }

    private fun processPayment() {
        val consumerNumber = etConsumerNumber.text.toString().trim()
        val billerName = etBillerName.text.toString().trim()
        val amountStr = etAmount.text.toString().trim()

        if (consumerNumber.isEmpty()) {
            etConsumerNumber.error = "Please enter consumer number"
            return
        }

        if (amountStr.isEmpty()) {
            etAmount.error = "Please enter amount"
            return
        }

        val amount = try {
            amountStr.toDouble()
        } catch (e: NumberFormatException) {
            etAmount.error = "Please enter valid amount"
            return
        }

        if (amount <= 0) {
            etAmount.error = "Amount must be greater than 0"
            return
        }

        // Disable button to prevent multiple clicks
        btnPayNow.isEnabled = false
        btnPayNow.text = "Processing..."

        // Call Payment SDK
        PaymentSDK.processPayment(
            consumerNumber = consumerNumber,
            billerName = billerName,
            amount = amount,
            callback = object : PaymentCallback {
                override fun onSuccess(result: PaymentResult) {
                    runOnUiThread {
                        btnPayNow.isEnabled = true
                        btnPayNow.text = "Pay Now"

                        // Navigate to receipt activity
                        val intent = Intent(this@ElectricityBillActivity, PaymentReceiptActivity::class.java)
                        intent.putExtra("payment_result", result)
                        intent.putExtra("consumer_number", consumerNumber)
                        intent.putExtra("biller_name", billerName)
                        intent.putExtra("amount", amount)
                        startActivity(intent)
                    }
                }

                override fun onFailure(result: PaymentResult) {
                    runOnUiThread {
                        btnPayNow.isEnabled = true
                        btnPayNow.text = "Pay Now"

                        // Navigate to receipt activity with failure result
                        val intent = Intent(this@ElectricityBillActivity, PaymentReceiptActivity::class.java)
                        intent.putExtra("payment_result", result)
                        intent.putExtra("consumer_number", consumerNumber)
                        intent.putExtra("biller_name", billerName)
                        intent.putExtra("amount", amount)
                        startActivity(intent)
                    }
                }

                override fun onError(error: String) {
                    runOnUiThread {
                        btnPayNow.isEnabled = true
                        btnPayNow.text = "Pay Now"
                        Toast.makeText(this@ElectricityBillActivity, "Error: $error", Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
    }
}