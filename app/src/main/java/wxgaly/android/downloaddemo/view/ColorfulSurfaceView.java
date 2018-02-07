package wxgaly.android.downloaddemo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import nova.android.wxgaly.downloaddemo.R;

/**
 * wxgaly.android.downloaddemo.view.
 *
 * @author Created by WXG on 2018/2/7 007 9:40.
 * @version V1.0
 */

public class ColorfulSurfaceView extends SurfaceView {

    private static final String TEXT = "欢迎使用诺瓦产品！";
    private static final int[] colors = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.GRAY, Color.CYAN, Color
            .WHITE, Color.YELLOW, Color.BLACK};
    private static final int TEXT_SIZE = 40;
    private static final int EDGE_WIDTH = 40;

    private Paint mPaint;
    private Shader shader;

    private LinearGradient linearGradient;
    private RadialGradient radialGradient;
    private SweepGradient sweepGradient;
    private BitmapShader bitmapShader;

    private Matrix matrix;
    private int mTranslate;
    private int mDegrees;
    private float mScale;
    private int x = 0;
    private float measureText = 0;

    private ColorfulCallback colorfulCallback;
    private ColorfulDrawThread colorfulDrawThread;
    private boolean isDraw = false;
    private boolean isColorful = false;
    private boolean isBitmap = false;
    private boolean isEdge = true;

    public ColorfulSurfaceView(Context context) {
        super(context);
        init();
    }

    public ColorfulSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorfulSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        SurfaceHolder holder = getHolder();
        colorfulCallback = new ColorfulCallback();
        holder.addCallback(colorfulCallback);
    }

    private void initPaint() {
        Paint paint = mPaint = new Paint();
        paint.setAntiAlias(false);
        paint.setDither(true);
        paint.setTextSize(TEXT_SIZE);
        paint.setSubpixelText(true);
        measureText = mPaint.measureText(TEXT);
        linearGradient = new LinearGradient(0, 0, measureText, measureText, colors, null, Shader.TileMode.REPEAT);

        shader = linearGradient;

        if (isColorful) {
            paint.setShader(shader);
        }

        if (isBitmap) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
            bitmapShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            shader = bitmapShader;
            paint.setShader(shader);
        }

        matrix = new Matrix();

        x = getMeasuredWidth();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initPaint();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (colorfulCallback != null) {
            getHolder().removeCallback(colorfulCallback);
        }
    }

    private class ColorfulCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            colorfulDrawThread = new ColorfulDrawThread();
            isDraw = true;
            new Thread(colorfulDrawThread).start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            isDraw = false;
            colorfulDrawThread = null;
        }
    }

    private class ColorfulDrawThread implements Runnable {

        @Override
        public void run() {
            while (isDraw) {
                Canvas canvas = getHolder().lockCanvas();

                drawText(canvas);
                if (isEdge) {
                    drawFadingEdge(canvas);
                }

                getHolder().unlockCanvasAndPost(canvas);

                x -= 1;
                if (x <= -measureText) {
                    x = getWidth();
                }

                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @SuppressLint("WrongConstant")
    private void drawFadingEdge(Canvas canvas) {

        LinearGradient shader = new LinearGradient(0, 0, EDGE_WIDTH, 1, colors[1] | 0xFF000000,
                colors[1] & 0x00FFFFFF, Shader.TileMode.CLAMP);

        Paint paint = new Paint();
        paint.setShader(shader);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        canvas.drawRect(0, 0, EDGE_WIDTH, getMeasuredHeight(), paint);

        shader = new LinearGradient(getMeasuredWidth() - EDGE_WIDTH, 0, getMeasuredWidth(), 1, colors[7] &
                0x00FFFFFF, colors[7] | 0xFF000000, Shader.TileMode.CLAMP);

        paint.setShader(shader);
        canvas.drawRect(getMeasuredWidth() - EDGE_WIDTH, 0, getMeasuredWidth(), getMeasuredHeight(), paint);

    }

    private void drawText(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG));
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        int baseLine = (int) ((getHeight() - fontMetrics.bottom - fontMetrics.top) / 2);

//        canvas.drawRect(new Rect(0, 0, (int) measureText, (int) (fontMetrics.bottom - fontMetrics.top)), mPaint);
        mPaint.setColor(Color.RED);
//        if (x % 2 == 0) {
//
//            mPaint.setColor(Color.RED);
//        } else {
//            mPaint.setColor(Color.YELLOW);
//        }

        //悬浮
//        canvas.drawText(TEXT, x + 2, baseLine + 2, getSuspendedPaint());

        canvas.drawText(TEXT, x, baseLine, mPaint);

        //套色
//        canvas.drawText(TEXT, x, baseLine, getStrokePaint());


        if (matrix != null) {
            translateMatrix();
//            rotateMatrix();
            shader.setLocalMatrix(matrix); //渐变开始偏移
        }
    }

    @NonNull
    private Paint getSuspendedPaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setDither(true);
        paint.setTextSize(TEXT_SIZE);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        return paint;
    }

    @NonNull
    private Paint getStrokePaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(TEXT_SIZE);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.YELLOW);
        return paint;
    }


    private void rotateMatrix() {
        if (mDegrees <= 0) {
            mDegrees += 1;
        } else if (mDegrees >= 360) {
            mDegrees -= 1;
        }
//        matrix.setRotate(mDegrees);
        matrix.preRotate(mDegrees);
    }

    private void translateMatrix() {
        mTranslate += 2;
//        if (mTranslate > getMeasuredWidth() * 2) {
//            mTranslate = -getMeasuredWidth();
//        } //当偏移超过两倍宽度是移到到最前
        matrix.setTranslate(mTranslate, 0); //设置偏移
    }

}
