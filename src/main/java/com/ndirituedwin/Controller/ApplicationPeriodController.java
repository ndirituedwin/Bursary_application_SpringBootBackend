//package com.ndirituedwin.Controller;
//
//import com.ndirituedwin.Dto.Requests.ApplicationPeriodRequest;
//import com.ndirituedwin.Dto.Requests.ApplicationPeriodStatusrequest;
//import com.ndirituedwin.Dto.Requests.ApplicationperiodYearMonthsrequest;
//import com.ndirituedwin.Entity.ApplicationPeriod;
//import com.ndirituedwin.Security.CurrentUser;
//import com.ndirituedwin.Security.UserPrincipal;
//import com.ndirituedwin.Service.ApplicationPeriodService;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//@Slf4j
//@AllArgsConstructor
//@RestController
//@RequestMapping("/api/applicationperiod")
//public class ApplicationPeriodController {
//
//    private final ApplicationPeriodService applicationPeriodService;
//
//    @PostMapping("/save")
//    public ResponseEntity<?> save(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody ApplicationPeriodRequest applicationPeriodRequest, BindingResult result){
//        if (result.hasErrors()){
//            Map<String,String> errorMap=new HashMap<>();
//            for (FieldError err: result.getFieldErrors()){
//                errorMap.put(err.getField(),err.getDefaultMessage());
//            }
//            log.info("logging error map {}",errorMap);
//            return new ResponseEntity<Map<String,String>>(errorMap,HttpStatus.BAD_REQUEST);
//        }else{
//        return new ResponseEntity<>(applicationPeriodService.save(currentUser, applicationPeriodRequest), HttpStatus.CREATED);
//    }
//   }
//   @GetMapping("/viewall")
//    public List<ApplicationPeriod> getall(@CurrentUser UserPrincipal currentUser){
//        return applicationPeriodService.getall(currentUser);
//   }
//    @GetMapping("/openapplicationperiods/viewall")
//    public Set<?> openapplicationperiods(@CurrentUser UserPrincipal currentUser){
//        return applicationPeriodService.openapplicationperiods(currentUser);
//    }
//    @GetMapping("/openapplicationperiod/fetch")
//    public ResponseEntity<?> openapplicationperiod(@CurrentUser UserPrincipal currentUser){
//        return new ResponseEntity<>(applicationPeriodService.openapplicationperiod(currentUser),HttpStatus.OK);
//    }
//
//
//
//    @PostMapping("/fetchappliationperiodyearmonths/viewall")
//    public List<ApplicationPeriod> fetchappliationperiodyearmonths(@CurrentUser UserPrincipal currentUser , @Valid @RequestBody ApplicationperiodYearMonthsrequest applicationperiodYearMonthsrequest ){
//        return applicationPeriodService.fetchappliationperiodyearmonths(currentUser,applicationperiodYearMonthsrequest);
//    }
//    @GetMapping("/applicationperiodsyears/viewall")
//    public Set<?> applicationperiodsyears(@CurrentUser UserPrincipal currentUser){
//        return applicationPeriodService.applicationperiodsyears(currentUser);
//    }
//    @PostMapping("/fetchallappliationperiodyearmonths/viewall")
//    public List<ApplicationPeriod> fetchallappliationperiodyearmonths(@CurrentUser UserPrincipal currentUser , @Valid @RequestBody ApplicationperiodYearMonthsrequest applicationperiodYearMonthsrequest ){
//        return applicationPeriodService.fetchallappliationperiodyearmonths(currentUser,applicationperiodYearMonthsrequest);
//    }
//
//   @PutMapping("updateisopen")
//    public ResponseEntity<?> update(@CurrentUser UserPrincipal currentUser,@Valid @RequestBody ApplicationPeriodStatusrequest applicationPeriodStatusrequest){
//
//        return ResponseEntity.ok(applicationPeriodService.update(currentUser,applicationPeriodStatusrequest));
//
//   }
//    @PostMapping("checkisopen")
//    public ResponseEntity<?> chheckisopen(@CurrentUser UserPrincipal currentUser,@Valid @RequestBody ApplicationPeriodStatusrequest applicationPeriodStatusrequest){
//
//        return ResponseEntity.ok(applicationPeriodService.checkopenapplicationperiod(currentUser,applicationPeriodStatusrequest));
//
//    }
//
//
//
//}
