package com.ndirituedwin.Controller.CountiesSubsandWards;

import com.ndirituedwin.Dto.Requests.Wardssubountiesandcounties.SubCountyRequest;
import com.ndirituedwin.Dto.Responses.SecSchool.PagedResponse;
import com.ndirituedwin.Dto.Responses.Wardssubountiesandcounties.CountyResponse;
import com.ndirituedwin.Dto.Responses.Wardssubountiesandcounties.SubCountyResponse;
import com.ndirituedwin.Security.CurrentUser;
import com.ndirituedwin.Security.UserPrincipal;
import com.ndirituedwin.Service.Countiesandsubsandwards.SubCountyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static com.ndirituedwin.Config.Constants.ApiUtils.DEFAULT_PAGE_NUMBER;
import static com.ndirituedwin.Config.Constants.ApiUtils.DEFAULT_PAGE_SIZE;

@RestController
@RequestMapping("/api/subcounty")
@Slf4j
@AllArgsConstructor
public class SubCountryController {


    private final SubCountyService subCountyService;
    @PostMapping("/save")
    public ResponseEntity<?> savesubcounty(@Valid @RequestBody SubCountyRequest subCountyRequest, BindingResult result){

        if (result.hasErrors()){
            Map<String,String> errorMap=new HashMap<>();
            for (FieldError err: result.getFieldErrors()){
                errorMap.put(err.getField(),err.getDefaultMessage());
            }
            log.info("logging error map {}",errorMap);
            return new ResponseEntity<Map<String,String>>(errorMap,HttpStatus.BAD_REQUEST);
        }else{

        return new ResponseEntity<>(subCountyService.save(subCountyRequest),HttpStatus.CREATED);
        }

    }
    @PostMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody SubCountyRequest subCountyRequest, BindingResult result){

        if (result.hasErrors()){
            Map<String,String> errorMap=new HashMap<>();
            for (FieldError err: result.getFieldErrors()){
                errorMap.put(err.getField(),err.getDefaultMessage());
            }
            log.info("logging error map {}",errorMap);
            return new ResponseEntity<Map<String,String>>(errorMap,HttpStatus.BAD_REQUEST);
        }else{

            return new ResponseEntity<>(subCountyService.update(subCountyRequest),HttpStatus.CREATED);
        }

    }
    @GetMapping("/viewall")
    PagedResponse<SubCountyResponse> viewall(@CurrentUser UserPrincipal currentUser,
                                             @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                             @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE) int size) {
        return subCountyService.getallsubcounties(currentUser, page, size);

    }
    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/byid/fetch/{subcountyId}")
    public ResponseEntity<?> viewstudentdetails(@CurrentUser UserPrincipal currentUser, @PathVariable("subcountyId") Long subcountyId){
        return ResponseEntity.ok(subCountyService.fetchbyid(currentUser,subcountyId));
    }

}
