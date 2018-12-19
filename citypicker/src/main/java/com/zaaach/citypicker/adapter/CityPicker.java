package com.zaaach.citypicker.adapter;

import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

import java.util.List;

public interface CityPicker {
    void updateLocation(LocatedCity location, @LocateState.State int state);

    void setCities(List<City> cities);
}
