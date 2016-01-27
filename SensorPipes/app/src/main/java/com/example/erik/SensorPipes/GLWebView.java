package com.example.erik.SensorPipes;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by user on 3/15/15.
 */
public class GLWebView extends WebView /*implements GLRenderable*/{

    private OpenGLRenderer mViewToGLRenderer;

    // default constructors

    public GLWebView(Context context) {
        super(context);
    }

    public GLWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GLWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // draw magic
    @Override
    public void draw( Canvas canvas ) {
        //returns canvas attached to gl texture to draw on
        Canvas glAttachedCanvas = mViewToGLRenderer.onDrawViewBegin();
        if(glAttachedCanvas != null) {
            //translate canvas to reflect view scrolling
            float xScale = glAttachedCanvas.getWidth() / (float)canvas.getWidth();
            glAttachedCanvas.scale(xScale, xScale);
            glAttachedCanvas.translate(-getScrollX(), -getScrollY());
            //draw the view to provided canvas
            super.draw(glAttachedCanvas);
        }
        // notify the canvas is updated
        mViewToGLRenderer.onDrawViewEnd();
    }

    public void setViewToGLRenderer(OpenGLRenderer viewTOGLRenderer){
        mViewToGLRenderer = viewTOGLRenderer;
    }
}
