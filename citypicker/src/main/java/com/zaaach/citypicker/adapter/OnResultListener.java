package com.zaaach.citypicker.adapter;

import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.model.City;

import java.util.List;

public interface OnResultListener {
    void onResult(List<City> data);

    void onRequestLocation(CityPicker picker);
}