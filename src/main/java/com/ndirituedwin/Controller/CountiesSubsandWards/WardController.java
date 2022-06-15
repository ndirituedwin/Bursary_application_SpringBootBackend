package com.ndirituedwin.Controller.CountiesSubsandWards;

import com.ndirituedwin.Dto.Requests.Wardssubountiesandcounties.WardRequest;
import com.ndirituedwin.Dto.Responses.SecSchool.PagedResponse;
import com.ndirituedwin.Dto.Responses.Wardssubountiesandcounties.ViewAllWards;
import com.ndirituedwin.Dto.Responses.Wardssubountiesandcounties.WardResponse;
import com.ndirituedwin.Security.CurrentUser;
import com.ndirituedwin.Security.UserPrincipal;
import com.ndirituedwin.Service.Countiesandsubsandwards.WardService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static com.ndirituedwin.Config.Constants.ApiUtils.DEFAULT_PAGE_NUMBER;
import static com.ndirituedwin.Config.Constants.ApiUtils.DEFAULT_PAGE_SIZE;

@RestController
@RequestMapping("/api/ward")
@Slf4j
@AllArgsConstructor
public class WardController {
   private final WardService wardService;
    @PostMapping("/save")
    public ResponseEntity<?> saveward(@Valid @RequestBody WardRequest wardrequest, BindingResult result){

        if (result.hasErrors()){
            Map<String,String> errorMap=new HashMap<>();
            for (FieldError err: result.getFieldErrors()){
                errorMap.put(err.getField(),err.getDefaultMessage());
            }
            log.info("logging error map {}",errorMap);
            return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
        }



        return new ResponseEntity<WardResponse>(wardService.save(wardrequest),HttpStatus.CREATED);

    }


}
