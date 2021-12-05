package org.example.controller;

import org.example.domain.Banners;
import org.example.service.LogRecordsService;
import org.example.service.SearchBannerByCategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class BannerSuitableToCategoriesController {
    @Autowired
    SearchBannerByCategoriesService searchService;

    @Autowired
    LogRecordsService logRecordsService;

    @GetMapping("/bid")
    public ResponseEntity<Banners> get(@RequestParam("cat") List<String> cat){
        Banners banner;
        try {
            banner = searchService.getBannerByCategories(cat, logRecordsService.getNotDisplayedBanners());
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(banner);
    }

}
