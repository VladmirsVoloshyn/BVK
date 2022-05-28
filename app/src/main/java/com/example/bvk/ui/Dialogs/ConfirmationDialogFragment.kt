package com.example.bvk.ui.Dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.bvk.databinding.FragmentConfirmationDialogBinding
import com.example.bvk.model.Mandrel

class ConfirmationDialogFragment(
    private val callKeyEvent: String,
    val mandrel: Mandrel,
    var listener: OnConfirmationListener,
    var position: Int
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
                "Вы уверены, что хотите восстановить умолчания? Все введенные данные будут потеряны. Потребуется перезапуск приложения"

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