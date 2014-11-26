package com.example.mladen.helloandroidactivitystates;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Mladen on 18.11.2014..
 */
public class MyViewForDrawing extends View {
    private static final String DEBUG_TAG = "MyViewForDrawing";

    private int mWitdhOfCustomView;
    private int mHeightOfCustomView;

    private Paint mPaint;
    private Paint mPaint01;

    private static float DEFAULT_FONT_SIZE = 700;
    private String mStringToDraw = "K";

    private float mX, mY;
    private Matrix mMatrix;

    public MyViewForDrawing(Context context, AttributeSet attrs) {
        super(context, attrs);
        //my initialize
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.darkpurple));
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(DEFAULT_FONT_SIZE);

        mPaint01 = new Paint();
        mPaint01.setColor(getResources().getColor(R.color.darkgreen));
        mPaint01.setTypeface(Typeface.DEFAULT_BOLD);
        mPaint01.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint01.setTextSize(DEFAULT_FONT_SIZE);

        mMatrix = new Matrix();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawText(mStringToDraw, mX, mY, mPaint);

//        canvas.setMatrix(mMatrix);
        canvas.translate(-mWitdhOfCustomView/4, 0);
        canvas.drawText(mStringToDraw, mX, mY, mPaint01);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heigthWithoutPadding = height - getPaddingTop() - getPaddingBottom();

        mWitdhOfCustomView = widthWithoutPadding;
        mHeightOfCustomView = heigthWithoutPadding;
        Log.d(DEBUG_TAG, "method onMeasure: (mWitdhOfCustomView,mHeightOfCustomView) =(" + mWitdhOfCustomView + "," + mHeightOfCustomView + ")");


        mX = mWitdhOfCustomView/2;
        mY = mHeightOfCustomView/2;

//        mMatrix.setTranslate(-mWitdhOfCustomView/4, 0);
    }
}
