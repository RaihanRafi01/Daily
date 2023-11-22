package com.example.daily.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import com.example.daily.R
import com.example.daily.databinding.ActivityMessageBinding
import com.example.daily.databinding.ActivityWeatherMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.locks.Condition

// https://api.openweathermap.org/data/2.5/weather?q=Dhaka&appid=4c212dc1137c7ab4b1fdcd272acdbe9b
class WeatherMain : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fetchWeatherData("Dhaka")
        searchCity()
    }

    private fun searchCity() {
        val searchView = binding.wSearch
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    private fun fetchWeatherData(cityName : String) {
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/").build().create(ApiWeatherInterface::class.java)
        val response = retrofit.getWeatherData(cityName,"4c212dc1137c7ab4b1fdcd272acdbe9b","metric")
        response.enqueue(object : Callback<WeatherAppData>{
            override fun onResponse(
                call: Call<WeatherAppData>,
                response: Response<WeatherAppData>
            ) {
                Log.e("Weather", response.body()?.weather.toString())
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    val condition = responseBody.weather.firstOrNull()?.main ?:"unknown"
                    binding.wCel.text = responseBody.main.temp.toString()+" °C"
                    binding.humidity.text = responseBody.main.humidity.toString()+" % "
                    binding.wMax.text = "Max "+responseBody.main.temp_max.toString()+" °C"
                    binding.wMin.text = "Min "+responseBody.main.temp_min.toString()+" °C"
                    binding.sea.text = responseBody.main.pressure.toString()+" hPa"
                    binding.wind.text = responseBody.wind.speed.toString()+" m/s"
                    binding.sunrise.text = time(responseBody.sys.sunrise.toLong())
                    binding.sunset.text = time(responseBody.sys.sunset.toLong())
                    binding.wWeather.text = condition
                    binding.condition.text = condition
                    binding.wCity.text = responseBody.name
                    binding.wDay.text = dayName(System.currentTimeMillis())
                    binding.wDate.text = date()

                    changeBG(condition)

                }
            }

            override fun onFailure(call: Call<WeatherAppData>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun changeBG(conditions: String) {
        when(conditions){
            "Haze" ->{
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }
        }
        binding.lottieAnimationView.playAnimation()
    }
    private fun time(timestamp: Long):String{
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format((Date(timestamp*1000)))
    }
    private fun dayName(timestamp: Long):String{
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }
    private fun date():String{
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return sdf.format((Date()))
    }
}

