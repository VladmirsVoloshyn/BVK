package com.example.bvk.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.bvk.databinding.AddMandrelDialogFragmentBinding
import com.example.bvk.model.Mandrel
import com.example.bvk.shouldShowError

class AddFragment(private val callKey: String, val mandrel: Mandrel) : DialogFragment() {

    var mBinding: AddMandrelDialogFragmentBinding? = null
    private val binding get() = mBinding!!
    private var listener: OnAddOrEditMandrelListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = AddMandrelDialogFragmentBinding.inflate(layoutInflater, container, false)

        if (callKey == "new") {
            binding.buttonAdd.text = "Add"
        } else {
            binding.buttonAdd.text = "Update"
        }
        return binding.root

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
        listener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAdd.setOnClickListener {

            if (!binding.mandrelVertexDiameter.shouldShowError(
                    "Please, set vertex diameter",
                    binding.textInputLayoutName
                ) && !binding.mandrelBaseDiameter.shouldShowError(
                    "Please, set abase diameter",
                    binding.textInputLayoutAge
                )
            ) {
                if (callKey == "new") {
                    listener?.onMandrelAdd(
                        Mandrel(
                            vertexDiameter = binding.mandrelVertexDiameter.text.toString()
                                .toDouble(),
                            baseDiameter = binding.mandrelBaseDiameter.text.toString().toDouble(),
                            height = 0
                        )
                    )
                } else {
                    listener?.onMandrelEdit(
                        Mandrel(
                            vertexDiameter = binding.mandrelVertexDiameter.text.toString()
                                .toDouble(),
                            baseDiameter = binding.mandrelBaseDiameter.text.toString().toDouble(),
                            height = 0
                        )
                    )
                }
                dialog?.dismiss()
            }
        }
        binding.buttonSkip.setOnClickListener {
            dialog?.dismiss()
        }

    }

    interface OnAddOrEditMandrelListener {
        fun onMandrelAdd(mandrel: Mandrel)
        fun onMandrelEdit(mandrel: Mandrel)
    }
}