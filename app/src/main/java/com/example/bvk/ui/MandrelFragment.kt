package com.example.bvk.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bvk.BVKApplication
import com.example.bvk.databinding.FragmentMandrelBinding
import com.example.bvk.model.Mandrel

class MandrelFragment : Fragment(), AddFragment.OnAddOrEditMandrelListener {

    private var _binding : FragmentMandrelBinding? = null
    private val binding get()= _binding!!

    private val viewModel : MandrelViewModel by viewModels {
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

        binding.addFab.setOnClickListener {
            val addFragment = AddFragment("new", Mandrel())
            addFragment.show(activity?.supportFragmentManager!!, "add")
        }
    }

    override fun onMandrelAdd(mandrel: Mandrel) {
        TODO("Not yet implemented")
    }

    override fun onMandrelEdit(mandrel: Mandrel) {
        TODO("Not yet implemented")
    }
}