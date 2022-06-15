package com.ndirituedwin.Controller.Secskulbursaryapplication;

import com.ndirituedwin.Dto.Requests.Secschool.SchoolTypeRequest;
import com.ndirituedwin.Dto.Responses.Schooltyperesponse;
import com.ndirituedwin.Security.CurrentUser;
import com.ndirituedwin.Security.UserPrincipal;
import com.ndirituedwin.Service.SecondarySchool.Schooltypeservice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bursary/type")
@Slf4j
public class SchoolTypeController {


    private  final Schooltypeservice schooltypeservice;

    public SchoolTypeController(Schooltypeservice schooltypeservice) {
        this.schooltypeservice = schooltypeservice;
    }

    @PostMapping("/save")
    public ResponseEntity<?> savetype(@Valid @RequestBody SchoolTypeRequest schoolTypeRequest,BindingResult result){

        if (result.hasErrors()){
            Map<String,String> errorMap=new HashMap<>();
            for (FieldError err: result.getFieldErrors()){
                errorMap.put(err.getField(),err.getDefaultMessage());
            }
            log.info("logging error map {}",errorMap);
            return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Schooltyperesponse>(schooltypeservice.save(schoolTypeRequest),HttpStatus.CREATED);
    }
    @GetMapping("/fetchtypes")
    public ResponseEntity<?> schooltypes(@CurrentUser UserPrincipal currentUser){
        return ResponseEntity.ok(schooltypeservice.fetchallschooltypes(currentUser));
    }
}


