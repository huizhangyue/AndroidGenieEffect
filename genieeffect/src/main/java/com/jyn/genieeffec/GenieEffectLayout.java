package com.jyn.genieeffec;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

/**
 * Created by jiao on 2019/7/24.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class GenieEffectLayout extends RelativeLayout {

    Context context;

    /**
     * 最大化的view
     */
    View maximizeView;

    /**
     * 最小化的view
     */
    View minimizeView;

    /**
     * 把目标view转后后得到的bitmap
     */
    Bitmap bitmap;

    Paint paint;

    MeshHelper mMeshHelper;

    /**
     * 最大化view的宽高值
     */
    int maximizeWidth;
    int maximizeHeight;

    /**
     * 最小化坐标位置
     */
    int anchorLeft;
    int anchorRight;

    boolean isStart;

    ValueAnimator valueAnimator;

    float posi;

    public GenieEffectLayout(Context context) {
        super(context);
        initLayout(context);
    }

    public GenieEffectLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
    }

    public GenieEffectLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context);
    }

    private void initLayout(Context context) {
        this.context = context;
        setBackground(null);
        setWillNotDraw(false);
        paint = new Paint();
        paint.setAntiAlias(true);
        valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setDuration(500);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(animation ->
                setPosi(animation.getAnimatedFraction()));
        valueAnimator.addListener(new AnimEndListener());
        mMeshHelper = new MeshHelper();
    }

    public GenieEffectLayout setMaximizeView(final View view) {
        maximizeView = view;
        return this;
    }

    public GenieEffectLayout setMinimizeView(View view) {
        minimizeView = view;
        return this;
    }

    public void start() {
        isStart = true;
        bitmap = loadBitmapFromView(maximizeView);
        maximizeWidth = bitmap.getWidth();
        maximizeHeight = bitmap.getHeight();
        anchorLeft = minimizeView.getLeft();
        anchorRight = minimizeView.getRight();

        mMeshHelper.init(getWidth(), getHeight());
        mMeshHelper.setBitmapDet(maximizeWidth, maximizeHeight);
        mMeshHelper.setAnchorDet(anchorLeft, anchorRight);

        invalidate();
        maximizeView.setVisibility(INVISIBLE);
        if (valueAnimator != null) {
            valueAnimator.start();
        }
    }

    public void setPosi(float posi) {
        this.posi = posi;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap == null || mMeshHelper == null || !isStart) return;
        float[] mesh = mMeshHelper.setPosiToFloats(posi);
//        canvas.drawBitmap(mBitmap, maximizeLeft, maximizeTop, mPaint);
        canvas.drawBitmapMesh(bitmap,
                mMeshHelper.getVetWidth(),
                mMeshHelper.getVetHeight()
                , mesh, 0, null, 0, paint);
    }


    private class AnimEndListener extends AnimatorListenerAdapter {

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            if (bitmap != null) {
                bitmap.recycle();
                bitmap = null;
            }
        }
    }

    /**
     * 把view转换成bitmap
     *
     * @param v 目标view
     * @return 转换后得到的bitmap
     */
    public Bitmap loadBitmapFromView(View v) {
        int height = v.getHeight();
        int width = v.getWidth();
        Log.i("main", "v height:" + v.getHeight());
        Log.i("main", "v width:" + v.getWidth());

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
//        c.drawColor(Color.WHITE);
        // 如果不设置canvas画布为白色，则生成透明
        v.layout(v.getLeft(), v.getTop(), width, height);
        v.draw(c);
        return bmp;
    }
}