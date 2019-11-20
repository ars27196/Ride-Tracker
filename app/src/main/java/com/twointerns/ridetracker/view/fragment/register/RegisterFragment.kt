package com.twointerns.ridetracker.view.fragment.register

import android.content.Context
import android.graphics.Typeface
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.twointerns.ridetracker.R
import com.twointerns.ridetracker.databinding.FragmentRegisterBinding
import com.twointerns.ridetracker.utils.OneButtonAlert
import com.twointerns.ridetracker.utils.ShowProgressAlert
import com.twointerns.ridetracker.viewmodel.RegisterViewModel

class RegisterFragment : Fragment() {

    private var dialog = ShowProgressAlert.newInstance()
    private lateinit var viewModel: RegisterViewModel
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        (viewModel).error.observe(this, Observer {
            if (it == "show") {
                OneButtonAlert.newInstance("Invalid Entry", "One or more fields are empty", "Okay")
                    .show(childFragmentManager, "")

            }
        })
        (viewModel).userAlreadyCreated.observe(this, Observer {
            if (it != null) {
                OneButtonAlert.newInstance("Invalid Sign Up", it, "Okay")
                    .show(childFragmentManager, "")

            }
        })

        viewModel.showDialog.observe(this, Observer {
            if (it) {
                if (!dialog.isVisible) {
                    dialog.show(childFragmentManager, "lol")
                }
            } else {
                dialog.dismiss()
            }

        })

        (viewModel ).sucess.observe(this, Observer {
            if (it) {
                Toast.makeText(context,"User Registration Successful",Toast.LENGTH_LONG).show()
                findNavController().popBackStack()

            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.register = viewModel
        changePasswordHintFont(context!!)


        binding.loginLinkRegister.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    private fun changePasswordHintFont(context: Context) {
        binding.emailRegister.setOnFocusChangeListener { _, _ ->


            if (binding.emailRegister.hasFocus()) {
                val typeface: Typeface? =
                    ResourcesCompat.getFont(context, R.font.roboto_bold)
                binding.emailWrapRegister.typeface = typeface

            } else {
                val typeface: Typeface? = ResourcesCompat.getFont(context, R.font.roboto_regular)
                binding.emailWrapRegister.typeface = typeface
            }
        }

        binding.passwordRegister.setOnFocusChangeListener { _, _ ->
            val typeface: Typeface? = ResourcesCompat.getFont(context, R.font.roboto_bold)

            if (binding.passwordRegister.hasFocus()) {

                binding.passwordWrapRegister.typeface = typeface

            } else {
                val typeface: Typeface? = ResourcesCompat.getFont(context, R.font.roboto_regular)
                binding.passwordWrapRegister.typeface = typeface
            }
        }
    }

}
