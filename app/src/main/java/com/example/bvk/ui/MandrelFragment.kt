package com.example.bvk.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bvk.BVKApplication
import com.example.bvk.R
import com.example.bvk.databinding.FragmentMandrelBinding
import com.example.bvk.model.Mandrel
import com.example.bvk.model.sample.SampleCapParameters
import com.example.bvk.ui.Dialogs.AddMandrelDialogFragment
import com.example.bvk.ui.Dialogs.ConfirmationDialogFragment
import com.example.bvk.ui.Dialogs.DeveloperModeDialogFragment
import com.example.bvk.ui.Dialogs.SampleCreateDialogFragment

class MandrelFragment : Fragment(), AddMandrelDialogFragment.OnAddOrEditMandrelListener,
    MandrelAdapter.OnPetListButtonClickListener,
    SampleCreateDialogFragment.OnSampleCreatedListener,
    DeveloperModeDialogFragment.OnPasswordEnterListener,
    ConfirmationDialogFragment.OnConfirmationListener,
    MainActivity.IfUpdateButtonClickedListener {

    private var _binding: FragmentMandrelBinding? = null
    private val binding get() = _binding!!
    private var actionBar: androidx.appcompat.app.ActionBar? = null

    private val viewModel: MandrelViewModel by viewModels {
        MandrelViewModelFactory(
            (requireActivity().application as BVKApplication).mandrelsRepository,
            (requireActivity().application as BVKApplication).schemasRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMandrelBinding.inflate(inflater, container, false)
        actionBar = (activity as AppCompatActivity).supportActionBar
        return binding.root
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inflateList()
        requireActivity().onBackPressedDispatcher.addCallback(backPressedCallback)

        if (viewModel.isSampleCreated) {
            binding.clearSampleButton.visibility = Button.VISIBLE
            binding.textViewLabel.text =
                activity?.resources?.getString(R.string.sample_param_label) + viewModel.sampleCapParameters.toString()
        } else {
            binding.textViewLabel.text =
                activity?.resources?.getString(R.string.mandrel_list_label)
            actionBar?.subtitle =
                activity?.resources?.getString(R.string.action_bar_subtitle_operator_mode)
        }
        if (!viewModel.isDeveloperMode) {
            setOperatorMode()
        } else {
            setAdminMode()
        }

        binding.addFab.setOnClickListener {
            val addFragment = AddMandrelDialogFragment(CALL_KEY_NEW, Mandrel(), this)
            addFragment.show(requireActivity().supportFragmentManager, ADD_FRAGMENT_TAG)
        }
        binding.createSampleFab.setOnClickListener {
            if (viewModel.isSampleCreated) {
                clearSample()
            }
            val sampleCreateDialogFragment = SampleCreateDialogFragment(this)
            sampleCreateDialogFragment.show(
                requireActivity().supportFragmentManager,
                SAMPLE_CREATE_FRAGMENT_TAG
            )
        }
        binding.clearSampleButton.setOnClickListener {
            clearSample()
        }

        binding.restoreDefaultButton.setOnClickListener {
            val confirmationDialogFragment = ConfirmationDialogFragment(
                RESTORE_DEFAULT_CONFIRMATION_CALL_KEY, listener = this
            )
            confirmationDialogFragment.show(
                requireActivity().supportFragmentManager,
                RESTORE_DEFAULT_CONFIRMATION_CALL_KEY
            )
        }

    }

    private fun setInitializeData() {
        viewModel.deleteAllMandrels()
        viewModel.insertMandrel(
            Mandrel(
                mandrelName = "29x1",
                vertexDiameter = 29.65,
                baseDiameter = 32.9,
                height = 75
            )
        )
        viewModel.insertMandrel(
            Mandrel(
                mandrelName = "29x2",
                vertexDiameter = 29.55,
                baseDiameter = 33.16,
                height = 75
            )
        )
        viewModel.insertMandrel(
            Mandrel(
                mandrelName = "29x3",
                vertexDiameter = 29.88,
                baseDiameter = 33.81,
                height = 75
            )
        )
        viewModel.insertMandrel(
            Mandrel(
                mandrelName = "29x4",
                vertexDiameter = 29.55,
                baseDiameter = 32.79,
                height = 75
            )
        )
        viewModel.insertMandrel(
            Mandrel(
                mandrelName = "33x1",
                vertexDiameter = 33.20,
                baseDiameter = 36.38,
                height = 75
            )
        )
        viewModel.insertMandrel(
            Mandrel(
                mandrelName = "43x1",
                vertexDiameter = 42.65,
                baseDiameter = 44.56,
                height = 75
            )
        )
        viewModel.insertMandrel(
            Mandrel(
                mandrelName = "56x1",
                vertexDiameter = 57.3,
                baseDiameter = 60.62,
                height = 75,
            )
        )
        viewModel.insertMandrel(
            Mandrel(
                mandrelName = "61x1",
                vertexDiameter = 60.6,
                baseDiameter = 64.4,
                height = 75,
            )
        )
        requireActivity().finish()
    }

    fun clearSample() {
        viewModel.isSampleCreated = false
        viewModel.mandrelsSampleList = MutableLiveData()
        if (viewModel.isDeveloperMode) {
            binding.textViewLabel.text =
                activity?.resources?.getString(R.string.mandrel_list_label)
            actionBar?.subtitle =
                activity?.resources?.getString(R.string.action_bar_subtitle_administrator_mode)
        } else {
            binding.textViewLabel.text =
                activity?.resources?.getString(R.string.mandrel_list_label)
            actionBar?.subtitle =
                activity?.resources?.getString(R.string.action_bar_subtitle_operator_mode)
        }
        binding.clearSampleButton.visibility = Button.INVISIBLE
        binding.nothingToShowTextView.visibility = TextView.INVISIBLE
        binding.mandrelsList.visibility = RecyclerView.VISIBLE
        inflateList()
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (viewModel.isSampleCreated) {
                clearSample()
            } else {
                activity?.finish()
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setAdminMode() {
        viewModel.isDeveloperMode = true
        binding.addFab.visibility = Button.VISIBLE
        binding.restoreDefaultButton.visibility = Button.VISIBLE
        binding.textViewLabel.text =
            activity?.resources?.getString(R.string.mandrel_list_label)
        actionBar?.subtitle =
            activity?.resources?.getString(R.string.action_bar_subtitle_administrator_mode)
        inflateList()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setOperatorMode() {
        viewModel.isDeveloperMode = false
        binding.addFab.visibility = Button.INVISIBLE
        binding.restoreDefaultButton.visibility = Button.INVISIBLE
        binding.textViewLabel.text =
            activity?.resources?.getString(R.string.mandrel_list_label)
        actionBar?.subtitle =
            activity?.resources?.getString(R.string.action_bar_subtitle_operator_mode)
        inflateList()
    }

    override fun onMandrelAdd(mandrel: Mandrel) {
        viewModel.insertMandrel(mandrel)
    }

    override fun onMandrelEdit(mandrel: Mandrel) {
        viewModel.updateMandrel(mandrel)
    }

    override fun onDeleteClick(position: Int) {
        val deleteConfirmationFragment = ConfirmationDialogFragment(
            DELETE_CONFIRMATION_CALL_KEY,
            viewModel.getMandrelsData().value?.get(position) ?: Mandrel(),
            this,
            position
        )
        deleteConfirmationFragment.show(activity?.supportFragmentManager!!, DELETE_DIALOG_TAG)
    }

    override fun onEditClick(mandrel: Mandrel) {
        val addFragment = AddMandrelDialogFragment(CALL_KEY_EDIT, mandrel, this)
        addFragment.show(activity?.supportFragmentManager!!, ADD_FRAGMENT_TAG)
    }

    override fun onItemClick(position: Int) {
        if (viewModel.isSampleCreated) {
            clearSample()
        }
        val sampleCreateDialogFragment =
            SampleCreateDialogFragment(this, viewModel.getItem(position)?.getSizeIdentifier())
        sampleCreateDialogFragment.show(
            requireActivity().supportFragmentManager,
            SAMPLE_CREATE_FRAGMENT_TAG
        )
    }

    private fun inflateList() {
        viewModel.getMandrelsData().observe(viewLifecycleOwner) {
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

    override fun onUpdateModeClicked() {
        if (!viewModel.isDeveloperMode) {
            val developerModeDialogFragment = DeveloperModeDialogFragment("123", this)
            developerModeDialogFragment.show(activity?.supportFragmentManager!!, "PASSWORD")
        } else {
            setOperatorMode()
        }
    }

    override fun onDeleteConfirm(position: Int) {
        viewModel.deleteMandrel(position)
    }

    override fun onRestoreDefaultConfirm() {
        setInitializeData()
    }

    companion object {
        val TAG = (MandrelFragment::class.java).toString()
        const val CALL_KEY_NEW = "new"
        const val CALL_KEY_EDIT = "edit"
        const val ADD_FRAGMENT_TAG = "add"
        const val SAMPLE_CREATE_FRAGMENT_TAG = "sample"
        const val DELETE_DIALOG_TAG = "delete"
        const val DELETE_CONFIRMATION_CALL_KEY = "DELETE"
        const val RESTORE_DEFAULT_CONFIRMATION_CALL_KEY = "RESTORE"
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


}