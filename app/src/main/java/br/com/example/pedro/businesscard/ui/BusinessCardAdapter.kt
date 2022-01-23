package br.com.example.pedro.businesscard.ui

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.example.pedro.businesscard.data.BusinessCard
import br.com.example.pedro.businesscard.databinding.LayoutItemviewBinding


class BusinessCardAdapter()
    : ListAdapter<BusinessCard, BusinessCardAdapter.ViewHolder>(DiffCalback()){

    var listenerShare : (View) -> Unit = {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inlfater = LayoutInflater.from(parent.context)
        val binding = LayoutItemviewBinding.inflate(inlfater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    // Class to implement ViewHolder in order to bind the layout item with the listAdapter
    inner class ViewHolder(
        private val binding: LayoutItemviewBinding

    ) : RecyclerView.ViewHolder(binding.root){

        fun bind(item:BusinessCard){
            binding.textviewName.text = item.name
            binding.textviewEmail.text = item.email
            binding.textviewCompany.text = item.company
            binding.textviewPhone.text = item.phone
            binding.cardContent2.setBackgroundColor(Color.parseColor(item.colorBackground))
            binding.cardContent2.setOnClickListener{
                listenerShare(it)
            }
        }
    }

}

class DiffCalback: DiffUtil.ItemCallback<BusinessCard>() {


    override fun areItemsTheSame(oldItem: BusinessCard, newItem: BusinessCard): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: BusinessCard, newItem: BusinessCard): Boolean {
        return oldItem.id == newItem.id
    }

}