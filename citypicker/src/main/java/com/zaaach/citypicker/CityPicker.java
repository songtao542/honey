package com.zaaach.citypicker;

import com.zaaach.citypicker.adapter.OnResultListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;
import com.zaaach.citypicker.model.HotCity;

import java.util.List;

import androidx.annotation.StyleRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * @Author: Bro0cL
 * @Date: 2018/2/6 17:52
 */
public class CityPicker {
    private static final String TAG = "CityPicker";

    private CityPicker() {
    }

    private FragmentManager mFragmentManager;
    private Fragment mTargetFragment;

    private boolean enableAnim;
    private boolean mMultipleMode;
    private int mAnimStyle;
    private LocatedCity mLocation;
    private List<HotCity> mHotCities;
    private boolean mEnableHotCities;
    private boolean mEnableLocation;
    private OnResultListener mOnResultListener;


    public static class Builder {

        CityPicker mPicker;

        public Builder() {
            mPicker = new CityPicker();
        }

        public CityPicker.Builder fragmentManager(FragmentManager fm) {
            mPicker.mFragmentManager = fm;
            return this;
        }

        public CityPicker.Builder targetFragment(Fragment targetFragment) {
            mPicker.mTargetFragment = targetFragment;
            return this;
        }

        /**
         * 设置动画效果
         *
         * @param animStyle
         * @return
         */
        public CityPicker.Builder animationStyle(@StyleRes int animStyle) {
            mPicker.mAnimStyle = animStyle;
            return this;
        }

        /**
         * 设置多选模式
         *
         * @param multipleMode
         * @return
         */
        public CityPicker.Builder multipleMode(boolean multipleMode) {
            mPicker.mMultipleMode = multipleMode;
            return this;
        }

        /**
         * 设置当前已经定位的城市
         *
         * @param location
         * @return
         */
        public CityPicker.Builder locatedCity(LocatedCity location) {
            mPicker.mLocation = location;
            return this;
        }

        public CityPicker.Builder hotCities(List<HotCity> data) {
            mPicker.mHotCities = data;
            return this;
        }

        public CityPicker.Builder enableHotCities(boolean enable) {
            mPicker.mEnableHotCities = enable;
            return this;
        }

        public CityPicker.Builder enableLocation(boolean enable) {
            mPicker.mEnableLocation = enable;
            return this;
        }

        /**
         * 启用动画效果，默认为false
         *
         * @param enable
         * @return
         */
        public CityPicker.Builder enableAnimation(boolean enable) {
            mPicker.enableAnim = enable;
            return this;
        }


        /**
         * 设置选择结果的监听器
         *
         * @param listener
         * @return
         */
        public CityPicker.Builder listener(OnResultListener listener) {
            mPicker.mOnResultListener = listener;
            return this;
        }

        public CityPicker build() {
            return mPicker;
        }

        public void show() {
            mPicker.show();
        }
    }


    public void show() {
        if (mFragmentManager == null) {
            throw new UnsupportedOperationException("CityPicker：method setFragmentManager() must be called.");
        }
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        final Fragment prev = mFragmentManager.findFragmentByTag(TAG);
        if (prev != null) {
            ft.remove(prev).commit();
            ft = mFragmentManager.beginTransaction();
        }
        ft.addToBackStack(null);
        final CityPickerDialogFragment cityPickerFragment = CityPickerDialogFragment.newInstance(enableAnim);
        cityPickerFragment.setLocatedCity(mLocation);
        cityPickerFragment.setHotCities(mHotCities);
        cityPickerFragment.enableHotCities(mEnableHotCities);
        cityPickerFragment.enableLocation(mEnableLocation);
        cityPickerFragment.setAnimationStyle(mAnimStyle);
        cityPickerFragment.setMultipleMode(mMultipleMode);
        cityPickerFragment.setOnResultListener(new OnResultListener() {
            @Override
            public void onResult(List<City> data) {
                mOnResultListener.onResult(data);
            }

            @Override
            public void onRequestLocation(CityPicker picker) {
                mOnResultListener.onRequestLocation(CityPicker.this);
            }
        });
        if (mTargetFragment != null) {
            cityPickerFragment.setTargetFragment(mTargetFragment, 0);
        }
        cityPickerFragment.show(ft, TAG);
    }

    /**
     * 定位完成
     *
     * @param location
     * @param state    onLocationChanged
     */
    public void locationChanged(LocatedCity location, @LocateState.State int state) {
        CityPickerDialogFragment fragment = (CityPickerDialogFragment) mFragmentManager.findFragmentByTag(TAG);
        if (fragment != null) {
            fragment.locationChanged(location, state);
        }
    }

}
