package com.zaaach.citypicker.adapter;

import com.zaaach.citypicker.model.City;

import java.util.List;

import androidx.annotation.NonNull;

public interface OnResultListener {
    void onResult(@NonNull List<City> data);
}
