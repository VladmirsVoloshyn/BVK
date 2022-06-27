package com.example.bvk.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.bvk.R
import com.example.bvk.databinding.SetValueDialogFragmentBinding
import com.example.bvk.shouldShowError

class SetValueDialogFragment(
    private val callKey: String,
    val oldValue: Int,
    private val onValueSetListener: OnValueSetListener? = null
) : DialogFragment() {

    private var mBinding: SetValueDialogFragmentBinding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = SetValueDialogFragmentBinding.inflate(inflater, container, false)
        binding.valueField.setText(oldValue.toString())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonEnter.setOnClickListener {
            if (!binding.valueField.shouldShowError(
                    activity?.resources?.getString(R.string.add_dialog_vertex_error_message),
                    binding.textInputLayoutValue
                )
            ) {
                if (callKey == CALL_KEY_ADHESIVE) {
                    onValueSetListener?.onValueSet(
                        binding.valueField.text.toString().toInt(),
                        CALL_KEY_ADHESIVE
                    )
                }
                if (callKey == CALL_KEY_MEMBRANE_DEPTH) {
                    onValueSetListener?.onValueSet(
                        binding.valueField.text.toString().toInt(),
                        CALL_KEY_MEMBRANE_DEPTH
                    )
                }
                dialog?.dismiss()
            }

        }

    }

    interface OnValueSetListener {
        fun onValueSet(newValue: Int, callKey: String)
    }


    companion object {
        private const val CALL_KEY_ADHESIVE = "adhesive"
        private const val CALL_KEY_MEMBRANE_DEPTH = "depth"
    }
}