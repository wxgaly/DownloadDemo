package nova.android.slidingcarddemo.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.widget_card.view.*
import nova.android.slidingcarddemo.R

/**
 *  nova.android.slidingcarddemo.view.
 *
 * @author Created by WXG on 2018/7/31 031 18:46.
 * @version V1.0
 */
class SlidingCardLayout : FrameLayout {

    var headerViewHeight: Int = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr)

    @SuppressLint("NewApi")
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context,
            attributeSet, defStyleAttr, defStyleRes) {
        LayoutInflater.from(context).inflate(R.layout.widget_card, this)


//        val adapter = SimpleAdapter(context)

        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.SlidingCardLayout, defStyleAttr, defStyleRes)
        header.setBackgroundColor(typedArray.getColor(R.styleable.SlidingCardLayout_android_colorBackground, 0x000))
        header.text = typedArray.getText(R.styleable.SlidingCardLayout_android_text)
        typedArray.recycle()

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w != oldw || h != oldh) {

            headerViewHeight = header.measuredHeight
        }
    }

}