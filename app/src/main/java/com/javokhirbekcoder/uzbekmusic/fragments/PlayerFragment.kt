package com.javokhirbekcoder.uzbekmusic.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.javokhirbekcoder.uzbekmusic.MainActivity
import com.javokhirbekcoder.uzbekmusic.R
import com.javokhirbekcoder.uzbekmusic.databinding.FragmentPlayerBinding
import com.javokhirbekcoder.uzbekmusic.interfaces.MusicServiceInterface
import com.javokhirbekcoder.uzbekmusic.models.MusicItem
import com.javokhirbekcoder.uzbekmusic.service.MusicService
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

@AndroidEntryPoint
class PlayerFragment : Fragment(R.layout.fragment_player) {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private var closed = false

    private val adSize: BannerAdSize
        get() {
            val adWidthPixels = resources.displayMetrics.widthPixels
            val adWidth = (adWidthPixels / resources.displayMetrics.density).roundToInt()
            return BannerAdSize.stickySize(requireContext(), adWidth)
        }

    private var playerCallBack: MusicServiceInterface? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            playerCallBack = context as MusicServiceInterface?
        } catch (e: ClassCastException) {
            // Error, class doesn't implement the interface
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater)

        //prepareMusic()

        MusicService.currentMusic.observe(viewLifecycleOwner) {
            Handler(Looper.getMainLooper()).postDelayed({
                setUI(it)
            }, 100)
        }
        try {
            setUI(MusicService.currentMusic.value!!)
        } catch (e: Exception) {
            Log.e("TAG", "PlayerFragment72, Set UI error", null)
        }

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.playPause.setOnClickListener {
            playPause()
            val animation = AnimationUtils.loadAnimation(context, R.anim.button_click)
            binding.playPauseCard.startAnimation(animation)
        }

        binding.previous.setOnClickListener {
            playerCallBack!!.prevMusic()
            val animation = AnimationUtils.loadAnimation(context, R.anim.button_prev_click)
            binding.prevCard.startAnimation(animation)
        }

        binding.next.setOnClickListener {
            playerCallBack!!.nextMusic()
            val animation = AnimationUtils.loadAnimation(context, R.anim.button_next_click)
            binding.nextCard.startAnimation(animation)
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    //mediaPlayer.seekTo(progress)
                    MusicService.mediaPlayer?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        if (MainActivity.yandexAd) {
            binding.bannerAd.setAdSize(adSize)
            binding.bannerAd.setAdUnitId(getString(R.string.banner_ad_id))
            val adRequest = AdRequest.Builder().build()
            binding.bannerAd.setBannerAdEventListener(object : BannerAdEventListener {
                override fun onAdLoaded() {
                    Log.d("TAG", "Banner loaded!")
                }

                override fun onAdFailedToLoad(adRequestError: AdRequestError) {
                    Log.d("TAG", "Banner Error! -> $adRequestError")
                }

                override fun onAdClicked() {
                    // Called when a click is recorded for an ad.
                }

                override fun onLeftApplication() {
                    // Called when user is about to leave application (e.g., to go to the browser), as a result of clicking on the ad.
                }

                override fun onReturnedToApplication() {
                    // Called when user returned to application after click.
                }

                override fun onImpression(impressionData: ImpressionData?) {
                    // Called when an impression is recorded for an ad.
                }
            })
            binding.bannerAd.loadAd(adRequest)
        }
        return binding.root
    }


    /*
        private fun nextMusic() {
           // if (!MusicService.musicPlayingInService) {
              */
    /*  val playingIndex = (activity as MainActivity).playingMusicIndex?: 0
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
                }*//*

      //  } else {
            val playingIndex = MusicService.playingMusicIndex
            val musicListSize = MusicService.playingMusicList.size

            if (MusicService.shuffle) {
                MusicService.playingMusicIndex = shuffleIndex(musicListSize)
                prepareMusic()
            } else {
                if (playingIndex + 1 != musicListSize) {
                    MusicService.playingMusicIndex += 1
                    prepareMusic()
                } else {
                    if (MusicService.repeat) {
                        MusicService.playingMusicIndex = 0
                        prepareMusic()
                    } else {
                        mediaPlayer.stop()
                        mediaPlayer.reset()
                    }
                }
            }
        //}
    }
*/

    private fun runTimer() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (!closed) {
                if (MusicService.mediaPlayer!!.isPlaying) {
                    binding.seekBar.progress = MusicService.mediaPlayer!!.currentPosition
                    binding.currentTime.text =
                        formatDuration(MusicService.mediaPlayer!!.currentPosition.toLong())
                    binding.playPause.setImageResource(R.drawable.pause)
                } else {
                    binding.playPause.setImageResource(R.drawable.play)
                }
                runTimer()
            } else {
                Log.d("TAG", "closed true")
            }
        }, 1000)
    }

    private fun setUI(music: MusicItem) {
        Log.d("TAG", "setUI")
        binding.artistName.text = music.artist
        binding.musicName.text = music.music_name
        Glide.with(requireContext())
            .load(music.music_img)
            .placeholder(R.drawable.music_note_3)
            .into(binding.musicImg)
        binding.endTime.text = formatDuration(MusicService.mediaPlayer!!.duration.toLong())
        binding.seekBar.max = MusicService.mediaPlayer!!.duration
    }

    private fun playPause() {
        if (MusicService.mediaPlayer != null && MusicService.mediaPlayer!!.isPlaying) {
            MusicService.mediaPlayer!!.pause()
            Log.d("TAG", "Pause: Player fragment")
            binding.playPause.setImageResource(R.drawable.play)
        } else {
            MusicService.mediaPlayer!!.start()
            Log.d("TAG", "Play: Player fragment")
            binding.playPause.setImageResource(R.drawable.pause)
        }
    }

    /*
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
                //mediaPlayer.reset()
                //mediaPlayer.setDataSource(requireContext(), musicUri)
                //mediaPlayer.setDataSource(getString(R.string.base_music_path) + music.music_url)
                //mediaPlayer.prepareAsync()
                //mediaPlayer.setOnPreparedListener {
                //  mediaPlayer.start()
                //if (!MusicService.musicPlayingInService) {

                //MusicService().nextMusic()
                binding.endTime.text = formatDuration(MusicService.mediaPlayer!!.duration.toLong())
                binding.seekBar.max = MusicService.mediaPlayer!!.duration

                binding.artistName.text = music.artist
                binding.musicName.text = music.music_name
                Glide.with(requireContext())
                    .load(music.music_img)
                    .placeholder(R.drawable.music_note_3)
                    .into(binding.musicImg)
                //  }
                // }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    */

    private fun formatDuration(duration: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onDestroyView() {
        _binding = null
        closed = true
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        runTimer()
        closed = false
    }

    override fun onDetach() {
        closed = true
        super.onDetach()
        playerCallBack = null
    }
}