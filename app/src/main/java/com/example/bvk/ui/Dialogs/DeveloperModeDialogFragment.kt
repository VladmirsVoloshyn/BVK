package com.example.bvk.ui.Dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.bvk.R
import com.example.bvk.databinding.DeveloperModeDialogFragmentBinding
import com.example.bvk.shouldShowError

class DeveloperModeDialogFragment(
    private val password: String,
    private var passwordEnterListener: OnPasswordEnterListener?
) : DialogFragment() {

    var mBinding: DeveloperModeDialogFragmentBinding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DeveloperModeDialogFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDetach() {
        passwordEnterListener = null
        mBinding = null
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonEnter.setOnClickListener {
            when {
                password == binding.password.text.toString() -> {
                    passwordEnterListener?.onPasswordEnter()
                    dialog?.dismiss()
                }
                password != binding.password.text.toString() -> {
                    binding.textInputLayoutPassword.error = activity?.resources?.getText(R.string.incorrect_password_error_message)
                }
                else -> {
                    binding.password.shouldShowError(
                        activity?.resources?.getText(R.string.incorrect_password_error_message).toString(),
                        binding.textInputLayoutPassword
                    )
                }
            }
        }
        binding.buttonSkip.setOnClickListener {
            dialog?.dismiss()
        }
    }

    interface OnPasswordEnterListener {
        fun onPasswordEnter()
    }
}