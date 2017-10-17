package com.tylersuehr.bubblesexample.widgets;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import com.tylersuehr.bubblesexample.R;
/**
 * Copyright 2017 Tyler Suehr
 * Created by tyler on 6/9/2017.
 *
 * <b>Summary</b>
 * This view will draw a circle with a border around it and then draw text in the center. This
 * is intended to be used as an 'extra' count for when a ViewGroup or something has too many
 * views, but still needs to display a count or extra data.
 *
 * <b>Important</b>
 * {@link #onMeasure(int, int)} dynamically measures our text size, so we do NOT need to set
 * a text size in the constructor or by using accessors.
 *
 * <b>Immutable Properties</b>
 * {@link #circleRadius} stores the radius based on the needed size.
 * {@link #viewSize} stores the smallest size of the view's dimensions.
 */
public class CircleCountView extends View {
    private final Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int borderWidth; // Width of the border around image
    private int borderColor; // Color of the border around image
    private int backColor; // Color of the area that's not the border
    private int circleRadius;
    private int viewSize;

    private int textColor;
    private Typeface typeface;
    private CharSequence text;


    public CircleCountView(Context context) {
        this(context, null);
    }

    public CircleCountView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleCountView(Context c, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(c, attrs, defStyleAttr);
        DisplayMetrics dm = getResources().getDisplayMetrics();

        // Set XML attributes
        TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.CircleCountView);
        this.backColor = a.getColor(R.styleable.CircleCountView_circleColor, ContextCompat.getColor(c, R.color.grey_600)); // Grey 600
        this.borderColor = a.getColor(R.styleable.CircleCountView_borderColor, ContextCompat.getColor(c, R.color.colorPrimary)); // Primary color
        this.borderWidth = a.getDimensionPixelSize(R.styleable.CircleCountView_borderWidth, (int)(1f * dm.density)); // 1dp
        this.textColor = a.getColor(R.styleable.CircleCountView_android_textColor, ContextCompat.getColor(c, R.color.grey_50)); // Grey 50
        this.typeface =  FontCache.create(c, a.getInt(R.styleable.CircleCountView_font, 2)); // Default MEDIUM
        this.text = a.hasValue(R.styleable.CircleCountView_android_text) ? "+" + a.getString(R.styleable.CircleCountView_android_text) : "";
        a.recycle();

        // Setup the circle paint
        this.circlePaint.setStyle(Paint.Style.FILL);
        this.circlePaint.setColor(borderColor);

        // Setup the text paint
        this.textPaint.setColor(textColor);
        this.textPaint.setTypeface(typeface);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        float scaledDensity = dm.scaledDensity;
        float density = dm.density;

        // If 'wrap_content', then make it a default size
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = (int)(40f * density); // 40dp default
        }

        // Dynamically measure our text to fit inside the circle with reasonable padding.
        // Do this by taking the measured width and removing the density from it, halving
        // that value, and then multiplying the scaled density to it for text.
        // (Ex: 40dp -> 40px -> 20px -> 20sp)
        int length = text.length();
        float textSize = (widthSize / density / length) * scaledDensity;
        this.textPaint.setTextSize(textSize);

        // Make the width and height the same ALWAYS!
        setMeasuredDimension(widthSize, widthSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Get the smallest size from the canvas (length or width)
        int viewWidth = getMeasuredWidth();
        int viewHeight = getMeasuredHeight();
        this.viewSize = Math.min(viewWidth, viewHeight);

        // Using that small size, calculate the exact center of the circle
        this.circleRadius = (viewSize - (borderWidth * 2)) / 2;

        // Maximize available border size
        if (viewSize == 0) { return; }
        if (viewSize / 3 < borderWidth) {
            this.borderWidth = viewSize / 3;
        }

        // Draw the border circle
        this.circlePaint.setColor(borderColor);
        int fullRadius = circleRadius + borderWidth;
        canvas.drawCircle(fullRadius, fullRadius, fullRadius, circlePaint); // Border circle

        // Draw the back circle
        this.circlePaint.setColor(backColor);
        canvas.drawCircle(fullRadius, fullRadius, circleRadius, circlePaint); // Back circle

        // Draw the text in the exact center of the circle
        float textWidth = textPaint.measureText(text, 0, text.length());
        float dx = (viewWidth >> 1) - ((int)textWidth >> 1); // Exact center X
        float dy = (viewHeight >> 1) - ((int)(textPaint.descent() + textPaint.ascent()) >> 1) - 1; // Exact center Y
        canvas.drawText(text, 0, text.length(), dx, dy, textPaint);
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        invalidate();
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        invalidate();
    }

    public int getBackColor() {
        return backColor;
    }

    public void setBackColor(int backColor) {
        this.backColor = backColor;
        invalidate();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        this.textPaint.setColor(textColor);
        invalidate();
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
        this.textPaint.setTypeface(typeface);
        invalidate();
    }

    public CharSequence getText() {
        return text;
    }

    public void setText(CharSequence text) {
        this.text = text;
        invalidate();
    }

    public void setCount(int extraCount) {
        this.text = "+" + extraCount;
        requestLayout();
        invalidate();
    }
}