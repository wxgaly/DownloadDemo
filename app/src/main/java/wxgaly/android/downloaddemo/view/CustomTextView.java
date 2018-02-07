package wxgaly.android.downloaddemo.view;

import android.content.Context;
import android.graphics.*;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

/**
 * nova.android.downloaddemo.
 *
 * @author Created by WXG on 2018/1/22 022 16:55.
 * @version V1.0
 */

public class CustomTextView extends android.support.v7.widget.AppCompatTextView {

    public static final String CUSTOM_FONT_XINWEI = "/sdcard/nova/viplex_terminal/font/STXINWEI.TTF";
    public static final String CUSTOM_FONT_XIHEI = "/sdcard/nova/viplex_terminal/font/STXIHEI.TTF";
    private static final String TAG = "wxg";
    private static final String TEXT = "请输入文字！";
    private static final char[] chars = TEXT.toCharArray();
    private static final int[] colors = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.GRAY, Color.CYAN, Color
            .BLACK, Color.WHITE, Color.YELLOW};

    private Paint mPaint;
    private Shader shader;
    private LinearGradient linearGradient;
    private RadialGradient radialGradient;
    private SweepGradient sweepGradient;
    private Matrix matrix;
    private int mTranslate;
    private int mDegrees;
    private float mScale;
    private int x = 0;
    private float measureText = 0;

    public CustomTextView(Context context) {
        super(context);
        init();
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {


    }

    private void initPaint() {
        Paint paint = mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        try {
            Typeface typeface = Typeface.createFromFile(CUSTOM_FONT_XIHEI);
            paint.setTypeface(typeface);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        paint.setColor(Color.BLACK);
        paint.setDither(true);
        paint.setTextSize(96);
        linearGradient = new LinearGradient(0, 0, getMeasuredWidth(), 0, colors, null, Shader.TileMode.MIRROR);
//        radialGradient = new RadialGradient(getMeasuredWidth() / 2, getMeasuredHeight() / 2, 20f, colors, null, Shader.TileMode
//                .MIRROR);
//        sweepGradient = new SweepGradient(0, 0, colors, null);
        shader = linearGradient;
        paint.setShader(shader);
        matrix = new Matrix();
        paint.setShadowLayer(2, 5, 5, Color.BLACK);
        measureText = mPaint.measureText(TEXT);
        x = getMeasuredWidth();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {


        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        Log.d(TAG, "ascent: " + fontMetrics.ascent);
        Log.d(TAG, "descent: " + fontMetrics.descent);
        int baseLine = (int) ((getHeight() - fontMetrics.bottom - fontMetrics.top) / 2);

//        canvas.drawRect(new Rect(0, 0, (int) measureText, (int) (fontMetrics.bottom - fontMetrics.top)), mPaint);
//        mPaint.setColor(Color.RED);

        canvas.drawText(TEXT, x, baseLine, mPaint);
        x -= 2;
        if (x <= -measureText) {
            x = getWidth();
        }
//        for (int i = 0; i < chars.length; i++) {
//            canvas.drawText(chars, i, 1, (measureText / chars.length) * i, baseLine, mPaint);
//        }

        if (matrix != null) {
            translateMatrix();
//            rotateMatrix();
            shader.setLocalMatrix(matrix); //渐变开始偏移
            postInvalidateDelayed(16); //刷新间隔
        }

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
        mTranslate += 5;
        if (mTranslate > getMeasuredWidth() * 2) {
            mTranslate = -getMeasuredWidth();
        } //当偏移超过两倍宽度是移到到最前
        matrix.setTranslate(mTranslate, 0); //设置偏移
    }
}
