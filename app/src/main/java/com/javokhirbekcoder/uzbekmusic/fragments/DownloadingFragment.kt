package com.javokhirbekcoder.uzbekmusic.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.javokhirbekcoder.uzbekmusic.MainActivity.Companion.yandexAd
import com.javokhirbekcoder.uzbekmusic.R
import com.javokhirbekcoder.uzbekmusic.databinding.FragmentDownloadingBinding
import com.javokhirbekcoder.uzbekmusic.models.ArtistsItem
import com.javokhirbekcoder.uzbekmusic.models.MusicItem
import com.javokhirbekcoder.uzbekmusic.utils.MusicDownloader
import com.javokhirbekcoder.uzbekmusic.viewModel.DownloadingFragmentViewModel
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.roundToInt

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

    private val adSize: BannerAdSize
        get() {
            val adWidthPixels = resources.displayMetrics.widthPixels
            val adWidth = (adWidthPixels / resources.displayMetrics.density).roundToInt()
            return BannerAdSize.stickySize(requireContext(), adWidth)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDownloadingBinding.inflate(inflater)


        binding.kirishBtn.setOnClickListener {
            findNavController().navigate(R.id.action_downloadingFragment_to_musicsFragment)
        }

        viewModel.getArtists().observe(viewLifecycleOwner) { artistsItems ->
            if (artistsItems.isNotEmpty()) {
                artistsList = artistsItems
                //initRecycler()

                viewModel.getAllMusics().observe(viewLifecycleOwner) {
                    allMusicsList.addAll(it)
                    prepareDownloading()
                }
            }
        }

        if (yandexAd) {
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
        refreshDownloadedMusicCount()
        refreshLoadedMusics()
        Log.d("TAG", "Timer start")
    }

    private fun refreshLoadedMusics() {

        Handler(Looper.getMainLooper()).postDelayed({

            Log.d("TAG", "Timer tick")

            viewModel.getOfflineMusics().observe(viewLifecycleOwner) { musicEntityList ->
                var offlineCount = 0;
                musicEntityList.forEach {
                    if (it.is_have_offline) {
                        offlineCount++
                    }
                }
                downloadedMusicsCount = offlineCount

                refreshDownloadedMusicCount()
            }

            if (downloadedMusicsCount == downloadingMusicsCount) {
                findNavController().navigate(R.id.action_downloadingFragment_to_musicsFragment)
            } else {
                refreshLoadedMusics()
            }

        }, 1000)
    }

    private fun refreshDownloadedMusicCount() {
        binding.downloadedMusicsCountTv.text = "$downloadedMusicsCount/$downloadingMusicsCount"
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}