package com.javokhirbekcoder.uzbekmusic.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.javokhirbekcoder.uzbekmusic.databinding.PlaylistLayoutBinding
import com.javokhirbekcoder.uzbekmusic.models.Artists

/*
Created by Javokhirbek on 27/02/2024 at 12:39
*/

class PlayListAdapter(
    private val list: Artists,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<PlayListAdapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onArtistClick(position: Int)
    }

    inner class MyViewHolder(private val binding: PlaylistLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {

            var requestOptions = RequestOptions()
            requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(16))


            Glide.with(binding.artistImg.context)
                .load(list[position].img)
                .apply(requestOptions)
                .into(binding.artistImg)
            binding.artistName.text = list[position].artist

            binding.myRoot.setOnClickListener {
                if (position == list.size - 1)
                    onItemClickListener.onArtistClick(-1)
                else
                    onItemClickListener.onArtistClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = PlaylistLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(position)
    }
}