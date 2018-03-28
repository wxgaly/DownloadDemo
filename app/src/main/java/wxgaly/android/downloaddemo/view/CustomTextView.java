package wxgaly.android.downloaddemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import java.util.regex.Pattern;

/**
 * nova.android.downloaddemo.
 *
 * @author Created by WXG on 2018/1/22 022 16:55.
 * @version V1.0
 */

public class CustomTextView extends android.support.v7.widget.AppCompatTextView {

    public static final String CUSTOM_FONT_XINWEI = "/sdcard/nova/viplex_terminal/font/STXINWEI" +
            ".TTF";
    public static final String CUSTOM_FONT_XIHEI = "/sdcard/nova/viplex_terminal/font/STXIHEI.TTF";
    private static final String TAG = "wxg";
    private static final String TEXT = "请输入文字";
    private static final int TEXT_SIZE = 50;
    private static final String TEXT_INDIA = "请输入ஆனால் நான் hello world உன்னை புரிந்து கொள்ள முடியவில்லை దయచేసి " +
            "టెక్స్ట్ని నమోదు చేయండి知道吗 कृपया पाठ दर्ज करे ದಯವಿಟ್ಟು ಪಠ್ಯವನ್ನು ನಮೂದಿಸಿ";
    private static final String TEXT_ENGLISH = "This is my treasure, health is the first wealth in life.";
    private static final char[] chars = TEXT.toCharArray();
    private static final int[] colors = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.GRAY,
            Color.CYAN, Color
            .BLACK, Color.WHITE, Color.YELLOW};

    private static final String SPACE_STR = " ";

    /**
     * 正则表达式，主要过滤支持的四种印度语，印地语、泰米尔语、泰卢固语以及卡纳达语。
     */
    private static final String REGEX_INDIA_LANGUAGE =
            "[\\u0b80-\\u0bff\\u0c00-\\u0c7f\\u0900-\\u097f\\u0c80-\\u0cff]";

    private static final String REGEX_ENGLISH_LANGUAGE = "[A-Za-z]";

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
        paint.setColor(Color.RED);
        paint.setDither(true);
        paint.setTextSize(TEXT_SIZE);
        linearGradient = new LinearGradient(0, 0, getMeasuredWidth(), 0, colors, null, Shader
                .TileMode.MIRROR);
//        radialGradient = new RadialGradient(getMeasuredWidth() / 2, getMeasuredHeight() / 2,
// 20f, colors, null, Shader.TileMode
//                .MIRROR);
//        sweepGradient = new SweepGradient(0, 0, colors, null);
        shader = linearGradient;
//        paint.setShader(shader);
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
//        Log.d(TAG, "ascent: " + fontMetrics.ascent);
//        Log.d(TAG, "descent: " + fontMetrics.descent);
        int baseLine = (int) ((getHeight() - fontMetrics.bottom - fontMetrics.top) / 2);

//        canvas.drawRect(new Rect(0, 0, (int) measureText, (int) (fontMetrics.bottom -
// fontMetrics.top)), mPaint);
//        mPaint.setColor(Color.RED);

        drawIndiaText(canvas);

//        canvas.drawText(TEXT, x, baseLine, mPaint);
//        x -= 2;
//        if (x <= -measureText) {
//            x = getWidth();
//        }
//        for (int i = 0; i < chars.length; i++) {
//            canvas.drawText(chars, i, 1, (measureText / chars.length) * i, baseLine, mPaint);
//        }

        if (matrix != null) {
//            translateMatrix();
//            rotateMatrix();
            shader.setLocalMatrix(matrix); //渐变开始偏移
//            postInvalidateDelayed(16); //刷新间隔
        }

    }

    private void drawIndiaText(Canvas canvas) {
        float textWidth = mPaint.measureText(TEXT_ENGLISH);
        int start = 0;
        int realCount = 1;
        float realWidth = 0f;
        int lineCount = 0;
        int len = TEXT_ENGLISH.length();
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
//        int baseLine = (int) ((getHeight() - fontMetrics.bottom - fontMetrics.top) / 2);
        int baseLine = (int) (fontMetrics.bottom - fontMetrics.top);

        while ((start + realCount) <= len) {
            realWidth = mPaint.measureText(TEXT_ENGLISH, start, start + realCount);

            if (realWidth > getMeasuredWidth()) {

                String str = TEXT_ENGLISH.substring(start + realCount - 1, start + realCount);
                int tempRealCount = realCount;

                while (!str.contains(SPACE_STR) && (checkIsContainsIndia(str) || checkIsContainsEnglish(str))) {
                    realCount--;

                    if (realCount == 0) {
                        realCount = tempRealCount;
                        while (!str.contains(SPACE_STR)) {
                            if (start + realCount >= len) {
                                break;
                            }
                            str = TEXT_ENGLISH.substring(start + realCount, start + realCount + 1);
                            realCount++;
                        }
                        break;
                    }

                    str = TEXT_ENGLISH.substring(start + realCount - 1, start + realCount);
                }

                String drawText = TEXT_ENGLISH.substring(start, start + realCount);
//                if (!TextUtils.isEmpty(drawText.trim())) {
                    canvas.drawText(drawText, 0, baseLine + TEXT_SIZE * lineCount, mPaint);
//                Log.d(TAG, "drawIndiaText: " + TEXT_INDIA.substring(start, start + realCount) + "---realWidth : " +
//                        realWidth);
                    Log.d(TAG, "drawIndiaText: " + drawText.trim());
                    lineCount++;
//                }

                start = start + realCount;
                realCount = 0;

            }

            realCount++;

        }

    }

    /**
     * @param str
     * @return
     */
    private static boolean checkIsContainsIndia(String str) {
        boolean res = false;

        if (!TextUtils.isEmpty(str)) {
            if (Pattern.compile(REGEX_INDIA_LANGUAGE).matcher(str).find()) {
                res = true;
            }
        }

        return res;
    }

    /**
     * @param str
     * @return
     */
    private static boolean checkIsContainsEnglish(String str) {
        boolean res = false;

        if (!TextUtils.isEmpty(str)) {
            if (Pattern.compile(REGEX_ENGLISH_LANGUAGE).matcher(str).find()) {
                res = true;
            }
        }

        return res;
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
