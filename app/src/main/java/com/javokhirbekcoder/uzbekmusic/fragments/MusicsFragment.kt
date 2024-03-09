package com.javokhirbekcoder.uzbekmusic.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.javokhirbekcoder.uzbekmusic.R
import com.javokhirbekcoder.uzbekmusic.adapters.MusicsAdapter
import com.javokhirbekcoder.uzbekmusic.adapters.PlayListAdapter
import com.javokhirbekcoder.uzbekmusic.databinding.FragmentMusicsBinding
import com.javokhirbekcoder.uzbekmusic.models.Artists
import com.javokhirbekcoder.uzbekmusic.models.ArtistsItem
import com.javokhirbekcoder.uzbekmusic.models.Music
import com.javokhirbekcoder.uzbekmusic.models.MusicItem

class MusicsFragment : Fragment(R.layout.fragment_musics), MusicsAdapter.OnOptionsClickListener, MusicsAdapter.OnItemClickListener {

    private var _binding: FragmentMusicsBinding? = null
    private val binding get() = _binding!!

    private var artists = Artists()
    private var musics = Music()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMusicsBinding.inflate(inflater)

        initRecycles()

        return binding.root
    }

    private fun initRecycles() {
        artists.add(ArtistsItem("Men", 0, "https://i.ytimg.com/vi/3i1t-nU_HsM/maxresdefault.jpg"))
        artists.add(ArtistsItem("Sen", 1, "https://i.ytimg.com/vi/3i1t-nU_HsM/maxresdefault.jpg"))
        artists.add(ArtistsItem("U", 2, "https://i.ytimg.com/vi/3i1t-nU_HsM/maxresdefault.jpg"))
        artists.add(ArtistsItem("Ular", 3, "https://i.ytimg.com/vi/3i1t-nU_HsM/maxresdefault.jpg"))

        val playlistAdapter = PlayListAdapter(artists)
        binding.playlistRecycle.adapter = playlistAdapter


        musics.add(MusicItem("Men", "0", "1:20", 0, "https://is4-ssl.mzstatic.com/image/thumb/Music126/v4/75/b7/db/75b7dbca-f72a-d703-8229-cd0252c6a677/8720693724435.png/1200x1200bf-60.jpg", "Sog`indim", "https://nevomusic.net/download.php?idfile=track-014307.mp3", "5.1"))
        musics.add(MusicItem("Men", "0", "1:20", 0, "https://is4-ssl.mzstatic.com/image/thumb/Music126/v4/75/b7/db/75b7dbca-f72a-d703-8229-cd0252c6a677/8720693724435.png/1200x1200bf-60.jpg", "Sog`indim", "https://nevomusic.net/download.php?idfile=track-014307.mp3", "5.1"))
        musics.add(MusicItem("Men", "0", "1:20", 0, "https://is4-ssl.mzstatic.com/image/thumb/Music126/v4/75/b7/db/75b7dbca-f72a-d703-8229-cd0252c6a677/8720693724435.png/1200x1200bf-60.jpg", "Sog`indim", "https://nevomusic.net/download.php?idfile=track-014307.mp3", "5.1"))
        musics.add(MusicItem("Men", "0", "1:20", 0, "https://is4-ssl.mzstatic.com/image/thumb/Music126/v4/75/b7/db/75b7dbca-f72a-d703-8229-cd0252c6a677/8720693724435.png/1200x1200bf-60.jpg", "Sog`indim", "https://nevomusic.net/download.php?idfile=track-014307.mp3", "5.1"))
        musics.add(MusicItem("Men", "0", "1:20", 0, "https://is4-ssl.mzstatic.com/image/thumb/Music126/v4/75/b7/db/75b7dbca-f72a-d703-8229-cd0252c6a677/8720693724435.png/1200x1200bf-60.jpg", "Sog`indim", "https://nevomusic.net/download.php?idfile=track-014307.mp3", "5.1"))
        musics.add(MusicItem("Men", "0", "1:20", 0, "https://is4-ssl.mzstatic.com/image/thumb/Music126/v4/75/b7/db/75b7dbca-f72a-d703-8229-cd0252c6a677/8720693724435.png/1200x1200bf-60.jpg", "Sog`indim", "https://nevomusic.net/download.php?idfile=track-014307.mp3", "5.1"))
        musics.add(MusicItem("Men", "0", "1:20", 0, "https://is4-ssl.mzstatic.com/image/thumb/Music126/v4/75/b7/db/75b7dbca-f72a-d703-8229-cd0252c6a677/8720693724435.png/1200x1200bf-60.jpg", "Sog`indim", "https://nevomusic.net/download.php?idfile=track-014307.mp3", "5.1"))
        musics.add(MusicItem("Men", "0", "1:20", 0, "https://is4-ssl.mzstatic.com/image/thumb/Music126/v4/75/b7/db/75b7dbca-f72a-d703-8229-cd0252c6a677/8720693724435.png/1200x1200bf-60.jpg", "Sog`indim", "https://nevomusic.net/download.php?idfile=track-014307.mp3", "5.1"))

        val musicsAdapter = MusicsAdapter(musics, this, this)
        binding.musicsRecycle.adapter = musicsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(requireContext(), musics[position].music_name, Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_musicsFragment_to_playerFragment)
    }

    override fun onOptionClick(position: Int) {
        Toast.makeText(requireContext(), "Positon = $position", Toast.LENGTH_SHORT).show()
    }
}