package com.example.mladen.helloandroidactivitystates;

import android.graphics.Point;

import java.util.Random;

/**
 * Created by Mladen on 12.11.2014..
 */
public class MyUtils {
    public enum MOVE_DIRECTION {
        HORIZONTAL,
        VERTICAL,
        SLOPE
    }

    final static private double HOR_DIRECTION_ALPHA_END_LIMIT = Math.PI / 6;
    final static private double VERT_DIRECTION_ALPHA_START_LIMIT = Math.PI / 3;

    public static int randomNumberInRange(Random random, int minX, int maxX) {
        return minX + random.nextInt(maxX - minX + 1);
    }

    public static int randomValueFromArray(Random random, int[] arr) {
        return arr[randomNumberInRange(random, 0, arr.length - 1)];
    }

    public static MOVE_DIRECTION getMoveDirection(Point t1, Point t2) {
        double deltaX = t2.x - t1.x;
        double deltaY = t2.y - t1.y;

        if (deltaX == 0) return MOVE_DIRECTION.VERTICAL;

        double alpha = Math.abs(Math.atan(deltaY / deltaX));

        if (alpha >= 0 && alpha < HOR_DIRECTION_ALPHA_END_LIMIT) {
            return MOVE_DIRECTION.HORIZONTAL;
        } else if (alpha >= HOR_DIRECTION_ALPHA_END_LIMIT && alpha < VERT_DIRECTION_ALPHA_START_LIMIT) {
            return MOVE_DIRECTION.SLOPE;
        } else {
            return MOVE_DIRECTION.VERTICAL;
        }
    }

    public static MOVE_DIRECTION getMoveDirection(Point t1, Point t2, int minMoveDistance) {
        double deltaX = t2.x - t1.x;
        double deltaY = t2.y - t1.y;

        if (Math.abs(deltaX) < minMoveDistance && Math.abs(deltaY) < minMoveDistance) {
            return null;
        }

        if (deltaX == 0) return MOVE_DIRECTION.VERTICAL;

        double alpha = Math.abs(Math.atan(deltaY / deltaX));

        if (alpha >= 0 && alpha < HOR_DIRECTION_ALPHA_END_LIMIT) {
            return MOVE_DIRECTION.HORIZONTAL;
        } else if (alpha >= HOR_DIRECTION_ALPHA_END_LIMIT && alpha < VERT_DIRECTION_ALPHA_START_LIMIT) {
            return MOVE_DIRECTION.SLOPE;
        } else {
            return MOVE_DIRECTION.VERTICAL;
        }
    }
}
