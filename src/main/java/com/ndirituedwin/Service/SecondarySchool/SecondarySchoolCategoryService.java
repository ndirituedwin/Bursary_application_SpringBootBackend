package com.ndirituedwin.Service.SecondarySchool;

import com.ndirituedwin.Dto.Requests.Secschool.SecondarySchoolCategoryRequest;
import com.ndirituedwin.Dto.Responses.SecondarrySchoolCategoryResponse;
import com.ndirituedwin.Entity.SecondarySchoolCategory;
import com.ndirituedwin.Repository.SecondarySchoolCategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class SecondarySchoolCategoryService {

    private final SecondarySchoolCategoryRepository secondarySchoolCategoryRepository;


    public SecondarrySchoolCategoryResponse save(SecondarySchoolCategoryRequest secondarySchoolCategoryRequest) {
        if (secondarySchoolCategoryRepository.existsByCategory(secondarySchoolCategoryRequest.getCategory())){
       return  SecondarrySchoolCategoryResponse.builder()
               .category(secondarySchoolCategoryRequest.getCategory())
               .message("The category provide already exists, please choose another name")
               .build();
        }
        SecondarySchoolCategory secondarySchoolCategory =new SecondarySchoolCategory();
        SecondarySchoolCategory savedcategory;
            try {
                secondarySchoolCategory.setCategory(secondarySchoolCategoryRequest.getCategory());
             savedcategory= secondarySchoolCategoryRepository.save(secondarySchoolCategory);


            }catch (Exception exception){
                return SecondarrySchoolCategoryResponse.builder()
                        .category("").message("an exception has occurred while saving the category"+exception.getMessage()).build();
            }
            return SecondarrySchoolCategoryResponse.builder()
                    .category(savedcategory.getCategory())
                    .message("secondary school category saved successfully")
                    .build();




    }
}
