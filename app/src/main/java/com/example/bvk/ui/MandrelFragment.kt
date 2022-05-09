package com.example.bvk.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bvk.BVKApplication
import com.example.bvk.databinding.FragmentMandrelBinding
import com.example.bvk.model.Mandrel
import com.example.bvk.model.sample.SampleCapParameters

class MandrelFragment : Fragment(), AddMandrelDialogFragment.OnAddOrEditMandrelListener,
    MandrelAdapter.OnPetListButtonClickListener,
    SampleCreateDialogFragment.OnSampleCreatedListener {

    private var _binding: FragmentMandrelBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MandrelViewModel by viewModels {
        MandrelViewModelFactory((activity?.application as BVKApplication).repository)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMandrelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       //viewModel.insert(Mandrel(vertexDiameter = 29.65, baseDiameter = 32.9, height = 75))
       //viewModel.insert(Mandrel(vertexDiameter = 29.55, baseDiameter = 33.16, height = 75))
       //viewModel.insert(Mandrel(vertexDiameter = 29.88, baseDiameter = 33.81, height = 75))
       //viewModel.insert(Mandrel(vertexDiameter = 29.55, baseDiameter = 32.79, height = 75))
       //viewModel.insert(Mandrel(vertexDiameter = 31.55, baseDiameter = 32.79, height = 75))
       //viewModel.insert(Mandrel(vertexDiameter = 65.55, baseDiameter = 32.79, height = 75))
       //viewModel.insert(Mandrel(vertexDiameter = 22.0, baseDiameter = 32.79, height = 75))

        inflateList()

        binding.addFab.setOnClickListener {
            val addFragment = AddMandrelDialogFragment(CALL_KEY_NEW, Mandrel(), this)
            addFragment.show(activity?.supportFragmentManager!!, ADD_FRAGMENT_TAG)
        }

        binding.searchFab.setOnClickListener {
            val sampleCreateDialogFragment = SampleCreateDialogFragment(this)
            sampleCreateDialogFragment.show(
                activity?.supportFragmentManager!!,
                SAMPLE_CREATE_FRAGMENT_TAG
            )
        }

        binding.clearSampleButton.setOnClickListener {
            viewModel.isSampleCreated = false
            inflateList()
            binding.textViewLabel.text = "Mandrels List"
            binding.clearSampleButton.visibility = Button.INVISIBLE
        }
    }

    override fun onMandrelAdd(mandrel: Mandrel) {
        viewModel.insert(mandrel)
    }

    override fun onMandrelEdit(mandrel: Mandrel) {
        viewModel.update(mandrel)
    }

    override fun onDeleteClick(position: Int) {
        viewModel.delete(position)
    }

    override fun onEditClick(mandrel: Mandrel) {
        val addFragment = AddMandrelDialogFragment(CALL_KEY_EDIT, mandrel, this)
        addFragment.show(activity?.supportFragmentManager!!, ADD_FRAGMENT_TAG)
    }

    private fun inflateList() {
        viewModel.getData().observe(viewLifecycleOwner) {
            val mandrelAdapter = MandrelAdapter(
                it.toCollection(ArrayList()),
                requireActivity().applicationContext,
                this,
                viewModel.isSampleCreated
            )
            binding.mandrelsList.adapter = mandrelAdapter
            binding.mandrelsList.layoutManager =
                LinearLayoutManager(requireActivity().applicationContext)

        }
    }

    @SuppressLint("SetTextI18n")
    override fun onSampleCreate(sampleCapParam: SampleCapParameters) {
        viewModel.createSample(sampleCapParam.capVertexDiameter, sampleCapParam.capHeight)
        binding.textViewLabel.text = "Sample for parameters $sampleCapParam"
        inflateList()
        binding.clearSampleButton.visibility = Button.VISIBLE
    }

    companion object {
        const val CALL_KEY_NEW = "new"
        const val CALL_KEY_EDIT = "edit"
        const val ADD_FRAGMENT_TAG = "add"
        const val SAMPLE_CREATE_FRAGMENT_TAG = "sample"
    }


}