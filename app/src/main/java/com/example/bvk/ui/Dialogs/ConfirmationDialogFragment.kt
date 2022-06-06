package com.example.bvk.ui.Dialogs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.bvk.R
import com.example.bvk.databinding.FragmentConfirmationDialogBinding
import com.example.bvk.model.Mandrel
import com.example.bvk.model.packageschema.PackageSchema

class ConfirmationDialogFragment(
    private val callKeyEvent: String,
    val mandrel: Mandrel = Mandrel(),
    val packageSchema: PackageSchema = PackageSchema(),
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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (callKeyEvent == DELETE_MANDREL_CONFIRMATION_CALL_KEY) {
            binding.deleteConfirmLabel.text = getString(R.string.confirmation_dialog_delete_mandrel_label) + SPACE + mandrel.mandrelName+ QST
            binding.confirmDialogSubmitButton.text = activity?.resources?.getText(R.string.menu_item_delete)

            binding.confirmDialogSubmitButton.setOnClickListener {
                listener.onDeleteConfirm(position, DELETE_MANDREL_CONFIRMATION_CALL_KEY)
                dialog?.dismiss()
            }
        }

        if (callKeyEvent == RESTORE_DEFAULT_CONFIRMATION_CALL_KEY) {

            binding.deleteConfirmLabel.text =
                activity?.resources?.getText(R.string.confirmation_dialog_restore_default_label)
            binding.confirmDialogSubmitButton.text = activity?.resources?.getText(R.string.confirm_restore_default_button_label)
            binding.confirmDialogSubmitButton.setOnClickListener {
                listener.onRestoreDefaultConfirm()
                dialog?.dismiss()
            }
        }

        if (callKeyEvent == DELETE_PACKAGE_SCHEMA_CONFIRMATION_CALL_KEY) {

            binding.deleteConfirmLabel.text = getString(R.string.confirmation_dialog_delete_schema_label) + SPACE + packageSchema.schemaName+ QST
            binding.confirmDialogSubmitButton.text = activity?.resources?.getText(R.string.menu_item_delete)

            binding.confirmDialogSubmitButton.setOnClickListener {
                listener.onDeleteConfirm(position, DELETE_PACKAGE_SCHEMA_CONFIRMATION_CALL_KEY)
                dialog?.dismiss()
            }
        }
        binding.confirmDialogCancelButton.setOnClickListener {
            dialog?.dismiss()
        }
    }

    companion object {
        const val DELETE_MANDREL_CONFIRMATION_CALL_KEY = "DELETE_MANDREL"
        const val DELETE_PACKAGE_SCHEMA_CONFIRMATION_CALL_KEY = "DELETE_PACKAGE_SCHEMA"
        const val RESTORE_DEFAULT_CONFIRMATION_CALL_KEY = "RESTORE"
        const val SPACE = " "
        const val QST = "?"
    }


    interface OnConfirmationListener {
        fun onDeleteConfirm(position: Int, confirmationKey : String)
        fun onRestoreDefaultConfirm()
    }
}