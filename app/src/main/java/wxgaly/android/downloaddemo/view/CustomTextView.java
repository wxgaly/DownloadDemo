package wxgaly.android.downloaddemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
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

    public static final String CUSTOM_FONT_XINWEI = "/sdcard/nova/viplex_terminal/font/STXINWEI.TTF";
    public static final String CUSTOM_FONT_XIHEI = "/sdcard/nova/viplex_terminal/font/STXIHEI.TTF";
    public static final String CUSTOM_FONT_KRUTI = "/sdcard/nova/viplex_terminal/font/Kruti_Dev.ttf";
    private static final String TAG = "wxg";
    private static final String TEXT = "请输入文字";
    private static final int TEXT_SIZE = 90;
    private static final String TEXT_INDIA = "请输入ஆனால் நான் hello world உன்னை புரிந்து கொள்ள முடியவில்லை దయచేసి " +
            "టెక్స్ట్ని నమోదు చేయండి知道吗 कृपया पाठ दर्ज करे ದಯವಿಟ್ಟು ಪಠ್ಯವನ್ನು ನಮೂದಿಸಿ";
    private static final String TEXT_ENGLISH = "This is my treasure, health is the first wealth in life.";
    private static final String TEXT_YINDI = "कृपया पाठ दर्ज करेंऐसा करने के लिए कई चीजें होंगी.";
    //    private static final char[] chars = TEXT.toCharArray();
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
//        Log.d(TAG, "bottom: " + fontMetrics.bottom);
//        Log.d(TAG, "top: " + fontMetrics.top);
        int baseLine = (int) ((getHeight() - fontMetrics.bottom - fontMetrics.top) / 2);

//        canvas.drawRect(new Rect(0, 0, (int) measureText, (int) (fontMetrics.bottom -
// fontMetrics.top)), mPaint);
//        mPaint.setColor(Color.RED);

        drawLineText(canvas, "my life");
//        drawIndiaText(canvas, TEXT);

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

    private void drawLineText(Canvas canvas, String text) {

        int realWidth = 0;
        float textWidth = 0;
        char[] chars = text.toCharArray();
        int lineCount = 0;

        Rect rect = new Rect();
        mPaint.getTextBounds("y", 0, 1, rect);
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float descent = fontMetrics.descent;

        float baseLine = (getHeight() - fontMetrics.bottom - fontMetrics.top) / 2;
        float ascent = fontMetrics.ascent;
        Log.d(TAG, "drawLineText: " + baseLine);
        Log.d(TAG, "ascent: " + ascent);
        Log.d(TAG, "descent: " + descent);
        Log.d(TAG, "rect.height(): " + rect.height());

        Paint paintBaseLine = new Paint();
        paintBaseLine.setColor(Color.GREEN);
        paintBaseLine.setStrokeWidth(1);

        Paint paintAscent = new Paint();
        paintAscent.setColor(Color.WHITE);
        paintAscent.setStrokeWidth(1);

        Paint paintDescent = new Paint();
        paintDescent.setColor(Color.BLUE);
        paintDescent.setStrokeWidth(1);

        for (int i = 0; i < chars.length; i++) {
            if (realWidth > getMeasuredWidth()) {
                realWidth = 0;
                lineCount += 1;

            }
            canvas.drawText(chars, i, 1, realWidth, baseLine * (lineCount + 1), mPaint);

            canvas.drawLine(realWidth, baseLine * (lineCount + 1), realWidth + mPaint.measureText(chars, i, 1),
                    baseLine * (lineCount + 1), paintBaseLine);

            canvas.drawLine(realWidth, (baseLine + ascent) * (lineCount + 1), realWidth + mPaint.measureText(chars,
                    i, 1), (baseLine + ascent) * (lineCount + 1), paintAscent);

            canvas.drawLine(realWidth, (baseLine + descent) * (lineCount + 1), realWidth + mPaint.measureText(chars, i,
                    1), (baseLine + descent) * (lineCount + 1), paintDescent);

            textWidth = mPaint.measureText(chars, i, 1);
            realWidth += textWidth;
        }

    }

    private void drawIndiaText(Canvas canvas, String text) {
        float textWidth = mPaint.measureText(text);
        int start = 0;
        int realCount = 1;
        float realWidth = 0f;
        int lineCount = 0;
        int len = text.length();
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        Log.d(TAG, "drawIndiaText: " + (fontMetrics.bottom - fontMetrics.top));
//        int baseLine = (int) ((getHeight() - fontMetrics.bottom - fontMetrics.top) / 2);
        float baseLine = fontMetrics.bottom - fontMetrics.top;

        Log.d(TAG, "baseLine: " + baseLine);

        while ((start + realCount) <= len) {
            realWidth = mPaint.measureText(text, start, start + realCount);

            if (realWidth > getMeasuredWidth()) {

                String str = text.substring(start + realCount - 1, start + realCount);
                int tempRealCount = realCount;

                while (!str.contains(SPACE_STR) && (checkIsContainsIndia(str) || checkIsContainsEnglish(str))) {
                    realCount--;

                    if (realCount == 0) {
                        realCount = tempRealCount;
                        while (!str.contains(SPACE_STR)) {
                            if (start + realCount >= len) {
                                break;
                            }
                            str = text.substring(start + realCount, start + realCount + 1);
                            realCount++;
                        }
                        break;
                    }

                    str = text.substring(start + realCount - 1, start + realCount);
                }

                String drawText = text.substring(start, start + realCount);
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
