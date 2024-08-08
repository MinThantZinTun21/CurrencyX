package com.mtzcurrencyex.ui

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mtz.currencyex.presentation.BaseMviActivity
import com.mtzcurrencyex.ExchangeRateViewModel
import com.mtzcurrencyex.R
import com.mtzcurrencyex.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity :
    BaseMviActivity<ExchangeScreenReducer.ExchangeRateState, ExchangeScreenReducer.ExchangeRateEvent, ExchangeScreenReducer.ExchangeRateEffect, ActivityMainBinding, ExchangeRateViewModel>() {
    override val viewModel: ExchangeRateViewModel by viewModels()
    override val layout: Int = R.layout.activity_main
    private val exchangeAdapter by lazy {
        QuoteListAdapter()
    }

    override fun handleEffect(effect: ExchangeScreenReducer.ExchangeRateEffect) {
        when (effect) {
            is ExchangeScreenReducer.ExchangeRateEffect.OnError -> {
                Toast.makeText(this, effect.error, Toast.LENGTH_SHORT).show()
            }

            is ExchangeScreenReducer.ExchangeRateEffect.OnSourceChange -> {
                Log.d("MainActivity", "OnSourceChange ${effect.source}")
                viewModel.refresh(effect.source)
            }
        }

    }

    override fun renderState(state: ExchangeScreenReducer.ExchangeRateState) {
        binding.apply {
            progressBar.isVisible = state.isLoading
            refreshLayout.isRefreshing = state.isRefreshing
        }
        val currencyAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, state.allPossibleSources)
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked)
        binding.spinnerCurrency.adapter = currencyAdapter

        binding.spinnerCurrency.setSelection(
            getPositionFromSource(
                state.selectedSource,
                state.allPossibleSources
            )
        )

        Log.d("MainActivity", "renderState ${state.selectedSource}")
        exchangeAdapter.submitList(state.quote)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            recRate.apply {
                adapter = exchangeAdapter
                layoutManager = LinearLayoutManager(this@MainActivity)
            }
            refreshLayout.setOnRefreshListener {
                //refresh data for selected source
                viewModel.refresh(spinnerCurrency.selectedItem?.toString() ?: Config.defaultSource)
            }
            spinnerCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    val selectItem = parent?.adapter?.getItem(position).toString()
                    viewModel.selectItem.value = selectItem
                    //  viewModel.getExchangeRate(parent?.adapter?.getItem(position).toString())
                }
            }

            btnConvert.setOnClickListener {
                // TODO: check if amount is valid
                viewModel.convert(edAmount.text.toString().toInt())
            }

        }

    }


    private fun getPositionFromSource(source: String, sources: List<String>) =
        sources.indexOfFirst { it == source }
}