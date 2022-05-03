package com.example.bvk

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

fun TextInputEditText.shouldShowError(errorMessage: String, textInputLayout: TextInputLayout) : Boolean{
    return if (text.isNullOrEmpty()){
        textInputLayout.error = errorMessage
        true
    }
    else false
}