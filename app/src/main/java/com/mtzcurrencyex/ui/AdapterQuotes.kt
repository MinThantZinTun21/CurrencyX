package com.mtzcurrencyex.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mtz.domain.model.QuoteModel
import com.mtzcurrencyex.databinding.ItemExchangeRateBinding

class QuoteListAdapter :
    ListAdapter<QuoteModel, QuoteListAdapter.QuoteViewHolder>(QuoteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val binding =
            ItemExchangeRateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val quote = getItem(position)
        holder.bind(quote)
    }

    class QuoteViewHolder(private val binding: ItemExchangeRateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(quote: QuoteModel) {
            binding.quote = quote
            binding.executePendingBindings()
        }
    }
}

class QuoteDiffCallback : DiffUtil.ItemCallback<QuoteModel>() {
    override fun areItemsTheSame(oldItem: QuoteModel, newItem: QuoteModel): Boolean {
        return oldItem.to == newItem.to && oldItem.source == newItem.source && oldItem.rate == newItem.rate
    }

    override fun areContentsTheSame(oldItem: QuoteModel, newItem: QuoteModel): Boolean {
        return oldItem.to == newItem.to
    }
}