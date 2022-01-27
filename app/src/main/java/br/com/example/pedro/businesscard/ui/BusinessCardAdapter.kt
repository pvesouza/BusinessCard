package br.com.example.pedro.businesscard.ui

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.shapes.Shape
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.example.pedro.businesscard.R
import br.com.example.pedro.businesscard.data.BusinessCard
import br.com.example.pedro.businesscard.databinding.LayoutItemviewBinding
import java.io.ByteArrayInputStream


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
            binding.cardContent2.setBackgroundResource(R.drawable.shape_background)
            binding.cardContent2.setOnClickListener{
                listenerShare(it)
            }
            // Restore the image from the sqlite db
            val imageBytes = item.image
            val imageInputStrem = ByteArrayInputStream(imageBytes)
            val bitmap1 = BitmapFactory.decodeStream(imageInputStrem)
            binding.imageViewQrCode.setImageBitmap(bitmap1)
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