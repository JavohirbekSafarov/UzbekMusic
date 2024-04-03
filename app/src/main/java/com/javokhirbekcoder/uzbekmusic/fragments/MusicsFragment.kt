package com.javokhirbekcoder.uzbekmusic.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
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

        try {
            if (MusicService.mediaPlayer != null) {
                setUI(MusicService.currentMusic.value!!)
            } else {
                binding.playing.root.visibility = View.GONE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        MusicService.currentMusic.observe(viewLifecycleOwner) {
            Handler(Looper.getMainLooper()).postDelayed({
                setUI(it)
            }, 100)
        }

        binding.shuffleBtn.setOnClickListener {
            with((activity as MainActivity)) {
                startService()
                if (MusicService.mediaPlayer != null) {
                    nextMusic()
                }
            }
            MusicService.shuffle = true

            binding.playing.root.visibility = View.VISIBLE
        }

        return binding.root
    }

    private fun setUI(musicItem: MusicItem) {
        with(binding.playing) {
            root.visibility = View.VISIBLE

            rotateImageContinuously(musicImg, musicItem.music_img)

            changePlayPauseBtnState()

            musicName.text = musicItem.music_name

            root.setOnClickListener {
                findNavController().navigate(R.id.action_musicsFragment_to_playerFragment)
            }
            playPause.setOnClickListener {
                playPauseFunc()
                rotateImageContinuously(musicImg, musicItem.music_img)
            }
            next.setOnClickListener {
                (activity as MainActivity).nextMusic()
            }
        }
    }

    private fun rotateImageContinuously(imageView: ImageView, imageUrl: String) {
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(110))

        Glide.with(imageView.context)
            .load(imageUrl)
            //.apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
            .apply(requestOptions)
            .placeholder(R.drawable.place_holder)
            .into(imageView)

        rotate(imageView)
    }

    private fun rotate(imageView: ImageView) {
        Handler(Looper.getMainLooper()).postDelayed({
            if (checkMediaPlaying())
                rotate(imageView)
            imageView.rotation = imageView.rotation + 1
        }, 80)
    }

    private fun playPauseFunc() {
        if (checkMediaPlaying()) {
            MusicService.mediaPlayer!!.pause()
            binding.playing.playPause.setImageResource(R.drawable.play)
        } else {
            MusicService.mediaPlayer!!.start()
            binding.playing.playPause.setImageResource(R.drawable.pause)
        }
    }

    private fun changePlayPauseBtnState() {
        if (checkMediaPlaying()) {
            binding.playing.playPause.setImageResource(R.drawable.pause)
            return
        }
        binding.playing.playPause.setImageResource(R.drawable.play)
    }

    private fun checkMediaPlaying(): Boolean {
        return MusicService.mediaPlayer != null && MusicService.mediaPlayer!!.isPlaying
    }

    private fun initRecycles() {

        viewModel.getLocalArtists().observe(viewLifecycleOwner) { items ->
            artists.clear()
            artists.addAll(items)
            /*   artists.add(
                   ArtistsItem(
                       "Qo'shish",
                       999999,
                       "https://us.123rf.com/450wm/oliviart/oliviart2004/oliviart200400338/144688847-plus-icon-isolated-on-white-background-add-plus-icon-addition-sign-medical-plus-icon.jpg?ver=6"
                   )
               )*/
            artists.sortBy { it.artist }
            val playlistAdapter = PlayListAdapter(artists, this)
            binding.playlistRecycle.adapter = playlistAdapter
            Log.d("TAG", "initRecycles, get local artist, size ${artists.size} ")
        }

        if (musicsAdapter == null) {
            Log.d("TAG", " ---> Musics adapter initialized!")
            musicsAdapter = MusicsAdapter(musics, this, this)

            viewModel.getAllMusics().observe(viewLifecycleOwner) { musicEntities ->
                musics.clear()
                for (music in musicEntities) {
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
                musics.sortBy { it.music_name }
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
        with((activity as MainActivity)) {
            startService()
            if (MusicService.mediaPlayer != null) {
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