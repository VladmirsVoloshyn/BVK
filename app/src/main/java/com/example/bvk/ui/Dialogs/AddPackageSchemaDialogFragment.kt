package com.example.bvk.ui.Dialogs

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.bvk.R
import com.example.bvk.databinding.AddPackageSchemaDialogFragmentBinding
import com.example.bvk.model.packageschema.PackageSchema
import com.example.bvk.shouldShowError

class AddPackageSchemaDialogFragment(
    private val callKey: String,
    val packageSchema: PackageSchema,
    var iListener: OnAddOrEditPackageSchemaListener,
    private val schemasUniqueNamesList: ArrayList<String>
) : DialogFragment() {

    var mBinding: AddPackageSchemaDialogFragmentBinding? = null
    private val binding get() = mBinding!!
    private var listener: OnAddOrEditPackageSchemaListener? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = AddPackageSchemaDialogFragmentBinding.inflate(layoutInflater, container, false)
        if (callKey == CALL_KEY_NEW) {
            binding.buttonAdd.text = activity?.resources?.getText(R.string.add_button_label)
        } else {
            binding.buttonAdd.text = resources.getText(R.string.update_button_label)
            binding.schemaName.setText(packageSchema.schemaName)
            binding.boxType.setText(packageSchema.boxType)
            binding.firstLineCount.setText(packageSchema.firstLineCount.toString())
            binding.schemaSecondLine.setText(packageSchema.secondLineCount.toString())
            binding.layerCount.setText(packageSchema.layerCount.toString())
            binding.capAmountInBundle.setText(packageSchema.capAmountInBundle.toString())
            binding.isStraightLayingCheckBox.isChecked = packageSchema.isStraightLaying
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
            if (!binding.schemaName.shouldShowError(
                    getString(R.string.add_dialog_set_name_error_message),
                    binding.textInputLayoutSchemaName
                ) && !binding.boxType.shouldShowError(
                    getString(R.string.add_dialog_box_type_error_message),
                    binding.textInputLayoutBoxType
                ) && !binding.firstLineCount.shouldShowError(
                    getString(R.string.add_dialog_first_line_count_error_message),
                    binding.textInputLayoutSchemaParameters
                )
                && !binding.schemaSecondLine.shouldShowError(
                    getString(R.string.add_dialog_second_line_error_message),
                    binding.textInputLayoutSchemaSecondLine
                )
                && !binding.layerCount.shouldShowError(
                    getString(R.string.add_schema_dialog_layer_count_error_message),
                    binding.textInputLayoutCapLayerCount
                )
                && !binding.capAmountInBundle.shouldShowError(
                    getString(R.string.add_dialog_cap_in_bundle_error_message),
                    binding.textInputLayoutCapAmountInBundle
                )
            ) {
                if (callKey == CALL_KEY_NEW) {

                    if (isUniqueName(binding.schemaName.text.toString())) {

                        val isStraightLaying = binding.isStraightLayingCheckBox.isChecked
                        listener?.onSchemaAdd(
                            PackageSchema(
                                schemaName = binding.schemaName.text.toString(),
                                boxType = binding.boxType.text.toString(),
                                firstLineCount = binding.firstLineCount.text.toString().toInt(),
                                secondLineCount = binding.schemaSecondLine.text.toString().toInt(),
                                layerCount = binding.layerCount.text.toString().toInt(),
                                capAmountInBundle = binding.capAmountInBundle.text.toString()
                                    .toInt(),
                                isStraightLaying = isStraightLaying
                            )
                        )
                        dialog?.dismiss()
                    } else if (!isUniqueName(binding.schemaName.text.toString())) {
                        binding.textInputLayoutSchemaName.error =
                            activity?.resources?.getString(R.string.add_dialog_unique_name_error_message)
                    }

                } else {
                    var pretindent: String? = null
                    for (schemaName in schemasUniqueNamesList) {
                        if (schemaName == packageSchema.schemaName) {
                            pretindent = schemaName
                        }
                    }
                    if (pretindent != null) {
                        schemasUniqueNamesList.remove(pretindent)
                    }
                    if (isUniqueName(binding.schemaName.text.toString())) {
                        val isStraightLaying = binding.isStraightLayingCheckBox.isChecked
                        listener?.onSchemaEdit(
                            PackageSchema(
                                id = packageSchema.id,
                                schemaName = binding.schemaName.text.toString(),
                                boxType = binding.boxType.text.toString(),
                                firstLineCount = binding.firstLineCount.text.toString().toInt(),
                                secondLineCount = binding.schemaSecondLine.text.toString().toInt(),
                                layerCount = binding.layerCount.text.toString().toInt(),
                                capAmountInBundle = binding.capAmountInBundle.text.toString()
                                    .toInt(),
                                isStraightLaying = isStraightLaying
                            )
                        )
                        dialog?.dismiss()
                    } else if (!isUniqueName(binding.schemaName.text.toString())) {
                        binding.textInputLayoutSchemaName.error =
                            activity?.resources?.getString(R.string.add_dialog_unique_name_error_message)
                    }
                }

            }
        }
        binding.buttonSkip.setOnClickListener {
            dialog?.dismiss()
        }

    }

    private fun isUniqueName(_schemaName: String): Boolean {
        for (schemaName in schemasUniqueNamesList) {
            if (_schemaName == schemaName) {
                return false
            }
        }
        return true
    }

    interface OnAddOrEditPackageSchemaListener {
        fun onSchemaAdd(schema: PackageSchema)
        fun onSchemaEdit(schema: PackageSchema)
    }

    companion object {
        const val CALL_KEY_NEW = "new"
    }
}