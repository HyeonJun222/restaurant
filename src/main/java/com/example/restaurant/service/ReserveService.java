package com.example.restaurant.service;

import com.example.restaurant.dto.ReserveDto;
import com.example.restaurant.entity.Reserve;
import com.example.restaurant.entity.Restaurant;
import com.example.restaurant.repo.ReserveRepository;
import com.example.restaurant.repo.RestaurantRepository;
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
public class ReserveService {
    private final ReserveRepository reserveRepository;
    private final RestaurantRepository restaurantRepository;

    public ReserveDto create(Long restId, ReserveDto dto) {
        // 1. 식당 정보를 조회한다.
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restId);

        // 1-1. 만약 없는 식당이라면 사용자에게 에러를 반환한다.
        if (optionalRestaurant.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        // # 요구사항: 식당이 여는 시간에 예약을 요청했는지 판단하자!
        // restaurant.open_hours <= reserve_hour
        // restaurant.close_hours > reserve_hour
        Restaurant restaurant = optionalRestaurant.get();
        if (restaurant.getOpenHours() > dto.getReserveHour()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        // 닫는시각 > 요청시각
        if (restaurant.getCloseHours() <= dto.getReserveHour())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        // ## 요구사항: 식당이 닫기 전에 떠난다.
        // restaurant.close_hours > reserve_hour
        if (dto.getReserveHour() + dto.getDuration() > restaurant.getCloseHours())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
/*
//  TODO: 나중에 구현하자
        // ###: 예약 가능 여부를 판단할 때, 'reserve_hour + duration' 에 해당한 시간은 다시 예약에 겹침 불가
        List<Reserve> dateReserves = reserveRepository.findAllByRestaurantIdAndDate(restId, dto.getDate());
        // 24시간 이니까 불리안 24개
        boolean[] reserveAble = new boolean[24];
        Arrays.fill(reserveAble, true);;
*/

        // 2. 새로운 예약 정보를 만들자.
        Reserve reserve = new Reserve(
                dto.getDate(),
                dto.getReserveHour(),
                dto.getPeople(),
                dto.getDuration(),
                optionalRestaurant.get()
        );

        // 3. 저장한 결과를 반환하자.
        return ReserveDto.fromEntity(reserveRepository.save(reserve));
}

public List<ReserveDto> readAll(Long restId) {
        /*
        List<ReserveDto> reserveDtoList = new ArrayList<>();
        for (Reserve entity: reserveRepository.findAllByRestaurantId(restId)){
            reserveDtoList.add(ReserveDto.fromEntity(entity));
        }

         */
        return reserveRepository
                .findAllByRestaurantId(restId)
                .stream()
                .map(ReserveDto::fromEntity)
                .collect(Collectors.toList());
}

public ReserveDto read(Long id) {
        return reserveRepository
                .findById(id)
                .map(ReserveDto::fromEntity)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
}

public void update() {
}

public void delete() {
}
}
