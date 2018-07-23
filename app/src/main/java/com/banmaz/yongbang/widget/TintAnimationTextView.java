package com.banmaz.yongbang.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

/** @author liqi create_time 2017/3/24 10:52 描述: */
public class TintAnimationTextView extends android.support.v7.widget.AppCompatTextView {

  private Paint mPaint;
  private float mAnimateValue = -1;
  private final int rectWidth = 40;
  private ValueAnimator mAnimator;

  public TintAnimationTextView(Context context) {
    super(context);
    initAnimation();
  }

  public TintAnimationTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initAnimation();
  }

  public TintAnimationTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initAnimation();
  }

  private void initAnimation() {
    mPaint = new Paint();
    Shader shader =
        new LinearGradient(
            50,
            0,
            100,
            100,
            new int[] {
                Color.parseColor("#33ffffff"),
                Color.parseColor("#88ffffff"),
                Color.parseColor("#33ffffff")
            },
            null,
            Shader.TileMode.REPEAT);
    mPaint.setShader(shader);
    mPaint.setStyle(Paint.Style.FILL);
    mAnimator = ValueAnimator.ofFloat(0, 1);
    mAnimator.addUpdateListener(
        new ValueAnimator.AnimatorUpdateListener() {
          @Override
          public void onAnimationUpdate(ValueAnimator animation) {
            mAnimateValue = (float) animation.getAnimatedValue();
            invalidate();
          }
        });
    mAnimator.setInterpolator(new LinearInterpolator());
    mAnimator.setRepeatMode(ValueAnimator.RESTART);
    mAnimator.setRepeatCount(ValueAnimator.INFINITE);
    mAnimator.setDuration(1500);
    mAnimator.setTarget(this);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    mAnimator.start();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (mAnimateValue != -1) {
      int measuredWidth = getMeasuredWidth();
      int top = getTop();
      int bottom = getBottom();
      float animateLeft = (measuredWidth - rectWidth - 10) * mAnimateValue;
      float animateRight = animateLeft + rectWidth;
      //            canvas.drawRect(animateLeft, top, animateRight, bottom, mPaint);
      Path path = new Path();
      path.moveTo(animateLeft + 10, top);
      path.lineTo(animateRight + 10, top);
      path.lineTo(animateRight, bottom);
      path.lineTo(animateLeft, bottom);
      path.close();
      canvas.drawPath(path, mPaint);
    }
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (mAnimator != null) {
      mAnimator.removeAllUpdateListeners();
      mAnimator.cancel();
    }
  }
}
