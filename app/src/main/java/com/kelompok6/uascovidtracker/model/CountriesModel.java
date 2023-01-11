package com.kelompok6.uascovidtracker.model;

public class CountriesModel {
    String mCountry, mTodayCase, mTodayDeath,mTodayRecovered, mCase, mDeath, mRecovered, mFlag;

    public CountriesModel(String mCountry, String mCase, String mDeath, String mRecovered, String mFlag) {
        this.mCountry = mCountry;
        this.mCase = mCase;
        this.mFlag = mFlag;
        this.mDeath = mDeath;
        this.mRecovered = mRecovered;
    }

    public String getmCountry() {
        return mCountry;
    }

    public void setmCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public String getmTodayCase() {
        return mTodayCase;
    }

    public void setmTodayCase(String mTodayCase) {
        this.mTodayCase = mTodayCase;
    }

    public String getmTodayDeath() {
        return mTodayDeath;
    }

    public void setmTodayDeath(String mTodayDeath) {
        this.mTodayDeath = mTodayDeath;
    }

    public String getmTodayRecovered() {
        return mTodayRecovered;
    }

    public void setmTodayRecovered(String mTodayRecovered) {
        this.mTodayRecovered = mTodayRecovered;
    }

    public String getmCase() {
        return mCase;
    }

    public void setmCase(String mCase) {
        this.mCase = mCase;
    }

    public String getmDeath() {
        return mDeath;
    }

    public void setmDeath(String mDeath) {
        this.mDeath = mDeath;
    }

    public String getmRecovered() {
        return mRecovered;
    }

    public void setmRecovered(String mRecovered) {
        this.mRecovered = mRecovered;
    }

    public String getmFlag() {
        return mFlag;
    }

    public void setmFlag(String mFlag) {
        this.mFlag = mFlag;
    }
}
