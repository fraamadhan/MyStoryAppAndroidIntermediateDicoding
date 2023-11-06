package com.example.mystoryapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mystoryapp.R
import com.example.mystoryapp.data.response.ListStoryItem
import com.example.mystoryapp.databinding.ItemStoriesBinding
import com.example.mystoryapp.ui.activity.DetailStoryActivity

class StoryAdapter: PagingDataAdapter<ListStoryItem, StoryAdapter.MyViewHolder> (DIFF_CALLBACK) {

    class MyViewHolder(private val binding: ItemStoriesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            binding.tvNameStory.text = story.name
            binding.tvDescriptionStory.text = story.description
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(binding.ivStory)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var imgPhoto: ImageView = holder.itemView.findViewById(R.id.iv_story)
        var nameStory: TextView = holder.itemView.findViewById<TextView>(R.id.tv_name_story)
        var descriptionStory: TextView = holder.itemView.findViewById(R.id.tv_description_story)
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.STORY_ID, story.id)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        holder.itemView.context as Activity,
                        Pair(imgPhoto, "story_pict"),
                        Pair(nameStory, "story_name"),
                        Pair(descriptionStory, "story_description")
                    )

                holder.itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>(){
            override fun areItemsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

}