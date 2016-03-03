package com.midoconline.app.Util.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.midoconline.app.R;

/**
 * Created by Prashant on 10/8/15.
 */
public class CircularNetworkImageView extends NetworkImageView {

    private Context mContext;
    private Bitmap mLocalBitmap;
    private boolean mShowLocal = false;

    public CircularNetworkImageView(Context context) {
        super(context);
        mContext = context;

    }

    public CircularNetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Convert bitmap to rounded image
     *
     * @param bmp    input bitmap
     * @param radius of output bitmap
     * @return rounded Bitmap image
     */
    public static Bitmap getCircularBitmap(Bitmap bmp, int radius) {
        if (bmp != null) {
            Bitmap sbmp;

            if (bmp.getWidth() != radius || bmp.getHeight() != radius) {
                float smallest = Math.min(bmp.getWidth(), bmp.getHeight());
                float factor = smallest / radius;
                sbmp = Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() / factor), (int) (bmp.getHeight() / factor), false);
            } else {
                sbmp = bmp;
            }

            Bitmap output = Bitmap.createBitmap(radius, radius,
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);


            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, radius, radius);

            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setDither(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.parseColor("#BAB399"));
            canvas.drawCircle(radius / (float) 2 + 0.7f,
                    radius / (float) 2 + 0.7f, radius / (float) 2 + 0.1f, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(sbmp, rect, rect, paint);
            return output;
        }
        return Bitmap.createBitmap(20, 20, Bitmap.Config.ALPHA_8);

    }

    @Override
    public void setImageUrl(String url, ImageLoader imageLoader) {
        mShowLocal = false;
        super.setImageUrl(url, imageLoader);
    }


    public void setLocalImageBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            mShowLocal = true;
        }
        this.mLocalBitmap = bitmap;
        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        super.onLayout(changed, left, top, right, bottom);
        if (mShowLocal) {
            setImageBitmap(mLocalBitmap);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bitmap = null;
        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        if (b != null) {
            bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
        } else {
            if (mContext != null) {
                Drawable img = ContextCompat.getDrawable(mContext, R.drawable.profile);
                bitmap = Bitmap.createBitmap(img.getIntrinsicWidth(), img.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }
        }
        int w = getWidth();
        Bitmap roundBitmap = getCircularBitmap(bitmap, w);
        canvas.drawBitmap(roundBitmap, 0, 0, null);
    }
}
