package com.micromos.knpmobile.ui.productcoilin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.micromos.knpmobile.databinding.CardItemShipInBinding
import com.micromos.knpmobile.dto.ShipOrder

class ProductCoilInAdapter(val viewModel: ProductCoilInViewModel) :
    RecyclerView.Adapter<ViewHolder>() {
    var items = listOf<ShipOrder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CardItemShipInBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel, items[position])
    }
}

class ViewHolder(val binding: CardItemShipInBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(viewModel: ProductCoilInViewModel, item: ShipOrder) {
        binding.viewModel = viewModel
        binding.shipInItem = item
        binding.executePendingBindings()
    }
}