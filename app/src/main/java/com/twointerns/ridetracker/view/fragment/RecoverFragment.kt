package com.twointerns.ridetracker.view.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import com.twointerns.ridetracker.R
import com.twointerns.ridetracker.databinding.FragmentRecoverBinding
import com.twointerns.ridetracker.utils.OneButtonAlert
import com.twointerns.ridetracker.viewmodel.RecoverViewModel

class RecoverFragment : Fragment() {

    companion object {
        fun newInstance() = RecoverFragment()
    }

    private lateinit var viewModel: RecoverViewModel
    private lateinit var binding: FragmentRecoverBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recover, container, false)
        viewModel = ViewModelProviders.of(this).get(RecoverViewModel::class.java)
        binding.recover = viewModel as RecoverViewModel

        (viewModel as RecoverViewModel).error.observe(this, Observer {
            if (it != null) {
                OneButtonAlert.newInstance("Invalid Entry", it, "Okay")
                    .show(childFragmentManager, "")
            }
        })

        (viewModel as RecoverViewModel).success.observe(this, Observer {
            if (it.equals("shown")) {
                OneButtonAlert.newInstance("Success", "Recovery Link sent to the email Id", "Okay")
                    .show(childFragmentManager, "")
            }
        })
        (viewModel as RecoverViewModel).empty.observe(this, Observer {
            if (it.equals("shown")) {
                OneButtonAlert.newInstance("Invalid Entry", "Email field is empty", "Okay")
                    .show(childFragmentManager, "")

            }
        })
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RecoverViewModel::class.java)
    }


}
