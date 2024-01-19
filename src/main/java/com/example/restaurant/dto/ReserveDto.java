package com.example.restaurant.dto;

import com.example.restaurant.entity.Reserve;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReserveDto {
    private Long id;

    @Setter
    private Integer date;

    @Setter
    private Integer reserveHour;

    @Setter
    private Integer people;

    @Setter
    private Integer duration;

    public static ReserveDto fromEntity(Reserve entity){
        return new ReserveDto(
                entity.getId(),
                entity.getDate(),
                entity.getReserveHour(),
                entity.getPeople(),
                entity.getDuration()
        );
    }
}
