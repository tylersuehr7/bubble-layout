package com.tylersuehr.bubblesexample.widgets;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import com.tylersuehr.bubblesexample.R;
/**
 * Copyright 2017 Tyler Suehr
 * Created by tyler on 4/28/2017.
 *
 * This is a normal image view, but crops the image to be circular and includes a
 * border as well.
 */
public class CircleImageViewLegacy extends AppCompatImageView {
    private final Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Rect circleRect = new Rect();

    private int borderColor;
    private int backColor;
    private int circleRadius;
    private int borderWidth;
    private int viewSize;


    public CircleImageViewLegacy(Context context) {
        this(context, null);
    }

    public CircleImageViewLegacy(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageViewLegacy(Context c, AttributeSet attrs, int defStyleAttr) {
        super(c, attrs, defStyleAttr);
        DisplayMetrics dm = getResources().getDisplayMetrics();

        // Setup defaults
        TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
        this.borderColor = a.getColor(R.styleable.CircleImageView_borderColor, ContextCompat.getColor(c, R.color.colorPrimary)); // Primary color
        this.borderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_borderWidth, (int)(2f * dm.density)); // 2dp
        this.backColor = ContextCompat.getColor(c, R.color.grey_600); // Grey 600
        a.recycle();

        // Setup border paint
        borderPaint.setStyle(Paint.Style.FILL);
        borderPaint.setColor(borderColor);

        // Setup main paint
        mainPaint.setColor(Color.parseColor("#FAFAFA")); // Grey 50
        mainPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    }

    /**
     * Double check on actual project to make sure this method doesn't fuck with
     * any other views.
     * @param widthMeasureSpec int
     * @param heightMeasureSpec int
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isInEditMode()) {
            super.onDraw(canvas);
            return;
        }

        int viewWidth = canvas.getWidth();
        int viewHeight = canvas.getHeight();
        viewSize = Math.min(viewWidth, viewHeight);

        int circleCenterX = (viewWidth - viewSize) / 2;
        int circleCenterY = (viewHeight - viewSize) / 2;
        circleRadius = (viewSize - (borderWidth * 2)) / 2;
        circleRect.set(0, 0, viewSize, viewSize);

        // Maximize available border size
        if (viewSize / 3 < borderWidth) {
            borderWidth = viewSize / 3;
        }

        if (viewSize == 0) {
            return;
        }

        Drawable drawable = getDrawable();
        Bitmap bitmap = cutIntoCircle(drawableToBitmap(drawable));
        if (bitmap == null) {
            return;
        }

        // Draw the border circle
        borderPaint.setColor(borderColor);
        int radius = circleRadius + borderWidth;
        canvas.translate(circleCenterX, circleCenterY);
        canvas.drawCircle(radius, radius, radius, borderPaint); // Draw the border circle

        // Draw the back circle
        borderPaint.setColor(backColor);
        canvas.drawCircle(radius, radius, circleRadius, borderPaint);

        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    private Bitmap drawableToBitmap(Drawable drawable) { // OutOfMemory Exception
        if (drawable == null) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(viewSize, viewSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, viewSize, viewSize);
        drawable.draw(canvas);

        return bitmap;
    }

    private Bitmap cutIntoCircle(Bitmap bitmap) { // OutOfMemory Exception
        if (bitmap == null) {
            return null;
        }

        Bitmap output = Bitmap.createBitmap(viewSize, viewSize, Bitmap.Config.ARGB_8888);

        int radius = circleRadius + borderWidth;
        Canvas canvas = new Canvas(output);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(radius, radius, circleRadius, borderPaint);
        canvas.drawBitmap(bitmap, circleRect, circleRect, mainPaint);

        return output;
    }
}