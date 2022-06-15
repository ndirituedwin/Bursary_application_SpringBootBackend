package com.ndirituedwin.Controller.CountiesSubsandWards;

import com.ndirituedwin.Dto.Requests.Wardssubountiesandcounties.FetchsubcountiesbycountyRequest;
import com.ndirituedwin.Dto.Requests.Wardssubountiesandcounties.FetchwardsbysubcountyRequest;
import com.ndirituedwin.Dto.Requests.Wardssubountiesandcounties.SaveCountyRequest;
import com.ndirituedwin.Dto.Responses.SecSchool.PagedResponse;
import com.ndirituedwin.Dto.Responses.Wardssubountiesandcounties.CountyResponse;
import com.ndirituedwin.Dto.Responses.Wardssubountiesandcounties.SubCountyResponse;
import com.ndirituedwin.Security.CurrentUser;
import com.ndirituedwin.Security.UserPrincipal;
import com.ndirituedwin.Service.Countiesandsubsandwards.CountyService;
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
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/county")
public class CountyController {

    private CountyService countyService;

    @GetMapping("/viewall")
    PagedResponse<CountyResponse> viewall(@CurrentUser UserPrincipal currentUser,
                                          @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                          @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE) int size) {
        return countyService.getallcounties(currentUser, page, size);

    }

    @PostMapping("/fetchcountysubcounties")
    public ResponseEntity<?> fetchcountysubcounties(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody FetchsubcountiesbycountyRequest fetchsubcountiesbycountyRequest,
                                                    @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                    @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE) int size
    ) {
        return ResponseEntity.ok(countyService.fetchcountysubcounties(currentUser, fetchsubcountiesbycountyRequest, page, size));
    }

    @PostMapping("/fetchsubcountywards")
    public ResponseEntity<?> fetchsubcountywards(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody FetchwardsbysubcountyRequest fetchwardsbysubcountyRequest,
                                                 @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                 @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE) int size
    ) {
        return ResponseEntity.ok(countyService.fetchsubcountywards(currentUser, fetchwardsbysubcountyRequest, page, size));
    }

    @PostMapping("/save")
    public ResponseEntity<?> savecounty(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody SaveCountyRequest saveCountyRequest, BindingResult result ) {
        if (result.hasErrors()){
            Map<String,String> errorMap=new HashMap<>();
            for (FieldError err: result.getFieldErrors()){
                errorMap.put(err.getField(),err.getDefaultMessage());
            }
            log.info("logging error map {}",errorMap);
            return new ResponseEntity<Map<String,String>>(errorMap,HttpStatus.BAD_REQUEST);
        }else{
        return new ResponseEntity<>(countyService.createcounty(currentUser, saveCountyRequest), HttpStatus.CREATED);
    }
}
}
