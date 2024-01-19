package com.example.restaurant;

import com.example.restaurant.dto.MenuDto;
import com.example.restaurant.entity.Menu;
import com.example.restaurant.entity.Restaurant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    public MenuDto create(Long restId, MenuDto dto) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restId);
        if (optionalRestaurant.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        else{
            Menu newMenu = new Menu(
                    dto.getName(),
                    dto.getPrice(),
                    optionalRestaurant.get()
            );
            return MenuDto.fromEntity(menuRepository.save(newMenu));
        }
    }

    public List<MenuDto> readAll(Long restId) {
        if (!menuRepository.existsById(restId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        List<MenuDto> menuDtoList = new ArrayList<>();
        List<Menu> menus = menuRepository.findAllByRestaurantId(restId);
        for (Menu entity: menus){
            menuDtoList.add(MenuDto.fromEntity(entity));
        }
        return menuDtoList;
    }

    public MenuDto read(Long restId, Long menuId) {
        // Optional 이용해서 Menu를 찾아줌
        Optional<Menu> optionalMenu = menuRepository.findById(menuId);
        if (optionalMenu.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        // 검증절차 이 식당에 메뉴가 맞나?
        Menu menu = optionalMenu.get();
        if (!menu.getRestaurant().getId().equals(restId))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        return MenuDto.fromEntity(menu);
    }

    public MenuDto update(Long restId, Long menuId, MenuDto dto) {
        // Optional 이용해서 Menu를 찾아줌
        Optional<Menu> optionalMenu = menuRepository.findById(menuId);
        if (optionalMenu.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        // 검증절차 이 식당에 메뉴가 맞나?
        Menu menu = optionalMenu.get();
        if (!menu.getRestaurant().getId().equals(restId))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        menu.setName(dto.getName());
        menu.setPrice(dto.getPrice());
        return MenuDto.fromEntity(menuRepository.save(menu));
    }

    public void delete(Long restId, Long menuId) {
        // Optional 이용해서 Menu를 찾아줌
        Optional<Menu> optionalMenu = menuRepository.findById(menuId);
        if (optionalMenu.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        // 검증절차 이 식당에 메뉴가 맞나?
        Menu menu = optionalMenu.get();
        if (!menu.getRestaurant().getId().equals(restId))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        menuRepository.deleteById(menuId);
    }
}
