package com.zaaach.citypicker.adapter;

import com.zaaach.citypicker.model.City;

public interface InnerListener {
    void dismiss(City data);

    void requestLocation();
}
