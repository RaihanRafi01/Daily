package com.example.daily.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.daily.R
import com.example.daily.databinding.ActivityMessageBinding
import com.example.daily.databinding.ActivityWeatherMainBinding

class WeatherMain : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}