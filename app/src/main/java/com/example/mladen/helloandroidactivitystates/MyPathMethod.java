package com.example.mladen.helloandroidactivitystates;

import android.graphics.Path;

/**
 * Created by Mladen on 23.11.2014..
 */
public class MyPathMethod {
    public static final int 	SEG_CLOSE 	= 4;
    public static final int 	SEG_CUBICTO 	=3;
    public static final int 	SEG_LINETO 	=1;
    public static final int 	SEG_MOVETO 	=0;
    public static final int 	SEG_QUADTO 	=2;
//    public static final int 	WIND_EVEN_ODD 	=0;
//    public static final int 	WIND_NON_ZERO 	=1;

    private int methodId;
    private float[] paramArr;

    public MyPathMethod(int methodId, float[] paramArr) {
        this.methodId = methodId;
        this.paramArr = paramArr;
    }

    public MyPathMethod(int methodId) {
        this.methodId = methodId;
    }

    public int getMethodName() {
        return methodId;
    }

    public void setMethodName(int methodId) {
        this.methodId = methodId;
    }

    public float[] getParamArr() {
        return paramArr;
    }

    public void setParamArr(float[] paramArr) {
        this.paramArr = paramArr;
    }

    @Override
    public String toString() {

        if(paramArr !=null){
            StringBuilder strBuilder = new StringBuilder();

            for(double item:paramArr){
                strBuilder.append(String.format("%f ", item));
            }
            return "MyPathMethod{" + "methodId=" + methodId + ", paramArr=" + strBuilder.toString() + '}';
        }

        return "MyPathMethod{" + "methodId=" + methodId  + '}';

    }

    public void drawWithMyPathMethod(Path pathToDraw){
        switch (methodId) {
            case SEG_MOVETO:
//                System.out.println("---> move to " + paramArr[0] + ", " + paramArr[1]);
                pathToDraw.moveTo(paramArr[0], paramArr[1]);
                break;
            case SEG_LINETO:
//                System.out.println("---> line to " + paramArr[0] + ", " + paramArr[1]);
                pathToDraw.lineTo(paramArr[0], paramArr[1]);
                break;
            case SEG_QUADTO:
//                System.out.println("---> quadratic to "
//                        + paramArr[0] + ", " + paramArr[1] + ", "
//                        + paramArr[2] + ", " + paramArr[3]);
                pathToDraw.quadTo(paramArr[0], paramArr[1], paramArr[2], paramArr[3]);
                break;
            case SEG_CUBICTO:
//                System.out.println("---> cubic to "
//                        + paramArr[0] + ", " + paramArr[1] + ", "
//                        + paramArr[2] + ", " + paramArr[3] + ", "
//                        + paramArr[4] + ", " + paramArr[5]);
                pathToDraw.cubicTo(paramArr[0], paramArr[1], paramArr[2], paramArr[3], paramArr[4], paramArr[5]);
                break;
            case SEG_CLOSE:
//                System.out.println("---> close");
                pathToDraw.close();
                break;
        }
    }
}
