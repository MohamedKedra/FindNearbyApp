package com.mohamed.findnearbyapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mohamed.findnearbyapp.Models.Item
import com.mohamed.findnearbyapp.R
import com.squareup.picasso.Picasso

class PlaceAdapter(val context: Context,var places : List<Item>) : RecyclerView.Adapter<PlaceAdapter.PlaceHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceHolder {
        return PlaceHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_place,parent,false))
    }

    override fun getItemCount(): Int = places.size

    override fun onBindViewHolder(holder: PlaceHolder, position: Int) {
        var venue = places.get(position).venue
        holder.title.text = venue.name
        holder.address.text = "${venue.location.address},${venue.location.city},${venue.location.state},${venue.location.country}"
        Picasso.with(context).load("${venue.categories.get(0).icon.prefix}${venue.categories.get(0).icon.suffix}").placeholder(R.drawable.ic_default).into(holder.icon)
    }

    class PlaceHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var title : TextView = itemView.findViewById(R.id.tv_title)
        var address : TextView = itemView.findViewById(R.id.tv_address)
        var icon : ImageView = itemView.findViewById(R.id.iv_icon)
    }

}