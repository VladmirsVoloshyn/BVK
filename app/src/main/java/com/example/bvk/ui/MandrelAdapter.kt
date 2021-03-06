package com.example.bvk.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.bvk.R
import com.example.bvk.databinding.MandrelRecyclerContainerBinding
import com.example.bvk.model.Mandrel
import java.text.DecimalFormat

class MandrelAdapter(
    private var mandrelsList: ArrayList<Mandrel>,
    val context: Context,
    private val listener: OnMandrelListButtonClickListener? = null,
    var isSampleCreate: Boolean = false,
    var isDeveloperMode: Boolean = false
) : RecyclerView.Adapter<MandrelAdapter.MandrelListViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val res: Resources = context.resources
    private var _binding: MandrelRecyclerContainerBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MandrelListViewHolder {
        _binding = MandrelRecyclerContainerBinding.inflate(layoutInflater, parent, false)
        setUIMode()
        return MandrelListViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: MandrelListViewHolder, position: Int) {
        binding.mandrelName.text =
            res.getString(R.string.name_prefix) + SPACE + mandrelsList[position].mandrelName
        binding.mandrelVertexDiameter.text =
            res.getText(R.string.vertex_prefix)
                .toString() + SPACE + mandrelsList[position].vertexDiameter + SPACE + res.getText(R.string.millimeter_postfix)
        binding.mandrelBaseDiameter.text =
            res.getText(R.string.base_prefix)
                .toString() + SPACE + mandrelsList[position].baseDiameter + SPACE + res.getText(R.string.millimeter_postfix)
        binding.mandrelHeight.text =
            res.getText(R.string.height_prefix)
                .toString() + SPACE + mandrelsList[position].height + SPACE + res.getText(R.string.millimeter_postfix)

        binding.adhesiveSleeveWeightTextView.text =
            res.getText(R.string.adhesive_sleeve_weight_prefix)
                .toString() + SPACE + (DecimalFormat(DOUBLE_PATTERN).format(mandrelsList[position].adhesiveSleeveWeight)) + SPACE + res.getText(
                R.string.millimeter_postfix
            )

        binding.membraneWightTextView.text = res.getText(R.string.membrane_weight_prefix)
            .toString() + SPACE + (DecimalFormat(DOUBLE_PATTERN).format(mandrelsList[position].membraneWight)) + SPACE + res.getText(
            R.string.millimeter_postfix
        )

        binding.totalMembraneLengthTextView.text =
            res.getString(R.string.total_membrane_length) + SPACE + (DecimalFormat(DOUBLE_PATTERN).format(
                (mandrelsList[position].totalMembraneLength)
            )) + SPACE + res.getText(R.string.meter_postfix)

        binding.mandrelInfelicity.text = res.getText(R.string.infelicity_prefix)
            .toString() + SPACE + mandrelsList[position].infelicityCoefficient
        res.getString(R.string.recommended_adhesive_sleeve_weight_hint)

        binding.recommendedAdhesiveSleeveWeightTextView.text =
            res.getString(R.string.recommended_adhesive_sleeve_weight_hint) + SPACE + mandrelsList[position].recommendedAdhesiveSleeveWeight

        if (mandrelsList[position].totalMembraneLength == 0.00) {
            binding.totalMembraneLengthTextView.visibility = TextView.GONE
        }
        if (mandrelsList[position].recommendedAdhesiveSleeveWeight == 0.00) {
            binding.recommendedAdhesiveSleeveWeightTextView.visibility = TextView.GONE
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setUIMode() {
        when (res.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.mandrelSimpleDataLayout.background =
                    res.getDrawable(R.drawable.card_bg_night)
                binding.mandrelSampleDataLayout.background =
                    res.getDrawable(R.drawable.card_bg_night)
                binding.menuButton.background = res.getDrawable(R.drawable.card_bg_night)
                binding.mandrelName.setTextColor(res.getColor(R.color.text_mode_night))
                binding.mandrelVertexDiameter.setTextColor(res.getColor(R.color.text_mode_night))
                binding.mandrelBaseDiameter.setTextColor(res.getColor(R.color.text_mode_night))
                binding.mandrelInfelicity.setTextColor(res.getColor(R.color.text_mode_night))
                binding.membraneWightTextView.setTextColor(res.getColor(R.color.text_mode_night))
                binding.adhesiveSleeveWeightTextView.setTextColor(res.getColor(R.color.text_mode_night))
                binding.totalMembraneLengthTextView.setTextColor(res.getColor(R.color.text_mode_night))
                binding.mandrelHeight.setTextColor(res.getColor(R.color.text_mode_night))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()

    }

    override fun getItemCount(): Int = mandrelsList.count()

    inner class MandrelListViewHolder(binding: MandrelRecyclerContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var popupMenu: PopupMenu

        init {
            itemView.setOnClickListener {
                listener?.onMandrelItemClick(adapterPosition)
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
                binding.menuButton.visibility = Button.GONE
            }

            binding.menuButton.setOnClickListener {
                popupMenu = PopupMenu(context, it)
                popupMenu.inflate(R.menu.recycler_container_menu)
                popupMenu.show()
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.menu_button_delete -> listener?.onMandrelDeleteClick(adapterPosition)
                        R.id.menu_button_update -> listener?.onMandrelEditClick(mandrelsList[adapterPosition])
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

    interface OnMandrelListButtonClickListener {
        fun onMandrelDeleteClick(position: Int)
        fun onMandrelEditClick(mandrel: Mandrel)
        fun onMandrelItemClick(position: Int)
    }
}
