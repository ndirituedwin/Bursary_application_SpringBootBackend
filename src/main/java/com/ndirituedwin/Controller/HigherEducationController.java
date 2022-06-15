//package com.ndirituedwin.Controller;
//
//import com.ndirituedwin.Dto.Requests.HigherEducation.HigherEducationApplRequest;
//import com.ndirituedwin.Dto.Requests.HigherEducation.HigherEducationRequest;
//import com.ndirituedwin.Dto.Responses.HigherEducationResponse;
//import com.ndirituedwin.Dto.Responses.SecSchool.PagedResponse;
//import com.ndirituedwin.Security.CurrentUser;
//import com.ndirituedwin.Security.UserPrincipal;
//import com.ndirituedwin.Service.HigherEducationService;
//import lombok.AllArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static com.ndirituedwin.Config.Constants.ApiUtils.DEFAULT_PAGE_NUMBER;
//import static com.ndirituedwin.Config.Constants.ApiUtils.DEFAULT_PAGE_SIZE;
//
//@RestController
//@RequestMapping("/api/bursary/highereducation")
//@AllArgsConstructor
//public class HigherEducationController {
//
////    private final HigherEducationService higherEducationService;
////
////    @PostMapping("/save")
////    public ResponseEntity<?> Save(@CurrentUser UserPrincipal currentUser,@Valid @RequestBody HigherEducationRequest higherEducationRequest, BindingResult result){
////
////        if (result.hasErrors()){
////            Map<String,String> errorMap=new HashMap<>();
////            for (FieldError err: result.getFieldErrors()){
////                errorMap.put(err.getField(),err.getDefaultMessage());
////            }
////            return new ResponseEntity<Map<String,String>>(errorMap,HttpStatus.BAD_REQUEST);
////        }else{
////        return new ResponseEntity<>(higherEducationService.save(currentUser,higherEducationRequest),HttpStatus.CREATED);
////        }
////    }
////    @GetMapping("/getall")
////    public List<?> highereducationcategorylist(@CurrentUser UserPrincipal currentUser){
////
////        return  higherEducationService.getall();
////    }
////    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
////    @GetMapping("/fetchallhigherlearningschools")
////    public PagedResponse<HigherEducationResponse> fetchallhigherlearningschools(@CurrentUser UserPrincipal currentUser,
////                                                                   @RequestParam(value = "page",defaultValue =DEFAULT_PAGE_NUMBER) int page,
////                                                                   @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE)  int size
////    ){
////        return higherEducationService.fetchallhigherlearningschools(currentUser,page,size);
////    }
////
////    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
////    @PostMapping("/savehigheeducationbursaryapplication")
////    public Object savehigheeducationbursaryapplication(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody HigherEducationApplRequest higherEducationApplRequest, BindingResult result){
////
////        if (result.hasErrors()){
////            Map<String,String> errorMap=new HashMap<>();
////            for (FieldError err: result.getFieldErrors()){
////                errorMap.put(err.getField(),err.getDefaultMessage());
////            }
////            return new ResponseEntity<Map<String,String>>(errorMap,HttpStatus.BAD_REQUEST);
////        }else{
////            return new ResponseEntity<>(higherEducationService.applybursary(currentUser,higherEducationApplRequest),HttpStatus.CREATED);
////        }
////    }
//
//}
