package com.javokhirbekcoder.uzbekmusic.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.javokhirbekcoder.uzbekmusic.R
import com.javokhirbekcoder.uzbekmusic.adapters.ArtistsAdapter
import com.javokhirbekcoder.uzbekmusic.databinding.FragmentHomeBinding
import com.javokhirbekcoder.uzbekmusic.models.Artists
import com.javokhirbekcoder.uzbekmusic.viewModel.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var list = Artists()
    private var adapter = ArtistsAdapter(list)
    private val viewmodel: HomeFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater)

        binding.saveBtn.setOnClickListener {
            Toast.makeText(requireContext(), adapter.getArtistsId().toString(), Toast.LENGTH_SHORT)
                .show()
            findNavController().navigate(R.id.action_homeFragment_to_musicsFragment)
        }

        viewmodel.getArtists().observe(viewLifecycleOwner) {
            list = it
            setAdapter()
        }

        return binding.root
    }

    private fun setAdapter() {
        adapter = ArtistsAdapter(list)
        binding.homeRecycler.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}