package com.example.restaurant.service;

import com.example.restaurant.repo.RestaurantRepository;
import com.example.restaurant.dto.RestaurantDto;
import com.example.restaurant.entity.Restaurant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository repository;

    public RestaurantDto create(RestaurantDto dto) {
        // Open < Close = ERROR
        if (dto.getCloseHour() > dto.getOpenHour())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        // HttpStatus.Bad_Request = Syntax ERROR

        Restaurant restaurant = new Restaurant(
                dto.getName(),
                dto.getCategory(),
                dto.getOpenHour(),
                dto.getCloseHour()
        );
        return RestaurantDto.fromEntity(repository.save(restaurant));
    }

    public List<RestaurantDto> readAll() {
        /*
        List<RestaurantDto> restaurantDtoList = new ArrayList<>();
        // 레스토랑 모든 정보를 가져옴
        List<Restaurant> restaurantList = repository.findAll();
        for (Restaurant entity: restaurantList){
            restaurantDtoList.add(RestaurantDto.fromEntity((entity)));
        }
        return restaurantDtoList;
        */
//            STREAM API 활용
            return repository
                    .findAll()
                    .stream()
//                    .map(entity -> RestaurantDto.fromEntity(entity))  // 참조로 바꾸라 함
                    .map(RestaurantDto::fromEntity)
                    .collect(Collectors.toList());

    }

    public RestaurantDto read(Long id) {
        /*
        Optional<Restaurant> optionalRestaurant = repository.findById(id);
        if (optionalRestaurant.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return RestaurantDto.fromEntity(optionalRestaurant.get());
        */
        // Optional.map(), Optional.orElseThrow()
        return repository.findById(id)
                .map(RestaurantDto::fromEntity)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        // .orElseThrow() 메서드의 경우 서버에러를 보내게 되는데 괄호 안에 어떤 오류는 던질건지 정할 수 있음
    }

    public RestaurantDto update(Long id, RestaurantDto dto) {
        Optional<Restaurant> optionalRestaurant = repository.findById(id);
        if (optionalRestaurant.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        Restaurant targetRestaurant = optionalRestaurant.get();
        targetRestaurant.setName(dto.getName());
        targetRestaurant.setCategory(dto.getCategory());
        targetRestaurant.setOpenHours(dto.getOpenHour());
        targetRestaurant.setCloseHours(dto.getCloseHour());
        return RestaurantDto.fromEntity(repository.save(targetRestaurant));
    }

    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        repository.deleteById(id);
    }
}
