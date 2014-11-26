package com.example.mladen.helloandroidactivitystates;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

/**
 * Created by Mladen on 14.11.2014..
 */
public class MyLinartView extends View {
    private static final String DEBUG_TAG = "MyLinartView";

    private Paint mPaint;

    final static private float STROKE_WIDTH_DP = 1;

    final static private int DELTAY_MIN_DP = 15;
    final static private int DELTAY_MAX_DP = 25;

    private Path mPath;
    private Path mOrigPath;
    private Path mUndoPath;
    private Path mRedoPath;

    private Random mRandom;

    private Point mT1, mT2;

    private int mMinMoveDistanceToProcess;

    private int mWitdhOfCustomView;
    private int mHeightOfCustomView;

    final static private int[] STRIPE_WIDTH_ARR = {10, 15, 20, 25};

    public void setmLinartMode(boolean mLinartMode) {
        this.mLinartMode = mLinartMode;
    }

    private boolean mLinartMode;
    private long mStartTouchTime;
    final static private long LONG_PRESS_MIN_DURATION = 500;
    private double xMovePos, yMovePos;
    final static private double MOVE_TOLERANCE_DP = 10;
    private double moveTolerancePix;



    //callback interface
    public interface MyCallbackClass {
        void callbackReturn(Point t1, Point t2, MyUtils.MOVE_DIRECTION direction);

        void callbackToShowPopup();
    }

    private MyCallbackClass myCallbackClass;

    public void registerCallback(MyCallbackClass callbackClass) {
        myCallbackClass = callbackClass;
    }
    //

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heigthWithoutPadding = height - getPaddingTop() - getPaddingBottom();

        mWitdhOfCustomView = widthWithoutPadding;
        mHeightOfCustomView = heigthWithoutPadding;
        Log.d(DEBUG_TAG, "(mWitdhOfCustomView,mHeightOfCustomView) =(" + mWitdhOfCustomView + "," + mHeightOfCustomView + ")");
    }

    public MyLinartView(Context context, AttributeSet attrs) {
        super(context, attrs);


        float density = getResources().getDisplayMetrics().density;
        Log.d(DEBUG_TAG, "density=***" + density + "***");

        mMinMoveDistanceToProcess = (int) (DELTAY_MIN_DP * density);

        float strokeWidthPx = STROKE_WIDTH_DP * density;

        moveTolerancePix = MOVE_TOLERANCE_DP * density;

        //this Paint object is needed every time onDraw is called therefore I create it here and keep it
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.darkpurple));
        mPaint.setStrokeWidth(strokeWidthPx);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Log.d(DEBUG_TAG, "onTouch: " + event.toString());

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mT1 = new Point((int) event.getX(), (int) event.getY());
                    if (!mLinartMode) {
                        mStartTouchTime = System.currentTimeMillis();
                        xMovePos = event.getX();
                        yMovePos = event.getY();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mT2 = new Point((int) event.getX(), (int) event.getY());

                    if (mLinartMode) {
                        //calculate move direction
                        MyUtils.MOVE_DIRECTION tmpDirection = MyUtils.getMoveDirection(mT1, mT2, mMinMoveDistanceToProcess);
                        Log.d(DEBUG_TAG, "tmpDirection: " + tmpDirection);
                        if (tmpDirection != null) {
                            myCallbackClass.callbackReturn(mT1, mT2, tmpDirection);
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        long touchDuration = System.currentTimeMillis() - mStartTouchTime;
                        Log.d(DEBUG_TAG, "touchDuration: " + touchDuration);
                        if (touchDuration >= LONG_PRESS_MIN_DURATION) {
                            myCallbackClass.callbackToShowPopup();
                        }
                    }

                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (!mLinartMode) {
//                        Log.d(DEBUG_TAG, "ACTION_MOVE" + event.toString());

                        if(MyUtils.calcDistanceMaxAbsDifference(xMovePos, yMovePos, event.getX(), event.getY()) > moveTolerancePix){
                            mStartTouchTime = System.currentTimeMillis();
                            xMovePos = event.getX();
                            yMovePos = event.getY();
                        }
                        else {
                            long touchDuration = System.currentTimeMillis() - mStartTouchTime;
                            Log.d(DEBUG_TAG, "touchDuration: " + touchDuration);
                            if (touchDuration >= LONG_PRESS_MIN_DURATION) {
                                myCallbackClass.callbackToShowPopup();
                            }
                        }

                    }
                }

                return true;
            }
        });


        mPath = new Path();
        mPath.moveTo(100, 100);
        mPath.lineTo(250, 50);
        mPath.lineTo(500, 200);
        mPath.lineTo(600, 400);
        mPath.lineTo(300, 500);
        mPath.lineTo(50, 300);
        mPath.close();

        mOrigPath = new Path(mPath);

        mRandom = new Random(System.currentTimeMillis());
        //calling invalidate causes the component to draw itself
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //
        canvas.drawPath(mPath, mPaint);
    }

    private Path createHorStripe(int left, int top, int width, int deltaY) {
        Path stripe = new Path();
        stripe.moveTo(left, top);
        stripe.lineTo(left + width, top);
        stripe.lineTo(left + width, top + deltaY);
        stripe.lineTo(left, top + deltaY);
        stripe.close();
        return stripe;
    }

    private Path createVertStripe(int left, int top, int height, int deltaX) {
        Path stripe = new Path();
        stripe.moveTo(left, top);
        stripe.lineTo(left + deltaX, top);
        stripe.lineTo(left + deltaX, top + height);
        stripe.lineTo(left, top + height);
        stripe.close();
        return stripe;
    }


    public void drawLinOpArtBetweenPoints(Point t1, Point t2, MyUtils.MOVE_DIRECTION direction) {
        mUndoPath = new Path(mPath);
        switch (direction) {
            case HORIZONTAL:
                linVertOpArtRandBitmapAddToPath(t1.x, t2.x);
                break;
            case VERTICAL:
                linHorOpArtRandBitmapAddToPath(t1.y, t2.y);
                break;
            case SLOPE:
//                Toast.makeText(getContext(), "Not yet implemented.", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    public void linHorOpArtRandBitmapAddToPath(int y1, int y2) {
        Log.d(DEBUG_TAG, "linHorOpArtRandBitmapAddToPath");
        Path newLineartPath = new Path();

        int minY, maxY;
        //1. step - find minY and maxY
        if (y1 <= y2) {
            minY = y1;
            maxY = y2;
        } else {
            minY = y2;
            maxY = y1;
        }

        int tmpStripeCol = 0;
        int tmpDeltaY;
        int lastYStripe = 0;
        for (int y = minY; y < maxY; y += tmpDeltaY) {
//            tmpDeltaY = MyUtils.randomNumberInRange(mRandom, DELTAY_MIN_DP, DELTAY_MAX_DP);
            tmpDeltaY = MyUtils.randomValueFromArray(mRandom, STRIPE_WIDTH_ARR);
//            Log.d(DEBUG_TAG, "tmpDeltaY=***" + tmpDeltaY + "***");
            Path tempStripe = createHorStripe(0, y, mWitdhOfCustomView, tmpDeltaY);

            if (tmpStripeCol == 0) {//white
                if (tempStripe.op(mPath, Path.Op.INTERSECT)) {
                    newLineartPath.addPath(tempStripe);
                }
            } else {//black
                tempStripe.op(mPath, Path.Op.DIFFERENCE);
                newLineartPath.addPath(tempStripe);
            }

            lastYStripe = y + tmpDeltaY;
            tmpStripeCol ^= 1;
        }

        Path selectedStripe = createHorStripe(0, minY, mWitdhOfCustomView, lastYStripe - minY);
        Path dontTouchedPath = new Path(mPath);
        dontTouchedPath.op(selectedStripe, Path.Op.DIFFERENCE);

        mPath.set(dontTouchedPath);
        mPath.op(newLineartPath, Path.Op.UNION);
    }

    public void linVertOpArtRandBitmapAddToPath(int x1, int x2) {
        Log.d(DEBUG_TAG, "linVertOpArtRandBitmapAddToPath");
        Path newLineartPath = new Path();

        int minX, maxX;
        //1. step - find minY and maxY
        if (x1 <= x2) {
            minX = x1;
            maxX = x2;
        } else {
            minX = x2;
            maxX = x1;
        }

        int tmpStripeCol = 0;
        int tmpDeltaX;
        int x;
        for (x = minX; x < maxX; x += tmpDeltaX) {
//            tmpDeltaX = MyUtils.randomNumberInRange(mRandom, DELTAY_MIN_DP, DELTAY_MAX_DP);
            tmpDeltaX = MyUtils.randomValueFromArray(mRandom, STRIPE_WIDTH_ARR);
//            Log.d(DEBUG_TAG, "tmpDeltaX=***" + tmpDeltaX + "***");
            Path tempStripe = createVertStripe(x, 0, mHeightOfCustomView, tmpDeltaX);

            if (tmpStripeCol == 0) {//white
                if (tempStripe.op(mPath, Path.Op.INTERSECT)) {
                    newLineartPath.addPath(tempStripe);
                }
            } else {//black
                tempStripe.op(mPath, Path.Op.DIFFERENCE);
                newLineartPath.addPath(tempStripe);
            }

            tmpStripeCol ^= 1;
        }

        Path selectedStripe = createVertStripe(minX, 0, mHeightOfCustomView, x - minX);
        Path dontTouchedPath = new Path(mPath);
        dontTouchedPath.op(selectedStripe, Path.Op.DIFFERENCE);

        mPath.set(dontTouchedPath);
        mPath.op(newLineartPath, Path.Op.UNION);
    }

    public void newArt() {
        mPath = new Path();
        invalidate();
    }

    public void removeArt() {
        mPath = new Path(mOrigPath);
        invalidate();
    }

    public void undoArt() {
        if (mUndoPath != null) {
            mRedoPath = new Path(mPath);
            mPath = new Path(mUndoPath);
            invalidate();
        }
    }

    public void redoArt() {
        if (mRedoPath != null) {
            mUndoPath = new Path(mPath);
            mPath = new Path(mRedoPath);
            invalidate();
        }
    }
}