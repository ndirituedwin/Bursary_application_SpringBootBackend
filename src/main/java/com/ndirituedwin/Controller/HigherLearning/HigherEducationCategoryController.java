package com.ndirituedwin.Controller.HigherLearning;

import com.ndirituedwin.Dto.Requests.HigherEducation.HigherEducationCategoryRequest;
import com.ndirituedwin.Service.HigherEducation.HigherEducationCategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/bursary/highereducationcategory")
@Slf4j
@AllArgsConstructor
public class HigherEducationCategoryController {


    private final HigherEducationCategoryService higherEducationserviceCategory;

    @PostMapping("/save")
    public Object savehighereducation(@Valid @RequestBody HigherEducationCategoryRequest higherEducationCategoryRequest, BindingResult result){
        if (result.hasErrors()){
            Map<String,String> errorMap=new HashMap<>();
            for (FieldError err: result.getFieldErrors()){
                errorMap.put(err.getField(),err.getDefaultMessage());
            }
            return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(higherEducationserviceCategory.save(higherEducationCategoryRequest),HttpStatus.CREATED);
        }

    }
}
