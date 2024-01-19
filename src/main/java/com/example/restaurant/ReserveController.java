package com.example.restaurant;

import com.example.restaurant.dto.ReserveDto;
import com.example.restaurant.service.ReserveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/restaurant/{restId}/reservation")
@RequiredArgsConstructor
public class ReserveController {
    private final ReserveService service;

    @PostMapping
    public ReserveDto create(
            @PathVariable("restId")
            Long restId,
            @RequestBody
            ReserveDto dto
    ){
        return service.create(restId, dto);
    }
}
