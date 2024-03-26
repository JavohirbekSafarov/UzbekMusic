package com.javokhirbekcoder.uzbekmusic.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.javokhirbekcoder.uzbekmusic.R
import com.javokhirbekcoder.uzbekmusic.databinding.FragmentDownloadingBinding
import com.javokhirbekcoder.uzbekmusic.models.ArtistsItem
import com.javokhirbekcoder.uzbekmusic.models.MusicItem
import com.javokhirbekcoder.uzbekmusic.utils.MusicDownloader
import com.javokhirbekcoder.uzbekmusic.viewModel.DownloadingFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("SetTextI18n")
class DownloadingFragment : Fragment(R.layout.fragment_downloading) {

    private var _binding: FragmentDownloadingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DownloadingFragmentViewModel by viewModels()

    @Inject
    lateinit var musicDownloader: MusicDownloader

    private lateinit var artistsList: List<ArtistsItem>
    private var allMusicsList = ArrayList<MusicItem>()
    private var musicsForDownload = ArrayList<MusicItem>()

    private val downloadMusicsCountInOnce = 10;


    private var downloadedMusicsCount = 0
    private var downloadingMusicsCount = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDownloadingBinding.inflate(inflater)

        viewModel.getArtists().observe(viewLifecycleOwner) { artistsItems ->
            if (artistsItems.isNotEmpty()) {
                artistsList = artistsItems
                initRecycler()

                viewModel.getAllMusics().observe(viewLifecycleOwner) {
                    allMusicsList.addAll(it)
                    prepareDownloading()
                }
            }
        }

        binding.saqlashBtn.setOnClickListener {
            findNavController().navigate(R.id.action_downloadingFragment_to_musicsFragment)
        }


        return binding.root
    }

    private fun initRecycler() {
        Log.d("TAG", "initRecycler")
    }


    private fun prepareDownloading() {

        for (artist in artistsList) {
            binding.logTv.text = binding.logTv.text.toString() + "\n" + artist.artist

            for (music in allMusicsList) {
                var musicCount = 0;
                if (artist.id == music.artist_id.toInt()) {
                    if (musicCount <= downloadMusicsCountInOnce) {

                        musicsForDownload.add(music)
                        binding.logTv.text =
                            binding.logTv.text.toString() + "\n\t\t\t" + music.music_name
                        musicCount++

                    }
                }
            }

//            viewModel.getMusicsOnline(artist.id).observe(viewLifecycleOwner){
//                Log.d("TAG", it.toString())
//                binding.logTv.text = binding.logTv.text.toString() + "\n\t\t" +  it.toString()
//            }
        }
        startDownloading()
    }

    private fun startDownloading() {

        for (music in musicsForDownload) {
            musicDownloader.downloadMusic(music)
        }

//        musicDownloader.downloadMusic(
//            MusicItem(
//                "Jaloliddin Ahmadaliyev",
//                "1",
//                "04:55",
//                1,
//                "https://is4-ssl.mzstatic.com/image/thumb/Music126/v4/75/b7/db/75b7dbca-f72a-d703-8229-cd0252c6a677/8720693724435.png/1200x1200bf-60.jpg",
//                "Bahor_chogi",
//                "https://is4-ssl.mzstatic.com/image/thumb/Music126/v4/75/b7/db/75b7dbca-f72a-d703-8229-cd0252c6a677/8720693724435.png/1200x1200bf-60.jpg",
//                "5.1"
//            )
//        )

        downloadingMusicsCount = musicsForDownload.size
        plusDownloadedMusicCount()
    }

    private fun plusDownloadedMusicCount() {
        downloadedMusicsCount++
        binding.downloadedMusicsCountTv.text = "$downloadedMusicsCount/$downloadingMusicsCount"
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}