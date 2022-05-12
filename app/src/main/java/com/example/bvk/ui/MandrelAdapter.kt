package com.example.bvk.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
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
    private var _binding : MandrelRecyclerContainerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MandrelListViewHolder {
        _binding = MandrelRecyclerContainerBinding.inflate(layoutInflater,parent, false)
        return MandrelListViewHolder(
            layoutInflater.inflate(
                R.layout.mandrel_recycler_container,
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MandrelListViewHolder, position: Int) {

        holder.mandrelName.text =
            res.getString(R.string.name_prefix) + SPACE + mandrelsList[position].vertexDiameter.toString() + res.getText(
                R.string.name_separator
            ) + mandrelsList[position].baseDiameter.toString() + res.getText(R.string.name_separator) + mandrelsList[position].height.toString()
        holder.mandrelVertexDiameter.text =
            res.getText(R.string.vertex_prefix).toString() + SPACE +  mandrelsList[position].vertexDiameter
        holder.mandrelBaseDiameter.text =
            res.getText(R.string.base_prefix).toString() + SPACE + mandrelsList[position].baseDiameter
        holder.mandrelHeight.text =
            res.getText(R.string.height_prefix).toString() + SPACE + mandrelsList[position].height
        holder.adhesiveSleeveWeight.text =
            res.getText(R.string.adhesive_sleeve_weight_prefix)
                .toString() + SPACE + mandrelsList[position].adhesiveSleeveWeight
        holder.membraneWeight.text = res.getText(R.string.membrane_weight_prefix)
            .toString() + SPACE + mandrelsList[position].membraneWight
        holder.tapperTextView.text =
            res.getText(R.string.tapper_prefix).toString() + SPACE + mandrelsList[position].tapper
    }

    override fun getItemCount(): Int = mandrelsList.count()
    inner class MandrelListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var mandrelName: TextView  = view.findViewById(R.id.mandrel_name)
        var mandrelVertexDiameter: TextView = view.findViewById(R.id.mandrel_vertex_diameter)
        var mandrelBaseDiameter: TextView= view.findViewById(R.id.mandrel_base_diameter)
        var mandrelHeight: TextView= view.findViewById(R.id.mandrel_height)
        var deleteButton: ImageButton= view.findViewById(R.id.delete_button)
        var editButton: ImageButton= view.findViewById(R.id.edit_button)
        private var sampleDataContainer: ConstraintLayout= view.findViewById(R.id.mandrelSampleDataLayout)
        var tapperTextView: TextView= view.findViewById(R.id.tapperTextView)
        var membraneWeight: TextView= view.findViewById(R.id.membraneWightTextView)
        var adhesiveSleeveWeight: TextView = view.findViewById(R.id.adhesiveSleeveWeightTextView)

        init {
            if (isSampleCreate) {
                sampleDataContainer.visibility = ConstraintLayout.VISIBLE
                deleteButton.visibility = Button.INVISIBLE
                editButton.visibility = Button.INVISIBLE
            } else {
                sampleDataContainer.visibility = ConstraintLayout.GONE
                deleteButton.visibility = Button.VISIBLE
                editButton.visibility = Button.VISIBLE
            }

            deleteButton.setOnClickListener {
                listener?.onDeleteClick(adapterPosition)
            }
            editButton.setOnClickListener {
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
