package com.example.kunalraghav.flickr;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

class RecyclerItemClickListener extends RecyclerView.SimpleOnItemTouchListener {
    private static final String TAG = "RecyclerItemClickListen";

    interface OnRecyclerClickListener{
        void OnItemClickListener(View view, int position);
        void OnItemLongClickListener(View view, int position);
    }

    private final OnRecyclerClickListener mlistener;
    private final GestureDetectorCompat mgestureDetector;

    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnRecyclerClickListener listener) {
        this.mlistener = listener;
        this.mgestureDetector = new GestureDetectorCompat(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d(TAG, "onSingleTapUp: starts");
                View childView = recyclerView.findChildViewUnder(e.getX(),e.getY());
                if(childView != null && mlistener != null){
                    Log.d(TAG, "onSingleTapUp: calling listener: listener.onItemClick");
                    mlistener.OnItemClickListener(childView,recyclerView.getChildAdapterPosition(childView));

                }
                return true;

            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.d(TAG, "onLongPress: starts");
//                super.onLongPress(e);
                View childView = recyclerView.findChildViewUnder(e.getX(),e.getY());
                if(childView != null && childView != null){
                    Log.d(TAG, "onLongPress: calling listener: listener.onItemLongClick");
                    mlistener.OnItemLongClickListener(childView,recyclerView.getChildAdapterPosition(childView));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        Log.d(TAG, "onInterceptTouchEvent: starts");
        if(mgestureDetector!=null){
            boolean result= mgestureDetector.onTouchEvent(e);
            Log.d(TAG, "onInterceptTouchEvent: returned: "+result);
            return result;
        }else {
            Log.d(TAG, "onInterceptTouchEvent: returned: false");
            return false;
        }
//        return super.onInterceptTouchEvent(rv, e);
    }
}
