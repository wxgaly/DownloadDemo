package nova.android.wavedemo.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import nova.android.wavedemo.R


/**
 *  nova.android.wavedemo.view.
 *
 * @author Created by WXG on 2018/8/8 008 14:43.
 * @version V1.0
 */
class WaveView : View {

    private val TAG = "WaveView"
    private val DEFAULT_DURATION = 2000f
    private val DEFAULT_ORIGINY = 500f
    private val DEFAULT_HEIGHT = 200f
    private val DEFAULT_LENGTH = 400f

    /**
     * 小船图片
     */
    private var waveView_boatBitmap: Int = 0

    private var waveView_rise: Boolean = false

    private var waveDuration: Float = DEFAULT_DURATION

    private var waveOriginY: Float = DEFAULT_ORIGINY

    private var waveHeight: Float = DEFAULT_HEIGHT

    private var waveLength: Float = DEFAULT_LENGTH

    private var paint: Paint? = null

    private var path: Path? = null

    private var dx: Int = 0

    private var dy: Int = 0

    private var mBitmap: Bitmap? = null

    private var mSecondBitmap: Bitmap? = null

    private var viewWidth: Int = 0

    private var viewHeight: Int = 0

    private var region: Region? = null

    private var secondRegion: Region? = null

    private var valueAnimator: ValueAnimator? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet,
            defStyleAttr) {
        init(context, attributeSet)
    }

    private fun init(context: Context, attributeSet: AttributeSet) {

        val a = context.obtainStyledAttributes(attributeSet, R.styleable.WaveView)
        waveView_boatBitmap = a.getResourceId(R.styleable.WaveView_boatBitmap, 0)
        waveView_rise = a.getBoolean(R.styleable.WaveView_rise, false)
        waveDuration = a.getDimension(R.styleable.WaveView_duration, DEFAULT_DURATION)
        waveOriginY = a.getDimension(R.styleable.WaveView_originY, DEFAULT_ORIGINY)
        waveHeight = a.getDimension(R.styleable.WaveView_waveHeight, DEFAULT_HEIGHT)
        waveLength = a.getDimension(R.styleable.WaveView_waveLength, DEFAULT_LENGTH)

        a.recycle()

        val options = BitmapFactory.Options()
        options.inSampleSize = 1
        if (waveView_boatBitmap > 0) {
            mBitmap = BitmapFactory.decodeResource(resources, waveView_boatBitmap, options)
            mBitmap = getCircleBitmap(mBitmap!!)
            mSecondBitmap = getCircleBitmap(BitmapFactory.decodeResource(resources, R.drawable.firefox, options))

        } else {
            mBitmap = BitmapFactory.decodeResource(resources, R.drawable.firefox, options)
            mBitmap = getCircleBitmap(mBitmap!!)
            mSecondBitmap = getCircleBitmap(BitmapFactory.decodeResource(resources, R.mipmap.google, options))
        }

        paint = Paint()
        paint?.apply {
            color = ContextCompat.getColor(context, R.color.waveColor)
            style = Paint.Style.FILL_AND_STROKE
        }

        path = Path()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        setMeasuredDimension(viewWidth, viewHeight)
        if (waveOriginY == 0f) {
            waveOriginY = height.toFloat()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawWave(canvas)
        //绘制头像
        drawBitmap(canvas)
    }

    private fun drawBitmap(canvas: Canvas) {
        mBitmap?.apply {
            val bounds = region?.bounds
            val secondBounds = secondRegion?.bounds
            Log.d(TAG, "width : $viewWidth, height : $viewHeight")
            bounds?.apply {
                Log.d(TAG, "bounds : --- ${this} ")
                if (top > 0 || right > 0) {
                    if (top < waveOriginY) { // 从波峰滑落到基准线
                        canvas.drawBitmap(mBitmap,
                                right.toFloat() - mBitmap!!.width / 2,
                                top.toFloat() - mBitmap!!.height,
                                paint)
                    } else {
                        canvas.drawBitmap(mBitmap,
                                right.toFloat() - mBitmap!!.width / 2,
                                bottom.toFloat() - mBitmap!!.height,
                                paint)
                    }
                } else {
                    canvas.drawBitmap(mBitmap,
                            viewWidth / 3f - mBitmap!!.width / 2f,
                            waveOriginY - mBitmap!!.height,
                            paint)
                }
            }

            secondBounds?.apply {
                Log.d(TAG, "bounds : --- ${this} ")
                if (top > 0 || right > 0) {
                    if (top < waveOriginY) { // 从波峰滑落到基准线
                        canvas.drawBitmap(mSecondBitmap,
                                right.toFloat() - mSecondBitmap!!.width / 2,
                                top.toFloat() - mSecondBitmap!!.height,
                                paint)
                    } else {
                        canvas.drawBitmap(mSecondBitmap,
                                right.toFloat() - mSecondBitmap!!.width / 2,
                                bottom.toFloat() - mSecondBitmap!!.height,
                                paint)
                    }
                } else {
                    canvas.drawBitmap(mSecondBitmap,
                            viewWidth * 2 / 3 - mSecondBitmap!!.width / 2f,
                            waveOriginY - mSecondBitmap!!.height,
                            paint)
                }
            }
        }
    }

    fun startAnimation() {
        valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator?.apply {
            duration = waveDuration.toLong()
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                val value: Float = it.animatedValue as Float
                dx = (waveLength * value).toInt()
                postInvalidate()
            }
            start()
        }
    }

    private fun drawWave(canvas: Canvas) {
        //定义曲线
        setPathData()

        canvas.drawPath(path, paint)

    }

    private fun setPathData() {
        path?.apply {
            reset()

            val halfWaveLength = waveLength / 2
            val totalLength = viewWidth.toFloat() + waveLength

            moveTo(-waveLength + dx, waveOriginY)

            var i = -waveLength
            while (i < totalLength) {

                rQuadTo(halfWaveLength / 2, -waveHeight, halfWaveLength, 0f)
                rQuadTo(halfWaveLength / 2, waveHeight, halfWaveLength, 0f)

                i += waveLength
            }

            region = Region()
            var x = (viewWidth / 3).toFloat()
            var clip = Region((x - 0.1).toInt(), 0, x.toInt(), height * 2)
            region?.setPath(path, clip)

            secondRegion = Region()
            x = (viewWidth * 2 / 3).toFloat()
            clip = Region((x - 0.1).toInt(), 0, x.toInt(), height * 2)
            secondRegion?.setPath(path, clip)

            lineTo(width.toFloat(), height.toFloat())
            lineTo(0f, height.toFloat())
            close()
        }
    }

    private fun cropBitmap(bitmap: Bitmap): Bitmap {//从中间截取一个正方形
        val w = bitmap.width // 得到图片的宽，高
        val h = bitmap.height
        val cropWidth = if (w >= h) h else w// 裁切后所取的正方形区域边长

        val cropBitmap = Bitmap.createBitmap(bitmap, (bitmap.width - cropWidth) / 2,
                (bitmap.height - cropWidth) / 2, cropWidth, cropWidth)
        return Bitmap.createScaledBitmap(cropBitmap, (cropWidth / 2.5).toInt(), (cropWidth / 2.5).toInt(), true)
    }

    private fun getCircleBitmap(bitmap: Bitmap): Bitmap? {//把图片裁剪成圆形

        val bitmapRes = cropBitmap(bitmap)//裁剪成正方形

        try {
            val circleBitmap = Bitmap.createBitmap(bitmapRes.width,
                    bitmapRes.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(circleBitmap)
            val paint = Paint()
            val rect = Rect(0, 0, bitmapRes.width,
                    bitmapRes.height)
            val rectF = RectF(Rect(0, 0, bitmapRes.width,
                    bitmapRes.height))
            var roundPx: Float = bitmapRes.width.toFloat()
            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = Color.WHITE
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            val src = Rect(0, 0, bitmapRes.width,
                    bitmapRes.height)
            canvas.drawBitmap(bitmapRes, src, rect, paint)
            return circleBitmap
        } catch (e: Exception) {
            return bitmap
        }

    }

}