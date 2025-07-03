package com.example.payment_sdk

import java.util.*
import kotlin.random.Random

object PaymentSDK {

    private const val SDK_VERSION = "1.0.0"

    /**
     * Process payment with the given parameters
     * @param consumerNumber Consumer number for the bill
     * @param billerName Name of the biller
     * @param amount Amount to be paid
     * @param callback Callback interface to handle payment response
     */
    fun processPayment(
        consumerNumber: String,
        billerName: String,
        amount: Double,
        callback: PaymentCallback
    ) {
        // Input validation
        if (consumerNumber.isEmpty()) {
            callback.onError("Consumer number cannot be empty")
            return
        }

        if (billerName.isEmpty()) {
            callback.onError("Biller name cannot be empty")
            return
        }

        if (amount <= 0) {
            callback.onError("Amount must be greater than 0")
            return
        }

        // Simulate network call delay
        Thread {
            try {
                // Simulate processing time (2-4 seconds)
                val processingTime = Random.nextLong(2000, 4000)
                Thread.sleep(processingTime)

                // Simulate payment success/failure (80% success rate)
                val isSuccess = Random.nextDouble() < 0.8

                val result = if (isSuccess) {
                    PaymentResult(
                        isSuccess = true,
                        transactionId = generateTransactionId(),
                        message = "Payment processed successfully",
                        timestamp = System.currentTimeMillis()
                    )
                } else {
                    PaymentResult(
                        isSuccess = false,
                        transactionId = generateTransactionId(),
                        message = "Payment failed due to insufficient balance",
                        errorCode = "INSUFFICIENT_BALANCE",
                        timestamp = System.currentTimeMillis()
                    )
                }

                // Call appropriate callback
                if (isSuccess) {
                    callback.onSuccess(result)
                } else {
                    callback.onFailure(result)
                }

            } catch (e: Exception) {
                callback.onError("Payment processing failed: ${e.message}")
            }
        }.start()
    }

    /**
     * Generate a unique transaction ID
     */
    private fun generateTransactionId(): String {
        val timestamp = System.currentTimeMillis()
        val randomNumber = Random.nextInt(100000, 999999)
        return "TXN$timestamp$randomNumber"
    }

    /**
     * Get SDK version
     */
    fun getSDKVersion(): String {
        return SDK_VERSION
    }

    /**
     * Check if payment is supported for the given parameters
     */
    fun isPaymentSupported(billerName: String, amount: Double): Boolean {
        // Simple validation rules
        return billerName.isNotEmpty() && amount > 0 && amount <= 100000
    }
}