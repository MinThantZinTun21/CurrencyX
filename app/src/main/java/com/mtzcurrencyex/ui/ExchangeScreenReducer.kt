package com.mtzcurrencyex.ui

import com.mtz.currencyex.presentation.Reducer
import com.mtz.domain.model.QuoteModel
import javax.annotation.concurrent.Immutable

class ExchangeScreenReducer :
    Reducer<ExchangeScreenReducer.ExchangeRateState, ExchangeScreenReducer.ExchangeRateEvent, ExchangeScreenReducer.ExchangeRateEffect> {
    @Immutable
    sealed class ExchangeRateEvent : Reducer.ViewEvent {
        data class UpdateLoading(val isLoading: Boolean) : ExchangeRateEvent()
        data class UpdatePossibleSources(val sources: List<String>) : ExchangeRateEvent()
        data class OnError(val message: String) : ExchangeRateEvent()
        data class UpdateQuote(val data: List<QuoteModel>) : ExchangeRateEvent()
        data class UpdateSelectSource(val source: String) : ExchangeRateEvent()
        data class OnSourceSelected(val source: String) : ExchangeRateEvent()
        data class UpdateRefreshing(val isRefreshing: Boolean) : ExchangeRateEvent()
        data class OnCovert(val amount: Double) : ExchangeRateEvent()
    }

    @Immutable
    sealed class ExchangeRateEffect : Reducer.ViewEffect {
        data class OnError(val error: String) : ExchangeRateEffect()
        data class OnSourceChange(val source: String) : ExchangeRateEffect()
    }

    @Immutable
    data class ExchangeRateState(
        val quote: List<QuoteModel>,
        val isLoading: Boolean = false,
        val isRefreshing: Boolean = false,
        val selectedSource: String = "",
        //default source
        val allPossibleSources: List<String> = emptyList(),
    ) : Reducer.ViewState {
        companion object {
            fun initial(): ExchangeRateState {
                return ExchangeRateState(
                    //default source
                    selectedSource = "USD", quote = emptyList(), isLoading = false
                )
            }
        }
    }

    override fun reduce(
        previousState: ExchangeRateState, event: ExchangeRateEvent
    ): Pair<ExchangeRateState, ExchangeRateEffect?> {
        return when (event) {
            is ExchangeRateEvent.UpdateLoading -> {
                previousState.copy(
                    isLoading = event.isLoading
                ) to null
            }

            is ExchangeRateEvent.OnError -> {
                previousState.copy(
                    isLoading = false, isRefreshing = false
                ) to ExchangeRateEffect.OnError(event.message)
            }

            is ExchangeRateEvent.UpdateQuote -> {
                // should not do but there is  no all possible sources api
                val sourceList = event.data.map { it.to }.toList()
                previousState.copy(
                    isLoading = false,
                    quote = event.data,
                    isRefreshing = false,
                    allPossibleSources = sourceList
                ) to null
            }

            is ExchangeRateEvent.UpdateRefreshing -> {
                previousState.copy(
                    isRefreshing = event.isRefreshing
                ) to null
            }

            is ExchangeRateEvent.UpdateSelectSource -> {
                previousState.copy(
                    selectedSource = event.source
                ) to null
            }


            is ExchangeRateEvent.UpdatePossibleSources -> {
                previousState.copy(
                    allPossibleSources = event.sources
                ) to null
            }

            is ExchangeRateEvent.OnSourceSelected -> {
                previousState.copy(
                    selectedSource = event.source
                ) to ExchangeRateEffect.OnSourceChange(event.source)
            }

            is ExchangeRateEvent.OnCovert -> {
                val updateQuote = previousState.quote.toMutableList().map {
                    it.copy(rate = it.rate * event.amount)
                }
                previousState.copy(quote = updateQuote) to null
            }
        }
    }


}