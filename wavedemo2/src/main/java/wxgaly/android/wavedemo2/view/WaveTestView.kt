package wxgaly.android.wavedemo2.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 *  wxgaly.android.wavedemo2.view.
 *
 * @author Created by WXG on 2018/8/24 0:22.
 * @version V1.0
 */
class WaveTestView : View {

    private val TAG = "WaveTestView"

    private val mPaint = Paint()
    private val mFirstPath = Path()
    private val mSecondPath = Path()

    /**
     * 两条正弦波之间的波，振幅比较低的那一条
     */
    private val mCenterPath = Path()

    /**
     * 采样点数量，越高越精细
     * 但高于一定限度后人眼察觉不出来
     */
    private val SAMPLINT_SIZE = 128

    private var mSamplingX: Array<Float>? = null //采样点
    private var mMapX: Array<Float>? = null
    private var mWidth = 0
    private var mHeight = 0
    private var mCenterHeight = 0
    private var mAmplitude = 0 // 振幅

    private val mCrestAndCrossPints = Array(9) { _ -> Array(1) { 1 } }
    private val rectF = RectF()
    private val mXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    private val mBackgroudColor = Color.rgb(24, 33, 41)
    private val mCenterPathColor = Color.argb(64, 255, 255, 255)

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet,
            defStyleAttr) {
        init()
    }

    private fun init() {
        mPaint.isDither = true // 防抖动
        mPaint.isAntiAlias = true // 抗锯齿

        for (i in 0..9) {
            mCrestAndCrossPints[i] = arrayOf(2)
        }
    }


}