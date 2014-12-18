package com.ael_bd.starlinkdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RadioButton;

/**
 * Created by imamin on 12/19/2014.
 */
public class HighlightedImageRadioButton extends RadioButton {


    private Drawable buttonDrawable;

    public HighlightedImageRadioButton(Context context) {
        super(context);
    }

    public HighlightedImageRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHighlightedImageRadioButton(context, attrs, 0);
    }

    public HighlightedImageRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initHighlightedImageRadioButton(context, attrs, defStyle);
    }

    private void initHighlightedImageRadioButton(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HighlightedImageRadioButton, defStyle, 0);
        if (a == null) return;
        Drawable normalDrawable = a.getDrawable(R.styleable.HighlightedImageRadioButton_normalDrawable);
        Drawable focusedDrawable = a.getDrawable(R.styleable.HighlightedImageRadioButton_focusedDrawable);

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, focusedDrawable);
        stateListDrawable.addState(new int[]{android.R.attr.state_checked, android.R.attr.state_enabled}, focusedDrawable);
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed}, focusedDrawable);
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_selected}, focusedDrawable);
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled}, normalDrawable);

        buttonDrawable = stateListDrawable;
       // setButtonDrawable(R.drawable.beetalk_common_buzz_transparent_bg);
    }

    /**
     * Fix for putting the drawable in the center
     * notice that we put the background color of the drawable to transparent
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (buttonDrawable != null) {
            buttonDrawable.setState(getDrawableState());
            final int verticalGravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
            final int height = buttonDrawable.getIntrinsicHeight();

            int y = 0;

            switch (verticalGravity) {
                case Gravity.BOTTOM:
                    y = getHeight() - height;
                    break;
                case Gravity.CENTER_VERTICAL:
                    y = (getHeight() - height) / 2;
                    break;
            }

            int buttonWidth = buttonDrawable.getIntrinsicWidth();
            int buttonLeft = (getWidth() - buttonWidth) / 2;
            buttonDrawable.setBounds(buttonLeft, y, buttonLeft+buttonWidth, y + height);
            buttonDrawable.draw(canvas);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        buttonDrawable = null;
    }
}
