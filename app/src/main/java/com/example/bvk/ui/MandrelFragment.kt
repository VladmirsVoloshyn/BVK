package com.example.bvk.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bvk.*
import com.example.bvk.databinding.FragmentMandrelBinding
import com.example.bvk.model.Mandrel
import com.example.bvk.model.databaseimportexport.ExportListManager
import com.example.bvk.model.packageschema.PackageSchema
import com.example.bvk.model.sample.SampleCapParameters
import com.example.bvk.ui.dialogs.*
import com.example.bvk.ui.packageschema.PackageSchemaAdapter

class MandrelFragment() : Fragment(), AddMandrelDialogFragment.OnAddOrEditMandrelListener,
    MandrelAdapter.OnMandrelListButtonClickListener,
    SampleCreateDialogFragment.OnSampleCreatedListener,
    EnterPasswordDialogFragment.OnPasswordEnterListener,
    ConfirmationDialogFragment.OnConfirmationListener,
    AddPackageSchemaDialogFragment.OnAddOrEditPackageSchemaListener,
    PackageSchemaAdapter.OnSchemaListButtonClickListener,
    MainActivity.OnUpdateModeListener
{

    private var _binding: FragmentMandrelBinding? = null
    private val binding get() = _binding!!
    private var actionBar: androidx.appcompat.app.ActionBar? = null

    private var preferences: SharedPreferences? = null

    private val viewModel: MandrelViewModel by viewModels {
        MandrelViewModelFactory(
            (requireActivity().application as BVKApplication).mandrelsRepository,
            (requireActivity().application as BVKApplication).schemasRepository,
            requireActivity().applicationContext
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMandrelBinding.inflate(inflater, container, false)
        actionBar = (activity as AppCompatActivity).supportActionBar
        preferences = activity?.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        return binding.root
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inflateMandrelsList()
        inflateSchemasList()

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
        if (!viewModel.isAdministratorMode) {
            setOperatorMode()
        } else {
            setAdministratorMode()
        }

        binding.addItemFab.setOnClickListener {
            if (viewModel.isMandrelViewMode) {
                val addMandrelFragment = AddMandrelDialogFragment(
                    CALL_KEY_NEW,
                    Mandrel(),
                    this,
                    viewModel.getMandrelUniqueNames()
                )
                addMandrelFragment.show(requireActivity().supportFragmentManager, ADD_FRAGMENT_TAG)
            } else {
                val addSchemaFragment =
                    AddPackageSchemaDialogFragment(
                        CALL_KEY_NEW,
                        PackageSchema(),
                        this,
                        viewModel.getSchemasUniqueNames()
                    )
                addSchemaFragment.show(requireActivity().supportFragmentManager, ADD_FRAGMENT_TAG)
            }
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
        binding.changeDataListFab.setOnClickListener {
            clearSample()
            if (viewModel.isMandrelViewMode) {
                setPackageSchemaDataView()
            } else {
                setMandrelDataView()
            }
        }

    }

    //ui modes
    private fun setMandrelDataView() {
        binding.changeDataListFab.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_baseline_backpack_24,
                null
            )
        )
        binding.mandrelsList.visibility = RecyclerView.VISIBLE
        binding.schemasList.visibility = RecyclerView.INVISIBLE
        binding.textViewLabel.text =
            activity?.resources?.getString(R.string.mandrel_list_label)
        binding.createSampleFab.visibility = Button.VISIBLE
        viewModel.isMandrelViewMode = true
    }

    private fun setPackageSchemaDataView() {
        inflateSchemasList()
        binding.changeDataListFab.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_baseline_sample_recycler_image,
                null
            )
        )
        binding.mandrelsList.visibility = RecyclerView.INVISIBLE
        binding.schemasList.visibility = RecyclerView.VISIBLE
        binding.textViewLabel.text = getString(R.string.package_schemas_list_label)
        binding.createSampleFab.visibility = Button.INVISIBLE
        viewModel.isMandrelViewMode = false
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setAdministratorMode() {
        viewModel.isAdministratorMode = true
        binding.addItemFab.visibility = Button.VISIBLE
        binding.changeDataListFab.visibility = Button.VISIBLE
        binding.textViewLabel.text =
            activity?.resources?.getString(R.string.mandrel_list_label)
        actionBar?.subtitle =
            activity?.resources?.getString(R.string.action_bar_subtitle_administrator_mode)
        inflateMandrelsList()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setOperatorMode() {
        viewModel.isAdministratorMode = false
        binding.addItemFab.visibility = Button.INVISIBLE
        binding.changeDataListFab.visibility = Button.INVISIBLE
        binding.textViewLabel.text =
            activity?.resources?.getString(R.string.mandrel_list_label)
        actionBar?.subtitle =
            activity?.resources?.getString(R.string.action_bar_subtitle_operator_mode)
        inflateMandrelsList()
    }

    fun clearSample() {
        viewModel.isSampleCreated = false
        viewModel.mandrelsSampleList = MutableLiveData()
        if (viewModel.isAdministratorMode) {
            binding.textViewLabel.text =
                activity?.resources?.getString(R.string.mandrel_list_label)
            actionBar?.subtitle =
                activity?.resources?.getString(R.string.action_bar_subtitle_administrator_mode)
            binding.addItemFab.visibility = Button.VISIBLE
            binding.changeDataListFab.visibility = Button.VISIBLE
        } else {
            binding.textViewLabel.text =
                activity?.resources?.getString(R.string.mandrel_list_label)
            actionBar?.subtitle =
                activity?.resources?.getString(R.string.action_bar_subtitle_operator_mode)
        }
        binding.clearSampleButton.visibility = Button.INVISIBLE
        binding.nothingToShowTextView.visibility = TextView.INVISIBLE
        binding.mandrelsList.visibility = RecyclerView.VISIBLE
        binding.schemasSampleList.visibility = RecyclerView.GONE
        binding.mandrelsSampleList.visibility = RecyclerView.GONE
        binding.mandrelsList.visibility = RecyclerView.VISIBLE
        inflateMandrelsList()
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (viewModel.isSampleCreated) {
                clearSample()
            } else if (!viewModel.isMandrelViewMode) {
                setMandrelDataView()
            } else {
                activity?.finish()
            }
        }
    }

    //mandrel data operation
    override fun onMandrelAdd(mandrel: Mandrel) {
        viewModel.insertMandrel(mandrel)
    }

    override fun onMandrelEdit(mandrel: Mandrel) {
        viewModel.updateMandrel(mandrel)
    }

    override fun onMandrelDeleteClick(position: Int) {
        val deleteConfirmationFragment = ConfirmationDialogFragment(
            DELETE_MANDREL_CONFIRMATION_CALL_KEY,
            viewModel.getMandrelsData().value?.get(position) ?: Mandrel(),
            PackageSchema(),
            this,
            position
        )
        deleteConfirmationFragment.show(activity?.supportFragmentManager!!, DELETE_DIALOG_TAG)
    }

    override fun onDeleteConfirm(position: Int, confirmationKey: String) {
        when (confirmationKey) {
            DELETE_MANDREL_CONFIRMATION_CALL_KEY -> viewModel.deleteMandrel(position)
            DELETE_PACKAGE_SCHEMA_CONFIRMATION_CALL_KEY -> viewModel.deleteSchema(position)
        }

    }

    override fun onMandrelEditClick(mandrel: Mandrel) {
        val addFragment = AddMandrelDialogFragment(
            CALL_KEY_EDIT,
            mandrel,
            this,
            viewModel.getMandrelUniqueNames()
        )
        addFragment.show(activity?.supportFragmentManager!!, ADD_FRAGMENT_TAG)
    }

    override fun onMandrelItemClick(position: Int) {
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

    @SuppressLint("SetTextI18n")
    override fun onSampleCreate(sampleCapParam: SampleCapParameters) {
        viewModel.createSample(sampleCapParam)
        binding.textViewLabel.text =
            activity?.resources?.getString(R.string.sample_param_label) + sampleCapParam.toString()
        binding.clearSampleButton.visibility = Button.VISIBLE
        binding.addItemFab.visibility = Button.INVISIBLE
        binding.changeDataListFab.visibility = Button.INVISIBLE
        viewModel.isSampleCreated = true
        inflateMandrelsSampleList()
        inflateSchemasSampleList(sampleCapParam)
        binding.schemasSampleList.visibility = View.VISIBLE
        binding.mandrelsList.visibility = View.GONE
        binding.mandrelsSampleList.visibility = View.VISIBLE
    }


    // schema data operation
    override fun onSchemaAdd(schema: PackageSchema) {
        viewModel.insertSchema(schema)
    }

    override fun onSchemaEdit(schema: PackageSchema) {
        viewModel.updateSchema(schema)
    }

    override fun onSchemaDeleteClick(position: Int) {
        val deleteConfirmationFragment = ConfirmationDialogFragment(
            DELETE_PACKAGE_SCHEMA_CONFIRMATION_CALL_KEY,
            Mandrel(),
            viewModel.getSchemasData().value?.get(position) ?: PackageSchema(),
            this,
            position
        )
        deleteConfirmationFragment.show(activity?.supportFragmentManager!!, DELETE_DIALOG_TAG)
    }

    override fun onSchemaEditClick(schema: PackageSchema) {
        val addFragment = AddPackageSchemaDialogFragment(
            CALL_KEY_EDIT,
            schema,
            this,
            viewModel.getSchemasUniqueNames()
        )
        addFragment.show(activity?.supportFragmentManager!!, ADD_FRAGMENT_TAG)
    }


    //inflate recyclers methods
    private fun inflateSchemasList() {
        viewModel.getSchemasData().observe(viewLifecycleOwner) {
            ExportListManager.setSchemasExportList(it.toCollection(ArrayList()))
            val schemasAdapter = PackageSchemaAdapter(
                it.toCollection(ArrayList()),
                requireActivity().applicationContext,
                this
            )
            binding.schemasList.adapter = schemasAdapter
            binding.schemasList.layoutManager =
                LinearLayoutManager(requireActivity().applicationContext)
        }
    }

    private fun inflateMandrelsList() {
        viewModel.getMandrelsData().observe(viewLifecycleOwner) {
            ExportListManager.setMandrelsExportList(it.toCollection(ArrayList()))
            if (it.isNullOrEmpty()) {
                binding.nothingToShowTextView.visibility = TextView.VISIBLE
                binding.mandrelsList.visibility = RecyclerView.INVISIBLE
            }
            val mandrelAdapter = MandrelAdapter(
                it.toCollection(ArrayList()),
                requireActivity().applicationContext,
                this,
                viewModel.isSampleCreated,
                viewModel.isAdministratorMode
            )
            binding.mandrelsList.adapter = mandrelAdapter
            binding.mandrelsList.layoutManager =
                LinearLayoutManager(requireActivity().applicationContext)
        }
    }

    private fun inflateMandrelsSampleList() {
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
                viewModel.isAdministratorMode
            )
            binding.mandrelsSampleList.adapter = mandrelAdapter
            binding.mandrelsSampleList.layoutManager =
                LinearLayoutManager(requireActivity().applicationContext)
        }
    }

    private fun inflateSchemasSampleList(sampleCapParam: SampleCapParameters) {
        val schemasAdapter = PackageSchemaAdapter(
            viewModel.findPackageSchemaList(sampleCapParam)!!,
            requireActivity().applicationContext,
            this,
            viewModel.isSampleCreated
        )
        binding.schemasSampleList.adapter = schemasAdapter
        binding.schemasSampleList.layoutManager =
            LinearLayoutManager(
                requireActivity().applicationContext,
                LinearLayoutManager.HORIZONTAL,
                false
            )

    }

    override fun onPasswordEnter() {
        setAdministratorMode()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onUpdate() {
        if (!viewModel.isAdministratorMode) {
            val enterPasswordDialogFragment = EnterPasswordDialogFragment(
                preferences?.getString(
                    PREFERENCE_KEY_PASSWORD,
                    DEFAULT_PASSWORD
                ), this
            )
            enterPasswordDialogFragment.show(
                requireActivity().supportFragmentManager,
                "PASSWORD"
            )
        } else {
            setOperatorMode()
            setMandrelDataView()
        }
    }

}