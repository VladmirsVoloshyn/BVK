package com.example.bvk.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.example.bvk.R
import com.example.bvk.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fragmentTransaction.replace(binding.container.id, MandrelFragment()).commit()
    }
}