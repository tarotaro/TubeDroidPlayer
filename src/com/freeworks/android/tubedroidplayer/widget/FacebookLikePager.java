package com.freeworks.android.tubedroidplayer.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;


import com.freeworks.android.tubedroidplayer.R;
/**
 * Created by IntelliJ IDEA.
 * User: tarotaro
 * Date: 12/02/01
 * Time: 12:07
 * To change this template use File | Settings | File Templates.
 */
public class FacebookLikePager extends FrameLayout {

    private Context mContext;
    private Scroller mScroller;
    private boolean mIsOpened = false;
    private View mBehindView;
    private View mWrapView;
    public int mRightSpaceWidth;
    

    public FacebookLikePager(Context context) {
        this(context, (AttributeSet)null);
        

    }
    
    

    public FacebookLikePager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        TypedArray tArray =
                context.obtainStyledAttributes(
                        attrs,
                        R.styleable.FacebookLikePager
                );


        mRightSpaceWidth = tArray.getDimensionPixelSize(R.styleable.FacebookLikePager_rightspace,80);
        mScroller = new Scroller(context, new AccelerateDecelerateInterpolator());

    }
    

    @Override
    public void computeScroll() {
        if (mWrapView == null) {
            mWrapView = findViewWithTag("Wrap");
        }
        if (mBehindView == null) {
            mBehindView = findViewWithTag("Behind");
        }

        if (mScroller.computeScrollOffset()) {
            int x = mScroller.getCurrX();
            if (x > 0) {
                x = x - (getMeasuredWidth()-mRightSpaceWidth);
            }
            mWrapView.scrollTo(x, 0);
            postInvalidate();
        }
    }

    public void setBehindView(View view) {
        if (mBehindView != null) {
            removeView(mBehindView);
        }
        mBehindView = view;
    }

    public void setWrapView(View view) {
        if (view != null) {
            removeView(mWrapView);
        }
        mWrapView = view;
    }

    public boolean isOpened() {
        return mIsOpened;
    }

    public void open() {        
        if (mIsOpened) return;
        mScroller.startScroll(-1, 0, -(getMeasuredWidth()-mRightSpaceWidth)+1, 0, 250);
        invalidate();
        mIsOpened = true;
    }

    public void close() {        
        if (!mIsOpened) return;
        mScroller.startScroll(1, 0, (getMeasuredWidth()-mRightSpaceWidth)-1, 0, 250);
        invalidate();
        mIsOpened = false;
    }

}

