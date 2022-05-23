package com.example.bvk.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.example.bvk.databinding.ActivityMainBinding
import com.example.bvk.model.sample.SampleCapParameters

class MainActivity : AppCompatActivity(), FragmentCommutator {

    private lateinit var binding: ActivityMainBinding
    private var mandrelFragment = MandrelFragment()
    private var fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
    private var saveIsSampleCreated: Boolean = false
    private var launchCounter = 0


    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mLaunchCounter = getSharedPreferences(LAUNCH_PREFERENCE , Context.MODE_PRIVATE)

       if(savedInstanceState == null){
           fragmentTransaction.replace(binding.container.id, mandrelFragment).commit()
       }
        launchCounter++
    }

    override fun onPause() {
        super.onPause()
    }

    companion object {
        const val IS_SAMPLE_CREATE_TAG = "is_sample_create"
        const val LAUNCH_PREFERENCE = "launch is counting"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_SAMPLE_CREATE_TAG, saveIsSampleCreated)
    }

    override fun getSavingState(isSampleCreated: Boolean) {
        saveIsSampleCreated = isSampleCreated
    }
}