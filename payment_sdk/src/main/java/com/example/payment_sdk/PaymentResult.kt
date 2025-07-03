package com.example.payment_sdk

import android.os.Parcel
import android.os.Parcelable

data class PaymentResult(
    val isSuccess: Boolean,
    val transactionId: String,
    val message: String,
    val errorCode: String? = null,
    val timestamp: Long = System.currentTimeMillis()
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isSuccess) 1 else 0)
        parcel.writeString(transactionId)
        parcel.writeString(message)
        parcel.writeString(errorCode)
        parcel.writeLong(timestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PaymentResult> {
        override fun createFromParcel(parcel: Parcel): PaymentResult {
            return PaymentResult(parcel)
        }

        override fun newArray(size: Int): Array<PaymentResult?> {
            return arrayOfNulls(size)
        }
    }
}