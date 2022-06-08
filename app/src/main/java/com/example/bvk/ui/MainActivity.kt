package com.example.bvk.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.FragmentTransaction
import com.example.bvk.R
import com.example.bvk.databinding.ActivityMainBinding
import com.example.bvk.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity(), FragmentCommutator {

    private lateinit var binding: ActivityMainBinding
    private var mandrelFragment = MandrelFragment()
    private var fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
    private var saveIsSampleCreated: Boolean = false
    private var onUpdateModeListener: MandrelFragment? = null

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            fragmentTransaction.replace(binding.container.id, mandrelFragment).commit()
        }
        onUpdateModeListener = mandrelFragment
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
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
        if (item.itemId == R.id.updateModeItem) {
            onUpdateModeListener?.onUpdateModeClicked()
        } else if (item.itemId == R.id.openSettingsItem) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    interface IfUpdateButtonClickedListener {
        fun onUpdateModeClicked()
    }

    companion object {
        const val IS_SAMPLE_CREATE_TAG = "is_sample_create"
    }
}