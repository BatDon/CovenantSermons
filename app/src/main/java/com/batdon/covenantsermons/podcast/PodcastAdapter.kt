package com.batdon.covenantsermons.podcast

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.batdon.covenantsermons.R
import com.batdon.covenantsermons.databinding.PodcastItemBinding
import com.batdon.covenantsermons.modelClass.Sermon
import com.batdon.covenantsermons.modelClass.SermonEntity.Companion.DOWNLOADING_STATE_IMAGES_CANCEL
import com.batdon.covenantsermons.modelClass.SermonEntity.Companion.DOWNLOADING_STATE_IMAGES_DOWNLOAD
import com.batdon.covenantsermons.modelClass.SermonEntity.Companion.DOWNLOADING_STATE_IMAGES_PLAY
import kotlinx.android.synthetic.main.podcast_item.view.*
import timber.log.Timber


class PodcastAdapter(private var mContext: Context?, private var sermonList: ArrayList<Sermon?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClick: ((Sermon) -> Unit)? = null
    var onDownloadCancelPlayClick: ((Sermon) -> Unit)?=null
    //val sermonList=sermonArrayList
//    var contacts: List<Contact> = emptyList()

    override fun getItemCount(): Int {
        return sermonList.size
    }

    inner class ItemViewHolder(var viewBinding: PodcastItemBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        init {
            viewBinding.root.setOnClickListener {
                Timber.i("sermonList[adapterPosition]= ${sermonList[adapterPosition]}")
                sermonList[adapterPosition]?.let { sermon -> onItemClick?.invoke(sermon) }
            }
            viewBinding.root.downloadCancelPlayIV.setOnClickListener {
                sermonList[adapterPosition]?.let { sermon -> onDownloadCancelPlayClick?.invoke(sermon) }
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
        val imageDrawable=sermonList[position]?.downloadingButtonImage?.let { downloadingImageState(it) }
        itemViewHolder.viewBinding.downloadCancelPlayIV.setImageDrawable(imageDrawable)
    }

    private fun downloadingImageState(downloadingStateImages: String)= when (downloadingStateImages) {
//        DownloadingStateImages.DOWNLOAD -> Resources.getDrawable(mContext?.getResources(),)
//        DownloadingStateImages.DOWNLOAD -> Resources.getDrawable(mContext?,mContext?.getDrawable(R.drawable.download_down_arrow))
        DOWNLOADING_STATE_IMAGES_DOWNLOAD -> mContext?.getResources()?.getDrawable(R.drawable.download_down_arrow)
        DOWNLOADING_STATE_IMAGES_CANCEL -> mContext?.getResources()?.getDrawable(R.drawable.cancel_icon)
        DOWNLOADING_STATE_IMAGES_PLAY -> mContext?.getResources()?.getDrawable(R.drawable.play_icon)
        else -> mContext?.getResources()?.getDrawable(R.drawable.download_down_arrow)
    }

    fun getSermonAt(position: Int): Sermon? {
        Timber.i("sermonList= ${sermonList[position]}")
        return sermonList[position]
    }

    fun updateSermonList(newList: ArrayList<Sermon?>) {
        sermonList.clear()
        sermonList.addAll(newList)
        notifyDataSetChanged()
    }

    //TODO remove only for testing
//    fun removeItem(position: Int) {
//        sermonList.removeAt(position)
//        notifyItemRemoved(position)
//    }

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