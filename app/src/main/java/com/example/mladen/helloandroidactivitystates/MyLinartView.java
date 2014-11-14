package com.example.mladen.helloandroidactivitystates;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Mladen on 14.11.2014..
 */
public class MyLinartView extends View {
    private static final String DEBUG_TAG = "MyLinartView";

    private Paint mPaint;
    private Paint mPaint01;

    private Paint mPaintForPath;


    private Bitmap mBitmap;
    private Canvas mCanvas;

    final static private int WIDTH_PX = 1100;
    final static private int HEIGHT_PX = 1100;

    final static private float STROKE_WIDTH_DP = 1;

    final static private int DELTAY_DP = 5;

    final static private int DELTAY_MIN_DP = 15;
    final static private int DELTAY_MAX_DP = 25;

    private int mDeltaYPx;

    private Path mPath;

    private Random mRandom;

    private Point mT1, mT2;

    private int mMinMoveDistanceToProcess;


    //callback interface
    public interface MyCallbackClass {
        void callbackReturn(Point t1, Point t2, MyUtils.MOVE_DIRECTION direction);
    }

    private MyCallbackClass myCallbackClass;

    public void registerCallback(MyCallbackClass callbackClass) {
        myCallbackClass = callbackClass;
    }
    //

    public MyLinartView(Context context, AttributeSet attrs) {
        super(context, attrs);


        float density = getResources().getDisplayMetrics().density;
        Log.d(DEBUG_TAG, "density=***" + density + "***");

        mMinMoveDistanceToProcess = (int) (DELTAY_MIN_DP * density);

        float strokeWidthPx = STROKE_WIDTH_DP * density;
        mDeltaYPx = (int) (DELTAY_DP * density);
        Log.d(DEBUG_TAG, "mDeltaYPx=***" + mDeltaYPx + "***");

        //this Paint object is needed every time onDraw is called therefore I create it here and keep it
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.darkpurple));
        mPaint.setStrokeWidth(strokeWidthPx);

        mPaint01 = new Paint();
        mPaint01.setColor(getResources().getColor(android.R.color.black));
        mPaint01.setTextSize(200);
        mPaint01.setStyle(Paint.Style.FILL);

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Log.d(DEBUG_TAG, "onTouch: " + event.toString());

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mT1 = new Point((int) event.getX(), (int) event.getY());
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mT2 = new Point((int) event.getX(), (int) event.getY());

                    //calculate move direction
                    MyUtils.MOVE_DIRECTION tmpDirection = MyUtils.getMoveDirection(mT1, mT2, mMinMoveDistanceToProcess);
                    Log.d(DEBUG_TAG, "tmpDirection: " + tmpDirection);
                    if (tmpDirection != null) {
                        myCallbackClass.callbackReturn(mT1, mT2, tmpDirection);
                        return true;
                    } else {
                        return false;
                    }

                }

                return true;
            }
        });


        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        mBitmap = Bitmap.createBitmap(WIDTH_PX, HEIGHT_PX, conf); // this creates a MUTABLE bitmap
        mCanvas = new Canvas(mBitmap);

        mPaintForPath = new Paint();
        mPaintForPath.setColor(Color.BLACK);
        mPaintForPath.setStrokeWidth(3);
        mPaintForPath.setStyle(Paint.Style.FILL_AND_STROKE);

        mPath = new Path();
        mPath.moveTo(100, 100);
        mPath.lineTo(250, 50);
        mPath.lineTo(500, 200);
        mPath.lineTo(600, 400);
        mPath.lineTo(300, 500);
        mPath.lineTo(50, 300);
        mPath.close();

        mRandom = new Random(System.currentTimeMillis());
        //
        myInitDrawing();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //
        canvas.drawBitmap(mBitmap, 0, 0, null);

    }

    private void myInitDrawing() {
        Log.d(DEBUG_TAG, "myInitDrawing");
//        mCanvas.drawLine(0, 0, WIDTH_PX, HEIGHT_PX, mPaint);
        mCanvas.drawPath(mPath, mPaintForPath);

        //calling invalidate causes the component to draw itself
        invalidate();
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
        switch (direction) {
            case HORIZONTAL:
                linVertOpArtRandBitmapAddToPath(t1.x, t2.x);
                break;
            case VERTICAL:
                linHorOpArtRandBitmapAddToPath(t1.y, t2.y);
                break;
            case SLOPE:
                Toast.makeText(getContext(), "Not yet implemented.", Toast.LENGTH_SHORT).show();
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

        int bitmapWidth = mBitmap.getWidth();
        int bitmapHeight = mBitmap.getHeight();

        Log.d(DEBUG_TAG, "mBitmap dimensions =(" + bitmapWidth + ", " + bitmapHeight + ")");

        int tmpStripeCol = 0;
        int tmpDeltaY;
        int lastYStripe = 0;
        Log.d(DEBUG_TAG, "(minY,maxY) =(" + minY + "," + maxY + ")");
        for (int y = minY; y < maxY; y += tmpDeltaY) {
            tmpDeltaY = MyUtils.randomNumberInRange(mRandom, DELTAY_MIN_DP, DELTAY_MAX_DP);
            Path tempStripe = createHorStripe(0, y, bitmapWidth, tmpDeltaY);

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

        Log.d(DEBUG_TAG, "lastYStripe =" + lastYStripe);
        Path selectedStripe = createHorStripe(0, minY, bitmapWidth, lastYStripe - minY);
        Path dontTouchedPath = new Path(mPath);
        dontTouchedPath.op(selectedStripe, Path.Op.DIFFERENCE);

        //create bitmap that we will draw on
        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap tempBmp = Bitmap.createBitmap(WIDTH_PX, HEIGHT_PX, conf); // this creates a MUTABLE bitmap
        Canvas tempCanvas = new Canvas(tempBmp);

        mPath.set(dontTouchedPath);
        mPath.op(newLineartPath, Path.Op.UNION);
        tempCanvas.drawPath(mPath, mPaint);

        if (null != mBitmap) {
            mBitmap.recycle();
        }
        mBitmap = tempBmp;
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

        int bitmapWidth = mBitmap.getWidth();
        int bitmapHeight = mBitmap.getHeight();

        Log.d(DEBUG_TAG, "mBitmap dimensions =(" + bitmapWidth + ", " + bitmapHeight + ")");

        int tmpStripeCol = 0;
        int tmpDeltaX;
        int x;
        for (x = minX; x < maxX; x += tmpDeltaX) {
            tmpDeltaX = MyUtils.randomNumberInRange(mRandom, DELTAY_MIN_DP, DELTAY_MAX_DP);
            Path tempStripe = createVertStripe(x, 0, bitmapHeight, tmpDeltaX);

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

        Path selectedStripe = createVertStripe(minX, 0, bitmapHeight, x - minX);
        Path dontTouchedPath = new Path(mPath);
        dontTouchedPath.op(selectedStripe, Path.Op.DIFFERENCE);

        //create bitmap that we will draw on
        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap tempBmp = Bitmap.createBitmap(WIDTH_PX, HEIGHT_PX, conf); // this creates a MUTABLE bitmap
        Canvas tempCanvas = new Canvas(tempBmp);

        mPath.set(dontTouchedPath);
        mPath.op(newLineartPath, Path.Op.UNION);
        tempCanvas.drawPath(mPath, mPaint);

        if (null != mBitmap) {
            mBitmap.recycle();
        }
        mBitmap = tempBmp;
    }

}