package com.twointerns.ridetracker.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.twointerns.ridetracker.R
import com.twointerns.ridetracker.databinding.LocationHistoryItemBinding
import com.twointerns.ridetracker.model.entity.LocationData
import com.twointerns.ridetracker.utils.GlobalUtils







class HistoryAdapter(
    context: Context, listOfLocationHistory: List<LocationData>,
    private val onCardClickListener: HistoryViewModel.OnCardClickListener
) :
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
        var listOfLocation:String =""
        currentItem.latlngList?.forEach {
           if(it.latitude!=null||it.longitude!=null) {
               listOfLocation += "${it.latitude},${it.longitude}" + "|"
           }
        }
        holder.bind(
            currentItem,
            "https://maps.googleapis.com/maps/api/staticmap?center=${currentItem.latlngList?.get(0)?.latitude},${currentItem.latlngList?.get(
                0
            )?.longitude}&zoom=16&size=1600x400&scale=2" +
                    "&path=color:0x0000ff|weight:5|${listOfLocation.substring(0,listOfLocation.length-1)}&key=${GlobalUtils.ApiKey}"
        )
        /*  AddressUtil.getAddress(mContext, currentItem.latlngList?.get(0)).observe(this,
              Observer {
                  holder.startLocation?.text = it

              })
          AddressUtil.getAddress(mContext, currentItem.latlngList?.last()).observe(mContext,
              Observer {
                  holder.startLocation?.text = it

              })*/
        holder.dotMenu?.setOnClickListener {
            val popup = PopupMenu(mContext, it).apply {
                inflate(R.menu.option_menu)
                show()
            }
            popup.setOnMenuItemClickListener (object :PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem?): Boolean {
                    when (item?.itemId) {
                        R.id.deleteLocation -> {

                            onCardClickListener.onMenuItemSelected(currentItem)
                            return true
                        }
                    }
                    return false
                }



            })
        }



        holder.parentLayout?.setOnClickListener {
            onCardClickListener.onCardSelected(currentItem)
        }
    }


}

class HistoryViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var binding: LocationHistoryItemBinding? = DataBindingUtil.bind(itemView)
    val parentLayout = binding?.parentLayout
    val mapPreview: ImageView? = binding?.lightMap
    val dotMenu = binding?.optionMenu
    fun bind(locationData: LocationData, url: String) {
        binding?.data = locationData
        mapPreview?.let {
            Glide.with(itemView)
                .load(url)
                .into(it)

        }

    }

    interface OnCardClickListener {
        fun onCardSelected(currentItem: LocationData)
        fun onMenuItemSelected(currentItem: LocationData)
    }
}
