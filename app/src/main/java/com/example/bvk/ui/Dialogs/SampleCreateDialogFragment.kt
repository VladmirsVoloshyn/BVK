package com.example.bvk.ui.Dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.bvk.R
import com.example.bvk.databinding.NewSampleCreateDialogFragmentBinding
import com.example.bvk.model.sample.SampleCapParameters
import com.example.bvk.shouldShowError

class SampleCreateDialogFragment(
    var onSampleCreatedListener: OnSampleCreatedListener? = null
) : DialogFragment() {

    var iListener: OnSampleCreatedListener? = null
    var mBinding: NewSampleCreateDialogFragmentBinding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = NewSampleCreateDialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonCreate.setOnClickListener {

            if (!binding.capVertexDiameter.shouldShowError(
                    activity?.resources?.getString(R.string.sample_dialog_vertex_error_message),
                    binding.textInputLayoutCapVertex
                ) && !binding.mandrelCapHeight.shouldShowError(
                    activity?.resources?.getString(R.string.sample_dialog_height_error_message),
                    binding.textInputLayoutCapHeight
                )
                && !binding.capCapInversion.shouldShowError(
                    activity?.resources?.getString(R.string.sample_dialog_inversion_error_message),
                    binding.textInputLayoutCapInversion
                )
                && !binding.capTotalAmount.shouldShowError(
                    activity?.resources?.getString(R.string.sample_dialog_amount_error_message),
                    binding.textInputLayoutCapTotalAmount
                )
            ) {

                onSampleCreatedListener?.onSampleCreate(
                    SampleCapParameters(
                        binding.capVertexDiameter.text.toString().toInt(),
                        binding.mandrelCapHeight.text.toString().toInt(),
                        binding.capCapInversion.text.toString().toInt(),
                        binding.capTotalAmount.text.toString().toInt()
                    )
                )
                dialog?.dismiss()
            }
        }
        binding.buttonSkip.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        iListener = onSampleCreatedListener
    }

    override fun onDestroy() {
        mBinding = null
        onSampleCreatedListener = null
        super.onDestroy()
    }

    interface OnSampleCreatedListener {
        fun onSampleCreate(sampleCapParam: SampleCapParameters)
    }
}