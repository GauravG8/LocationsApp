package com.mylocations.list.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mylocations.databinding.ListItemBinding

class LocationListAdapter(private var items: List<LocationListItem>, private var listener: OnItemClickListener)
    : RecyclerView.Adapter<LocationListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position], listener)

    fun replaceData(list: List<LocationListItem>){
        items = list
        notifyDataSetChanged()
    }

    interface OnItemClickListener{
        fun onItemClick(id: Int)
    }

    class ViewHolder(private var binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: LocationListItem, listener: OnItemClickListener){
            binding.viewModel = item
            binding.root.setOnClickListener({
                _ -> listener.onItemClick(item.location.id)
            })

            binding.executePendingBindings()
        }
    }
}