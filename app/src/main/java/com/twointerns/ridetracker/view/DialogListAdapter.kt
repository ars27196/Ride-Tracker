package com.twointerns.ridetracker.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.twointerns.ridetracker.R

class DialogListAdapter(
    context: Context,
    count: Int
) : BaseAdapter() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    val _count = count
    val _context=context
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder
        var view: View? = convertView

        if(view==null){
           view= layoutInflater.inflate(R.layout.simple_list_item, parent, false)


        viewHolder = ViewHolder(
            view?.findViewById(R.id.text_view)!!,
            view?.findViewById(R.id.image_view)!!
        )
        view?.tag = viewHolder
    } else {
        viewHolder = view.tag as ViewHolder
    }

        when (position) {
            0 -> {
                viewHolder.textView.text = _context.getString(R.string.google_plus_title)
                viewHolder.imageView.setImageResource(R.drawable.ic_google_plus_icon)
            }
            1 -> {
                viewHolder.textView.text = _context.getString(R.string.google_maps_title)
                viewHolder.imageView.setImageResource(R.drawable.ic_google_maps_icon)
            }
            else -> {
                viewHolder.textView.text = _context.getString(R.string.google_messenger_title)
                viewHolder.imageView.setImageResource(R.drawable.ic_google_messenger_icon)
            }
        }

        return view!!
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return _count
    }
    data class ViewHolder(val textView: TextView, val imageView: ImageView)

}