package org.example.service;

import org.example.domain.Banners;
import org.example.domain.Categories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class SearchBannerByCategoriesService {
    @Autowired
    BannersService bannersService;

    @Autowired
    CategoriesService categoriesService;

    public Banners getBannerByCategories(List<String> requestIdList, List<Banners> notDisplayedBanners) throws NoSuchElementException{
        List<Banners> banners = bannersService
                .getAllBanners()
                    .stream()
                    .filter(b -> !notDisplayedBanners.contains(b))
                    .collect(Collectors.toList());

        if(requestIdList.isEmpty()){
            return bannersService.getBannerWithMaxPrice(banners);
        }

        for(String requestId : requestIdList){
            Categories category = categoriesService.getCategoryByRequestId(requestId);
            banners = bannersFilterByCategory(banners, category);
        }

        if(banners.size() > 1){
            return bannersService.getBannerWithMaxPrice(banners);
        }else if (banners.size() == 0){
            throw new NoSuchElementException();
        }else {
            return banners.get(0);
        }

    }

    public List<Banners> bannersFilterByCategory(List<Banners> banners, Categories category){
        return banners
                .stream()
                .filter(banner -> banner.getCategories().contains(category))
                .collect(Collectors.toList());
    }
}
