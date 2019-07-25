package com.jyn.genieeffec;

import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.Nullable;

/**
 * Created by linzx on 17-7-27.
 * 供给CaptureAnimView的支持类,核心数据计算均在此处
 */

public class MeshHelper {
    /**
     * 容器的宽高值
     */
//    float width;
    float height;


    /**
     * 宽高值
     */
    float mapWidth;
    float mapHeight;

    /**
     * 最小化位置 设置两个锚点
     */
    int anchorLeft;
    int anchorRight;

    /**
     * 横向分格数
     */
    private final int WIDTH_DET = 20;

    /**
     * 纵向分格数
     */
    private final int HEIGHT_DET = 20;

    /**
     * 插值器
     */
    private AccelerateInterpolator mInterpolator = new AccelerateInterpolator();

    public void init(@Nullable int width, @Nullable int height) {
//        this.width = width;
        this.height = height;
    }

    /**
     * 设置bitmap宽高值
     *
     * @param mapWidth  bitmap 宽
     * @param mapHeight bitmap 高
     */
    public void setBitmapDet(int mapWidth, int mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    public void setAnchorDet(int anchorLeft, int anchorRight) {
        this.anchorLeft = anchorLeft;
        this.anchorRight = anchorRight;
    }

    /**
     * | ------------------------------
     * | |                          ||
     * | \                         / |
     * |  |                       /  |
     * |  |                      /   |
     * |  | leftLine            /    |
     * |  \                    /     |
     * |   |                  /      |
     * |   |                 /       |
     * |   |                /        |
     * |   |               /         |
     * |   \              /          |
     * |    |            /rightLine  |
     * |    |           /            |
     * |    |          /
     * | 0.1f        0.3f
     * |
     *
     * @param posi
     * @return
     */
    public float[] setPosiToFloats(float posi) {

        //左右边线运动轨迹
        LinePosi leftLine;
        LinePosi rightLine;

        //靠拢阙值
        float threshold = 0.2f;

        //在0~0.3f的部分,左右轨迹要逐渐向中心靠拢
        if (posi <= threshold) {
            //轨迹差值
//            float diff = posi / threshold * posi / threshold;
            float diff = Math.abs(posi / threshold);
            leftLine = new LinePosi(0, anchorLeft * diff, 0, height);
            rightLine = new LinePosi(mapWidth, anchorRight + (mapWidth - anchorRight) * (1 - diff), 0, height);
        } else {
            //在0.3f~1f,左右轨迹保持不变,图像按照此轨迹作为边界进行运动
            leftLine = new LinePosi(0, anchorLeft, 0, height);
            rightLine = new LinePosi(mapWidth, anchorRight, 0, height);
        }

        float destY = height * posi;


        float[] newFloat = new float[(WIDTH_DET + 1) * (HEIGHT_DET + 1) * 2];

        int num = 0;

        for (int i = 0; i <= HEIGHT_DET; i++) {
            for (int j = 0; j <= WIDTH_DET; j++) {

                //Y轴点位根据实际bitmap高度进行平均
                float posiY = destY + mapHeight * i / HEIGHT_DET;

                float leftPosiX = leftLine.inputY(posiY);
                float rightPosiX = rightLine.inputY(posiY);
                float disPosiX = rightPosiX - leftPosiX;

                //X点位根据两个line在Y值时的差值进行平均分布
                float posiX = disPosiX * j / WIDTH_DET + leftPosiX;

                //先X后Y输出
                newFloat[num] = posiX;
                num++;
                if (posiY > height) {
                    posiY = height;
                }
                newFloat[num] = posiY;
                num++;
            }
        }

        return newFloat;
    }

    public int getVetWidth() {
        return WIDTH_DET;
    }

    public int getVetHeight() {
        return HEIGHT_DET;
    }

    /**
     * 描述运动轨迹(起点XY和终点XY以及变函数Interpolator),根据输入量,可输出相对应的值
     */
    private class LinePosi {
        private float fromX, toX, fromY, toY;

        LinePosi(float fromX, float toX, float fromY, float toY) {
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
        }

        public float inputY(float yP) {
            float disLength = toY - fromY;
            float disXLength = toX - fromX;
            float disFloat = yP / disLength;

            float disXFloat = mInterpolator.getInterpolation(disFloat);
            float merage = disXLength * disXFloat;
            return fromX + merage;
        }
    }
}

