package com.banmaz.yongbang.utils;

import android.view.View;
import android.view.ViewGroup;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Description:
 *
 * <p>Created by liuguoquan on 2017/12/5 14:09.
 */
public class AnimationUtil {

  public static void show(final View v, int height) {
    ValueAnimator animator = ValueAnimator.ofInt(0, height);
    animator.addUpdateListener(
        new ValueAnimator.AnimatorUpdateListener() {
          @Override
          public void onAnimationUpdate(ValueAnimator animation) {
            int value = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams params = v.getLayoutParams();
            params.height = value;
            v.setLayoutParams(params);
          }
        });
    animator.addListener(
        new AnimatorListenerAdapter() {
          @Override
          public void onAnimationStart(Animator animation) {
            v.setVisibility(View.VISIBLE);
          }
        });
    animator.setTarget(v);
    animator.setDuration(200);
    animator.start();
  }

  public static void animateToHeight(final View v, int from, int to) {
    ValueAnimator animator = ValueAnimator.ofInt(from, to);
    animator.addUpdateListener(
        new ValueAnimator.AnimatorUpdateListener() {
          @Override
          public void onAnimationUpdate(ValueAnimator animation) {
            int value = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams params = v.getLayoutParams();
            params.height = value;
            v.setLayoutParams(params);
          }
        });
    animator.setTarget(v);
    animator.setDuration(200);
    animator.start();
  }

  public static void disappear(final View v, int height) {
    ValueAnimator animator = ValueAnimator.ofInt(height, 0);
    animator.addUpdateListener(
        new ValueAnimator.AnimatorUpdateListener() {
          @Override
          public void onAnimationUpdate(ValueAnimator animation) {
            int value = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams params = v.getLayoutParams();
            params.height = value;
            v.setLayoutParams(params);
          }
        });
    animator.addListener(
        new AnimatorListenerAdapter() {
          @Override
          public void onAnimationEnd(Animator animation) {
            v.setVisibility(View.GONE);
          }
        });
    animator.setTarget(v);
    animator.setDuration(200);
    animator.start();
  }

  public static void setScale(final View v, float from, float to) {
    ValueAnimator animator = ValueAnimator.ofFloat(from, to);
    animator.addUpdateListener(
        new ValueAnimator.AnimatorUpdateListener() {
          @Override
          public void onAnimationUpdate(ValueAnimator animation) {
            float value = (float) animation.getAnimatedValue();
            v.setScaleX(value);
            v.setScaleY(value);
          }
        });
    animator.setTarget(v);
    animator.setDuration(200);
    animator.start();
  }

  public static void setScaleX(final View v, float from, float to) {
    ValueAnimator animator = ValueAnimator.ofFloat(from, to);
    animator.addUpdateListener(
        new ValueAnimator.AnimatorUpdateListener() {
          @Override
          public void onAnimationUpdate(ValueAnimator animation) {
            float value = (float) animation.getAnimatedValue();
            v.setScaleX(value);
          }
        });
    animator.setTarget(v);
    animator.setDuration(200);
    animator.start();
  }

  public static void setScaleY(final View v, float from, float to) {
    ValueAnimator animator = ValueAnimator.ofFloat(from, to);
    animator.addUpdateListener(
        new ValueAnimator.AnimatorUpdateListener() {
          @Override
          public void onAnimationUpdate(ValueAnimator animation) {
            float value = (float) animation.getAnimatedValue();
            v.setScaleY(value);
          }
        });
    animator.setTarget(v);
    animator.setDuration(200);
    animator.start();
  }
}
