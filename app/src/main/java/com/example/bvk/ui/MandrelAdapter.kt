package com.example.bvk.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.bvk.R
import com.example.bvk.databinding.MandrelRecyclerContainerBinding
import com.example.bvk.model.Mandrel

class MandrelAdapter(
    private var mandrelsList: List<Mandrel>,
    context: Context,
    private val listener: OnPetListButtonClickListener? = null,
    var isSampleCreate: Boolean = false
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
        binding.mandrelName.text = mandrelsList[position].mandrelName
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
                .toString() + SPACE + mandrelsList[position].adhesiveSleeveWeight
        binding.membraneWightTextView.text = res.getText(R.string.membrane_weight_prefix)
            .toString() + SPACE + mandrelsList[position].membraneWight
        binding.tapperTextView.text =
            res.getText(R.string.tapper_prefix).toString() + SPACE + mandrelsList[position].tapper
        binding.TotalMembraneLengthTextView.text =
            res.getString(R.string.total_membrane_length) + SPACE + mandrelsList[position].totalMembraneLength
    }

    override fun getItemCount(): Int = mandrelsList.count()

    inner class MandrelListViewHolder(binding: MandrelRecyclerContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            if (isSampleCreate) {
                binding.mandrelSampleDataLayout.visibility = ConstraintLayout.VISIBLE
                binding.deleteButton.visibility = Button.INVISIBLE
                binding.editButton.visibility = Button.INVISIBLE
            } else {
                binding.mandrelSampleDataLayout.visibility = ConstraintLayout.GONE
                binding.deleteButton.visibility = Button.VISIBLE
                binding.editButton.visibility = Button.VISIBLE
            }

            binding.deleteButton.setOnClickListener {
                listener?.onDeleteClick(adapterPosition)
            }
            binding.editButton.setOnClickListener {
                listener?.onEditClick(mandrelsList[adapterPosition])
            }
        }
    }

    companion object {
        const val SPACE = " "
    }

    interface OnPetListButtonClickListener {
        fun onDeleteClick(position: Int)
        fun onEditClick(mandrel: Mandrel)
    }
}
