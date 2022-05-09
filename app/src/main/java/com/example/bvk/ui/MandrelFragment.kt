package com.example.bvk.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bvk.BVKApplication
import com.example.bvk.databinding.FragmentMandrelBinding
import com.example.bvk.model.Mandrel
import com.example.bvk.model.sample.SampleCapParameters

class MandrelFragment : Fragment(), AddMandrelDialogFragment.OnAddOrEditMandrelListener,
    MandrelAdapter.OnPetListButtonClickListener, SampleCreateDialogFragment.OnSampleCreatedListener {

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

        viewModel.mandrelsList.observe(viewLifecycleOwner) {
            val mandrelAdapter = MandrelAdapter(
                it.toCollection(ArrayList()),
                requireActivity().applicationContext,
                this
            )
            binding.mandrelsList.adapter = mandrelAdapter
            binding.mandrelsList.layoutManager =
                LinearLayoutManager(requireActivity().applicationContext)
        }

        binding.addFab.setOnClickListener {
            val addFragment = AddMandrelDialogFragment(CALL_KEY_NEW, Mandrel(), this)
            addFragment.show(activity?.supportFragmentManager!!, ADD_FRAGMENT_TAG)
        }

        binding.searchFab.setOnClickListener {
            val sampleCreateDialogFragment = SampleCreateDialogFragment(this)
            sampleCreateDialogFragment.show(activity?.supportFragmentManager!!, SAMPLE_CREATE_FRAGMENT_TAG)
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

    override fun onSampleCreate(sampleCapParam: SampleCapParameters) {
        binding.textViewSample.text = sampleCapParam.capVertexDiameter.toString() + " " + sampleCapParam.capHeight
    }

    companion object {
        const val CALL_KEY_NEW = "new"
        const val CALL_KEY_EDIT = "edit"
        const val ADD_FRAGMENT_TAG = "add"
        const val SAMPLE_CREATE_FRAGMENT_TAG = "sample"
    }


}