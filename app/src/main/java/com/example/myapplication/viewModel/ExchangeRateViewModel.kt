package com.example.myapplication.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.network.ExchangeRateRepository
import kotlinx.coroutines.launch

sealed class UIState<out T> {
    data class Success<ExchangeRateResponse>(val data: ExchangeRateResponse) : UIState<ExchangeRateResponse>() // can use Any T if wanted
    data class Error(val message: String) : UIState<Nothing>()
}
class ExchangeRateViewModel : ViewModel() {
    private val repository = ExchangeRateRepository()

private val _uiState = MutableLiveData<UIState<Any>>()
    val uiState: LiveData<UIState<Any>> = _uiState
    fun fetchExchangeRate(from: String,to:String,amount:Double) =
        viewModelScope.launch {
            try {
                val response = repository.fetchExchangeRate(from,to,amount)
                _uiState.value = UIState.Success(response)
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = UIState.Error(e.message ?: "An unknown error occurred")
            }
        }

}
