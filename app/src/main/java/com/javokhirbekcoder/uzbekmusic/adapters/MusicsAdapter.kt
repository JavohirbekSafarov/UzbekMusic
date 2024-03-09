package com.javokhirbekcoder.uzbekmusic.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.javokhirbekcoder.uzbekmusic.databinding.MusicsLayoutBinding
import com.javokhirbekcoder.uzbekmusic.models.Music

/*
Created by Javokhirbek on 27/02/2024 at 13:07
*/

class MusicsAdapter(
    private val list: Music,
    private val onItemClickListener: OnItemClickListener,
    private val onOptionsClickListener: OnOptionsClickListener
    ) :
    RecyclerView.Adapter<MusicsAdapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    interface OnOptionsClickListener{
        fun onOptionClick(position: Int)
    }

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
                .into(binding.musicImg)

            binding.myRoot.setOnClickListener {
                onItemClickListener.onItemClick(position)
            }
            binding.optionsMenu.setOnClickListener{
                onOptionsClickListener.onOptionClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = MusicsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(position)
    }
}