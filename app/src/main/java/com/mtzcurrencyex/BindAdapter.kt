package com.mtzcurrencyex

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


object BindAdapter {
    @BindingAdapter("bindVisibility")
    @JvmStatic
    fun setBindVisibility(view: ProgressBar, isVisible: Boolean) {
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    @BindingAdapter("app:refreshing")
    @JvmStatic
    fun setRefreshing(swipeRefreshLayout: SwipeRefreshLayout, isRefreshing: Boolean) {
        swipeRefreshLayout.isRefreshing = isRefreshing
    }
}