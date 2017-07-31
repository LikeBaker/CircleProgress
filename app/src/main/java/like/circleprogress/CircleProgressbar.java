package like.circleprogress;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CircleProgressbar extends View {

    private int mSweepAngle = 0;
    private float mScoreText = 0;
    private int mCircleWidth;
    private int mCircleBg;
    private int mCircleRaidus;
    private float mTextSize;
    private Paint mAssistCirclePaint;
    private int mAssistCircleWidth = 2;

    public void setProgressbar(int mSweepAngle) {
        this.mSweepAngle = mSweepAngle;
        mScoreText = mSweepAngle / 360f * 100;
        invalidate();
    }

    private Paint mCirclePaint;
    private Paint mArcPaint;
    private Paint mTextScorePaint;
    private Paint mTextDescPaint;

    public CircleProgressbar(Context context) {
        super(context);
        init();
    }

    public CircleProgressbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleProgress);

        mCircleWidth = ta.getDimensionPixelOffset(R.styleable.CircleProgress_circle_width, 25);
        mCircleBg = ta.getColor(R.styleable.CircleProgress_circle_background_bg, Color.GRAY);
        mCircleRaidus = ta.getDimensionPixelOffset(R.styleable.CircleProgress_circle_radius, 400);
        mTextSize = ta.getDimension(R.styleable.CircleProgress_score_textsize, 100);
        Log.d("LikeCircleProgressbar", "mTextSize:" + mTextSize);

        ta.recycle();
        init();
    }

    private void init() {
        mCirclePaint = new Paint();
        mCirclePaint.setStyle(Paint.Style.STROKE);//绘制空心圆，矩形
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mCircleBg);
        mCirclePaint.setStrokeWidth(mCircleWidth);

        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(mCircleWidth);

        mTextScorePaint = new Paint();
        mTextScorePaint.setAntiAlias(true);
        mTextScorePaint.setTextSize(mTextSize);
        mTextScorePaint.setTextAlign(Paint.Align.CENTER);
        mTextScorePaint.setColor(Color.BLACK);

        mTextDescPaint = new Paint();
        mTextDescPaint.setAntiAlias(true);
        mTextDescPaint.setTextSize(mTextSize/3);
        mTextDescPaint.setTextAlign(Paint.Align.CENTER);
        mTextDescPaint.setColor(Color.BLACK);

        mAssistCirclePaint = new Paint();
        mAssistCirclePaint.setAntiAlias(true);
        mAssistCirclePaint.setStyle(Paint.Style.STROKE);
        mAssistCirclePaint.setColor(getResources().getColor(R.color.likeGray));
        mAssistCirclePaint.setStrokeWidth(mAssistCircleWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int mCencerY = getHeight() / 2;
        int mCencerX = getWidth() / 2;

        canvas.drawCircle(mCencerX, mCencerY, mCircleRaidus, mCirclePaint);
        canvas.drawCircle(mCencerX, mCencerY, mCircleRaidus+mCircleWidth/2+mAssistCircleWidth/2, mAssistCirclePaint);
        canvas.drawCircle(mCencerX, mCencerY, mCircleRaidus-mCircleWidth/2-mAssistCircleWidth/2, mAssistCirclePaint);
        canvas.drawCircle(mCencerX, mCencerY, mCircleRaidus+(mCircleWidth*3/2)+mAssistCircleWidth*3/2, mAssistCirclePaint);

        Resources resources = getResources();

        //扇形颜色渐变，float[]参数：0f-0°, 1f-360°
        SweepGradient sweepGradient = new SweepGradient(mCencerX, mCencerY, new int[]{resources.getColor(R.color.likeProgressRed), Color.YELLOW, resources.getColor(R.color.likeProgressGreen), resources.getColor(R.color.likeProgressBlue)}, new float[]{0f, 0.33f, 0.66f, 1f});
        Matrix gradientMatrix = new Matrix();
        gradientMatrix.preRotate(90, mCencerX, mCencerY);//渐变色起始位置顺时针旋转90度
        sweepGradient.setLocalMatrix(gradientMatrix);
        mArcPaint.setShader(sweepGradient);

        RectF mRectf = new RectF(mCencerX - mCircleRaidus, mCencerY - mCircleRaidus, mCencerX + mCircleRaidus, mCencerY + mCircleRaidus);//颜色容器
        canvas.drawArc(mRectf, 90, mSweepAngle, false, mArcPaint);

        Paint.FontMetricsInt fontMetrics = mTextScorePaint.getFontMetricsInt();
        int baseline = (int) ((mRectf.bottom + mRectf.top - fontMetrics.bottom - fontMetrics.top) / 2);
        canvas.drawText(String.valueOf((int) mScoreText), mRectf.centerX(), baseline - mTextSize/4, mTextScorePaint);

        canvas.drawText("当前得分", mRectf.centerX(), baseline + mTextSize/5, mTextDescPaint);
    }
}
