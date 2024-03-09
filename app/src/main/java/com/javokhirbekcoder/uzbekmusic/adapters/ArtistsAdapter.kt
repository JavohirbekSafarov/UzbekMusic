package com.javokhirbekcoder.uzbekmusic.adapters

import android.content.DialogInterface.OnClickListener
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.javokhirbekcoder.uzbekmusic.MainActivity
import com.javokhirbekcoder.uzbekmusic.databinding.ArtistsLayoutBinding
import com.javokhirbekcoder.uzbekmusic.models.Artists


/*
Created by Javokhirbek on 26/02/2024 at 15:27
*/

class ArtistsAdapter(private val list: Artists) :
    RecyclerView.Adapter<ArtistsAdapter.MyViewHolder>() {

        private var selectedList = ArrayList<Int>()

    inner class MyViewHolder(private val binding: ArtistsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            binding.artistName.text = list[position].artist;

            var requestOptions = RequestOptions()
            requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(150))

            Glide.with(binding.root.context)
                .load(list[position].img)
                .apply(requestOptions)
                .into(binding.artistImg)

            binding.myRoot.setOnClickListener {
                if (binding.artistCheck.isChecked){
                    binding.artistCheck.isChecked = false
                    selectedList.remove(list[position].id)
                }else{
                    binding.artistCheck.isChecked = true
                    selectedList.add(list[position].id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ArtistsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(position)
    }

    public fun getArtistsId() = selectedList

}