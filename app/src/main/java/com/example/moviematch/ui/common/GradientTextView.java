package com.example.moviematch.ui.common;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.example.moviematch.R;

public class GradientTextView extends AppCompatTextView {

    public GradientTextView(Context context) {
        super(context);
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GradientTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0) {
            Shader textShader = new LinearGradient(
                    0, 0, w, 0,
                    new int[]{
                            getResources().getColor(R.color.accent_cyan),
                            getResources().getColor(R.color.accent_purple)
                    },
                    null,
                    Shader.TileMode.CLAMP);
            getPaint().setShader(textShader);
            invalidate();
        }
    }
}
