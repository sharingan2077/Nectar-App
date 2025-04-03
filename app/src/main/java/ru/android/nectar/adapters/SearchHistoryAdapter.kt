package ru.android.nectar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.android.nectar.databinding.ItemSearchHistoryBinding

class SearchHistoryAdapter(
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<SearchHistoryAdapter.HistoryViewHolder>() {

    private val items = mutableListOf<String>()

    fun submitList(newItems: List<String>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(private val binding: ItemSearchHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(text: String) {
            binding.tvHistoryItem.text = text
            binding.root.setOnClickListener {
                onClick(text)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSearchHistoryBinding.inflate(inflater, parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
