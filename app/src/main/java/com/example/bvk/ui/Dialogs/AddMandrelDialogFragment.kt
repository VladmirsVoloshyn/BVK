package com.example.bvk.ui.Dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.bvk.R
import com.example.bvk.databinding.AddMandrelDialogFragmentBinding
import com.example.bvk.model.Mandrel
import com.example.bvk.shouldShowError

class AddMandrelDialogFragment(
    private val callKey: String,
    val mandrel: Mandrel,
    var iListener: OnAddOrEditMandrelListener,
    private val mandrelsUniqueNamesList : ArrayList<String>
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
            binding.buttonAdd.text = activity?.resources?.getText(R.string.add_button_label)
        } else {
            binding.buttonAdd.text = activity?.resources?.getText(R.string.update_button_label)
            binding.mandrelName.setText(mandrel.mandrelName)
            binding.mandrelVertexDiameter.setText(mandrel.vertexDiameter.toString())
            binding.mandrelBaseDiameter.setText(mandrel.baseDiameter.toString())
            binding.mandrelHeight.setText(mandrel.height.toString())
            binding.mandrelInfelicity.setText(mandrel.infelicityCoefficient.toString())
            binding.mandrelMaxInfelicityHeight.setText(mandrel.maxInfelicityHeight.toString())
        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = iListener
    }

    override fun onDestroyView() {
        mBinding = null
        listener = null
        super.onDestroyView()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonAdd.setOnClickListener {

            if (!binding.mandrelVertexDiameter.shouldShowError(
                    activity?.resources?.getString(R.string.add_dialog_vertex_error_message),
                    binding.textInputLayoutVertex
                ) && !binding.mandrelBaseDiameter.shouldShowError(
                    activity?.resources?.getString(R.string.add_dialog_base_error_message),
                    binding.textInputLayoutBase
                ) && !binding.mandrelHeight.shouldShowError(
                    activity?.resources?.getString(R.string.add_dialog_height_error_message),
                    binding.textInputLayoutHeight
                )
                && !binding.mandrelName.shouldShowError(
                    activity?.resources?.getString(R.string.add_dialog_name_error_message),
                    binding.textInputLayoutHeight
                )
                && !binding.mandrelInfelicity.shouldShowError(
                    getString(R.string.add_dialog_infelicity_error_message),
                    binding.textInputLayoutInfelicity
                )
                && !binding.mandrelMaxInfelicityHeight.shouldShowError(
                    getString(R.string.add_dialog_infelicity_error_message),
                    binding.textInputLayoutMaxInfelicityHeight
                )
            ) {
                if (callKey == CALL_KEY_NEW) {
                    if (isUniqueName(binding.mandrelName.text.toString())) {
                        listener?.onMandrelAdd(
                            Mandrel(
                                mandrelName = binding.mandrelName.text.toString(),
                                vertexDiameter = binding.mandrelVertexDiameter.text.toString()
                                    .toDouble(),
                                baseDiameter = binding.mandrelBaseDiameter.text.toString()
                                    .toDouble(),
                                height = binding.mandrelHeight.text.toString().toInt(),
                                infelicityCoefficient = binding.mandrelInfelicity.text.toString()
                                    .toDouble(),
                                maxInfelicityHeight = binding.mandrelMaxInfelicityHeight.text.toString()
                                    .toInt()
                            )
                        )
                        dialog?.dismiss()
                    }else  if (!isUniqueName(binding.mandrelName.text.toString())){
                        binding.textInputLayoutName.error = activity?.resources?.getString(R.string.add_dialog_unique_name_error_message)
                    }
                } else {
                    var pretindent : String? = null
                    for (mandrelName in  mandrelsUniqueNamesList){
                        if (mandrelName == mandrel.mandrelName){
                            pretindent = mandrelName
                        }
                    }
                    if (pretindent!=null){
                        mandrelsUniqueNamesList.remove(pretindent)
                    }
                    if (isUniqueName(binding.mandrelName.text.toString())) {
                        listener?.onMandrelEdit(
                            Mandrel(
                                mandrelName = binding.mandrelName.text.toString(),
                                id = mandrel.id,
                                vertexDiameter = binding.mandrelVertexDiameter.text.toString()
                                    .toDouble(),
                                baseDiameter = binding.mandrelBaseDiameter.text.toString()
                                    .toDouble(),
                                height = binding.mandrelHeight.text.toString().toInt(),
                                infelicityCoefficient = binding.mandrelInfelicity.text.toString()
                                    .toDouble(),
                                maxInfelicityHeight = binding.mandrelMaxInfelicityHeight.text.toString()
                                    .toInt()
                            )
                        )
                        dialog?.dismiss()
                    }else if (!isUniqueName(binding.mandrelName.text.toString())) {
                        binding.textInputLayoutName.error =
                            activity?.resources?.getString(R.string.add_dialog_unique_name_error_message)
                    }
                }
            }
        }
        binding.buttonSkip.setOnClickListener {
            dialog?.dismiss()
        }

    }

    private fun isUniqueName(mandrelName : String): Boolean{
        for (mandrelsName in mandrelsUniqueNamesList){
            if (mandrelName == mandrelsName){
                return false
            }
        }
        return true
    }

    interface OnAddOrEditMandrelListener {
        fun onMandrelAdd(mandrel: Mandrel)
        fun onMandrelEdit(mandrel: Mandrel)
    }

    companion object {
        const val CALL_KEY_NEW = "new"
    }
}