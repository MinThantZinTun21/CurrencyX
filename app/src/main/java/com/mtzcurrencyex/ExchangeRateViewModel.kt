package com.mtzcurrencyex

import androidx.lifecycle.viewModelScope
import com.mtz.currencyex.fn.Either
import com.mtz.currencyex.presentation.BaseViewModel
import com.mtz.domain.usecases.GetExchangeRateUseCase
import com.mtzcurrencyex.ui.Config
import com.mtzcurrencyex.ui.ExchangeScreenReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeRateViewModel @Inject constructor(
    private val exchangeRateUseCase: GetExchangeRateUseCase
) : BaseViewModel<ExchangeScreenReducer.ExchangeRateState, ExchangeScreenReducer.ExchangeRateEvent, ExchangeScreenReducer.ExchangeRateEffect>(
    ExchangeScreenReducer.ExchangeRateState.initial(), reducer = ExchangeScreenReducer()
) {
    val selectItem = MutableStateFlow<String>("")

    init {
        getExchangeRate(Config.defaultSource)
        viewModelScope.launch {
            selectItem.collectLatest {

                onItemSelected(it)
            }
        }
    }

    fun convert(amout: Int) {
        sendEvent(ExchangeScreenReducer.ExchangeRateEvent.OnCovert(amout.toDouble()))
    }

    fun getExchangeRate(source: String) {
        viewModelScope.launch {
            sendEvent(ExchangeScreenReducer.ExchangeRateEvent.UpdateLoading(true))
            exchangeRateUseCase.execute(source)
                .collectLatest { it ->
                    sendEvent(ExchangeScreenReducer.ExchangeRateEvent.UpdateLoading(false))
                    if (it is Either.SUCCESS) {
                        sendEvent(ExchangeScreenReducer.ExchangeRateEvent.UpdateQuote(it.DATA.quotes))
                    } else {
                        val error = it as Either.FAILED
                        sendEventForEffect(
                            ExchangeScreenReducer.ExchangeRateEvent.OnError(
                                error.ERROR.getErrorInfo().toString()
                            )
                        )
                    }
                }
        }
    }

    fun onItemSelected(source: String) {
        sendEventForEffect(ExchangeScreenReducer.ExchangeRateEvent.OnSourceSelected(source))
    }

    fun refresh(source: String) {
        viewModelScope.launch {
            sendEvent(ExchangeScreenReducer.ExchangeRateEvent.UpdateRefreshing(true))
            exchangeRateUseCase.execute(source)
                .collectLatest { it ->
                    if (it is Either.SUCCESS) {
                        sendEvent(ExchangeScreenReducer.ExchangeRateEvent.UpdateQuote(it.DATA.quotes))
                    } else {
                        val error = it as Either.FAILED
                        sendEventForEffect(
                            ExchangeScreenReducer.ExchangeRateEvent.OnError(
                                error.ERROR.getErrorInfo().toString()
                            )
                        )
                    }
                }
        }
    }
}


