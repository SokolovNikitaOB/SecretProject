package org.example.controller;

import org.example.domain.Banners;
import org.example.domain.Categories;
import org.example.service.BannersService;
import org.example.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/banners")
public class BannersController {

    @Autowired
    BannersService bannersService;

    @Autowired
    CategoriesService categoriesService;

    @GetMapping()
    public String banners(Model model){
        Banners banners = bannersService.getBannerForStarterPage();
        model.addAttribute("banner", banners);
        return "banners/banners";
    }

    @GetMapping("/{id}")
    public String banner(@PathVariable Long id, Model model){
        Banners banner = bannersService.getBanner(id);
        if(banner.isDeleted()){
            return "redirect:/banners";
        }
        model.addAttribute("banner", banner);
        return "banners/banners-example";
    }


    @GetMapping("/add")
    public String pageAddBanner(Model model){
        model.addAttribute("banner", new Banners());
        return "banners/banners-add";
    }

    @PostMapping()
    public String addBanner(@ModelAttribute("banner") @Valid Banners banner,
                            BindingResult bindingResult,
                            @RequestParam(required = false) List<String> categoryRequestIds){
        bannersService.addCategoriesToBanner(categoryRequestIds, banner);
        addNewBannerErrors(bindingResult,banner);

        if(bindingResult.hasErrors()){
            return "banners/banners-add";
        }

        bannersService.addBanner(banner);

        return "redirect:/banners";
    }

    @DeleteMapping("/{id}")
    public String deleteBanner(@PathVariable Long id){
        bannersService.deleteBanner(id);
        return "redirect:/banners";
    }

    @GetMapping("/{id}/edit")
    public String pageEditBanner(@PathVariable Long id, Model model){
        Banners currentBanner = bannersService.getBanner(id);
        if(currentBanner.isDeleted()){
            return "redirect:/banners";
        }
        model.addAttribute("banner", currentBanner);
        return "banners/banners-edit";
    }

    @PatchMapping("/{id}")
    public String editBanner(@ModelAttribute("banner") @Valid Banners banner,
                             BindingResult bindingResult,
                             @RequestParam(required = false) List<String> categoryRequestIds,
                             @PathVariable Long id){
        bannersService.addCategoriesToBanner(categoryRequestIds, banner);
        addNewBannerErrors(bindingResult,banner);

        if(bindingResult.hasErrors()){
            return "banners/banners-edit";
        }
        bannersService.editBanner(id, banner);
        return "redirect:/banners/" + id;
    }

    @ModelAttribute("banners")
    public List<Banners> banners(){
        return bannersService.getAllBanners();
    }

    @ModelAttribute("categories")
    public List<Categories> categories(){
        return categoriesService.getAllCategories();
    }

    @ResponseBody
    @GetMapping("/search")
    public List<Banners> searchBannersByName(@RequestParam(name = "input") String input){
        return bannersService.getBannersContainingInput(input);
    }

    public void addNewBannerErrors(BindingResult bindingResult, Banners banner){
        if(bannersService.countBannersWithName(banner) > 0){
            bindingResult.addError(new FieldError("banner","name","Name should be unique" ));
        }
    }


}
