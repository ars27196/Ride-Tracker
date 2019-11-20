package com.twointerns.ridetracker.view.fragment.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.twointerns.ridetracker.R
import com.twointerns.ridetracker.database.LocationRoomDatabase
import com.twointerns.ridetracker.databinding.FragmentHistoryBinding
import com.twointerns.ridetracker.databinding.LocationHistoryItemBinding
import com.twointerns.ridetracker.view.adapter.HistoryAdapter
import com.twointerns.ridetracker.viewmodel.HistoryViewModel

class HistoryFragment : Fragment() {

    private lateinit var dashboardViewModel: HistoryViewModel
    private lateinit var binding: FragmentHistoryBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =ViewModelProviders.of(this).get(HistoryViewModel::class.java)

        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_history, container, false)
        val result=LocationRoomDatabase.getDatabase(context!!).locationDao().retrieveLocationData()

        result.observe(this, Observer {
            val adapter=HistoryAdapter(context!!,it)
            binding.recyclerViewHistory.apply {
                this.adapter=adapter
                layoutManager=LinearLayoutManager(context)
            }
        })



        return binding.root


    }
}