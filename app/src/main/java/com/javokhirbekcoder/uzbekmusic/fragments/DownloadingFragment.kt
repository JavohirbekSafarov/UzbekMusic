package com.javokhirbekcoder.uzbekmusic.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.javokhirbekcoder.uzbekmusic.R
import com.javokhirbekcoder.uzbekmusic.databinding.FragmentDownloadingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DownloadingFragment : Fragment(R.layout.fragment_downloading) {

    private var _binding: FragmentDownloadingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDownloadingBinding.inflate(inflater)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}