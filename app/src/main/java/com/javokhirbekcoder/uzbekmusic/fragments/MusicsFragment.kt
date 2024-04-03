package com.javokhirbekcoder.uzbekmusic.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.javokhirbekcoder.uzbekmusic.MainActivity
import com.javokhirbekcoder.uzbekmusic.R
import com.javokhirbekcoder.uzbekmusic.adapters.MusicsAdapter
import com.javokhirbekcoder.uzbekmusic.adapters.PlayListAdapter
import com.javokhirbekcoder.uzbekmusic.databinding.FragmentMusicsBinding
import com.javokhirbekcoder.uzbekmusic.models.Artists
import com.javokhirbekcoder.uzbekmusic.models.Music
import com.javokhirbekcoder.uzbekmusic.models.MusicItem
import com.javokhirbekcoder.uzbekmusic.service.MusicService
import com.javokhirbekcoder.uzbekmusic.viewModel.MusicFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MusicsFragment : Fragment(R.layout.fragment_musics), MusicsAdapter.OnOptionsClickListener,
    MusicsAdapter.OnItemClickListener, PlayListAdapter.OnItemClickListener {

    private var _binding: FragmentMusicsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MusicFragmentViewModel by viewModels()

    private var artists = Artists()
    private var musics = Music()
    private var musicsAdapter: MusicsAdapter? = null

//    @Inject
//    lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMusicsBinding.inflate(inflater)

        initRecycles()

        binding.playing.next.setOnClickListener {
            Toast.makeText(requireContext(), "Next", Toast.LENGTH_SHORT).show()
        }
        binding.playing.playPause.setOnClickListener {
            try {
                /* if (mediaPlayer.isPlaying) {
                     mediaPlayer.pause()
                 } else {
                     mediaPlayer.start()
                 }*/
            } catch (e: Exception) {
                Log.d("TAG", "Error playing, footer, -> ${e.printStackTrace()}")
            }
        }

        return binding.root
    }

    private fun initRecycles() {

        viewModel.getLocalArtists().observe(viewLifecycleOwner) {
            artists.clear()
            artists.addAll(it)
            /*   artists.add(
                   ArtistsItem(
                       "Qo'shish",
                       999999,
                       "https://us.123rf.com/450wm/oliviart/oliviart2004/oliviart200400338/144688847-plus-icon-isolated-on-white-background-add-plus-icon-addition-sign-medical-plus-icon.jpg?ver=6"
                   )
               )*/
            val playlistAdapter = PlayListAdapter(artists, this)
            binding.playlistRecycle.adapter = playlistAdapter
            Log.d("TAG", "initRecycles, get local artist, size ${artists.size} ")
        }

        if (musicsAdapter == null) {
            Log.d("TAG", " ---> Musics adapter initialized!")
            musicsAdapter = MusicsAdapter(musics, this, this)

            viewModel.getAllMusics().observe(viewLifecycleOwner) {
                musics.clear()
                for (music in it) {
                    musics.add(
                        MusicItem(
                            music.artist,
                            music.artist,
                            music.duration,
                            music.id,
                            music.music_img,
                            music.music_name,
                            music.music_name_path,
                            null
                        )
                    )
                }
                MusicService.playingMusicList.clear()
                MusicService.playingMusicList.addAll(musics)

                Log.d("TAG", "musics size in service = ${MusicService.playingMusicList.size}")
                Log.d("TAG", "recycler item count = ${musicsAdapter!!.itemCount}")
                musicsAdapter!!.notifyDataSetChanged()
                //musicsAdapter!!.notifyItemRangeInserted(0, musics.size)
            }
            binding.musicsRecycle.adapter = musicsAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        musicsAdapter = null
        _binding = null
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(requireContext(), position.toString(), Toast.LENGTH_SHORT).show()
        if (MusicService.playingMusicIndex != position) {
            MusicService.playingMusicIndex = position
            Log.d("TAG", "music index changed $position MusicFragment126")
        }
        with((activity as MainActivity)){
            startService()
            if (MusicService.mediaPlayer != null){
                startMusic()
            }
        }

        findNavController().navigate(R.id.action_musicsFragment_to_playerFragment)
    }

    override fun onOptionClick(position: Int) {
        Toast.makeText(requireContext(), "Positon = $position", Toast.LENGTH_SHORT).show()
    }

    override fun onArtistClick(position: Int) {
        if (position == -1) {
            findNavController().navigate(R.id.action_musicsFragment_to_homeFragment)
        } else {
            Toast.makeText(
                requireContext(),
                "Artist=${artists[position].artist}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}