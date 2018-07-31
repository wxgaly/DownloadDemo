package nova.android.slidingcarddemo.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import nova.android.slidingcarddemo.R

/**
 *  nova.android.slidingcarddemo.adapter.
 *
 * @author Created by WXG on 2018/7/31 031 19:07.
 * @version V1.0
 */
class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>() {

    private val ITEMS = arrayListOf(
            "赵丽颖", "Angelababy", "林心如",
            "柳岩", "陈乔恩", "苍老师",
            "宋茜", "杨幂", "范冰冰",
            "唐嫣", "刘亦菲", "刘诗诗",
            "高圆圆", "李沁", "郑爽")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {

        return RecyclerHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false))
    }

    override fun getItemCount(): Int = ITEMS.size

    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {

    }

    inner class RecyclerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tv: TextView = itemView.findViewById(R.id.tv_item)

        init {
            itemView.isClickable = true
        }
    }

}