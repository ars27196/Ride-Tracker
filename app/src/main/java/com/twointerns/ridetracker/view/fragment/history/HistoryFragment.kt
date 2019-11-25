package com.twointerns.ridetracker.view.fragment.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.twointerns.ridetracker.R
import com.twointerns.ridetracker.database.LocationRoomDatabase
import com.twointerns.ridetracker.databinding.FragmentHistoryBinding
import com.twointerns.ridetracker.model.entity.LocationData
import com.twointerns.ridetracker.view.adapter.HistoryAdapter
import com.twointerns.ridetracker.viewmodel.HistoryViewModel

class HistoryFragment : Fragment(),
    com.twointerns.ridetracker.view.adapter.HistoryViewModel.OnCardClickListener {



    private lateinit var dashboardViewModel: HistoryViewModel
    private lateinit var binding: FragmentHistoryBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel = ViewModelProviders.of(this).get(HistoryViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
        populateRecyclerView()



        return binding.root


    }

    private fun populateRecyclerView() {
        val result =
            LocationRoomDatabase.getDatabase(context!!).locationDao().retrieveLocationData()

        result.observe(this, Observer {
            val adapter = HistoryAdapter(context!!, it, this)
            binding.recyclerViewHistory.apply {
                this.adapter = adapter
                layoutManager = LinearLayoutManager(context)
            }
        })
    }

    override fun onCardSelected(currentItem: LocationData) {
        val action =
            HistoryFragmentDirections.actionNavigationHistoryToDetailRideFragment(currentItem)
        findNavController().navigate(action)
    }

    override fun onMenuItemSelected(currentItem: LocationData) {
        LocationRoomDatabase.getDatabase(context!!).locationDao().deleteLocationData(currentItem)
        populateRecyclerView()

    }
}