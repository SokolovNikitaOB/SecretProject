package org.example.service;

import org.example.domain.Banners;
import org.example.domain.Categories;
import org.example.repository.BannersRepo;
import org.example.repository.CategoriesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class BannersService {

    @Autowired
    private CategoriesRepo categoriesRepo;

    @Autowired
    private BannersRepo bannersRepo;

    public List<Banners> getAllBanners(){
        Iterable<Banners> bannersIterable = bannersRepo.findAll();
        ArrayList<Banners> banners = new ArrayList<>();
        for(Banners b: bannersIterable){
            if(!b.isDeleted()){
                banners.add(b);
            }
        }
        return banners;

    }

    public Banners getBanner(Long id){
        return bannersRepo.findById(id).get();
    }

    public void addBanner(Banners banner){
        bannersRepo.save(banner);
    }

    public void addCategoriesToBanner(List<String> categoryRequestIds, Banners banner){
        if(categoryRequestIds != null){
            List<Categories> categories = new ArrayList<>();
            categoryRequestIds.stream().forEach(requestId -> categories.add(categoriesRepo.findByRequestId(requestId)));
            banner.setCategories(categories);
        }
    }

    public void deleteBanner(Long id){
        Banners banner = getBanner(id);
        if(banner != null){
            banner.setDeleted(true);
            bannersRepo.save(banner);
        }
    }

    public void editBanner(Long id, Banners banner) {
        Banners editedBanner = getBanner(id);
        editedBanner.setName(banner.getName());
        editedBanner.setPrice(banner.getPrice());
        editedBanner.setTextField(banner.getTextField());
        editedBanner.setCategories(banner.getCategories());
        bannersRepo.save(editedBanner);

    }

    public List<Banners> getBannersContainingInput(String input){
        return getAllBanners().stream().filter(banner -> banner.getName().toLowerCase(Locale.ROOT).contains(input) && !banner.isDeleted()).collect(Collectors.toList());
    }

    public Banners getBannerWithMaxPrice(List<Banners> banners){
        if(banners.isEmpty()){
            return null;
        }
        return banners.stream().max((b1,b2) -> b1.getPrice().compareTo(b2.getPrice())).get();

    }
    public Banners getBannerForStarterPage(){
        List<Banners> banners = getAllBanners();
        if(banners.isEmpty()){
            return null;
        }
        return banners.get(0);

    }

    public int countBannersWithName(Banners banner){
        String name = banner.getName();
        Long id = banner.getId();
        return (int) getAllBanners().stream().filter(b -> b.getName().equals(name) && !b.getId().equals(id)).filter(b -> !b.equals(banner)).count();

    }

}
