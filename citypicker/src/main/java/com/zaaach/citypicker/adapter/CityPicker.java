package com.zaaach.citypicker.adapter;

import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

import java.util.List;

public interface CityPicker {
    public void updateLocation(LocatedCity location, @LocateState.State int state);

    public void setCities(List<City> cities);

}
