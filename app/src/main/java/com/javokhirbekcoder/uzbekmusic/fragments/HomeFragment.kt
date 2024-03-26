package com.javokhirbekcoder.uzbekmusic.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
    private val viewmodel: HomeFragmentViewModel by viewModels()
    private lateinit var adapter: ArtistsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater)



        viewmodel.getMusicsCount().observe(viewLifecycleOwner){
            if (it.isNotEmpty() && it.size > 0){
                findNavController().navigate(R.id.action_homeFragment_to_musicsFragment)
            }
        }

        binding.saveBtn.setOnClickListener {
            //val listSize = adapter.getArtists().size

            //Log.d("TAG", "Tanlanganlar o'lchami $listSize")

            //val selectedArtistsList = List<ArtistsItem>(adapter.getArtistsId().size, list[0])
            //val selectedArtistsList = listOf<ArtistsItem>(adapter.getArtists())

            //if (listSize > 0) {
            if (true) {
                //viewmodel.deleteAllArtists()
                //viewmodel.saveArtists(adapter.getArtists())
                //showDownloadStartingDialog()

                findNavController().navigate(R.id.action_homeFragment_to_downloadingFragment)
            } else {
                Toast.makeText(requireContext(), "Iltimos Sanatkor tanlang!", Toast.LENGTH_SHORT)
                    .show()
            }

        }

        viewmodel.getOnlineArtists().observe(viewLifecycleOwner) {
            list = it
            setAdapter()
        }

        return binding.root
    }

/*    private fun showDownloadStartingDialog() {
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Diqqat!")
            .setIcon(R.drawable.download)
            .setMessage("Tanlangan sa'natkorlarning musiqalari yuklab olish boshlanmoqda...")
            .setPositiveButton("Ok") { p0, p1 -> }
            .show()
    }*/

    private fun setAdapter() {
        adapter = ArtistsAdapter(list, viewmodel)
        binding.homeRecycler.layoutManager = GridLayoutManager(requireContext(), 2, VERTICAL, false)
        binding.homeRecycler.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}