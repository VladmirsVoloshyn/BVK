package com.example.bvk.ui.Dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.bvk.R
import com.example.bvk.databinding.AddPackageSchemaDialogFragmentBinding
import com.example.bvk.model.Mandrel
import com.example.bvk.model.packageschema.PackageSchema

class AddPackageSchemaDialogFragment(
    private val callKey: String,
    val mandrel: Mandrel,
    var iListener: OnAddOrEditPackageSchemaListener
) : DialogFragment() {

    var mBinding: AddPackageSchemaDialogFragmentBinding? = null
    private val binding get() = mBinding!!
    private var listener: OnAddOrEditPackageSchemaListener? = null
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = AddPackageSchemaDialogFragmentBinding.inflate(layoutInflater, container, false)
        if (callKey == CALL_KEY_NEW) {
            binding.buttonAdd.text = activity?.resources?.getText(R.string.add_button_label)
        } else {
            //return editable package schema
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
                if (callKey == CALL_KEY_NEW) {
                    listener?.onSchemaAdd(
                        PackageSchema(
                            schemaName = binding.schemaName.toString(),
                            boxType = binding.boxType.toString(),
                            firstLineCount = binding.schemaParameters.toString()[0].toInt(),
                            secondLineCount = binding.schemaParameters.toString()[1].toInt()
                        )
                    )
                }
                dialog?.dismiss()
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