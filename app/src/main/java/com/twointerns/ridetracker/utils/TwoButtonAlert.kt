package com.twointerns.ridetracker.utils

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.twointerns.ridetracker.R
import java.io.Serializable

class TwoButtonAlert : DialogFragment() {


    companion object {
        private const val ARG_MESSAGE = "message"
        private const val ARG_TITLE = "title"
        private const val ARG_BUTTON1 = "button1"
        private const val ARG_BUTTON2="button2"
        private const val ARG_CALLBACK1 = "callback1"
        private const val ARG_CALLBACK2 = "callback2"



        fun newInstance(title: String, message: String = "", button1: String,button2:String, handleClick1: () -> Unit = {},handleClick2: () -> Unit = {}): TwoButtonAlert = TwoButtonAlert().apply {
            arguments = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_MESSAGE, message)
                putString(ARG_BUTTON1, button1)
                putString(ARG_BUTTON2, button2)
                putSerializable(ARG_CALLBACK1, handleClick1 as Serializable)
                putSerializable(ARG_CALLBACK2, handleClick2 as Serializable)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        @Suppress("UNCHECKED_CAST")
        val handleClick1 = arguments?.getSerializable(ARG_CALLBACK1) as () -> Unit
        val handleClick2 = arguments?.getSerializable(ARG_CALLBACK2) as () -> Unit

        return AlertDialog.Builder(activity, R.style.DialogStyle)
            .setTitle(arguments?.getString(ARG_TITLE))
            .setMessage(arguments?.getString(ARG_MESSAGE))
            .setCancelable(false)
            .setPositiveButton(arguments?.getString(ARG_BUTTON1)) { _, _ ->
                dismiss()
                handleClick1()
            }
            .setNegativeButton(arguments?.getString(ARG_BUTTON2))
            { _, _ ->
                dismiss()
                handleClick2()
            }

            .create()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (ignored: IllegalStateException) {

        }
    }


}