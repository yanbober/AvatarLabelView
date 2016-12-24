/**
 * MIT License
 *
 * Copyright (c) 2016 yanbo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package cn.label.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * A tool class that adds a label to the various Views.
 */
public final class LabelViewHelper {
    private static final int ROTATE_LEFT = -45;
    private static final int ROTATE_RIGHT = 45;

    private static final int STYLE_NORMAL = 0;
    private static final int STYLE_ITALIC = 1;
    private static final int STYLE_BOLD = 2;

    private Paint mTextTitlePaint;
    private int mTextTitleColor;
    private float mTextTitleSize;
    private Rect mTextTitleRect;
    private int mTextTitleStyle;

    private Paint mTextContentPaint;
    private int mTextContentColor;
    private float mTextContentSize;
    private Rect mTextContentRect;
    private int mTextContentStyle;

    private Paint mBgTrianglePaint;
    private int mBgTriangleColor;

    private float mTopPadding;
    private float mBottomPadding;
    private float mCenterPadding;
    private float mTopDistance;

    private float mRouteDegrees;

    private String mTextTitle;
    private String mTextContent;

    private int mBgTriangleWidth;
    private int mBgTriangleHeight;

    public LabelViewHelper(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LabelView);
        mTopPadding = typedArray.getDimension(R.styleable.LabelView_labelTopPadding,
                context.getResources().getDimensionPixelSize(R.dimen.default_label_top_padding));
        mCenterPadding = typedArray.getDimension(R.styleable.LabelView_labelCenterPadding, 0);
        mBottomPadding = typedArray.getDimension(R.styleable.LabelView_labelBottomPadding,
                context.getResources().getDimensionPixelSize(R.dimen.default_label_bottom_padding));
        mTopDistance = typedArray.getDimension(R.styleable.LabelView_labelTopDistance, 0);
        mBgTriangleColor = typedArray.getColor(R.styleable.LabelView_backgroundColor, Color.BLUE);
        mTextTitleColor = typedArray.getColor(R.styleable.LabelView_textTitleColor, Color.WHITE);
        mTextContentColor = typedArray.getColor(R.styleable.LabelView_textContentColor, Color.WHITE);
        mTextTitleSize = typedArray.getDimension(R.styleable.LabelView_textTitleSize,
                context.getResources().getDimensionPixelSize(R.dimen.default_label_title_size));
        mTextContentSize = typedArray.getDimension(R.styleable.LabelView_textContentSize,
                context.getResources().getDimensionPixelSize(R.dimen.default_label_content_size));
        mTextTitle = typedArray.getString(R.styleable.LabelView_textTitle);
        mTextContent = typedArray.getString(R.styleable.LabelView_textContent);
        mTextTitleStyle = typedArray.getInt(R.styleable.LabelView_textTitleStyle, STYLE_NORMAL);
        mTextContentStyle = typedArray.getInt(R.styleable.LabelView_textContentStyle, STYLE_NORMAL);
        mRouteDegrees = typedArray.getInt(R.styleable.LabelView_direction, ROTATE_LEFT);
        typedArray.recycle();

        initAllArt();
        resetAllMeasureSize();
    }

    public void drawLabel(View view, Canvas canvas) {
        if (canvas == null || view == null) {
            throw new IllegalArgumentException("LabelViewHelper draw canvas or view cant't be null!");
        }

        canvas.save();
        if (mRouteDegrees == ROTATE_LEFT){
            canvas.translate(-mBgTriangleWidth / 2, 0);
            canvas.rotate(mRouteDegrees, mBgTriangleWidth / 2, 0);
        }else if (mRouteDegrees == ROTATE_RIGHT){
            int rotateViewWH= (int) (mBgTriangleHeight * Math.sqrt(2));
            canvas.translate(view.getMeasuredWidth() - rotateViewWH, -mBgTriangleHeight);
            canvas.rotate(mRouteDegrees, 0, mBgTriangleHeight);
        }

        Path path = new Path();
        path.moveTo(0, mBgTriangleHeight);
        if (mTopDistance < 0) {
            // mTopDistance > 0 represents a trapezoid, otherwise represents a triangle.
            mTopDistance = 0;
        }
        path.lineTo(mBgTriangleWidth / 2 - mTopDistance, mTopDistance);
        path.lineTo(mBgTriangleWidth / 2 + mTopDistance, mTopDistance);
        path.lineTo(mBgTriangleWidth, mBgTriangleHeight);
        path.close();
        canvas.drawPath(path, mBgTrianglePaint);

        if (!TextUtils.isEmpty(mTextTitle)) {
            canvas.drawText(mTextTitle, (mBgTriangleWidth) / 2, mTopDistance + mTopPadding + mTextTitleRect.height(), mTextTitlePaint);
        }
        if (!TextUtils.isEmpty(mTextContent)) {
            canvas.drawText(mTextContent, (mBgTriangleWidth) / 2, (mTopDistance + mTopPadding + mTextTitleRect.height() + mCenterPadding + mTextContentRect.height()), mTextContentPaint);
        }

        canvas.restore();
    }

    public int getBgTriangleWidth() {
        return mBgTriangleWidth;
    }

    public int getBgTriangleHeight() {
        return mBgTriangleHeight;
    }

    public void setTextContent(String content) {
        mTextContent = content;
        resetAllMeasureSize();
    }

    public String getTextContent() {
        return mTextContent;
    }

    public void setTextTitle(String title) {
        mTextTitle =title;
        resetAllMeasureSize();
    }

    public String getTextTitle() {
        return mTextTitle;
    }

    public void setLabelBackGroundColor(int color) {
        mBgTrianglePaint.setColor(color);
    }

    private void initAllArt() {
        mTextTitleRect = new Rect();
        mTextContentRect = new Rect();

        mTextTitlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextTitlePaint.setColor(mTextTitleColor);
        mTextTitlePaint.setTextAlign(Paint.Align.CENTER);
        mTextTitlePaint.setTextSize(mTextTitleSize);
        if (mTextTitleStyle == STYLE_ITALIC) {
            mTextTitlePaint.setTypeface(Typeface.SANS_SERIF);
        } else if (mTextTitleStyle == STYLE_BOLD) {
            mTextTitlePaint.setTypeface(Typeface.DEFAULT_BOLD);
        }

        mTextContentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextContentPaint.setColor(mTextContentColor);
        mTextContentPaint.setTextAlign(Paint.Align.CENTER);
        mTextContentPaint.setTextSize(mTextContentSize);
        if (mTextContentStyle == STYLE_ITALIC) {
            mTextContentPaint.setTypeface(Typeface.SANS_SERIF);
        } else if (mTextContentStyle == STYLE_BOLD) {
            mTextContentPaint.setTypeface(Typeface.DEFAULT_BOLD);
        }

        mBgTrianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgTrianglePaint.setColor(mBgTriangleColor);
    }

    private void resetAllMeasureSize() {
        if (!TextUtils.isEmpty(mTextTitle)) {
            mTextTitlePaint.getTextBounds(mTextTitle, 0, mTextTitle.length(), mTextTitleRect);
        }

        if (!TextUtils.isEmpty(mTextContent)) {
            mTextContentPaint.getTextBounds(mTextContent, 0, mTextContent.length(), mTextContentRect);
        }

        mBgTriangleHeight = (int) (mTopDistance + mTopPadding + mCenterPadding + mBottomPadding + mTextTitleRect.height() + mTextContentRect.height());
        mBgTriangleWidth = 2 * mBgTriangleHeight;
    }
}
