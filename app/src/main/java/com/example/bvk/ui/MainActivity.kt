package com.example.bvk.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.disklrucache.DiskLruCache
import com.example.bvk.R
import com.example.bvk.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), FragmentCommutator {

    private lateinit var binding: ActivityMainBinding
    private var mandrelFragment = MandrelFragment()
    private var fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
    private var saveIsSampleCreated: Boolean = false
    private var onUpdateModeListener = mandrelFragment
    private var launchCounter = 0

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            fragmentTransaction.replace(binding.container.id, mandrelFragment).commit()
        }
    }

    companion object {
        const val IS_SAMPLE_CREATE_TAG = "is_sample_create"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_SAMPLE_CREATE_TAG, saveIsSampleCreated)
    }

    override fun getSavingState(isSampleCreated: Boolean) {
        saveIsSampleCreated = isSampleCreated
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onUpdateModeListener.onUpdateModeClicked()
        return super.onOptionsItemSelected(item)
    }

    interface IfUpdateButtonClickedListener {
        fun onUpdateModeClicked()
    }
}