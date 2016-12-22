package cn.label.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yan on 16-12-21.
 */

public class LabelView extends View {
    Paint mTextPaint;
    int mTextColor ;
    float mTextSize ;
    float mTextHeight;
    float mTextWidth;
    int mTextStyle;

    Paint mNumPaint;
    int mNumColor ;
    float mNumSize;
    float mNumHeight;
    float mNumWidth;
    int mNumStyle;

    float mTopPadding;
    float mBottomPadding;
    float mCenterPadding;


    Paint mTrianglePaint;
    int mBackGroundColor;

    float mDegrees;

    String mText = "Top";
    String mNum = "01";

    int width;
    int height;

    public static final int DEGREES_LEFT=-45;
    public static final int DEGREES_RIGHT=45;

    private int mTopDistance = 0;


    public LabelView(Context context) {
        this(context, null);
    }

    public LabelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LabelTextView);

        mTopPadding = ta.getDimension(R.styleable.LabelTextView_labelTopPadding, dp2px(7));
        mCenterPadding = ta.getDimension(R.styleable.LabelTextView_labelCenterPadding, dp2px(3));
        mBottomPadding = ta.getDimension(R.styleable.LabelTextView_labelBottomPadding, dp2px(3));

        mBackGroundColor=ta.getColor(R.styleable.LabelTextView_backgroundColor, Color.parseColor("#66000000"));
        mTextColor=ta.getColor(R.styleable.LabelTextView_textColor, Color.WHITE);
        mNumColor=ta.getColor(R.styleable.LabelTextView_numColor, Color.WHITE);

        mTextSize=ta.getDimension(R.styleable.LabelTextView_textSize,sp2px(8));
        mNumSize=ta.getDimension(R.styleable.LabelTextView_numSize,sp2px(11));

        mText=ta.getString(R.styleable.LabelTextView_text);
        mNum=ta.getString(R.styleable.LabelTextView_num);

        mTextStyle=ta.getInt(R.styleable.LabelTextView_textStyle,0);
        mNumStyle=ta.getInt(R.styleable.LabelTextView_numStyle,2);

        mDegrees = ta.getInt(R.styleable.LabelTextView_direction, 45);

        ta.recycle();

        initTextPaint();
        initNumPaint();
        initTrianglePaint();

        resetTextStatus();
        resetNumStatus();

    }


    public void setNum(String num) {
        mNum = num;
        resetNumStatus();
        invalidate();
    }

    public void setText(String text){
        mText=text;
        resetTextStatus();
        invalidate();
    }

    public void setBackGroundColor(int color){
        mTrianglePaint.setColor(color);
        invalidate();
    }

    private void initTextPaint() {
        //初始化绘制修饰文本的画笔
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mTextSize);
        if (mTextStyle==1){
            mTextPaint.setTypeface(Typeface.SANS_SERIF);
        }else if (mTextStyle==2){
            mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }

    private void initNumPaint() {
        //初始化绘制数字文本的画笔
        mNumPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNumPaint.setColor(mNumColor);
        mNumPaint.setTextAlign(Paint.Align.CENTER);
        mNumPaint.setTextSize(mNumSize);
        if (mNumStyle==1){
            mNumPaint.setTypeface(Typeface.SANS_SERIF);
        }else if (mNumStyle==2){
            mNumPaint.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }

    private void initTrianglePaint(){
        //初始化绘制三角形背景的画笔
        mTrianglePaint= new Paint(Paint.ANTI_ALIAS_FLAG);
        mTrianglePaint.setColor(mBackGroundColor);
    }

    private void resetTextStatus() {
        // 测量文字高度
        Rect rectText = new Rect();
        mTextPaint.getTextBounds(mText, 0, mText.length(), rectText);
        mTextWidth = rectText.width();
        mTextHeight = rectText.height();
    }

    private void resetNumStatus() {
        // 测量数字高度
        Rect rectNum = new Rect();
        mNumPaint.getTextBounds(mNum, 0, mNum.length(), rectNum);
        mNumWidth = rectNum.width();
        mNumHeight = rectNum.height();
    }




    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        canvas.save();
        if (mDegrees==DEGREES_LEFT){
            canvas.translate(-width/2, 0);
            canvas.rotate(mDegrees, width/2, 0);
        }else if (mDegrees==DEGREES_RIGHT){
            canvas.translate(0, -height);
            canvas.rotate(mDegrees, 0, height);
        }

        Path path = new Path();
        path.moveTo(0, height);
        if (mTopDistance > 0) {
            path.lineTo(width / 2 - mTopDistance, mTopDistance + mTopPadding);
            path.lineTo(width / 2 + mTopDistance, mTopDistance + mTopPadding);
        } else {
            path.lineTo(width / 2, 0);
        }
        path.lineTo(width, height);
        path.close();
        canvas.drawPath(path, mTrianglePaint);

        canvas.drawText(mText, (width) / 2, mTopDistance + mTopPadding + mTextHeight, mTextPaint);

        canvas.drawText(mNum, (width) / 2, (mTopDistance + mTopPadding + mTextHeight + mCenterPadding + mNumHeight), mNumPaint);

        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = (int) (mTopDistance + mTopPadding + mCenterPadding + mBottomPadding + mTextHeight + mNumHeight);
        width = 2 * height;
        int realHeight= (int) (height * Math.sqrt(2));
        setMeasuredDimension(realHeight, realHeight);
    }

    public int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public float sp2px(float spValue) {
        final float scale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return spValue * scale;
    }
}
