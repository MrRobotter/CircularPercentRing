package com.joinyon.circularpercenring;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.text.DecimalFormat;


public class CircularPercentRing extends View {
    private Paint paint;//画圆环
    private int circleWidth;
    private int roundBackgroundColor;
    private int textColor;
    private float textSize;
    private float roundWidth;
    private float progress = 0;
    private int[] colors = {0xffff4639, 0xffCDD513, 0xff3CDF5F};
    private int radius;
    private RectF oval;
    private Paint mPaintText;//画文字
    private int maxColorNumber = 100;
    private float singlPoint = 9;
    private float lineWidth = 0.3f;
    private int circleCenter;
    private SweepGradient sweepGradient;
    private boolean isLine;
    //以下是扩展为动画效果。
    private BarAnimation anim;
    private int stepNumberMax = 6000;// 默认最值
    private DecimalFormat format = new DecimalFormat("#.0");// 格式为保留小数点后一位
    private float mSweepAnglePer;
    private float mPercent;
    private int stepNumber, stepNumberNow;

    /**
     * 分割的数量
     *
     * @param maxColorNumber 数量
     */
    public void setMaxColorNumber(int maxColorNumber) {
        this.maxColorNumber = maxColorNumber;
        singlPoint = (float) 360 / (float) maxColorNumber;
        invalidate();
    }

    /**
     * 是否是线条
     *
     * @param line true 是 false否
     */
    public void setLine(boolean line) {
        isLine = line;
        invalidate();
    }

    public int getCircleWidth() {
        return circleWidth;
    }

    public CircularPercentRing(Context context) {
        this(context, null);
    }

    public CircularPercentRing(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularPercentRing(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CircularPercentRing);
        maxColorNumber = mTypedArray.getInt(R.styleable.CircularPercentRing_circleNumber, 40);
        circleWidth = mTypedArray.getDimensionPixelOffset(R.styleable.CircularPercentRing_circleWidth, getDpValue(180));
        roundBackgroundColor = mTypedArray.getColor(R.styleable.CircularPercentRing_roundColor, 0xffdddddd);
        textColor = mTypedArray.getColor(R.styleable.CircularPercentRing_circleTextColor, 0xff999999);
        roundWidth = mTypedArray.getDimensionPixelOffset(R.styleable.CircularPercentRing_circleRoundWidth, 50);
        textSize = mTypedArray.getDimension(R.styleable.CircularPercentRing_circleTextSize, getDpValue(8));
        colors[0] = mTypedArray.getColor(R.styleable.CircularPercentRing_circleColor1, 0xffff4639);
        colors[1] = mTypedArray.getColor(R.styleable.CircularPercentRing_circleColor2, 0xffcdd513);
        colors[2] = mTypedArray.getColor(R.styleable.CircularPercentRing_circleColor3, 0xff3cdf5f);
        initView();
        mTypedArray.recycle();
    }

    /**
     * 进度条动画
     *
     * @author Administrator
     */
    public class BarAnimation extends Animation {
        public BarAnimation() {

        }

        /**
         * 每次系统调用这个方法时， 改变mSweepAnglePer，mPercent，stepNumberNow的值，
         * 然后调用postInvalidate()不停的绘制view。
         */
        @Override
        protected void applyTransformation(float interpolatedTime,
                                           Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                mPercent = (Float.parseFloat(format.format(interpolatedTime
                        * progress)));// 将浮点值四舍五入保留一位小数
                mSweepAnglePer = interpolatedTime * stepNumber * 360
                        / stepNumberMax;
                stepNumberNow = (int) (interpolatedTime * stepNumber);
            } else {
                mPercent = (Float.parseFloat(format.format(progress)));// 将浮点值四舍五入保留一位小数
                mSweepAnglePer = stepNumber * 360 / stepNumberMax;
                stepNumberNow = stepNumber;
            }

            //  Log.e("AAA", "mPercent=" + mPercent + "interpolatedTime=" + interpolatedTime);
            postInvalidate();
        }
    }

    /**
     * 空白出颜色背景
     *
     * @param roundBackgroundColor
     */
    public void setRoundBackgroundColor(int roundBackgroundColor) {
        this.roundBackgroundColor = roundBackgroundColor;
        paint.setColor(roundBackgroundColor);
        invalidate();
    }

    /**
     * 刻度字体颜色
     *
     * @param textColor
     */
    public void setTextColor(int textColor) {
        this.textColor = textColor;
        mPaintText.setColor(textColor);
        invalidate();
    }

    /**
     * 刻度字体大小
     *
     * @param textSize
     */
    public void setTextSize(float textSize) {
        this.textSize = textSize;
        mPaintText.setTextSize(textSize);
        invalidate();
    }

    /**
     * 渐变颜色
     *
     * @param colors
     */
    public void setColors(int[] colors) {
        if (colors.length < 2) {
            throw new IllegalArgumentException("colors length < 2");
        }
        this.colors = colors;
        sweepGradientInit();
        invalidate();
    }

    /**
     * 设置最大步数
     *
     * @param steps
     */
    public void setStepNumberMax(int steps) {
        this.stepNumberMax = steps;
    }

    /**
     * 更新百分比和设置一圈动画时间
     *
     * @param progress
     * @param time
     */
    public void update(float progress, int time) {
        this.progress = progress;
        anim.setDuration(time);
        //setAnimationTime(time);
        this.startAnimation(anim);
    }


    /**
     * 间隔角度大小
     *
     * @param lineWidth
     */
    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        invalidate();
    }


    private int getDpValue(int w) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, w, getContext().getResources().getDisplayMetrics());
    }

    /**
     * 圆环宽度
     *
     * @param roundWidth 宽度
     */
    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
        if (roundWidth > circleCenter) {
            this.roundWidth = circleCenter;
        }
        radius = (int) (circleCenter - this.roundWidth / 2); // 圆环的半径
        oval.left = circleCenter - radius;
        oval.right = circleCenter + radius;
        oval.bottom = circleCenter + radius;
        oval.top = circleCenter - radius;
        paint.setStrokeWidth(this.roundWidth);
        invalidate();
    }

    /**
     * 圆环的直径
     *
     * @param circleWidth 直径
     */
    public void setCircleWidth(int circleWidth) {
        this.circleWidth = circleWidth;
        circleCenter = circleWidth / 2;

        if (roundWidth > circleCenter) {
            roundWidth = circleCenter;
        }
        setRoundWidth(roundWidth);
        sweepGradient = new SweepGradient(this.circleWidth / 2, this.circleWidth / 2, colors, null);
        //旋转 不然是从0度开始渐变
        Matrix matrix = new Matrix();
        matrix.setRotate(-90, this.circleWidth / 2, this.circleWidth / 2);
        sweepGradient.setLocalMatrix(matrix);
    }

    /**
     * 渐变初始化
     */
    public void sweepGradientInit() {
        //渐变颜色
        sweepGradient = new SweepGradient(this.circleWidth / 2, this.circleWidth / 2, colors, null);
        //旋转 不然是从0度开始渐变
        Matrix matrix = new Matrix();
        matrix.setRotate(-90, this.circleWidth / 2, this.circleWidth / 2);
        sweepGradient.setLocalMatrix(matrix);
    }

    public void initView() {

        circleCenter = circleWidth / 2;//半径
        singlPoint = (float) 360 / (float) maxColorNumber;
        radius = (int) (circleCenter - roundWidth / 2); // 圆环的半径
        sweepGradientInit();
        mPaintText = new Paint();
        mPaintText.setColor(textColor);
        mPaintText.setTextAlign(Paint.Align.CENTER);
        mPaintText.setTextSize(textSize);
        mPaintText.setAntiAlias(true);

        paint = new Paint();

        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        // 用于定义的圆弧的形状和大小的界限
        oval = new RectF(circleCenter - radius, circleCenter - radius, circleCenter + radius, circleCenter + radius);
        anim = new BarAnimation();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //是否是线条模式
//        if (!isLine) {
//            float start = -90f;
//            float p = ((float) maxColorNumber / (float) 100);
//            p = (int) (progress * p);
//            for (int i = 0; i < p; i++) {
//                paint.setColor(roundBackgroundColor);
//                canvas.drawArc(oval, start + singlPoint - lineWidth, lineWidth, false, paint); // 绘制间隔快
//                start = (start + singlPoint);
//            }
//        }
        //偏移角度
        double sinA = (roundWidth / 2) / (circleCenter - (roundWidth / 2));
        // Log.e("TAG", "sinA=" + sinA);
        //double angle = Math.asin((roundWidth / 2) / (circleCenter - (roundWidth / 2)));
        double angle = Math.asin(sinA);
        float lastAngle = (float) (angle * 180 / 3.14);
        // Log.e("TAG", "lastAngle=" + lastAngle);

        //绘制剩下的空白区域
        paint.setShader(null);
        paint.setColor(roundBackgroundColor);
        //canvas.drawArc(oval, -90, (float) (-(progress * 3.6)), false, paint);
        canvas.drawArc(oval, -90 + lastAngle, (float) (-(100f * 3.6) - 2 * lastAngle), false, paint);

        paint.setShader(sweepGradient);
        //背景渐变颜色
        //paint.setShader(sweepGradient);
        paint.setColor(roundBackgroundColor);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.MITER);
        //canvas.drawArc(oval, -90, (float) ((100 - progress) * 3.6), false, paint);
        if (mPercent > 3.0f) {
            canvas.drawArc(oval, -90 + lastAngle, (float) ((mPercent) * 3.6) - 2 * lastAngle, false, paint);
        }

        //绘制文字刻度
//        for (int i = 1; i <= 10; i++) {
//            canvas.save();// 保存当前画布
//            canvas.rotate(360 / 10 * i, circleCenter, circleCenter);
//            canvas.drawText(i * 10 + "", circleCenter, circleCenter - radius + roundWidth / 2 + getDpValue(4) + textSize, mPaintText);
//            canvas.restore();//
//        }
    }

    OnProgressScore onProgressScore;

    public interface OnProgressScore {
        void setProgressScore(float score);

    }

    public synchronized void setProgress(final float p) {
        progress = p;
        postInvalidate();
    }

    /**
     * @param p
     */
    public synchronized void setProgress(final float p, OnProgressScore onProgressScore) {
        this.onProgressScore = onProgressScore;
        // progress = p;
        postInvalidate();
    }

}
