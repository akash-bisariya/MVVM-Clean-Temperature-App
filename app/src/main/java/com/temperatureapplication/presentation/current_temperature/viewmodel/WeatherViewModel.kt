package com.temperatureapplication.presentation.current_temperature.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.temperatureapplication.data.remote.dto.ForecastData
import com.temperatureapplication.data.remote.dto.TemperatureData
import com.temperatureapplication.domain.model.WeatherForecastState
import com.temperatureapplication.domain.model.WeatherState
import com.temperatureapplication.domain.usecase.WeatherForecastUseCase
import com.temperatureapplication.domain.usecase.WeatherUseCase
import com.temperatureapplication.utils.Constants
import com.temperatureapplication.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherUseCase: WeatherUseCase,
    private val forecastUseCase: WeatherForecastUseCase
) : ViewModel() {


    private val _weatherState = MutableStateFlow(WeatherState())
    val weatherState: StateFlow<WeatherState> = _weatherState
    private val _weatherForecastState = MutableStateFlow(WeatherForecastState())
    val weatherForecastState: StateFlow<WeatherForecastState> = _weatherForecastState

    fun getWeatherData(appId: String, city: String, unit: String) {
        weatherUseCase(appId, city, unit).onEach { res ->
            when (res) {
                is Resource.Success -> {
                    _weatherState.value =
                        WeatherState(temperatureData = res.data ?: TemperatureData())

                }

                is Resource.Error -> {
                    _weatherState.value =
                        WeatherState(error = res.message ?: "Unexpected Error happen")

                }

                is Resource.Loading -> {
                    _weatherState.value = WeatherState(loading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getWeatherForeCastData(appId: String, city: String, unit: String) {
        forecastUseCase(appId, city, unit).onEach { res ->
            when (res) {
                is Resource.Success -> {
                    _weatherForecastState.value =
                        WeatherForecastState(forecastData = res.data ?: ForecastData())

                }

                is Resource.Error -> {
                    _weatherForecastState.value =
                        WeatherForecastState(error = res.message ?: "Unexpected Error happen")

                }

                is Resource.Loading -> {
                    _weatherForecastState.value = WeatherForecastState(loading = true)
                }
            }
        }.launchIn(viewModelScope)
    }


}