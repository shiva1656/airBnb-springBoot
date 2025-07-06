package com.projects.airBnbApp.service;

import com.projects.airBnbApp.dto.HotelDto;

public interface HotelService {
    //Just checking weather git hub is working or not.
    //Just trying to push.
    //Try making changes
    HotelDto createNewHotel(HotelDto hotelDto);
    HotelDto getHotelById(Long id);

    HotelDto updateHotelById(Long id,HotelDto hotelDto);

    void deleteHotelById(Long id);
}
