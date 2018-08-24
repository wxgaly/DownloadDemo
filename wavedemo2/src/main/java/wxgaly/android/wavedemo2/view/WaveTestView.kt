package wxgaly.android.wavedemo2.view

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Message
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
    private val mLinePaint = Paint()
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

    private var mSamplingX: Array<Float?>? = null //采样点
    private var mMapX: Array<Float?>? = null
    private var mWidth = 0
    private var mHeight = 0
    private var mCenterHeight = 0
    private var mAmplitude = 0 // 振幅
    private var mStartTime = System.currentTimeMillis() // 动画开始时间

    private val mCrestAndCrossPints = Array(9) { _ -> Array(1) { 1 } }
    private val rectF = RectF()
    private val mXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    private val mBackgroudColor = Color.rgb(24, 33, 41)
    private val mCenterPathColor = Color.argb(64, 255, 255, 255)

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet,
            defStyleAttr) {
        init()
    }

    private fun init() {
        mPaint.isDither = true // 防抖动
        mPaint.isAntiAlias = true // 抗锯齿

        for (i in 0 until 9) {
            mCrestAndCrossPints[i] = arrayOf(2)
        }
    }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//    }
//
//    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
//        super.onLayout(changed, left, top, right, bottom)
//    }

    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        calculateSampling(canvas)
        drawWave(canvas)
        mHandler.sendEmptyMessageDelayed(0, 16)
    }

    private fun drawWave(canvas: Canvas) {
        canvas.drawColor(Color.rgb(24, 33, 41))

        mFirstPath.rewind()
        mSecondPath.rewind()
        mCenterPath.rewind()

        //移动到中点位置
        mFirstPath.moveTo(0f, mCenterHeight.toFloat())
        mSecondPath.moveTo(0f, mCenterHeight.toFloat())
        mCenterPath.moveTo(0f, mCenterHeight.toFloat())

        val offset = (System.currentTimeMillis() - mStartTime) / 500f

        var x: Float
        var curV: Float

        for (i in 0..SAMPLINT_SIZE) {

            x = mSamplingX!![i]!!
            curV = if (i < SAMPLINT_SIZE) {
                (mAmplitude * calculateMapY(mMapX!![i]!!, offset)).toFloat()
            } else {
                0f
            }

            mFirstPath.lineTo(x, mCenterHeight + curV)
            mSecondPath.lineTo(x, mCenterHeight - curV)
            mCenterPath.lineTo(x, mCenterHeight + curV / 3)
        }

        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.WHITE
        mPaint.strokeWidth = 1f

        canvas.drawPath(mFirstPath, mPaint)
        canvas.drawPath(mSecondPath, mPaint)
        canvas.drawPath(mCenterPath, mPaint)

        val saveCount = canvas.saveLayer(0F, 0F, mWidth.toFloat(), mHeight.toFloat(), null, Canvas.ALL_SAVE_FLAG)

        // 下一个图层
        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.BLUE
        mPaint.xfermode = mXfermode

        mPaint.shader = LinearGradient(0F, (mCenterHeight - mAmplitude).toFloat(), mWidth.toFloat(),
                (mCenterHeight + mAmplitude).toFloat(), Color.BLUE, Color.GREEN, Shader.TileMode.CLAMP)

        rectF.set(0F, (mCenterHeight - mAmplitude).toFloat(), mWidth.toFloat(), (mCenterHeight + mAmplitude).toFloat())

        canvas.drawRect(rectF, mPaint)


//        mPaint.shader = null
        mPaint.xfermode = null

        //叠加图层
        canvas.restoreToCount(saveCount)


        //描边
        mLinePaint.style = Paint.Style.STROKE
        mLinePaint.color = Color.BLUE
        mLinePaint.strokeWidth = 3f

        canvas.drawPath(mFirstPath, mLinePaint)
        canvas.drawPath(mSecondPath, mLinePaint)
        mLinePaint.color = mCenterPathColor
        canvas.drawPath(mCenterPath, mLinePaint)
    }

    private fun calculateSampling(canvas: Canvas) {
        if (mSamplingX == null) {
            mWidth = canvas.width
            mHeight = canvas.height

            mCenterHeight = if (mHeight == 0) {
                50
            } else {
                mHeight.shr(1)
            }

            mAmplitude = if (mWidth == 0) {
                30
            } else {
                mWidth.shr(3)
            }

            mSamplingX = arrayOfNulls(SAMPLINT_SIZE + 1)
            mMapX = arrayOfNulls(SAMPLINT_SIZE + 1)

            val gap = mWidth / SAMPLINT_SIZE.toFloat()

            var x: Float

            for (i in 0 until SAMPLINT_SIZE) {
                x = i * gap
                mSamplingX!![i] = x

                mMapX!![i] = (x / mWidth.toFloat()) * 4 - 2 //映射到[-2, 2]
            }

            mSamplingX!![SAMPLINT_SIZE] = 0f
            mMapX!![SAMPLINT_SIZE] = 0f

        }
    }

    /**
     * 获取Y坐标
     */
    private fun calculateMapY(mapX: Float, offset: Float): Double {

        val offsetA = offset.rem(2)

        val sinFunx = Math.sin(0.75 * Math.PI * mapX - offsetA * Math.PI)

        val recessionFun = Math.pow((4 / (4 + Math.pow(mapX.toDouble(), 4.0))), 2.5)

        return sinFunx * recessionFun
    }


}