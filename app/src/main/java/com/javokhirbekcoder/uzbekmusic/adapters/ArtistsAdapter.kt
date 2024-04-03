package com.javokhirbekcoder.uzbekmusic.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.javokhirbekcoder.uzbekmusic.R
import com.javokhirbekcoder.uzbekmusic.databinding.ArtistsLayoutBinding
import com.javokhirbekcoder.uzbekmusic.models.Artists
import com.javokhirbekcoder.uzbekmusic.models.ArtistsItem
import com.javokhirbekcoder.uzbekmusic.viewModel.HomeFragmentViewModel


/*
Created by Javokhirbek on 26/02/2024 at 15:27
*/

class ArtistsAdapter(
    private val list: Artists,
    private val homeFragmentViewModel: HomeFragmentViewModel
) : RecyclerView.Adapter<ArtistsAdapter.MyViewHolder>() {

    private var selectedList = ArrayList<ArtistsItem>()
    private lateinit var selectedSaver: ArrayList<Boolean>

    init {
        selectedList.clear()
    }

    inner class MyViewHolder(private val binding: ArtistsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            binding.artistName.text = list[position].artist;

            var requestOptions = RequestOptions()
            requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(13))

            Glide.with(binding.root.context)
                .load(list[position].img)
                .apply(requestOptions)
                .placeholder(R.drawable.place_holder)
                .into(binding.artistImg)


            binding.myRoot.setOnClickListener {

                if (!selectedSaver[position]) {
                    selectedSaver[position] = true
                    homeFragmentViewModel.saveArtist(list[position])
                    binding.linearLayout.setBackgroundColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.primary
                        )
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ArtistsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        selectedSaver = ArrayList<Boolean>().apply {
            addAll(listOf(*Array(itemCount) { false }))
        }
        return MyViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(position)
    }
}