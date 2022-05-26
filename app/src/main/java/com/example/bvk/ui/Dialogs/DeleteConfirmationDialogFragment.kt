package com.example.bvk.ui.Dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.bvk.databinding.FragmentDeleteConfirmationDialogBinding
import com.example.bvk.model.Mandrel

class DeleteConfirmationDialogFragment(
    val mandrel: Mandrel,
    var listener: OnDeleteConfirmationListener,
    var position: Int
) : DialogFragment() {

    private var mBinding: FragmentDeleteConfirmationDialogBinding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentDeleteConfirmationDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.deleteConfirmLabel.append(mandrel.mandrelName)

        binding.confirmDialogDeleteButton.setOnClickListener {
            listener.onDeleteConfirm(position)
            dialog?.dismiss()
        }
        binding.confirmDialogCancelButton.setOnClickListener {
            dialog?.dismiss()
        }

    }


    interface OnDeleteConfirmationListener {
        fun onDeleteConfirm(position: Int)
    }
}