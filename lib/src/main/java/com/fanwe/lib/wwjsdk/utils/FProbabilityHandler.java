package com.fanwe.lib.wwjsdk.utils;

import java.util.Random;

/**
 * Created by Administrator on 2018/1/3.
 */
public class FProbabilityHandler
{
    /**
     * 分子
     */
    private int mNumerator;
    /**
     * 分母
     */
    private int mDenominator = Integer.MAX_VALUE;

    private Random mRandom = new Random();

    private int mHitCount;
    private int mTotalCount;

    /**
     * 设置分子
     *
     * @param numerator
     */
    public synchronized void setNumerator(int numerator)
    {
        mNumerator = numerator;
    }

    /**
     * 设置分母
     *
     * @param denominator
     */
    public synchronized void setDenominator(int denominator)
    {
        mDenominator = denominator;
        if (denominator <= 0)
        {
            throw new IllegalArgumentException("denominator must > 0");
        }
    }

    /**
     * 当前随机是否击中概率
     *
     * @return true-击中概率
     */
    public synchronized boolean random()
    {
        int value = mRandom.nextInt(mDenominator) + 1;
        boolean hit = value > 0 && value <= mNumerator;

        if (mTotalCount >= Integer.MAX_VALUE)
        {
            mHitCount = 0;
            mTotalCount = 0;
        }

        if (hit)
        {
            mHitCount++;
        }
        mTotalCount++;

        return hit;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("hit:").append(mHitCount).append(" ").append("total:").append(mTotalCount);
        return sb.toString();
    }
}
