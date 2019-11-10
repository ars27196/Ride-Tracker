package com.twointerns.ridetracker.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.fragment.app.DialogFragment
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.twointerns.ridetracker.R


class ShowProgressAlert : DialogFragment() {
    lateinit var ft :FragmentTransaction

    companion object {
        val dialog = this
        fun newInstance(): ShowProgressAlert = ShowProgressAlert()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.progress_dialog, container)

    }


    override fun show(manager: FragmentManager, tag: String?) {
        try {
            ft = manager.beginTransaction()

            if (this.isAdded) {
//                ft.remove(this)

            }else {
                ft.add(this, tag)
                ft.addToBackStack(null)
                ft.commitAllowingStateLoss()
            }
        } catch (e: IllegalStateException) {

        }
    }

    override fun dismiss() {
        ft.remove(this)
        super.dismiss()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDialog()?.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            setCanceledOnTouchOutside(false)
        }


    }

}