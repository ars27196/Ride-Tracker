package com.twointerns.ridetracker.view.fragment.register

import android.content.Context
import android.graphics.Typeface
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.twointerns.ridetracker.R
import com.twointerns.ridetracker.databinding.FragmentLogInBinding
import com.twointerns.ridetracker.utils.OneButtonAlert
import com.twointerns.ridetracker.utils.ShowProgressAlert
import com.twointerns.ridetracker.viewmodel.LogInViewModel

class LogInFragment : Fragment() {


    private lateinit var viewModel: LogInViewModel
    private lateinit var binding: FragmentLogInBinding
    private var dialog = ShowProgressAlert.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LogInViewModel::class.java)
        viewModel.success.observe(this, Observer {
            if (it) {
                findNavController().navigate(R.id.action_logInFragment_to_homeActivity)
            }
        })
        viewModel.failure.observe(this, Observer {
            if (it != null) {
                OneButtonAlert.newInstance("Invalid Sign Up", it, "Okay")
                    .show(childFragmentManager, "")

            }
        })

        viewModel.error.observe(this, Observer {
            if (it.equals("show")) {
                OneButtonAlert.newInstance("Invalid Entry", "One or more fields are empty", "Okay")
                    .show(childFragmentManager, "")

            }
        })

        viewModel.showDialog.observe(this, Observer {
            if (it) {
                if (!dialog.isVisible) {
                    dialog.show(childFragmentManager , "lol")
                }
            } else {

                dialog.dismiss()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_log_in, container, false)

        binding.forgotPasswordLinkLogin.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_recoverFragment)
        }

        binding.signUpLinkLogin.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_registerFragment)
        }

        // observing viewmodel state
        changePasswordHintFont(context!!)
        binding.login = viewModel

        binding.logoLogin.setOnClickListener {
            binding.emailLogin.setText("ars27196@gmail.com")
            binding.passwordLogin.setText("ars1234")
        }

        return binding.root
    }


    private fun changePasswordHintFont(context: Context) {
        binding.emailLogin.setOnFocusChangeListener { _, _ ->


            if (binding.emailLogin.hasFocus()) {
                val typeface: Typeface? =
                    ResourcesCompat.getFont(context, R.font.roboto_bold)
                binding.emailWrapLogin.typeface = typeface

            } else {
                val typeface: Typeface? = ResourcesCompat.getFont(context, R.font.roboto_regular)
                binding.emailWrapLogin.typeface = typeface
            }
        }

        binding.passwordLogin.setOnFocusChangeListener { _, _ ->
            val typeface: Typeface? = ResourcesCompat.getFont(context, R.font.roboto_bold)

            if (binding.passwordLogin.hasFocus()) {

                binding.passwordWrapLogin.typeface = typeface

            } else {
                val typeface: Typeface? = ResourcesCompat.getFont(context, R.font.roboto_regular)
                binding.passwordWrapLogin.typeface = typeface
            }
        }
    }

}
