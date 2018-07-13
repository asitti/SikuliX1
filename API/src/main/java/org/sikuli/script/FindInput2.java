/*
 * Copyright (c) 2010-2018, sikuli.org, sikulix.com - MIT license
 */

package org.sikuli.script;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;

import java.awt.*;

public class FindInput2 {

  private Mat target = null;
  private boolean targetTypeText = false;
  private String targetText = "";

  private Mat source = null;

  private double similarity = 0.7;

  private boolean findAll = false;

  public boolean isValid() {
    if (Do.SX.isNull(source)) {
      return false;
    }
    if (Do.SX.isNotNull(target)) {
      return true;
    }
    if (targetTypeText && !targetText.isEmpty()) {
      return true;
    }
    return false;
  }

  public boolean isText() {
    return targetTypeText && !targetText.isEmpty();
  }

  public boolean isFindAll() {
    return findAll;
  }

  public void setTargetText(String text) {
    targetText = text;
    targetTypeText = true;
  }

  public void setTarget(Mat target) {
    this.target = target;
  }

  public Mat getTarget() {
    return target;
  }

  public void setSource(Mat source) {
    this.source = source;
  }

  public Mat getBase() {
    return source;
  }

  public void setSimilarity(double similarity) {
    this.similarity = similarity;
  }

  public double getScore() {
    return similarity;
  }

  public void setFindAll() {
    findAll = true;
  }

  protected boolean plainColor = false;
  protected boolean blackColor = false;
  protected boolean whiteColor = false;

  public boolean isPlainColor() {
    return isValid() && plainColor;
  }

  public boolean isBlack() {
    return isValid() && blackColor;
  }

  public boolean isWhite() {
    return isValid() && blackColor;
  }

  public double getResizeFactor() {
    return isValid() ? resizeFactor : 1;
  }

  protected double resizeFactor;

  private final int resizeMinDownSample = 12;
  private int[] meanColor = null;
  private double minThreshhold = 1.0E-5;

  public Color getMeanColor() {
    return new Color(meanColor[2], meanColor[1], meanColor[0]);
  }

  public boolean isMeanColorEqual(Color otherMeanColor) {
    Color col = getMeanColor();
    int r = (col.getRed() - otherMeanColor.getRed()) * (col.getRed() - otherMeanColor.getRed());
    int g = (col.getGreen() - otherMeanColor.getGreen()) * (col.getGreen() - otherMeanColor.getGreen());
    int b = (col.getBlue() - otherMeanColor.getBlue()) * (col.getBlue() - otherMeanColor.getBlue());
    return Math.sqrt(r + g + b) < minThreshhold;
  }

  public void setAttributes() {
    plainColor = false;
    blackColor = false;
    resizeFactor = Math.min(((double) target.width()) / resizeMinDownSample,
            ((double) target.height()) / resizeMinDownSample);
    resizeFactor = Math.max(1.0, resizeFactor);
    MatOfDouble pMean = new MatOfDouble();
    MatOfDouble pStdDev = new MatOfDouble();
    Core.meanStdDev(target, pMean, pStdDev);
    double sum = 0.0;
    double[] arr = pStdDev.toArray();
    for (int i = 0; i < arr.length; i++) {
      sum += arr[i];
    }
    if (sum < minThreshhold) {
      plainColor = true;
    }
    sum = 0.0;
    arr = pMean.toArray();
    meanColor = new int[arr.length];
    for (int i = 0; i < arr.length; i++) {
      meanColor[i] = (int) arr[i];
      sum += arr[i];
    }
    if (sum < minThreshhold && plainColor) {
      blackColor = true;
    }
    if (meanColor.length > 1) {
      whiteColor = isMeanColorEqual(Color.WHITE);
    }
  }


//TODO for compilation - remove when native is obsolete
  public static long getCPtr(FindInput2 p) {
    return 0;
  }
}