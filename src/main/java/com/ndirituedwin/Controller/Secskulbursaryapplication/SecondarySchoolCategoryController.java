package com.ndirituedwin.Controller.Secskulbursaryapplication;

import com.ndirituedwin.Dto.Requests.Secschool.SecondarySchoolCategoryRequest;
import com.ndirituedwin.Dto.Responses.SecondarrySchoolCategoryResponse;
import com.ndirituedwin.Service.SecondarySchool.SecondarySchoolCategoryService;
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
@RequestMapping("/api/bursary/secondaryschoolcategory")
@Slf4j
@AllArgsConstructor
public class SecondarySchoolCategoryController {

    private final SecondarySchoolCategoryService secondarySchoolCategoryService;

    @PostMapping("/save")
    public ResponseEntity<?> savesecondaryschooolcategory(@Valid @RequestBody SecondarySchoolCategoryRequest secondarySchoolCategoryRequest, BindingResult result){

        if (result.hasErrors()){
            Map<String,String> errorMap=new HashMap<>();
            for (FieldError err: result.getFieldErrors()){
                errorMap.put(err.getField(),err.getDefaultMessage());
            }
            log.info("logging error map {}",errorMap);
            return new ResponseEntity<Map<String,String>>(errorMap,HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<SecondarrySchoolCategoryResponse>(secondarySchoolCategoryService.save(secondarySchoolCategoryRequest), HttpStatus.CREATED);
    }



}
