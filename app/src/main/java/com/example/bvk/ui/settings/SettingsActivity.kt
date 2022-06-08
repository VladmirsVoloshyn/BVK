package com.example.bvk.ui.settings

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bvk.R
import com.example.bvk.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.subtitle = "settings"
        setUI()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setUI(){
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.passwordSettingsContainer.background = getDrawable(R.drawable.card_bg)
                binding.adhesiveSaveLineSettingsContainer.background = getDrawable(R.drawable.card_bg)
                binding.membraneDepthSettingsContainer.background = getDrawable(R.drawable.card_bg)
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.passwordSettingsContainer.background = getDrawable(R.drawable.card_bg_night)
                binding.adhesiveSaveLineSettingsContainer.background = getDrawable(R.drawable.card_bg_night)
                binding.membraneDepthSettingsContainer.background = getDrawable(R.drawable.card_bg_night)
            }
        }

    }

}