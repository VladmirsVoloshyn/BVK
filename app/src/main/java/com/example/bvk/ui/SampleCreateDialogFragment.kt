package com.example.bvk.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
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
                    "Please, set cap vertex diameter",
                    binding.textInputLayoutCapVertex
                ) && !binding.mandrelCapHeight.shouldShowError(
                    "Please, set a cap height",
                    binding.textInputLayoutCapHeight
                )
            ) {

                onSampleCreatedListener?.onSampleCreate(
                    SampleCapParameters(
                        binding.capVertexDiameter.text.toString().toInt(),
                        binding.mandrelCapHeight.text.toString().toInt()
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