package com.example.bvk.ui.packageschema

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.bvk.R
import com.example.bvk.databinding.SchemaRecyclerContainerBinding
import com.example.bvk.model.Mandrel
import com.example.bvk.model.packageschema.PackageSchema

class PackageSchemaAdapter(
    private val schemasList: List<PackageSchema>,
    val context: Context,
    val listener: OnSchemaListButtonClickListener?
) : RecyclerView.Adapter<PackageSchemaAdapter.PackageSchemaViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val res: Resources = context.resources
    private var _binding: SchemaRecyclerContainerBinding? = null
    private val binding get() = _binding!!


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageSchemaViewHolder {
        _binding = SchemaRecyclerContainerBinding.inflate(layoutInflater, parent, false)
        setUIMode()
        return PackageSchemaViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PackageSchemaViewHolder, position: Int) {
        binding.schemaNameTextView.text =
            context.getString(R.string.package_schema_name_label) + SPACE + schemasList[position].schemaName
        binding.boxTypeTextView.text =
            context.getString(R.string.package_schema_box_type_label) + SPACE + schemasList[position].boxType
        binding.schemaSignsTextView.text =
            context.getString(R.string.schema_format_label) + SPACE + schemasList[position].firstLineCount.toString() + context.getString(
                R.string.name_separator
            ) + schemasList[position].secondLineCount.toString()
        binding.totalCountInBoxTextView.text =
            context.getString(R.string.schema_cup_in_box_label) + SPACE + schemasList[position].capAmountInBox
        binding.totalCountInBundleTextView.text =
            context.getString(R.string.schema_cap_in_bundle_label) + SPACE + schemasList[position].capAmountInBundle

        binding.schemaImage.setSchema(
            schemasList[position].firstLineCount,
            schemasList[position].secondLineCount
        )

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setUIMode() {
        when (res.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.schemaDataLayout.background = res.getDrawable(R.drawable.card_bg)
                binding.menuButton.background = res.getDrawable(R.drawable.card_bg)


            }
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.schemaDataLayout.background =
                    res.getDrawable(R.drawable.card_bg_night)
                binding.menuButton.background = res.getDrawable(R.drawable.card_bg_night)
                binding.schemaNameTextView.setTextColor(res.getColor(R.color.text_mode_night))
                binding.boxTypeTextView.setTextColor(res.getColor(R.color.text_mode_night))
                binding.schemaSignsTextView.setTextColor(res.getColor(R.color.text_mode_night))
                binding.totalCountInBoxTextView.setTextColor(res.getColor(R.color.text_mode_night))
                binding.totalCountInBundleTextView.setTextColor(res.getColor(R.color.text_mode_night))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = schemasList.count()

    companion object {
        private const val SPACE = " "
    }

    inner class PackageSchemaViewHolder(binding: SchemaRecyclerContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var popupMenu: PopupMenu

        init {
            binding.menuButton.setOnClickListener {
                popupMenu = PopupMenu(context, it)
                popupMenu.inflate(R.menu.recycler_container_menu)
                popupMenu.show()
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.menu_button_delete -> listener?.onSchemaDeleteClick(adapterPosition)
                        R.id.menu_button_update -> listener?.onSchemaEditClick(schemasList[adapterPosition])
                    }
                    true
                }
            }
        }
    }

    interface OnSchemaListButtonClickListener {
        fun onSchemaDeleteClick(position: Int)
        fun onSchemaEditClick(schema: PackageSchema)
    }
}