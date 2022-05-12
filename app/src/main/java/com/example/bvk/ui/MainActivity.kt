package com.example.bvk.ui

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.example.bvk.databinding.ActivityMainBinding
import com.example.bvk.model.sample.SampleCapParameters

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fragmentTransaction.replace(binding.container.id, MandrelFragment()).commit()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }


    companion object {
        const val IS_SAMPLE_CREATE_TAG = "is_sample_create"
        const val SAMPLE_V_PARAM_TAG = "sample_v_param"
        const val SAMPLE_H_PARAM_TAG = "sample_h_param"
    }
}