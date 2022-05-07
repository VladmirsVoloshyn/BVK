package com.example.bvk.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.bvk.databinding.AddMandrelDialogFragmentBinding
import com.example.bvk.model.Mandrel
import com.example.bvk.shouldShowError

class AddFragment(
    private val callKey: String,
    val mandrel: Mandrel,
    var iListener: OnAddOrEditMandrelListener
) : DialogFragment() {
    var mBinding: AddMandrelDialogFragmentBinding? = null
    private val binding get() = mBinding!!
    private var listener: OnAddOrEditMandrelListener? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = AddMandrelDialogFragmentBinding.inflate(layoutInflater, container, false)
        if (callKey == CALL_KEY_NEW) {
            binding.buttonAdd.text = ADD_BUTTON_TEXT
        } else {
            binding.buttonAdd.text = UPDATE_BUTTON_TEXT
            binding.mandrelVertexDiameter.setText(mandrel.vertexDiameter.toString())
            binding.mandrelBaseDiameter.setText(mandrel.baseDiameter.toString())
            binding.mandrelHeight.setText(mandrel.height.toString())
        }
        return binding.root
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = iListener
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
                    binding.textInputLayoutVertex
                ) && !binding.mandrelBaseDiameter.shouldShowError(
                    "Please, set a base diameter",
                    binding.textInputLayoutBase
                ) && !binding.mandrelHeight.shouldShowError(
                    "Please, set height",
                    binding.textInputLayoutHeight
                )
            ) {
                if (callKey == CALL_KEY_NEW) {
                    listener?.onMandrelAdd(
                        Mandrel(
                            vertexDiameter = binding.mandrelVertexDiameter.text.toString()
                                .toDouble(),
                            baseDiameter = binding.mandrelBaseDiameter.text.toString().toDouble(),
                            height = binding.mandrelHeight.text.toString().toInt()
                        )
                    )
                } else {
                    listener?.onMandrelEdit(
                        Mandrel(
                            id = mandrel.id,
                            vertexDiameter = binding.mandrelVertexDiameter.text.toString()
                                .toDouble(),
                            baseDiameter = binding.mandrelBaseDiameter.text.toString().toDouble(),
                            height = binding.mandrelHeight.text.toString().toInt()
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
    companion object {
        const val CALL_KEY_NEW = "new"
        const val ADD_BUTTON_TEXT = "Add"
        const val UPDATE_BUTTON_TEXT = "Update"
    }
}