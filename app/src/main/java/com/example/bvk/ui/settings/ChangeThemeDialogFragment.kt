package com.example.bvk.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

import com.example.bvk.databinding.FragmentDialogSetThemeBinding

@Deprecated("will improved in latest versions")
class ChangeThemeDialogFragment(
    private var listener: OnThemeChangeListener? = null,
    private val currentTheme: String
) :
    DialogFragment() {

    private var _binding: FragmentDialogSetThemeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogSetThemeBinding.inflate(layoutInflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var theme = "none"
        with(binding) {
            when (currentTheme) {
                "dark" -> dark.isChecked = true
                "light" -> light.isChecked = true
                "green" -> green.isChecked = true
                "brown" -> brown.isChecked = true
            }
            dark.setOnClickListener {
                theme = Theme.DARK.desc
            }
            light.setOnClickListener {
                theme = Theme.LIGHT.desc
            }
            green.setOnClickListener {
                theme = Theme.GREEN.desc
            }
            brown.setOnClickListener {
                theme = Theme.BROWN.desc
            }
            cancel.setOnClickListener {
                if (theme == "none" || theme == currentTheme) {
                    dialog?.dismiss()
                } else{
                    listener?.onThemeChanged(theme)
                    dialog?.dismiss()
                }
            }
        }
    }

    override fun onDetach() {
        _binding = null
        listener = null
        super.onDetach()
    }

    interface OnThemeChangeListener {
        fun onThemeChanged(theme : String)
    }

}
