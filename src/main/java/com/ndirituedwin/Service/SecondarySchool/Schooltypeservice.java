package com.ndirituedwin.Service.SecondarySchool;

import com.ndirituedwin.Dto.Requests.Secschool.SchoolTypeRequest;
import com.ndirituedwin.Dto.Responses.ApiResponse;
import com.ndirituedwin.Dto.Responses.Schooltyperesponse;
import com.ndirituedwin.Entity.SchoolType;
import com.ndirituedwin.Repository.Schooltyperepository;
import com.ndirituedwin.Security.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


@Service
@Slf4j
@AllArgsConstructor
public class Schooltypeservice {
    private final Schooltyperepository schooltyperepository;

    public Schooltyperesponse save(SchoolTypeRequest schoolTypeRequest) {
        long schoolType=schooltyperepository.countByType(schoolTypeRequest.getType());
        SchoolType schoolType1=new SchoolType();
        if (!(schoolType>0)){
             schoolType1.setType(schoolTypeRequest.getType());
             try {
                 SchoolType schoolType2=schooltyperepository.save(schoolType1);
                 return Schooltyperesponse.builder()
                         .type(schoolType2.getType())
                         .message("school type saved ")
                         .build();
             }catch (Exception exception){
                 return Schooltyperesponse.builder()
                         .message("an exception has occurred while trying to save school type "+ exception.getMessage())
                         .build();
             }
         }
        return null;

    }


    @Transactional(readOnly = true)
    public Object fetchallschooltypes(UserPrincipal currentUser) {
       try {
           List<SchoolType> schoolTypes=schooltyperepository.findAll();
           if (schoolTypes.isEmpty()){
               return Collections.emptyList();
           }else{
               return schoolTypes;
           }
       }catch (Exception ex){
           return ApiResponse.builder().success(false).message("an error has occurred while fetching schooltypes "+ex.getMessage()).httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).build();
       }
    }
}