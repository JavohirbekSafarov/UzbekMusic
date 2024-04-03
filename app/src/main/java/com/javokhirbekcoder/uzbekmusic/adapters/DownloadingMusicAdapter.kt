package com.javokhirbekcoder.uzbekmusic.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.javokhirbekcoder.uzbekmusic.R
import com.javokhirbekcoder.uzbekmusic.databinding.MusicsLayoutBinding
import com.javokhirbekcoder.uzbekmusic.models.Music

/*
Created by Javokhirbek on 12/03/2024 at 12:02
*/

class DownloadingMusicAdapter(private val list: Music) :
    RecyclerView.Adapter<DownloadingMusicAdapter.MyViewHolder>() {

    inner class MyViewHolder(private val binding: MusicsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {

            var requestOptions = RequestOptions()
            requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(16))

            binding.musicName.text = list[position].music_name
            binding.musicArtist.text = list[position].artist
            Glide.with(binding.musicImg.context)
                .load(list[position].music_img)
                .apply(requestOptions)
                .placeholder(R.drawable.place_holder)
                .into(binding.musicImg)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = MusicsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(position)
    }
}