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

    private val mCrestAndCrossPints = Array(9) { _ -> Array(3) { 1f } }
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
            mCrestAndCrossPints[i] = Array(2) { 1f }
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
        canvas.drawColor(mBackgroudColor)

        mFirstPath.rewind()
        mSecondPath.rewind()
        mCenterPath.rewind()

        //移动到中点位置
        mFirstPath.moveTo(0f, mCenterHeight.toFloat())
        mSecondPath.moveTo(0f, mCenterHeight.toFloat())
        mCenterPath.moveTo(0f, mCenterHeight.toFloat())

        val offset = (System.currentTimeMillis() - mStartTime) / 2000f

        var x: Float
        var xy: Array<Float>

        // 波形函数的值，包括上一点，当前点和下一点
        var curV = 0f
        var lastV = 0f

        // 计算下一个采样点
        var nextV = (mAmplitude * calculateMapY(mMapX!![0]!!, offset)).toFloat()

        // 波形函数的绝对值，用于筛选波峰和交错点
        var absLastV = 0f
        var absCurV = 0f
        var absNextV = 0f

        // 上一个筛选出的点是波峰还是交错点
        var lastIsCrest = false

        // 筛选出的波峰和交叉点的数量，包括起点和终点
        var crestAndCrossCount = 0

        for (i in 0..SAMPLINT_SIZE) {

            //计算采样点的位置
            x = mSamplingX!![i]!!
            lastV = curV
            curV = nextV


            nextV = if (i < SAMPLINT_SIZE) {
                (mAmplitude * calculateMapY(mMapX!![i + 1]!!, offset)).toFloat()
            } else {
                0f
            }

            mFirstPath.lineTo(x, mCenterHeight + curV)
            mSecondPath.lineTo(x, mCenterHeight - curV)

            // 中间线的振幅是上线两根线振幅的1/5
            mCenterPath.lineTo(x, mCenterHeight + curV / 5F)

            //记录极点值
            absLastV = Math.abs(lastV)
            absCurV = Math.abs(curV)
            absNextV = Math.abs(nextV)

            if (i == 0 || i == SAMPLINT_SIZE || (lastIsCrest && absCurV < absNextV && absCurV > absLastV)) {
                xy = mCrestAndCrossPints[crestAndCrossCount++]
                xy[0] = x
                xy[1] = 0f
                lastIsCrest = false
            } else if (!lastIsCrest && absCurV > absLastV && absCurV > absNextV) {
                xy = mCrestAndCrossPints[crestAndCrossCount++]
                xy[0] = x
                xy[1] = curV
                lastIsCrest = true
            }

        }

        mFirstPath.lineTo(mWidth.toFloat(), mCenterHeight.toFloat())
        mSecondPath.lineTo(mWidth.toFloat(), mCenterHeight.toFloat())
        mCenterPath.lineTo(mWidth.toFloat(), mCenterHeight.toFloat())

        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.WHITE
        mPaint.strokeWidth = 1f

        canvas.drawPath(mFirstPath, mPaint)
        canvas.drawPath(mSecondPath, mPaint)
        canvas.drawPath(mCenterPath, mPaint)

        //记录Layer
        val saveCount = canvas.saveLayer(0F, 0F, mWidth.toFloat(), mHeight.toFloat(), null, Canvas.ALL_SAVE_FLAG)


        // 下一个图层
        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.BLUE
        mPaint.xfermode = mXfermode

        var startX = 0f
        var crestY = 0f
        var endX = 0f

        for (i in 2 until crestAndCrossCount step 2) {

            //每隔两个点绘制一个矩形
            startX = mCrestAndCrossPints[i - 2][0]
            crestY = mCrestAndCrossPints[i - 1][1]
            endX = mCrestAndCrossPints[i][0]

            mPaint.shader = LinearGradient(0F, (mCenterHeight + crestY), mWidth.toFloat(),
                    (mCenterHeight - crestY), Color.BLUE, Color.GREEN, Shader.TileMode.CLAMP)

            rectF.set(startX, (mCenterHeight + crestY), endX, (mCenterHeight - crestY))

            canvas.drawRect(rectF, mPaint)
        }


//        mPaint.shader = null
        mPaint.xfermode = null

        //叠加图层
        canvas.restoreToCount(saveCount)


        //描边
        mLinePaint.style = Paint.Style.STROKE
        mLinePaint.color = Color.BLUE
        mLinePaint.strokeWidth = 3f
        canvas.drawPath(mFirstPath, mLinePaint)

        mLinePaint.color = Color.GREEN
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