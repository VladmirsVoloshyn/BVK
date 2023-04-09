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
    private val oldValue: Int,
    private var onValueSetListener: OnValueSetListener? = null
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
        if (callKey == CALL_KEY_ADHESIVE) {
            binding.labelDialog.append(SPACE +getString(R.string.set_value_dialog_adhesive_line_label) + SPACE + getString(R.string.millimeter_postfix))
        }
        if (callKey == CALL_KEY_MEMBRANE_DEPTH){
            binding.labelDialog.append(SPACE + getString(R.string.set_value_dialog_membrane_length_label)+ SPACE + getString(R.string.micrometer_postfix))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonEnter.setOnClickListener {
            if (!binding.valueField.shouldShowError(
                    "Please, set value",
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
        binding.buttonSkip.setOnClickListener {
            dialog?.dismiss()
        }

    }

    interface OnValueSetListener {
        fun onValueSet(newValue: Int, callKey: String)
    }



    override fun onDestroyView() {
        mBinding = null
        onValueSetListener = null
        super.onDestroyView()

    }


    companion object {
        private const val SPACE = " "
        private const val CALL_KEY_ADHESIVE = "adhesive"
        private const val CALL_KEY_MEMBRANE_DEPTH = "depth"
    }
}