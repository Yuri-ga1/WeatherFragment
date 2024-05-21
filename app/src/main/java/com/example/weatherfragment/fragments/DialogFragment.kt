package com.example.weatherfragment


import android.app.Dialog
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.weatherfragment.fragments.FullWeatherInfo
import com.example.weatherfragment.fragments.ShortWeatherInfo
import com.example.weatherfragment.fragments.UserWeatherInfo

class MyDialogFragment : DialogFragment(){

    var onPositiveClickListener: OnClickListener? = null
    var onNegativeClickListener: OnClickListener? = null
    var onNeutralClickListener: OnClickListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Внимание!")
            .setMessage("Выберете вариант отображения погоды")
            .setPositiveButton("Кратко") { dialogInterface, which ->
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainerView, ShortWeatherInfo())
                    .commitAllowingStateLoss()
                onPositiveClickListener?.onClick(dialog, which)
                dismiss()
            }
            .setNegativeButton("Подробно") { dialogInterface, which ->
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainerView, FullWeatherInfo())
                    .commitAllowingStateLoss()
                onNegativeClickListener?.onClick(dialog, which)
                dismiss()
            }
            .setNeutralButton("Свои города"){ dialogInterface, which ->
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainerView, UserWeatherInfo())
                    .commitAllowingStateLoss()
                onNeutralClickListener?.onClick(dialog, which)
                dismiss()
            }
            .create()
            .apply {
                setCancelable(false)
                setCanceledOnTouchOutside(false)
            }
    }
}