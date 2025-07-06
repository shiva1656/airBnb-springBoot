package com.projects.airBnbApp.service;


import com.projects.airBnbApp.dto.HotelDto;
import com.projects.airBnbApp.entity.Hotel;
import com.projects.airBnbApp.exception.ResourceNotFoundException;
import com.projects.airBnbApp.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService{

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;

    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Creating a new hotel with name: {}",hotelDto.getName());

        //modelMapper is to convert DTO to Hotel class as the field names are same it converts it.
        Hotel hotel=modelMapper.map(hotelDto,Hotel.class);
        hotel.setActive(false);

        hotel=hotelRepository.save(hotel);

        log.info("Created a new hotel with ID: {}",hotelDto.getId());
        return modelMapper.map(hotel,HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long id) {

        log.info("Getting the hotel with ID: {}",id);
        Hotel hotel=hotelRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Hotel not found with ID:"+id));
        return modelMapper.map(hotel,HotelDto.class);

    }

    @Override
    public HotelDto updateHotelById(Long id,HotelDto hotelDto) {

        log.info("Updating the hotel with ID: {}",id);
        Hotel hotel=hotelRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Hotel not found with ID:"+id));
        //here modelMapper will transfer all the info from hotelDto to hotel
        modelMapper.map(hotelDto,hotel);
        hotel.setId(id);
        hotel=hotelRepository.save(hotel);
        return modelMapper.map(hotel,HotelDto.class);
    }

    @Override
    public void deleteHotelById(Long id) {
        boolean exists=hotelRepository.existsById(id);

        if(!exists){
            throw new ResourceNotFoundException("Hotel not found with ID:"+id);
        }
        hotelRepository.deleteById(id);
        //TODO: delete the future inventories for this hotel.

    }


}
