/*
 * Copyright (c) 2017 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.banmaz.yongbang.utils;

import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

public class Animators {

  private Animators() {
  }

  public static ValueAnimator makeDeterminateCircularPrimaryProgressAnimator(
      final ProgressBar[] progressBars) {
    ValueAnimator animator = ValueAnimator.ofInt(0, 100);
    animator.setDuration(2000);
    animator.setInterpolator(new LinearInterpolator());
    animator.setRepeatCount(ValueAnimator.INFINITE);
    animator.setRepeatMode(ValueAnimator.RESTART);
    animator.addUpdateListener(
        new ValueAnimator.AnimatorUpdateListener() {
          @Override
          public void onAnimationUpdate(ValueAnimator animator) {
            int value = (int) animator.getAnimatedValue();
            for (ProgressBar progressBar : progressBars) {
              progressBar.setProgress(value);
            }
          }
        });
    return animator;
  }

  public static ValueAnimator makeDeterminateCircularPrimaryAndSecondaryProgressAnimator(
      final ProgressBar[] progressBars) {
    ValueAnimator animator = makeDeterminateCircularPrimaryProgressAnimator(progressBars);
    animator.addUpdateListener(
        new ValueAnimator.AnimatorUpdateListener() {
          @Override
          public void onAnimationUpdate(ValueAnimator animator) {
            int value = Math.round(1.25f * (int) animator.getAnimatedValue());
            for (ProgressBar progressBar : progressBars) {
              progressBar.setSecondaryProgress(value);
            }
          }
        });
    return animator;
  }
}
