package com.mtz.currencyex.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

abstract class BaseMviActivity<State : Reducer.ViewState, Event : Reducer.ViewEvent, Effect : Reducer.ViewEffect, VB : ViewDataBinding, VM : BaseViewModel<State, Event, Effect>> :
    AppCompatActivity() {
    protected abstract val viewModel: VM
    lateinit var binding: VB
    protected abstract val layout: Int
    protected abstract fun renderState(state: State)
    protected abstract fun handleEffect(effect: Effect)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layout)
        binding.lifecycleOwner = this
        lifecycleScope.launch {
            viewModel.state.collect { renderState(it) }
        }
        lifecycleScope.launch {
            viewModel.effect
                .flowOn(Dispatchers.IO)
                .collect {
                    handleEffect(it)
                }
        }
    }


}