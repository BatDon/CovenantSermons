package com.example.covenantsermons.podcast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.covenantsermons.databinding.PodcastItemBinding
import com.example.covenantsermons.modelClass.Sermon


class PodcastAdapter(private var sermonList: ArrayList<Sermon?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClick: ((Sermon) -> Unit)? = null
    //val sermonList=sermonArrayList
//    var contacts: List<Contact> = emptyList()

    override fun getItemCount(): Int {
        return sermonList.size
    }

    inner class ItemViewHolder(var viewBinding: PodcastItemBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        init {
            viewBinding.root.setOnClickListener {
                sermonList[adapterPosition]?.let { sermon -> onItemClick?.invoke(sermon) }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = PodcastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

//        val inflater = LayoutInflater.from(parent!!.getContext())
//        val view = inflater.inflate(R.layout.podcast_list_fragment, parent, false)
//
        //listen is extension function
//        RecyclerView.ViewHolder(view).listen { pos, type ->
//            val item = items.get(pos)
//        return ItemViewHolder(viewBinding = binding).listen{pos,type ->
//            val sermon = sermonList[pos]


       // }



        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewHolder = holder as PodcastAdapter.ItemViewHolder
        itemViewHolder.viewBinding.sermonItemDateTV.text = sermonList[position]?.date
        itemViewHolder.viewBinding.sermonItemTitleTV.text = sermonList[position]?.title
        val formattedDuration="${sermonList[position]?.duration.toString()} min"
        itemViewHolder.viewBinding.sermonItemDurationTV.text = formattedDuration
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