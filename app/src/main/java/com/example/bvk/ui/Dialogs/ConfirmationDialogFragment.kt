package com.example.bvk.ui.Dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.bvk.R
import com.example.bvk.databinding.FragmentConfirmationDialogBinding
import com.example.bvk.model.Mandrel

class ConfirmationDialogFragment(
    private val callKeyEvent: String,
    val mandrel: Mandrel = Mandrel(),
    var listener: OnConfirmationListener,
    var position: Int = 0
) : DialogFragment() {

    private var mBinding: FragmentConfirmationDialogBinding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentConfirmationDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (callKeyEvent == DELETE_CONFIRMATION_CALL_KEY) {

            binding.deleteConfirmLabel.append(mandrel.mandrelName)
            binding.confirmDialogSubmitButton.text = activity?.resources?.getText(R.string.menu_item_delete)

            binding.confirmDialogSubmitButton.setOnClickListener {
                listener.onDeleteConfirm(position)
                dialog?.dismiss()
            }
            binding.confirmDialogCancelButton.setOnClickListener {
                dialog?.dismiss()
            }
        }

        if (callKeyEvent == RESTORE_DEFAULT_CONFIRMATION_CALL_KEY) {

            binding.deleteConfirmLabel.text =
                activity?.resources?.getText(R.string.confirnation_dialog_restore_default_label)
            binding.confirmDialogSubmitButton.text = activity?.resources?.getText(R.string.confirm_restore_default_button_label)
            binding.confirmDialogSubmitButton.setOnClickListener {
                listener.onRestoreDefaultConfirm()
                dialog?.dismiss()
            }
            binding.confirmDialogCancelButton.setOnClickListener {
                dialog?.dismiss()
            }
        }
    }

    companion object {
        const val DELETE_CONFIRMATION_CALL_KEY = "DELETE"
        const val RESTORE_DEFAULT_CONFIRMATION_CALL_KEY = "RESTORE"
    }


    interface OnConfirmationListener {
        fun onDeleteConfirm(position: Int)
        fun onRestoreDefaultConfirm()
    }
}