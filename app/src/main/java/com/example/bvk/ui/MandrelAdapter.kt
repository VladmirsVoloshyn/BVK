package com.example.bvk.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bvk.R
import com.example.bvk.model.Mandrel

class MandrelAdapter(
    private var mandrelsList: List<Mandrel>,
    context: Context,
    private val listener: OnPetListButtonClickListener? = null
) : RecyclerView.Adapter<MandrelAdapter.MandrelListViewHolder>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MandrelListViewHolder {
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
        holder.mandrelName?.text = "name : " +
            (mandrelsList[position].vertexDiameter.toString() + "x" + mandrelsList[position].baseDiameter.toString() + "x" + mandrelsList[position].height)
        holder.mandrelVertexDiameter?.text = "vertex diameter = " + mandrelsList[position].vertexDiameter
        holder.mandrelBaseDiameter?.text = "base diameter = " + mandrelsList[position].baseDiameter
        holder.mandrelHeight?.text = "height = " + mandrelsList[position].height
    }
    override fun getItemCount(): Int = mandrelsList.count()
    inner class MandrelListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var mandrelName: TextView? = null
        var mandrelVertexDiameter: TextView? = null
        var mandrelBaseDiameter: TextView? = null
        var mandrelHeight: TextView? = null
        var deleteButton: ImageButton? = null
        var editButton: ImageButton? = null
        init {
            mandrelName = view.findViewById(R.id.mandrel_name)
            mandrelVertexDiameter = view.findViewById(R.id.cap_vertex_diameter)
            mandrelBaseDiameter = view.findViewById(R.id.mandrel_base_diameter)
            mandrelHeight = view.findViewById(R.id.mandrel_height)
            deleteButton = view.findViewById(R.id.delete_button)
            editButton = view.findViewById(R.id.edit_button)

            deleteButton?.setOnClickListener {
                listener?.onDeleteClick(adapterPosition)
            }
            editButton?.setOnClickListener {
                listener?.onEditClick(mandrelsList[adapterPosition])
            }
        }
    }
    interface OnPetListButtonClickListener {
        fun onDeleteClick(position: Int)
        fun onEditClick(mandrel: Mandrel)
    }
}