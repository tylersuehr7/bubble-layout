package com.tylersuehr.bubblesexample.widgets;
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
import com.tylersuehr.bubblesexample.R;
/**
 * Copyright 2017 Tyler Suehr
 * Created by tyler on 6/9/2017.
 */
public class BubbleLayout extends ViewGroup {
    private final BubbleViewPool bubblePool;
    private final BubbleParams params;
    private int excess = 0;


    public BubbleLayout(Context context) {
        this(context, null);
    }

    public BubbleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleLayout(Context c, AttributeSet attrs, int defStyleAttr) {
        super(c, attrs, defStyleAttr);
        this.params = new BubbleParams(c, attrs);
        this.bubblePool = new BubbleViewPool(c, params);
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
                if (params.useOffset) {
                    // Only calculate half the bubble size because we're offsetting them
                    widthSize = (params.bubbleSize * getChildCount());
                    widthSize -= (params.bubbleOffset * (getChildCount() - 1));
                } else {
                    // Use the full width of all bubbles
                    widthSize = (params.bubbleSize * getChildCount());

                    // Take into account bubble margin since we're not offsetting
                    // No bubble margin on the last bubble!
                    widthSize += (params.bubbleMargin * (getChildCount() - 1));
                }
                break;

            // Just take the given width
            default:
            case MeasureSpec.EXACTLY:
                widthSize = MeasureSpec.getSize(widthMeasureSpec);
                break;
        }

        // Each child will be the specified bubble size
        int childSpec = MeasureSpec.makeMeasureSpec(params.bubbleSize, MeasureSpec.EXACTLY);
        measureChildren(childSpec, childSpec);

        // The height will just be the bubble size
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), childSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left, right;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            if (params.useOffset) {
                // Offset the children after the 0 position
                left = i * ((i > 0) ? (child.getMeasuredWidth() - params.bubbleOffset) : child.getMeasuredWidth());
                right = left + child.getMeasuredWidth();
            } else {
                // Adjust for the bubble margin because we're not offsetting
                left = i * (child.getMeasuredWidth() + params.bubbleMargin);
                right = left + child.getMeasuredWidth() + params.bubbleMargin;
            }

            child.layout(left, 0, right, child.getMeasuredHeight());
        }
    }

    /**
     * We want to make sure that the only views being added to this ViewGroup are only
     * {@link CircleImageView} or {@link CircleCountView}.
     */
    @Override
    public void onViewAdded(View child) {
        if (!(child instanceof CircleImageView || child instanceof CircleCountView)) {
            throw new IllegalArgumentException("View must be either CircleImageView or CircleCountView!");
        }
    }

    /**
     * Before calling the super class method, we want to put all the CircleImageViews
     * back into the {@link #bubblePool}.
     */
    @Override
    public void removeAllViews() {
        // Put the CircleImageViews back in the pool
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof CircleImageView) {
                this.bubblePool.checkIn((CircleImageView)child);
            }
        }
        super.removeAllViews();
    }

    /**
     * Adds a bubble using the given Drawable.
     * @param drawable {@link Drawable}
     */
    public void addBubble(Drawable drawable) {
        // If the child count exceeds the peek, just add a CircleCountView
        if (getChildCount() >= params.bubblePeek) {
            this.excess++;

            // Re-use the CircleCountView if possible
            CircleCountView countView;
            if (excess > 1) {
                countView = (CircleCountView)getChildAt(params.bubblePeek);
            } else {
                // Instantiate a new CircleCountView to use
                countView = new CircleCountView(getContext());
                countView.setLayoutParams(new LayoutParams(params.bubbleSize, params.bubbleSize));
                countView.setBorderColor(params.bubbleBorderColor);
                countView.setBorderWidth(params.bubbleBorderWidth);
                addView(countView);
            }
            countView.setCount(excess);
        } else {
            // Use our bubble pool to get a CircleImageView
            CircleImageView bubbleView = bubblePool.checkOut();
            bubbleView.setImageDrawable(drawable);
            addView(bubbleView);
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
     * Adds a bubble using the given Bitmap and creating a BitmapDrawable.
     * @param bitmap {@link Bitmap}
     */
    public void addBubble(Bitmap bitmap) {
        BitmapDrawable bpd = new BitmapDrawable(getResources(), bitmap);
        addBubble(bpd);
    }

    /**
     * Reset the excess count and remove all bubbles from this ViewGroup.
     */
    public void clearBubbles() {
        this.excess = 0;
        removeAllViews();
    }


    private class BubbleParams {
        private final float DENSITY = getResources().getDisplayMetrics().density;
        private final int DEFAULT_BUBBLE_SIZE = (int)(40f * DENSITY);
        private final int DEFAULT_BUBBLE_MARGIN = (int)(4f * DENSITY);
        private final int DEFAULT_BUBBLE_OFFSET = 2; // 1/2 of each bubble
        private final int DEFAULT_BUBBLE_PEEK = 4;
        private final int DEFAULT_BORDER_WIDTH = (int)(1f * DENSITY);

        int bubbleSize; // Size of each bubble
        int bubbleOffset; // The distance away each bubble is from each other
        int bubblePeek; // The amount of bubbles showing before the count is displayed
        int bubbleBorderColor; // The border color of each bubble
        int bubbleBorderWidth; // The border width of each bubble
        int bubbleMargin; // The space in between each bubble (only used if not using offsets)
        boolean useOffset; // Whether or not to offset each bubble

        private BubbleParams(Context c, AttributeSet attrs) {
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.BubbleLayout);
            this.bubbleSize = a.getDimensionPixelSize(R.styleable.BubbleLayout_bubbleSize, DEFAULT_BUBBLE_SIZE);
            this.bubbleOffset = bubbleSize / (a.getInt(R.styleable.BubbleLayout_bubbleOffset, DEFAULT_BUBBLE_OFFSET));
            this.bubblePeek = a.getInt(R.styleable.BubbleLayout_bubblePeek, DEFAULT_BUBBLE_PEEK);
            this.bubbleBorderWidth = a.getDimensionPixelSize(R.styleable.BubbleLayout_borderWidth, DEFAULT_BORDER_WIDTH);
            this.bubbleBorderColor = a.getColor(R.styleable.BubbleLayout_borderColor, ContextCompat.getColor(c, R.color.colorPrimary));
            this.bubbleMargin = a.getDimensionPixelSize(R.styleable.BubbleLayout_bubbleMargin, DEFAULT_BUBBLE_MARGIN);
            this.useOffset = a.getBoolean(R.styleable.BubbleLayout_useBubbleOffset, true);
            a.recycle();
        }
    }


    private static class BubbleViewPool extends ObjectPool<CircleImageView> {
        private final BubbleParams bp;
        private final Context c;


        private BubbleViewPool(Context c, BubbleParams bp) {
            this.bp = bp;
            this.c = c;
        }

        @Override
        protected CircleImageView onCreate() {
            CircleImageView image = new CircleImageView(c);
            image.setLayoutParams(new LayoutParams(bp.bubbleSize, bp.bubbleSize));
            image.setBorderColor(bp.bubbleBorderColor);
            image.setBorderWidth(bp.bubbleBorderWidth);
            return image;
        }

        @Override
        boolean onValidate(CircleImageView o) {
            return true;
        }

        @Override
        void onExpired(CircleImageView o) {}
    }
}