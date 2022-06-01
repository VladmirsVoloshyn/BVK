package com.example.bvk.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.bvk.R
import com.example.bvk.databinding.MandrelRecyclerContainerBinding
import com.example.bvk.model.Mandrel
import java.text.DecimalFormat

class MandrelAdapter(
    private var mandrelsList: List<Mandrel>,
    val context: Context,
    private val listener: OnPetListButtonClickListener? = null,
    var isSampleCreate: Boolean = false,
    var isDeveloperMode: Boolean = false
) : RecyclerView.Adapter<MandrelAdapter.MandrelListViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val res: Resources = context.resources
    private var _binding: MandrelRecyclerContainerBinding? = null
    private val binding get() = _binding!!
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MandrelListViewHolder {
        _binding = MandrelRecyclerContainerBinding.inflate(layoutInflater, parent, false)
        return MandrelListViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MandrelListViewHolder, position: Int) {
        binding.mandrelName.text =
            res.getString(R.string.name_prefix) + SPACE + mandrelsList[position].mandrelName
        binding.mandrelVertexDiameter.text =
            res.getText(R.string.vertex_prefix)
                .toString() + SPACE + mandrelsList[position].vertexDiameter
        binding.mandrelBaseDiameter.text =
            res.getText(R.string.base_prefix)
                .toString() + SPACE + mandrelsList[position].baseDiameter
        binding.mandrelHeight.text =
            res.getText(R.string.height_prefix).toString() + SPACE + mandrelsList[position].height
        binding.adhesiveSleeveWeightTextView.text =
            res.getText(R.string.adhesive_sleeve_weight_prefix)
                .toString() + SPACE + (DecimalFormat(DOUBLE_PATTERN).format(mandrelsList[position].adhesiveSleeveWeight))
        binding.membraneWightTextView.text = res.getText(R.string.membrane_weight_prefix)
            .toString() + SPACE + (DecimalFormat(DOUBLE_PATTERN).format(mandrelsList[position].membraneWight))
        binding.totalMembraneLengthTextView.text =
            res.getString(R.string.total_membrane_length) + SPACE + (DecimalFormat(DOUBLE_PATTERN).format((mandrelsList[position].totalMembraneLength)))
        binding.mandrelInfelicity.text = res.getText(R.string.infelicity_prefix).toString() + SPACE + mandrelsList[position].infelicity
        res.getString(R.string.recommended_adhesive_sleeve_weight_hint)
        binding.recommendedAdhesiveSleeveWeightTextView.text = res.getString(R.string.recommended_adhesive_sleeve_weight_hint) + SPACE + mandrelsList[position].recommendedAdhesiveSleeveWeight

        if (mandrelsList[position].totalMembraneLength == 0.00)  {
            binding.totalMembraneLengthTextView.visibility = TextView.GONE
        }
        if (mandrelsList[position].recommendedAdhesiveSleeveWeight == 0.00)  {
            binding.recommendedAdhesiveSleeveWeightTextView.visibility = TextView.GONE
        }

    }

    override fun getItemCount(): Int = mandrelsList.count()

    inner class MandrelListViewHolder(binding: MandrelRecyclerContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var popupMenu: PopupMenu

        init {
            itemView.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }

            if (isSampleCreate) {
                binding.mandrelSampleDataLayout.visibility = ConstraintLayout.VISIBLE
                binding.menuButton.visibility = Button.INVISIBLE
            } else {
                binding.mandrelSampleDataLayout.visibility = ConstraintLayout.GONE
            }

            if (!isDeveloperMode) {
                binding.mandrelVertexDiameter.visibility = TextView.GONE
                binding.mandrelBaseDiameter.visibility = TextView.GONE
                binding.mandrelHeight.visibility = TextView.GONE
                binding.mandrelInfelicity.visibility = TextView.GONE
                binding.menuButton.visibility = Button.INVISIBLE
            }

            binding.menuButton.setOnClickListener {
                popupMenu = PopupMenu(context, it)
                popupMenu.inflate(R.menu.recycler_container_menu)
                popupMenu.show()
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.menu_button_delete -> listener?.onDeleteClick(adapterPosition)
                        R.id.menu_button_update -> listener?.onEditClick(mandrelsList[adapterPosition])
                    }
                    true
                }
            }
        }
    }

    companion object {
        const val SPACE = " "
        const val DOUBLE_PATTERN = "#0.00"
    }

    interface OnPetListButtonClickListener {
        fun onDeleteClick(position: Int)
        fun onEditClick(mandrel: Mandrel)
        fun onItemClick(position: Int)
    }
}
