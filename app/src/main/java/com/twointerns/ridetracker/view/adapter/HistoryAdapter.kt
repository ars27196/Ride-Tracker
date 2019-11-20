package com.twointerns.ridetracker.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.twointerns.ridetracker.R
import com.twointerns.ridetracker.databinding.LocationHistoryItemBinding
import com.twointerns.ridetracker.model.entity.LocationData
import com.twointerns.ridetracker.utils.AddressUtil
import com.twointerns.ridetracker.utils.GlobalUtils

class HistoryAdapter(context: Context, listOfLocationHistory: List<LocationData>) :
    RecyclerView.Adapter<HistoryViewModel>() {
    private val historyList = listOfLocationHistory
    private val mContext = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewModel {

        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.location_history_item, parent, false)

        return HistoryViewModel(layout.rootView)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun onBindViewHolder(holder: HistoryViewModel, position: Int) {
        val currentItem = historyList[position]
        holder.bind(currentItem,"https://maps.googleapis.com/maps/api/staticmap?center=${currentItem.latlngList?.get(0)?.latitude},${currentItem.latlngList?.get(0)?.longitude}&zoom=14&size=1200x1200&scale=1&key=${GlobalUtils.ApiKey}")
        /*  AddressUtil.getAddress(mContext, currentItem.latlngList?.get(0)).observe(this,
              Observer {
                  holder.startLocation?.text = it

              })
          AddressUtil.getAddress(mContext, currentItem.latlngList?.last()).observe(mContext,
              Observer {
                  holder.startLocation?.text = it

              })*/
        holder.parentLayout?.setOnClickListener {

        }
    }


}

class HistoryViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var binding: LocationHistoryItemBinding? = DataBindingUtil.bind(itemView)
    val parentLayout = binding?.parentLayout
    val startLocation = binding?.startLocation
    val stopLocation = binding?.stopLocation
    val mapPreview: ImageView? =binding?.lightMap

    fun bind(locationData: LocationData,url:String) {
        binding?.data = locationData
        mapPreview?.let {
            Glide.with(itemView)
                .load(url)
                .into(it)

        }

    }

}
