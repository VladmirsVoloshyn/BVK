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
    var iListener: OnAddOrEditPackageSchemaListener
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
            binding.schemaName.setText(packageSchema.schemaName)
            binding.boxType.setText(packageSchema.boxType)
            binding.firstLineCount.setText(packageSchema.firstLineCount.toString())
            binding.schemaSecondLine.setText(packageSchema.secondLineCount.toString())
            binding.capAmountInBox.setText(packageSchema.capAmountInBox.toString())
            binding.capAmountInBundle.setText(packageSchema.capAmountInBundle.toString())
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
                    getString(R.string.add_dialog_second_line_eror_message),
                    binding.textInputLayoutSchemaParameters
                )
                && !binding.capAmountInBox.shouldShowError(
                    getString(R.string.add_dialog_cap_amount_in_box_error_message),
                    binding.textInputLayoutCapAmountInBox
                )
                && !binding.capAmountInBundle.shouldShowError(
                    getString(R.string.add_dialog_cap_in_bundle_error_message),
                    binding.textInputLayoutCapAmountInBundle
                )
            ) {
                if (callKey == CALL_KEY_NEW) {
                    listener?.onSchemaAdd(
                        PackageSchema(
                            schemaName = binding.schemaName.text.toString(),
                            boxType = binding.boxType.text.toString(),
                            firstLineCount = binding.firstLineCount.text.toString().toInt(),
                            secondLineCount = binding.schemaSecondLine.text.toString().toInt(),
                            capAmountInBox = binding.capAmountInBox.text.toString().toInt(),
                            capAmountInBundle = binding.capAmountInBundle.text.toString().toInt()
                        )
                    )
                } else {
                    listener?.onSchemaEdit(
                        PackageSchema(
                            id = packageSchema.id,
                            schemaName = binding.schemaName.text.toString(),
                            boxType = binding.boxType.text.toString(),
                            firstLineCount = binding.firstLineCount.text.toString().toInt(),
                            secondLineCount = binding.schemaSecondLine.text.toString().toInt(),
                            capAmountInBox = binding.capAmountInBox.text.toString().toInt(),
                            capAmountInBundle = binding.capAmountInBundle.text.toString().toInt()
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

    interface OnAddOrEditPackageSchemaListener {
        fun onSchemaAdd(schema: PackageSchema)
        fun onSchemaEdit(schema: PackageSchema)
    }

    companion object {
        const val CALL_KEY_NEW = "new"
    }
}