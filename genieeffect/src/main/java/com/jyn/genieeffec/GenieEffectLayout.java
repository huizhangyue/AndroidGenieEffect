package com.jyn.genieeffec;

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
    Bitmap mBitmap;

    Paint mPaint;

    MeshHelper mMeshHelper;

    /**
     * 最大化View的位置坐标
     */
    int maximizeLeft;
    int maximizeTop;
    int maximizeRight;
    int maximizeBottom;

    /**
     * 最大化view的宽高值
     */
    int maximizeWidth;
    int maximizeHeight;

    boolean isStart;

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
        view.post(() -> {
            maximizeLeft = view.getLeft();
            maximizeTop = view.getTop();
        });
        return this;
    }

    public GenieEffectLayout setMinimizeView(View view) {
        minimizeView = view;
        return this;
    }

    public void init() {
        this.post(() -> {
            mBitmap = loadBitmapFromView(maximizeView);
            maximizeWidth = mBitmap.getWidth();
            maximizeHeight = mBitmap.getHeight();
            Log.i("main", "maximizeWidth:" + maximizeWidth);
            Log.i("main", "maximizeHeight:" + maximizeHeight);
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mMeshHelper = new MeshHelper();
        });
    }

    public void start() {
        isStart = true;
        invalidate();
        maximizeView.setVisibility(GONE);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isStart) {
            canvas.drawBitmap(mBitmap, maximizeLeft, maximizeTop, mPaint);
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
        c.drawColor(Color.WHITE);
        // 如果不设置canvas画布为白色，则生成透明
        v.layout(v.getLeft(), v.getTop(), height, width);
        v.draw(c);
        return bmp;
    }
}