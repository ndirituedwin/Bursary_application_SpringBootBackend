package com.ndirituedwin.Service.HigherEducation;


import com.ndirituedwin.Dto.Requests.HigherEducation.HigherEducationCategoryRequest;
import com.ndirituedwin.Dto.Responses.HigherEducationCategoryResponse;
import com.ndirituedwin.Entity.Bursary.HigherLearning.HigherEducationnCategory;
import com.ndirituedwin.Repository.HigherEducationCategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class HigherEducationCategoryService {
    private final HigherEducationCategoryRepository higherEducationCategoryRepository;


    public Object save(HigherEducationCategoryRequest higherEducationCategoryRequest) {

        if (higherEducationCategoryRepository.existsByCategory(higherEducationCategoryRequest.getCategory())){
            return new ResponseEntity<>(
                    HigherEducationCategoryResponse.builder()
                            .message("the category already exists")
                            .build()
                    , HttpStatus.BAD_REQUEST);
        }
        try {
            HigherEducationnCategory higherEducationnCategory =new HigherEducationnCategory();
            higherEducationnCategory.setCategory(higherEducationCategoryRequest.getCategory());
            HigherEducationnCategory saved= higherEducationCategoryRepository.save(higherEducationnCategory);
            return new ResponseEntity<>(HigherEducationCategoryResponse.builder().category(saved.getCategory()).httpStatus(HttpStatus.CREATED).message("category saved").build(),HttpStatus.CREATED);
        }catch (Exception e){
            return  ResponseEntity.internalServerError().body(
                    HigherEducationCategoryResponse.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).message("an exception ha occurred while trying to save highereducation "+e.getMessage()).build()
            );
        }
    }
}
