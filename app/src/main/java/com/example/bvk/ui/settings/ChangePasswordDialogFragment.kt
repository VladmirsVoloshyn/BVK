package com.example.bvk.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.bvk.R
import com.example.bvk.databinding.DialogFragmentChangePasswordBinding
import com.example.bvk.shouldShowError

class ChangePasswordDialogFragment(
    private val oldPassword: String?,
    private var listener: OnPasswordChangeListener?
) : DialogFragment() {

    private var _binding: DialogFragmentChangePasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentChangePasswordBinding.inflate(layoutInflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonChange.setOnClickListener {
            if (!binding.oldPasswordTextEdit.shouldShowError(
                    getString(R.string.change_password_dialog_old_password_error),
                    binding.oldPasswordLayout
                ) && !binding.newPasswordTextEdit.shouldShowError(
                    getString(R.string.change_password_dialog_new_password_error),
                    binding.newPasswordLayout
                )
            ) {
                if (binding.oldPasswordTextEdit.text.toString() != oldPassword) {
                    binding.oldPasswordLayout.error = getString(R.string.change_password_dialog_incorrect_old_password_error)
                }else if (binding.oldPasswordTextEdit.text.toString() == oldPassword){
                    listener?.onPasswordChanged(binding.newPasswordTextEdit.text.toString())
                    dialog?.dismiss()
                }
            }
        }
        binding.buttonCancel.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun onDetach() {
        _binding = null
        listener = null
        super.onDetach()
    }

    interface OnPasswordChangeListener {
        fun onPasswordChanged(password: String)
    }

}