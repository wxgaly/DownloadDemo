package nova.android.slidingcarddemo.view

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import nova.android.slidingcarddemo.R
import nova.android.slidingcarddemo.adapter.RecyclerAdapter

/**
 *  nova.android.slidingcarddemo.view.
 *
 * @author Created by WXG on 2018/7/31 031 18:46.
 * @version V1.0
 */

class SlidingCardLayout : FrameLayout, CoordinatorLayout.AttachedBehavior {

    val TAG = "SlidingCardLayout"
    var headerViewHeight: Int = 0
    var headerView: TextView? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet,
            defStyleAttr) {
        val cardLayout = LayoutInflater.from(context).inflate(R.layout.widget_card, this)

        val recyclerView = cardLayout.findViewById<RecyclerView>(R.id.recycler)
        val adapter = RecyclerAdapter(recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        headerView = cardLayout.findViewById(R.id.header)
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.SlidingCardLayout, defStyleAttr, 0)

        this.headerView!!.setBackgroundColor(typedArray.getColor(R.styleable
                .SlidingCardLayout_android_colorBackground, 0x000))
        this.headerView!!.text = typedArray.getText(R.styleable.SlidingCardLayout_android_text)
        typedArray.recycle()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w != oldw || h != oldh) {
            headerViewHeight = headerView!!.measuredHeight
        }
    }

    override fun getBehavior(): CoordinatorLayout.Behavior<SlidingCardLayout> = SlidingCardBehavior()

}