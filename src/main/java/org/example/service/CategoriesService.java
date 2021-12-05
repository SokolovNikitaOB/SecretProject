package org.example.service;

import org.example.domain.Categories;
import org.example.repository.CategoriesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class CategoriesService {

    @Autowired
    CategoriesRepo categoriesRepo;

    public List<Categories> getAllCategories(){
        Iterable<Categories> categoriesIterable = categoriesRepo.findAll();
        ArrayList<Categories> categories = new ArrayList<>();
        for(Categories c: categoriesIterable){
            if(!c.isDeleted())
            categories.add(c);
        }
        return categories;
    }

    public Categories getCategory(Long id){
        return categoriesRepo.findById(id).get();
    }

    public void addCategory(Categories category){
        categoriesRepo.save(category);
    }

    public boolean deleteCategory(Long id){
        Categories category = getCategory(id);
        if(category != null && category.getBanners().stream().filter(b -> !b.isDeleted()).count() == 0){
            category.setDeleted(true);
            categoriesRepo.save(category);
            return true;
        }
        return false;
    }

    public void editCategory(Long id, Categories category){
        Categories editedCategory = getCategory(id);
        editedCategory.setName(category.getName());
        editedCategory.setRequestId(category.getRequestId());
        categoriesRepo.save(editedCategory);
    }

    public List<Categories> getCategoriesContainingInput(String input){
        List<Categories> categories = getAllCategories();
        if(categories != null){
            return categories.stream().filter(category -> category.getName().toLowerCase(Locale.ROOT).contains(input) && !category.isDeleted()).collect(Collectors.toList());
        }
        return null;
    }

    public Categories getCategoryByRequestId(String requestId){
        Categories category = categoriesRepo.findByRequestId(requestId);
        if(category != null && !category.isDeleted()){
            return categoriesRepo.findByRequestId(requestId);
        }
        return null;
    }

    public Categories getCategoryForStarterPage(){
        List<Categories> categories = getAllCategories();
        if(categories.isEmpty()){
            return null;
        }
        return categories.get(0);

    }

    public int countCategoriesByName(Categories category){
        String name = category.getName();
        Long id = category.getId();
        return  (int) getAllCategories().stream().filter(c -> c.getName().equals(name) && !c.getId().equals(id)).count();
    }

    public int countCategoriesByRequestId(Categories category){
        String requestId = category.getRequestId();
        Long id = category.getId();
        return  (int) getAllCategories().stream().filter(c -> c.getRequestId().equals(requestId) && !c.getId().equals(id)).count();
    }
}
