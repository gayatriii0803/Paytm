package com.example.paytm

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.example.payment_sdk.PaymentResult
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.*

class PaymentReceiptActivity : AppCompatActivity() {
    private lateinit var tvStatusIcon: TextView
    private lateinit var tvPaymentStatus: TextView
    private lateinit var tvTransactionId: TextView
    private lateinit var tvAmount: TextView
    private lateinit var tvBillerName: TextView
    private lateinit var tvConsumerNumber: TextView
    private lateinit var tvDateTime: TextView
    private lateinit var btnDone: MaterialButton
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
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment_receipt)
        setupViews()
        displayPaymentResult()
        setupListeners()
    }
    private fun setupViews() {
        tvStatusIcon = findViewById(R.id.tvStatusIcon)
        tvPaymentStatus = findViewById(R.id.tvPaymentStatus)
        tvTransactionId = findViewById(R.id.tvTransactionId)
        tvAmount = findViewById(R.id.tvAmount)
        tvBillerName = findViewById(R.id.tvBillerName)
        tvConsumerNumber = findViewById(R.id.tvConsumerNumber)
        tvDateTime = findViewById(R.id.tvDateTime)
        btnDone = findViewById(R.id.btnDone)
    }

    private fun displayPaymentResult() {
        val paymentResult = intent.getParcelableExtra<PaymentResult>("payment_result")
        val consumerNumber = intent.getStringExtra("consumer_number")
        val billerName = intent.getStringExtra("biller_name")
        val amount = intent.getDoubleExtra("amount", 0.0)

        if (paymentResult != null) {
            // Update status
            if (paymentResult.isSuccess) {
                tvStatusIcon.text = "✓"
                tvStatusIcon.setBackgroundColor(ContextCompat.getColor(this, R.color.success))
                tvPaymentStatus.text = "Payment Successful"
            } else {
                tvStatusIcon.text = "✗"
                tvStatusIcon.setBackgroundColor(ContextCompat.getColor(this, R.color.error))
                tvPaymentStatus.text = "Payment Failed"
            }

            // Update transaction details
            tvTransactionId.text = paymentResult.transactionId
            tvAmount.text = "₹${String.format("%.2f", amount)}"
            tvBillerName.text = billerName ?: "N/A"
            tvConsumerNumber.text = consumerNumber ?: "N/A"

            // Update date and time
            val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
            tvDateTime.text = dateFormat.format(Date())
        }
    }

    private fun setupListeners() {
        btnDone.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}
