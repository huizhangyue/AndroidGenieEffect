package com.jyn.genieeffec;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
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
     * 最大化View的位置坐标
     */
//    int maximizeLeft;
//    int maximizeTop;

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

    /**
     * 横纵方向网格数
     */
//    int meshWidth = 20;
//    int meshHeight = 20;

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
    }

    public GenieEffectLayout setMaximizeView(final View view) {
        maximizeView = view;
//        view.post(() -> {
//            maximizeLeft = view.getLeft();
//            maximizeTop = view.getTop();
//        });
        return this;
    }

    public GenieEffectLayout setMinimizeView(View view) {
        minimizeView = view;
        return this;
    }

    public void init() {
        this.post(() -> {
            bitmap = loadBitmapFromView(maximizeView);

            //获取bitmap的宽高
            maximizeWidth = bitmap.getWidth();
            maximizeHeight = bitmap.getHeight();

            //获取锚点位置
            anchorLeft = minimizeView.getLeft();
            anchorRight = minimizeView.getRight();

            mMeshHelper = new MeshHelper();
            mMeshHelper.init(getWidth(), getHeight());
            mMeshHelper.setBitmapDet(maximizeWidth, maximizeHeight);
            mMeshHelper.setAnchorDet(anchorLeft, anchorRight);
        });

        paint = new Paint();
        paint.setAntiAlias(true);
        valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setDuration(8000);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(animation ->
                setPosi(animation.getAnimatedFraction()));
    }

    public void start() {
        isStart = true;
        invalidate();
        maximizeView.setVisibility(GONE);
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
        c.drawColor(Color.WHITE);
        // 如果不设置canvas画布为白色，则生成透明
        v.layout(v.getLeft(), v.getTop(), height, width);
        v.draw(c);
        return bmp;
    }
}