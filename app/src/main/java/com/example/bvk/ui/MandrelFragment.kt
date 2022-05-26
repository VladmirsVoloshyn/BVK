package com.example.bvk.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bvk.BVKApplication
import com.example.bvk.R
import com.example.bvk.databinding.FragmentMandrelBinding
import com.example.bvk.model.Mandrel
import com.example.bvk.model.sample.SampleCapParameters
import com.example.bvk.ui.Dialogs.AddMandrelDialogFragment
import com.example.bvk.ui.Dialogs.DeleteConfirmationDialogFragment
import com.example.bvk.ui.Dialogs.DeveloperModeDialogFragment
import com.example.bvk.ui.Dialogs.SampleCreateDialogFragment

class MandrelFragment : Fragment(), AddMandrelDialogFragment.OnAddOrEditMandrelListener,
    MandrelAdapter.OnPetListButtonClickListener,
    SampleCreateDialogFragment.OnSampleCreatedListener,
    DeveloperModeDialogFragment.OnPasswordEnterListener,
    DeleteConfirmationDialogFragment.OnDeleteConfirmationListener {

    private var _binding: FragmentMandrelBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MandrelViewModel by viewModels {
        MandrelViewModelFactory((activity?.application as BVKApplication).repository)
    }
    private var savingStateListener: FragmentCommutator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMandrelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        savingStateListener = activity as FragmentCommutator
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inflateList()
        // viewModel.insert(Mandrel(mandrelName = "29x1" , vertexDiameter = 29.65, baseDiameter = 32.9, height = 75))
        // viewModel.insert(Mandrel(mandrelName = "29x2" ,vertexDiameter = 29.55, baseDiameter = 33.16, height = 75))
        // viewModel.insert(Mandrel(mandrelName = "29x3" ,vertexDiameter = 29.88, baseDiameter = 33.81, height = 75))
        // viewModel.insert(Mandrel(mandrelName = "29x4" ,vertexDiameter = 29.55, baseDiameter = 32.79, height = 75))
        // viewModel.insert(Mandrel(mandrelName = "33x1" ,vertexDiameter = 33.20, baseDiameter = 36.38, height = 75))
        // viewModel.insert(Mandrel(mandrelName = "43x1" ,vertexDiameter = 42.65, baseDiameter = 44.56, height = 75))
        // viewModel.insert(Mandrel(mandrelName = "56x1" ,vertexDiameter = 57.3, baseDiameter = 60.62, height = 75))
        if (viewModel.isSampleCreated) {
            binding.clearSampleButton.visibility = Button.VISIBLE
            binding.textViewLabel.text =
                activity?.resources?.getString(R.string.sample_param_label) + viewModel.sampleCapParameters.toString()
        } else {
            binding.textViewLabel.text =
                activity?.resources?.getString(R.string.mandrel_list_operator_label)
        }
        if (!viewModel.isDeveloperMode) {
            setOperatorMode()
        } else {
            setAdminMode()
        }

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
            binding.textViewLabel.text =
                activity?.resources?.getString(R.string.mandrel_list_operator_label)
            binding.clearSampleButton.visibility = Button.INVISIBLE
            binding.nothingToShowTextView.visibility = TextView.INVISIBLE
            binding.mandrelsList.visibility = RecyclerView.VISIBLE
            inflateList()
        }
        binding.updateModeButton.setOnClickListener {
            if (!viewModel.isDeveloperMode) {
                val developerModeDialogFragment = DeveloperModeDialogFragment("123", this)
                developerModeDialogFragment.show(activity?.supportFragmentManager!!, "PASSWORD")
            } else {
                setOperatorMode()
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setAdminMode() {
        viewModel.isDeveloperMode = true
        binding.addFab.visibility = Button.VISIBLE
        binding.textViewLabel.text =
            activity?.resources?.getString(R.string.mandrel_list_admin_label)
        binding.updateModeButton.setImageDrawable(activity?.resources!!.getDrawable(R.drawable.ic_baseline_close_24))
        inflateList()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setOperatorMode() {
        viewModel.isDeveloperMode = false
        binding.addFab.visibility = Button.INVISIBLE
        binding.textViewLabel.text =
            activity?.resources?.getString(R.string.mandrel_list_operator_label)
        binding.updateModeButton.setImageDrawable(activity?.resources!!.getDrawable(R.drawable.ic_baseline_arrow_circle_up_24))
        inflateList()
    }

    override fun onMandrelAdd(mandrel: Mandrel) {
        viewModel.insert(mandrel)
    }

    override fun onMandrelEdit(mandrel: Mandrel) {
        viewModel.update(mandrel)
    }

    override fun onDeleteClick(position: Int) {
        val deleteConfirmationFragment = DeleteConfirmationDialogFragment(
            viewModel.getData().value?.get(position) ?: Mandrel(),
            this,
            position)
        deleteConfirmationFragment.show(activity?.supportFragmentManager!!, DELETE_DIALOG_TAG)
    }

    override fun onEditClick(mandrel: Mandrel) {
        val addFragment = AddMandrelDialogFragment(CALL_KEY_EDIT, mandrel, this)
        addFragment.show(activity?.supportFragmentManager!!, ADD_FRAGMENT_TAG)
    }

    private fun inflateList() {
        viewModel.getData().observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.nothingToShowTextView.visibility = TextView.VISIBLE
                binding.mandrelsList.visibility = RecyclerView.INVISIBLE
            }
            val mandrelAdapter = MandrelAdapter(
                it.toCollection(ArrayList()),
                requireActivity().applicationContext,
                this,
                viewModel.isSampleCreated,
                viewModel.isDeveloperMode
            )
            binding.mandrelsList.adapter = mandrelAdapter
            binding.mandrelsList.layoutManager =
                LinearLayoutManager(requireActivity().applicationContext)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onSampleCreate(sampleCapParam: SampleCapParameters) {
        viewModel.createSample(sampleCapParam)
        binding.textViewLabel.text =
            activity?.resources?.getString(R.string.sample_param_label) + sampleCapParam.toString()
        binding.clearSampleButton.visibility = Button.VISIBLE
        viewModel.isSampleCreated = true
        inflateList()
    }

    override fun onPasswordEnter() {
        setAdminMode()
    }

    override fun onDeleteConfirm(position: Int) {
        viewModel.delete(position)
    }

    companion object {
        val TAG = MandrelFragment::class.java
        const val CALL_KEY_NEW = "new"
        const val CALL_KEY_EDIT = "edit"
        const val ADD_FRAGMENT_TAG = "add"
        const val SAMPLE_CREATE_FRAGMENT_TAG = "sample"
        const val DELETE_DIALOG_TAG = "delete"
    }

    override fun onDestroyView() {
        _binding = null
        savingStateListener = null
        super.onDestroyView()
    }




}