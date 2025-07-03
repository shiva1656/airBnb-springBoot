package com.projects.airBnbApp.service;

import com.projects.airBnbApp.dto.HotelDto;
import com.projects.airBnbApp.entity.Hotel;

public interface HotelService {

    //Checking weather github is working or not
    HotelDto createNewHotel(HotelDto hotelDto);
    HotelDto getHotelById(Long id);
}
