package com.example.payment_sdk

interface PaymentCallback {
    fun onSuccess(result: PaymentResult)
    fun onFailure(result: PaymentResult)
    fun onError(error: String)
}