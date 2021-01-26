package com.example.covenantsermons.podcast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.covenantsermons.databinding.PodcastItemBinding
import com.example.covenantsermons.modelDatabase.Sermon

class PodcastAdapter(private var sermonList: ArrayList<Sermon?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return sermonList.size
    }

    class ItemViewHolder(var viewBinding: PodcastItemBinding) : RecyclerView.ViewHolder(viewBinding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = PodcastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewHolder = holder as ItemViewHolder
        itemViewHolder.viewBinding.sermonItemDateTV.text = sermonList[position]?.timeStamp.toString()
        itemViewHolder.viewBinding.sermonItemTitleTV.text = sermonList[position]?.title
        itemViewHolder.viewBinding.sermonItemDateTV.text = sermonList[position]?.duration.toString()
    }

    fun updateSermonList(newList: ArrayList<Sermon?>) {
        sermonList.clear()
        sermonList.addAll(newList)
        notifyDataSetChanged()
    }
}

//class Items_RVAdapter(private var itemsCells: ArrayList<String?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    override fun getItemCount(): Int {
//        return itemsCells.size
//    }
//
//    class ItemViewHolder(var viewBinding: ItemRowBinding) : RecyclerView.ViewHolder(viewBinding.root)
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ItemViewHolder(binding)
//    }
//
//
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val itemViewHolder = holder as ItemViewHolder
//        itemViewHolder.viewBinding.itemtextview.text = itemsCells[position]
//    }
//}