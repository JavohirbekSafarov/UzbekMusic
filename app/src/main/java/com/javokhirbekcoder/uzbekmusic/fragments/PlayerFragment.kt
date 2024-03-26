package com.javokhirbekcoder.uzbekmusic.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.javokhirbekcoder.uzbekmusic.MainActivity
import com.javokhirbekcoder.uzbekmusic.R
import com.javokhirbekcoder.uzbekmusic.databinding.FragmentPlayerBinding
import com.javokhirbekcoder.uzbekmusic.models.MusicItem
import com.javokhirbekcoder.uzbekmusic.service.MusicService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class PlayerFragment : Fragment(R.layout.fragment_player) {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var mediaPlayer: MediaPlayer

    private lateinit var timer: Timer
    private var closed = false

    private val shuffle = false
    private val repeat = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater)

        prepareMusic()

        binding.playPause.setOnClickListener {
            playPause()
        }

        binding.next.setOnClickListener {
            nextMusic()
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        runTimer()

        mediaPlayer.setOnErrorListener { p0, p1, p2 ->
            nextMusic()
            true
            //findNavController().navigateUp()
        }

        mediaPlayer.setOnCompletionListener {
            nextMusic()
        }

        return binding.root
    }

    private fun nextMusic() {
        if (!MusicService.musicPlayingInService) {

            val playingIndex = (activity as MainActivity).playingMusicIndex
            val musicListSize = (activity as MainActivity).playingMusicList.size
            binding.seekBar.progress = 0

            if (shuffle) {
                (activity as MainActivity).playingMusicIndex = shuffleIndex(musicListSize)
                prepareMusic()
            } else {
                if (playingIndex + 1 != musicListSize) {
                    (activity as MainActivity).playingMusicIndex += 1
                    prepareMusic()
                } else {
                    if (repeat) {
                        (activity as MainActivity).playingMusicIndex = 0
                        prepareMusic()
                    } else {
                        mediaPlayer.reset()
                    }
                }
            }
        } else {
            val playingIndex = MusicService.playingMusicIndex
            val musicListSize = MusicService.playingMusicList.size

            if (shuffle) {
                MusicService.playingMusicIndex = shuffleIndex(musicListSize)
                prepareMusic()
            } else {
                if (playingIndex + 1 != musicListSize) {
                    MusicService.playingMusicIndex += 1
                    prepareMusic()
                } else {
                    if (repeat) {
                        MusicService.playingMusicIndex = 0
                        prepareMusic()
                    } else {
                        mediaPlayer.reset()
                    }
                }
            }
        }
    }

    private fun runTimer() {
        timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                CoroutineScope(Dispatchers.Main).launch {
                    if (!closed) {
                        if (mediaPlayer.isPlaying) {
                            binding.seekBar.progress = mediaPlayer.currentPosition
                            binding.currentTime.text =
                                formatDuration(mediaPlayer.currentPosition.toLong())
                        }
                    } else {
                        timer.cancel()
                    }
                }
            }
        }, 0, 1000)
    }

    private fun prepareMusic() {
        try {
            if (!MusicService.musicPlayingInService) {
                val music =
                    (activity as MainActivity).playingMusicList[(activity as MainActivity).playingMusicIndex]
                setData(music)
                Log.d("TAG", "Music from Activity")
            } else {
                val music = MusicService.playingMusicList[MusicService.playingMusicIndex]
                setData(music)
                Log.d("TAG", "Music from Service")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun playPause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            binding.playPause.setImageResource(R.drawable.play)
        } else {
            mediaPlayer.start()
            binding.playPause.setImageResource(R.drawable.pause)
        }
    }

    private fun setData(music: MusicItem) {

        //val musicBasePath = requireContext().getString(R.string.base_music_path2) + music.music_url
//        val musicBasePath2 =
//            requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC + "/.OnlineGroupMusic/")
//                .toString() + music.music_url
//        val f = File(
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC),
//            ".OnlineGroupMusic/${music.music_url}"
//        )
        //music.music_url
        //val musicUri: Uri = Uri.parse(musicBasePath2)

        try {
            mediaPlayer.reset()
            //mediaPlayer.setDataSource(requireContext(), musicUri)
            mediaPlayer.setDataSource(getString(R.string.base_music_path) + music.music_url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                mediaPlayer.start()
                if (!MusicService.musicPlayingInService) {
                    binding.endTime.text = formatDuration(mediaPlayer.duration.toLong())
                    binding.seekBar.max = mediaPlayer.duration

                    binding.artistName.text = music.artist
                    binding.musicName.text = music.music_name
                    Glide.with(requireContext())
                        .load(music.music_img)
                        .placeholder(R.drawable.music_note_3)
                        .into(binding.musicImg)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun formatDuration(duration: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun shuffleIndex(listSize: Int): Int {
        return Random.nextInt(listSize)
    }

    override fun onDestroyView() {
        _binding = null
        closed = true
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        runTimer()
        MusicService.musicPlayingInService = false
        closed = false
    }

    override fun onDetach() {
//        timer.cancel()
//        closed = true
        super.onDetach()
    }
}