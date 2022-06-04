package com.example.bvk.ui.packageschema

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bvk.databinding.SchemaRecyclerContainerBinding
import com.example.bvk.model.packageschema.PackageSchema

class PackageSchemaAdapter(
    val schemasList: List<PackageSchema>,
    val context: Context
) : RecyclerView.Adapter<PackageSchemaAdapter.PackageSchemaViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val res: Resources = context.resources
    private var _binding: SchemaRecyclerContainerBinding? = null
    private val binding get() = _binding!!


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageSchemaViewHolder {
        _binding = SchemaRecyclerContainerBinding.inflate(layoutInflater, parent, false)
        return PackageSchemaViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PackageSchemaViewHolder, position: Int) {
        binding.schemaNameTextView.text = schemasList[position].schemaName
        binding.boxTypeTextView.text = schemasList[position].boxType
        binding.schemaSignsTextView.text = schemasList[position].firstLineCount.toString() +"-"+ schemasList[position].secondLineCount.toString()
        binding.schemaImage.setSchema(schemasList[position].firstLineCount, schemasList[position].secondLineCount)
    }

    override fun getItemCount(): Int = schemasList.count()

    inner class PackageSchemaViewHolder(binding: SchemaRecyclerContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

        }
    }
}