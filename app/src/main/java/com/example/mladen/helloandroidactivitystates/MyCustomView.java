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
 * Created by Mladen on 11.11.2014..
 */
public class MyCustomView extends View {
    private static final String DEBUG_TAG = "MyCustomView";

    private Paint mPaint;
    private Paint mPaint01;

    private Paint mPaintForPath;

    private int mX = 0;
    private int mY = 0;
    final private int WIDTH = 200;
    final private int HEIGHT = 200;

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
    public interface MyCallbackClass{
        void callbackReturn(Point t1, Point t2, MyUtils.MOVE_DIRECTION direction);
    }

    private MyCallbackClass myCallbackClass;

    public void registerCallback(MyCallbackClass callbackClass){
        myCallbackClass = callbackClass;
    }
    //


    public MyCustomView(Context context, AttributeSet attrs) {
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
                    if(tmpDirection != null){
                        myCallbackClass.callbackReturn(mT1, mT2, tmpDirection);
                        return true;
                    }
                    else{
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

    public void drawSomething() {
        Log.d(DEBUG_TAG, "drawSomething");
//        mCanvas.drawRect(0, 0, WIDTH_PX/2, HEIGHT_PX/2, mPaint01);
        mCanvas.drawText("Hello world", 50, 200, mPaint01);
        //calling invalidate causes the component to draw itself
        invalidate();
    }


    public void exploreBitmap() {
        Log.d(DEBUG_TAG, "drawSomething");

        int bitmapWidth = mBitmap.getWidth();
        int bitmapHeight = mBitmap.getHeight();

        Log.d(DEBUG_TAG, "mBitmap dimensions =(" + bitmapWidth + ", " + bitmapHeight + ")");

        int[] pix = new int[bitmapWidth * bitmapHeight];
        mBitmap.getPixels(pix, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);

        int a, r, g, b, index;
        int newA, newR, newG, newB;

        for (int y = 0; y < bitmapHeight; y++) {
            for (int x = 0; x < bitmapWidth; x++) {
                index = y * bitmapWidth + x;
                a = (pix[index] >> 24) & 0xff;
                r = (pix[index] >> 16) & 0xff;
                g = (pix[index] >> 8) & 0xff;
                b = pix[index] & 0xff;


                if (a != 0 || r != 0 || g != 0 || b != 0) {
//                    Log.d(DEBUG_TAG, String.format("(a,r,g,b)=(%d,%d,%d,%d)", a, r, g, b));
                    newR = 255;
                } else {
                    newR = r;
                }

                newA = a;
                newG = g;
                newB = b;

//                pix[index] = 0xff000000 | (newR << 16) | (newG << 8) | newB;
                pix[index] = (newA << 24) | (newR << 16) | (newG << 8) | newB;
            }
        }

        Log.d(DEBUG_TAG, "finished with processing");

        Bitmap bm = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        bm.setPixels(pix, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);

        if (null != mBitmap) {
            mBitmap.recycle();
        }
        mBitmap = bm;

        //calling invalidate causes the component to draw itself
        invalidate();
    }

    private void myInitDrawing() {
        Log.d(DEBUG_TAG, "myInitDrawing");
//        mCanvas.drawLine(0, 0, WIDTH_PX, HEIGHT_PX, mPaint);
        mCanvas.drawPath(mPath, mPaintForPath);

        //calling invalidate causes the component to draw itself
        invalidate();
    }


    public void lineartBitmap() {
        Log.d(DEBUG_TAG, "lineartBitmap");

        int bitmapWidth = mBitmap.getWidth();
        int bitmapHeight = mBitmap.getHeight();

        Log.d(DEBUG_TAG, "mBitmap dimensions =(" + bitmapWidth + ", " + bitmapHeight + ")");


        //create bitmap that we will draw on
        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap tempBmp = Bitmap.createBitmap(WIDTH_PX, HEIGHT_PX, conf); // this creates a MUTABLE bitmap
        Canvas tempCanvas = new Canvas(tempBmp);


        int[] pix = new int[bitmapWidth * bitmapHeight];
        mBitmap.getPixels(pix, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);

        int a, r, g, b, index;

        int x, t1, t2;
        for (int y = 0; y < bitmapHeight; y += mDeltaYPx) {
//            Log.d(DEBUG_TAG, "y="+y);
            x = 0;
            while (x < bitmapWidth) {
                while (x < bitmapWidth) {
                    index = y * bitmapWidth + x;
                    a = (pix[index] >> 24) & 0xff;
                    r = (pix[index] >> 16) & 0xff;
                    g = (pix[index] >> 8) & 0xff;
                    b = pix[index] & 0xff;

                    if (!isWhite(a, r, g, b)) {
//                        Log.d(DEBUG_TAG, "not white at (x,y)="+"("+x+","+y+")");
                        break;
                    }

                    ++x;
                }

                if (x == bitmapWidth) continue;

                t1 = x;

                while (x < bitmapWidth) {
                    index = y * bitmapWidth + x;
                    a = (pix[index] >> 24) & 0xff;
                    r = (pix[index] >> 16) & 0xff;
                    g = (pix[index] >> 8) & 0xff;
                    b = pix[index] & 0xff;

                    if (isWhite(a, r, g, b)) {
                        break;
                    }

                    ++x;
                }

                t2 = x - 1;

                //draw line t1 - t2
//                Log.d(DEBUG_TAG, "draw line = (" + t1 + ", " + y + ") to ("+ t2 + ", " + y + ")");
                tempCanvas.drawLine(t1, y, t2, y, mPaint);
            }
        }

        Log.d(DEBUG_TAG, "finished with processing");

        if (null != mBitmap) {
            mBitmap.recycle();
        }
        mBitmap = tempBmp;

        //calling invalidate causes the component to draw itself
        invalidate();
    }

    private boolean isWhite(int a, int r, int g, int b) {
        return (a == 0 && r == 0 && g == 0 && b == 0);
    }

    public void linHorOpArtBitmap() {
        Log.d(DEBUG_TAG, "linHorOpArtBitmap");

        int bitmapWidth = mBitmap.getWidth();
        int bitmapHeight = mBitmap.getHeight();

        Log.d(DEBUG_TAG, "mBitmap dimensions =(" + bitmapWidth + ", " + bitmapHeight + ")");


        //create bitmap that we will draw on
        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap tempBmp = Bitmap.createBitmap(WIDTH_PX, HEIGHT_PX, conf); // this creates a MUTABLE bitmap
        Canvas tempCanvas = new Canvas(tempBmp);


        int[] pix = new int[bitmapWidth * bitmapHeight];
        mBitmap.getPixels(pix, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);

        int a, r, g, b, index;

        int x, t1, t2;
        int tmpStripeCol = 0;
        for (int y = 0; y < bitmapHeight; y += mDeltaYPx) {
            Log.d(DEBUG_TAG, "tmpStripeCol=" + tmpStripeCol);
            Path tempStripe = createHorStripe(0, y, bitmapWidth, mDeltaYPx);

            if (tmpStripeCol == 0) {//white
                if (tempStripe.op(mPath, Path.Op.INTERSECT)) {
                    tempCanvas.drawPath(tempStripe, mPaint);
                }
            } else {//black
                tempStripe.op(mPath, Path.Op.DIFFERENCE);
                tempCanvas.drawPath(tempStripe, mPaint);
            }

            tmpStripeCol ^= 1;
        }

        Log.d(DEBUG_TAG, "finished with processing");

        if (null != mBitmap) {
            mBitmap.recycle();
        }
        mBitmap = tempBmp;

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

    public void linHorOpArtRandBitmap() {
        Log.d(DEBUG_TAG, "linHorOpArtBitmap");

        int bitmapWidth = mBitmap.getWidth();
        int bitmapHeight = mBitmap.getHeight();

        Log.d(DEBUG_TAG, "mBitmap dimensions =(" + bitmapWidth + ", " + bitmapHeight + ")");

        //create bitmap that we will draw on
        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap tempBmp = Bitmap.createBitmap(WIDTH_PX, HEIGHT_PX, conf); // this creates a MUTABLE bitmap
        Canvas tempCanvas = new Canvas(tempBmp);

        int tmpStripeCol = 0;
        int tmpDeltaY;
        for (int y = 0; y < bitmapHeight; y += tmpDeltaY) {
            tmpDeltaY = MyUtils.randomNumberInRange(mRandom, DELTAY_MIN_DP, DELTAY_MAX_DP);
//            Log.d(DEBUG_TAG, "tmpDeltaY=" + tmpDeltaY);

//            Log.d(DEBUG_TAG, "tmpStripeCol="+tmpStripeCol);
            Path tempStripe = createHorStripe(0, y, bitmapWidth, tmpDeltaY);

            if (tmpStripeCol == 0) {//white
                if (tempStripe.op(mPath, Path.Op.INTERSECT)) {
                    tempCanvas.drawPath(tempStripe, mPaint);
                }
            } else {//black
                tempStripe.op(mPath, Path.Op.DIFFERENCE);
                tempCanvas.drawPath(tempStripe, mPaint);
            }

            tmpStripeCol ^= 1;
        }

        Log.d(DEBUG_TAG, "finished with processing");

        if (null != mBitmap) {
            mBitmap.recycle();
        }
        mBitmap = tempBmp;

        //calling invalidate causes the component to draw itself
        invalidate();
    }

    public void linVertOpArtRandBitmap() {
        Log.d(DEBUG_TAG, "linHorOpArtBitmap");

        int bitmapWidth = mBitmap.getWidth();
        int bitmapHeight = mBitmap.getHeight();

        Log.d(DEBUG_TAG, "mBitmap dimensions =(" + bitmapWidth + ", " + bitmapHeight + ")");


        //create bitmap that we will draw on
        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap tempBmp = Bitmap.createBitmap(WIDTH_PX, HEIGHT_PX, conf); // this creates a MUTABLE bitmap
        Canvas tempCanvas = new Canvas(tempBmp);

        int tmpStripeCol = 0;
        int tmpDeltaX;
        for (int x = 0; x < bitmapWidth; x += tmpDeltaX) {
            tmpDeltaX = MyUtils.randomNumberInRange(mRandom, DELTAY_MIN_DP, DELTAY_MAX_DP);
//            Log.d(DEBUG_TAG, "tmpDeltaX=" + tmpDeltaX);

//            Log.d(DEBUG_TAG, "tmpStripeCol="+tmpStripeCol);
            Path tempStripe = createVertStripe(x, 0, bitmapHeight, tmpDeltaX);

            if (tmpStripeCol == 0) {//white
                if (tempStripe.op(mPath, Path.Op.INTERSECT)) {
                    tempCanvas.drawPath(tempStripe, mPaint);
                }
            } else {//black
                tempStripe.op(mPath, Path.Op.DIFFERENCE);
                tempCanvas.drawPath(tempStripe, mPaint);
            }

            tmpStripeCol ^= 1;
        }

        Log.d(DEBUG_TAG, "finished with processing");

        if (null != mBitmap) {
            mBitmap.recycle();
        }
        mBitmap = tempBmp;

        //calling invalidate causes the component to draw itself
        invalidate();
    }

    public void linHorOpArtRandBitmapAddToPath() {
        Log.d(DEBUG_TAG, "linHorOpArtBitmap");
        Path newLineartPath = new Path();

        int bitmapWidth = mBitmap.getWidth();
        int bitmapHeight = mBitmap.getHeight();

        Log.d(DEBUG_TAG, "mBitmap dimensions =(" + bitmapWidth + ", " + bitmapHeight + ")");

        //create bitmap that we will draw on
        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap tempBmp = Bitmap.createBitmap(WIDTH_PX, HEIGHT_PX, conf); // this creates a MUTABLE bitmap
        Canvas tempCanvas = new Canvas(tempBmp);

        int tmpStripeCol = 0;
        int tmpDeltaY;
        for (int y = 0; y < bitmapHeight; y += tmpDeltaY) {
            tmpDeltaY = MyUtils.randomNumberInRange(mRandom, DELTAY_MIN_DP, DELTAY_MAX_DP);
//            Log.d(DEBUG_TAG, "tmpDeltaY=" + tmpDeltaY);

//            Log.d(DEBUG_TAG, "tmpStripeCol="+tmpStripeCol);
            Path tempStripe = createHorStripe(0, y, bitmapWidth, tmpDeltaY);

            if (tmpStripeCol == 0) {//white
                if (tempStripe.op(mPath, Path.Op.INTERSECT)) {
                    tempCanvas.drawPath(tempStripe, mPaint);
                    newLineartPath.addPath(tempStripe);
                }
            } else {//black
                tempStripe.op(mPath, Path.Op.DIFFERENCE);
                tempCanvas.drawPath(tempStripe, mPaint);
                newLineartPath.addPath(tempStripe);
            }

            tmpStripeCol ^= 1;
        }

        Log.d(DEBUG_TAG, "finished with processing");

        if (null != mBitmap) {
            mBitmap.recycle();
        }
        mBitmap = tempBmp;
        mPath = newLineartPath;

        //calling invalidate causes the component to draw itself
        invalidate();
    }


    public void linHorOpArtRandBitmap(int y1, int y2) {
        Log.d(DEBUG_TAG, "linHorOpArtBitmap");

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

        //create bitmap that we will draw on
        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap tempBmp = Bitmap.createBitmap(WIDTH_PX, HEIGHT_PX, conf); // this creates a MUTABLE bitmap
        Canvas tempCanvas = new Canvas(tempBmp);

        int tmpStripeCol = 0;
        int tmpDeltaY;
        for (int y = minY; y < maxY; y += tmpDeltaY) {
            tmpDeltaY = MyUtils.randomNumberInRange(mRandom, DELTAY_MIN_DP, DELTAY_MAX_DP);
//            Log.d(DEBUG_TAG, "tmpDeltaY=" + tmpDeltaY);

//            Log.d(DEBUG_TAG, "tmpStripeCol="+tmpStripeCol);
            Path tempStripe = createHorStripe(0, y, bitmapWidth, tmpDeltaY);

            if (tmpStripeCol == 0) {//white
                if (tempStripe.op(mPath, Path.Op.INTERSECT)) {
                    tempCanvas.drawPath(tempStripe, mPaint);
                }
            } else {//black
                tempStripe.op(mPath, Path.Op.DIFFERENCE);
                tempCanvas.drawPath(tempStripe, mPaint);
            }

            tmpStripeCol ^= 1;
        }

        Log.d(DEBUG_TAG, "finished with processing");

        if (null != mBitmap) {
            mBitmap.recycle();
        }
        mBitmap = tempBmp;

        //calling invalidate causes the component to draw itself
        invalidate();
    }

    public void linVertOpArtRandBitmap(int x1, int x2) {
        Log.d(DEBUG_TAG, "linHorOpArtBitmap");

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

        //create bitmap that we will draw on
        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap tempBmp = Bitmap.createBitmap(WIDTH_PX, HEIGHT_PX, conf); // this creates a MUTABLE bitmap
        Canvas tempCanvas = new Canvas(tempBmp);

        int tmpStripeCol = 0;
        int tmpDeltaX;
        for (int x = minX; x < maxX; x += tmpDeltaX) {
            tmpDeltaX = MyUtils.randomNumberInRange(mRandom, DELTAY_MIN_DP, DELTAY_MAX_DP);
//            Log.d(DEBUG_TAG, "tmpDeltaX=" + tmpDeltaX);

//            Log.d(DEBUG_TAG, "tmpStripeCol="+tmpStripeCol);
            Path tempStripe = createVertStripe(x, 0, bitmapHeight, tmpDeltaX);

            if (tmpStripeCol == 0) {//white
                if (tempStripe.op(mPath, Path.Op.INTERSECT)) {
                    tempCanvas.drawPath(tempStripe, mPaint);
                }
            } else {//black
                tempStripe.op(mPath, Path.Op.DIFFERENCE);
                tempCanvas.drawPath(tempStripe, mPaint);
            }

            tmpStripeCol ^= 1;
        }

        Log.d(DEBUG_TAG, "finished with processing");

        if (null != mBitmap) {
            mBitmap.recycle();
        }
        mBitmap = tempBmp;

        //calling invalidate causes the component to draw itself
        invalidate();
    }

    public void drawLinOpArtBetweenPoints(Point t1, Point t2, MyUtils.MOVE_DIRECTION direction){
        switch (direction) {
            case HORIZONTAL:
                linVertOpArtRandBitmapAddToPath(t1.x,t2.x);
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

        Path selectedStripe = createHorStripe(0, minY, bitmapWidth, maxY);
        Path dontTouchedPath = new Path(mPath);
        dontTouchedPath.op(selectedStripe, Path.Op.DIFFERENCE);

        int tmpStripeCol = 0;
        int tmpDeltaY;
        for (int y = minY; y < maxY; y += tmpDeltaY) {
            tmpDeltaY = MyUtils.randomNumberInRange(mRandom, DELTAY_MIN_DP, DELTAY_MAX_DP);
//            Log.d(DEBUG_TAG, "tmpDeltaY=" + tmpDeltaY);

//            Log.d(DEBUG_TAG, "tmpStripeCol="+tmpStripeCol);
            Path tempStripe = createHorStripe(0, y, bitmapWidth, tmpDeltaY);

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

        Log.d(DEBUG_TAG, "finished with processing");

        //create bitmap that we will draw on
        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap tempBmp = Bitmap.createBitmap(WIDTH_PX, HEIGHT_PX, conf); // this creates a MUTABLE bitmap
        Canvas tempCanvas = new Canvas(tempBmp);

        mPath.set(dontTouchedPath);
        mPath.op(newLineartPath,Path.Op.UNION);
        tempCanvas.drawPath(mPath, mPaint);

        if (null != mBitmap) {
            mBitmap.recycle();
        }
        mBitmap = tempBmp;

//        //calling invalidate causes the component to draw itself
//        invalidate();
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

        Path selectedStripe = createHorStripe(minX, 0, bitmapHeight, maxX);
        Path dontTouchedPath = new Path(mPath);
        dontTouchedPath.op(selectedStripe, Path.Op.DIFFERENCE);

        int tmpStripeCol = 0;
        int tmpDeltaX;
        for (int x = minX; x < maxX; x += tmpDeltaX) {
            tmpDeltaX = MyUtils.randomNumberInRange(mRandom, DELTAY_MIN_DP, DELTAY_MAX_DP);
//            Log.d(DEBUG_TAG, "tmpDeltaX=" + tmpDeltaX);

//            Log.d(DEBUG_TAG, "tmpStripeCol="+tmpStripeCol);
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

        Log.d(DEBUG_TAG, "finished with processing");

        //create bitmap that we will draw on
        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap tempBmp = Bitmap.createBitmap(WIDTH_PX, HEIGHT_PX, conf); // this creates a MUTABLE bitmap
        Canvas tempCanvas = new Canvas(tempBmp);

        mPath.set(dontTouchedPath);
        mPath.op(newLineartPath,Path.Op.UNION);
        tempCanvas.drawPath(mPath, mPaint);

        if (null != mBitmap) {
            mBitmap.recycle();
        }
        mBitmap = tempBmp;

//        //calling invalidate causes the component to draw itself
//        invalidate();
    }

    public void exploreBitmapPixels() {
        Log.d(DEBUG_TAG, "exploreBitmapPixels");
        long startTime = System.currentTimeMillis();

        int bitmapWidth = mBitmap.getWidth();
        int bitmapHeight = mBitmap.getHeight();

        Log.d(DEBUG_TAG, "mBitmap dimensions =(" + bitmapWidth + ", " + bitmapHeight + ")");

        int[] pix = new int[bitmapWidth * bitmapHeight];
        mBitmap.getPixels(pix, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);

        int a, r, g, b, index;
        int newA, newR, newG, newB;

        for (int y = 0; y < bitmapHeight; y++) {
            for (int x = 0; x < bitmapWidth; x++) {
                index = y * bitmapWidth + x;
                a = (pix[index] >> 24) & 0xff;
                r = (pix[index] >> 16) & 0xff;
                g = (pix[index] >> 8) & 0xff;
                b = pix[index] & 0xff;


                if (a != 0 || r != 0 || g != 0 || b != 0) {
//                    Log.d(DEBUG_TAG, String.format("(a,r,g,b)=(%d,%d,%d,%d)", a, r, g, b));
                    newR = 255;
                } else {
                    newR = r;
                }

                newA = a;
                newG = g;
                newB = b;

//                pix[index] = 0xff000000 | (newR << 16) | (newG << 8) | newB;
                pix[index] = (newA << 24) | (newR << 16) | (newG << 8) | newB;
            }
        }

        Log.d(DEBUG_TAG, "finished with processing");

        Bitmap bm = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        bm.setPixels(pix, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);

        if (null != mBitmap) {
            mBitmap.recycle();
        }
        mBitmap = bm;

        long endTime = System.currentTimeMillis();
        Log.d(DEBUG_TAG, String.format("Duration is %d", endTime-startTime));

        //calling invalidate causes the component to draw itself
        invalidate();
    }

}
