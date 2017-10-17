package com.tylersuehr.bubbles;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public class BubbleLayout extends ViewGroup {
    /* Size of each bubble */
    private int bubbleSize;
    /* Distance each bubble is from each other */
    private int bubbleOffset;
    /* Amount of bubbles showing before count is shown */
    private int bubblePeek;
    /* Border color of each bubble */
    private int bubbleBorderColor;
    /* Border width of each bubble */
    private int bubbleBorderWidth;
    /* Space in between each bubble (only used when no offsets) */
    private int bubbleMargin;
    /* Whether or not to offset each bubble */
    private boolean useOffset;
    /* Stores count of excess bubbles (used when peek exceeded) */
    private int excess = 0;


    public BubbleLayout(Context context) {
        this(context, null);
    }

    public BubbleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleLayout(Context c, AttributeSet attrs, int defStyleAttr) {
        super(c, attrs, defStyleAttr);

        // Create default sizes from density
        final float density = getResources().getDisplayMetrics().density;
        final int DEFAULT_BUBBLE_SIZE = (int)(40f * density);
        final int DEFAULT_BUBBLE_MARGIN = (int)(4f * density);
        final int DEFAULT_BORDER_WIDTH = (int)(1f * density);
        final int DEFAULT_BUBBLE_OFFSET = 2; // 1/2 of each bubble
        final int DEFAULT_BUBBLE_PEEK = 4;

        // Set XML attributes
        TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.BubbleLayout);
        this.bubbleSize = a.getDimensionPixelSize(R.styleable.BubbleLayout_bubbleSize, DEFAULT_BUBBLE_SIZE);
        this.bubbleOffset = bubbleSize / (a.getInt(R.styleable.BubbleLayout_bubbleOffset, DEFAULT_BUBBLE_OFFSET));
        this.bubblePeek = a.getInt(R.styleable.BubbleLayout_bubblePeek, DEFAULT_BUBBLE_PEEK);
        this.bubbleBorderWidth = a.getDimensionPixelSize(R.styleable.BubbleLayout_borderWidth, DEFAULT_BORDER_WIDTH);
        this.bubbleBorderColor = a.getColor(R.styleable.BubbleLayout_borderColor, ContextCompat.getColor(c, R.color.default_circle_border_color));
        this.bubbleMargin = a.getDimensionPixelSize(R.styleable.BubbleLayout_bubbleMargin, DEFAULT_BUBBLE_MARGIN);
        this.useOffset = a.getBoolean(R.styleable.BubbleLayout_useBubbleOffset, true);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Respect the mode of the width
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize;
        switch (widthMode) {
            // The width should be just big enough to show all bubbles!
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                if (useOffset) {
                    // Only calculate half the bubble size because we're offsetting them
                    widthSize = (bubbleSize * getChildCount());
                    widthSize -= (bubbleOffset * (getChildCount() - 1));
                } else {
                    // Use the full width of all bubbles
                    widthSize = (bubbleSize * getChildCount());

                    // Take into account bubble margin since we're not offsetting
                    // No bubble margin on the last bubble!
                    widthSize += (bubbleMargin * (getChildCount() - 1));
                }
                break;

            // Just take the given width
            default:
            case MeasureSpec.EXACTLY:
                widthSize = MeasureSpec.getSize(widthMeasureSpec);
                break;
        }

        // Each child will be the specified bubble size
        int childSpec = MeasureSpec.makeMeasureSpec(bubbleSize, MeasureSpec.EXACTLY);
        measureChildren(childSpec, childSpec);

        // The height will just be the bubble size
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), childSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left, right;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            if (useOffset) {
                // Offset the children after the 0 position
                left = i * ((i > 0) ? (child.getMeasuredWidth() - bubbleOffset) : child.getMeasuredWidth());
                right = left + child.getMeasuredWidth();
            } else {
                // Adjust for the bubble margin because we're not offsetting
                left = i * (child.getMeasuredWidth() + bubbleMargin);
                right = left + child.getMeasuredWidth() + bubbleMargin;
            }

            child.layout(left, 0, right, child.getMeasuredHeight());
        }
    }

    /**
     * Ensure that only {@link CircleImageView} and {@link CircleCountView} are added to
     * this ViewGroup.
     */
    @Override
    public void onViewAdded(View child) {
        if (!(child instanceof CircleImageView || child instanceof CircleCountView)) {
            throw new IllegalArgumentException("View must be either CircleImageView or CircleCountView!");
        }
    }

    /**
     * Adds a bubble using the given Drawable resource.
     * @param res {@link DrawableRes}
     */
    public void addBubble(@DrawableRes int res) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), res);
        addBubble(drawable);
    }

    /**
     * Adds a bubble using the given Bitmap.
     * @param bitmap {@link Bitmap}
     */
    public void addBubble(Bitmap bitmap) {
        BitmapDrawable bpd = new BitmapDrawable(getResources(), bitmap);
        addBubble(bpd);
    }

    /**
     * Adds a bubble using the given Drawable.
     * @param drawable {@link Drawable}
     */
    public void addBubble(Drawable drawable) {
        // If the child count exceeds the peek, just add a CircleCountView
        if (getChildCount() >= bubblePeek) {
            this.excess++;

            // Re-use the CircleCountView if possible
            CircleCountView countView;
            if (excess > 1) {
                countView = (CircleCountView)getChildAt(bubblePeek);
            } else {
                // Instantiate a new CircleCountView to use
                countView = createThemedCount();
                addView(countView);
            }
            countView.setCount(excess);
        } else {
            // Add a new CircleImageView to the ViewGroup
            addView(createThemedImage(drawable));
        }
    }

    /**
     * Reset the excess count and remove all bubbles from this ViewGroup.
     */
    public void clearBubbles() {
        this.excess = 0;
        removeAllViews();
    }

    private CircleCountView createThemedCount() {
        CircleCountView count = new CircleCountView(getContext());
        count.setLayoutParams(new LayoutParams(bubbleSize, bubbleSize));
        count.setBorderColor(bubbleBorderColor);
        count.setBorderWidth(bubbleBorderWidth);
        return count;
    }

    private CircleImageView createThemedImage(Drawable dr) {
        CircleImageView image = new CircleImageView(getContext());
        image.setLayoutParams(new LayoutParams(bubbleSize, bubbleSize));
        image.setBorderColor(bubbleBorderColor);
        image.setBorderWidth(bubbleBorderWidth);
        image.setImageDrawable(dr);
        return image;
    }
}