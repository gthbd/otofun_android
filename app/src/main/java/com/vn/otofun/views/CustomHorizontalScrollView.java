package com.vn.otofun.views;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class CustomHorizontalScrollView extends HorizontalScrollView {
	public CustomHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	public CustomHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public CustomHorizontalScrollView(Context context) {
		super(context);
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (mOnScrollListener != null)
			mOnScrollListener.onScrollChanged(l, oldl, t, oldt);
	}
	
	private OnScrollListener mOnScrollListener;
	public void setOnScrollListener(OnScrollListener l) {
		mOnScrollListener = l;
	}
	
	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
				scrollRangeY, 0, maxOverScrollY, isTouchEvent);
	}

	
	public boolean isParentMode() {
		return this.isParentMode;
	}
	private boolean isParentMode = false;
	public void setParentMode(boolean isParent) {
		isParentMode = isParent;
	}
	private GestureDetector flingDetector;
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		boolean ret = super.dispatchTouchEvent(ev);
		if (isParentMode && !isGingerBread()) {
			if (flingDetector == null)  {
				flingDetector = new GestureDetector(
					getContext(), 
					new GestureDetector.SimpleOnGestureListener() {
						public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
							CustomHorizontalScrollView.this.fling(-(int)velocityX);
							return true;
						}
					}
				);
			}
			
			if (!flingDetector.onTouchEvent(ev)) {
				super.onInterceptTouchEvent(ev);
				super.onTouchEvent(ev);
			}
		}
		return ret;
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		boolean ret = super.onInterceptTouchEvent(ev);
		if (isParentMode && !isGingerBread()) {
			super.onTouchEvent(ev);
			ret = false;
		}
		return ret;
	}

    private boolean isGingerBread() {
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD;
    }
}
