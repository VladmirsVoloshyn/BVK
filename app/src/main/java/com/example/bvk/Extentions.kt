package com.example.bvk

import android.view.Window
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

fun TextInputEditText.shouldShowError(
    errorMessage: String?,
    textInputLayout: TextInputLayout
): Boolean {
    return if (text.isNullOrEmpty()) {
        textInputLayout.error = errorMessage
        true
    } else false
}

fun isContains(inputElement: Any, existList: List<Any>): Boolean {
    for (elements in existList) {
        if (elements.hashCode() == inputElement.hashCode()) {
            return true
        }
    }
    return false
}

fun changeStatusBarColor(activity: FragmentActivity, statusBarColor : Int) {
    val window : Window = activity.window
    window.statusBarColor = ResourcesCompat.getColor(activity.resources,
        statusBarColor,null)
}

