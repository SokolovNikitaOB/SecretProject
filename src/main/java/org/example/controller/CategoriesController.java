package org.example.controller;

import org.example.domain.Categories;
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
@RequestMapping("/categories")
public class CategoriesController {

    @Autowired
    CategoriesService categoriesService;

    @GetMapping()
    public String categories(Model model){
        Categories category = categoriesService.getCategoryForStarterPage();
        model.addAttribute("category", category);
        return "categories/categories";
    }

    @GetMapping("/{id}")
    public String category(@PathVariable Long id, Model model){
        Categories categories = categoriesService.getCategory(id);
        if(categories.isDeleted()){
            return "redirect:/categories";
        }
        model.addAttribute("category", categories);
        return "categories/categories-example";
    }


    @GetMapping("/add")
    public String pageAddCategory(Model model){
        model.addAttribute("category", new Categories());
        return "categories/categories-add";
    }

    @PostMapping()
    public String addCategories(@ModelAttribute("category") @Valid Categories category,
                                BindingResult bindingResult){

        addNewCategoryErrors(bindingResult, category);

        if(bindingResult.hasErrors()){
            return "categories/categories-add";
        }

        categoriesService.addCategory(category);
        return "redirect:/categories";
    }

    @DeleteMapping("/{id}")
    public String deleteCategory(@PathVariable Long id, Model model){
        boolean isDeleted = categoriesService.deleteCategory(id);
        if(!isDeleted){
            model.addAttribute("isNotDeleted", "This category is related with banners");
            return category(id, model);
        }
        return "redirect:/categories";
    }

    @GetMapping("/{id}/edit")
    public String pageEditCategory(@PathVariable Long id, Model model){
        Categories currentCategory = categoriesService.getCategory(id);
        if(currentCategory.isDeleted()){
            return "redirect:/categories";
        }
        model.addAttribute("category", currentCategory);
        return "categories/categories-edit";
    }

    @PatchMapping("/{id}")
    public String editCategory(@ModelAttribute("category") @Valid Categories category,
                               BindingResult bindingResult,
                               @PathVariable Long id){
        addNewCategoryErrors(bindingResult, category);

        if(bindingResult.hasErrors()){
            return  "categories/categories-edit";
        }
        categoriesService.editCategory(id, category);
        return "redirect:/categories/" + id;
    }

    @ModelAttribute("categories")
    public List<Categories> categories(){
        return categoriesService.getAllCategories();
    }

    @ResponseBody
    @GetMapping("/search")
    public List<Categories> searchCategoriesById(@RequestParam(name = "input") String input){
        return categoriesService.getCategoriesContainingInput(input);
    }

    public void addNewCategoryErrors(BindingResult bindingResult, Categories category){
        if(categoriesService.countCategoriesByName(category) > 0){
            bindingResult.addError(new FieldError("banner","name","Name should be unique" ));
        }

        if(categoriesService.countCategoriesByRequestId(category) > 0){
            bindingResult.addError(new FieldError("banner","requestId","Request ID should be unique" ));
        }
    }


}
