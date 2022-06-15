package com.ndirituedwin.Controller.HigherLearning;


import com.ndirituedwin.Dto.Requests.HigherEducation.AwardBursaryHigherEducation;
import com.ndirituedwin.Dto.Requests.HigherEducation.CheckhigherStudentByadmnoandschool;
import com.ndirituedwin.Dto.Requests.HigherEducation.HigherEducationApplRequest;
import com.ndirituedwin.Dto.Requests.HigherEducation.HigherEducationRequest;
import com.ndirituedwin.Dto.Requests.Secschool.*;
import com.ndirituedwin.Dto.Responses.HigherEducationResponse;
import com.ndirituedwin.Dto.Responses.HigherLearning.HigherLearningBursaryAwardsResponse;
import com.ndirituedwin.Dto.Responses.SecSchool.BursaryAwardsResponse;
import com.ndirituedwin.Dto.Responses.SecSchool.PagedResponse;
import com.ndirituedwin.Dto.Responses.SecSchool.SchoolResponse;
import com.ndirituedwin.Dto.Responses.SecSchool.SecSchoolStudentResponse;
import com.ndirituedwin.Security.CurrentUser;
import com.ndirituedwin.Security.UserPrincipal;
import com.ndirituedwin.Service.HigherEducation.HigherEducationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ndirituedwin.Config.Constants.ApiUtils.DEFAULT_PAGE_NUMBER;
import static com.ndirituedwin.Config.Constants.ApiUtils.DEFAULT_PAGE_SIZE;

@RestController
@RequestMapping("/api/bursary/highereducation")
@AllArgsConstructor
public class HigherEducationController {
    private final HigherEducationService higherEducationService;

    @PostMapping("/save")
    public ResponseEntity<?> Save(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody HigherEducationRequest higherEducationRequest, BindingResult result){

        if (result.hasErrors()){
            Map<String,String> errorMap=new HashMap<>();
            for (FieldError err: result.getFieldErrors()){
                errorMap.put(err.getField(),err.getDefaultMessage());
            }
            return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(higherEducationService.save(currentUser,higherEducationRequest),HttpStatus.CREATED);
        }
    }
    @GetMapping("/getall")
    public List<?> highereducationcategorylist(@CurrentUser UserPrincipal currentUser){

        return  higherEducationService.getall();
    }
    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/fetchallhigherlearningschools")
    public PagedResponse<HigherEducationResponse> fetchallhigherlearningschools(@CurrentUser UserPrincipal currentUser,
                                                                                @RequestParam(value = "page",defaultValue =DEFAULT_PAGE_NUMBER) int page,
                                                                                @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE)  int size
    ){
        return higherEducationService.fetchallhigherlearningschools(currentUser,page,size);
    }

    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/savehigheeducationbursaryapplication")
    public Object savehigheeducationbursaryapplication(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody HigherEducationApplRequest higherEducationApplRequest, BindingResult result){

        if (result.hasErrors()){
            Map<String,String> errorMap=new HashMap<>();
            for (FieldError err: result.getFieldErrors()){
                errorMap.put(err.getField(),err.getDefaultMessage());
            }
            return new ResponseEntity<Map<String,String>>(errorMap,HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(higherEducationService.applybursary(currentUser,higherEducationApplRequest),HttpStatus.CREATED);
        }
    }
    @PostMapping("/checkbyhigherstudentadmnoandschool")
    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<?> checkbyhigherstudentadmnoandschool(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody CheckhigherStudentByadmnoandschool checkhigherStudentByadmnoandschool ){
        return ResponseEntity.ok(higherEducationService.checkbyhigherstudentadmnoandschool(currentUser,checkhigherStudentByadmnoandschool));
    }
    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/getbursaryapplicationstoupate")
    public ResponseEntity<?> getbursaryapplicationstoupate(@CurrentUser UserPrincipal currentuser,
                                                           @RequestParam(value = "page",defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                           @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE) int size ){

        return ResponseEntity.ok(higherEducationService.getbursaryapplicationstoupate(currentuser,page,size));

    }


    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @PutMapping("/approveordisaprove/")
    public ResponseEntity<?> applybursary(@CurrentUser UserPrincipal currentuser,@Valid @RequestBody UpdateIsApprovedRequest updateIsApprovedRequestd){
        return  ResponseEntity.ok(higherEducationService.apdateisapproved(currentuser,updateIsApprovedRequestd));

    }
    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @PutMapping("/approveordisaprovearray/")
    public Object approveordisaprovearray(@CurrentUser UserPrincipal currentuser,@Valid @RequestBody List<Long> ids){
        return  ResponseEntity.ok(higherEducationService.approveordisaprovearray(currentuser,ids));

    }

    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/viewapplications")
    public ResponseEntity<?> vieapplications(@CurrentUser UserPrincipal currentuser,
                                             @RequestParam(value = "page",defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                             @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE) int size ){

        return ResponseEntity.ok(higherEducationService.getbursaryapplications(currentuser,page,size));

    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/viewapplicationsbyapplicationyearandmonth")
    public ResponseEntity<?> viewapplicationsbyapplicationyearandmont(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody FetcBursaryApplicationsBy fetcBursaryApplicationsBy,
                                                                      @RequestParam(value = "page",defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                                      @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE) int size){

        return ResponseEntity.ok(higherEducationService.findAllByBursaryApplicationsByApplicationYearandmonth(currentUser,fetcBursaryApplicationsBy,page,size));
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/fetchsbursaryapplicationssbyschoolcategory/{categoryId}")
    public ResponseEntity<?> fetchstudentsbyschoolcategory(@CurrentUser UserPrincipal currentUser,@PathVariable("categoryId") Long categoryId,
                                                           @RequestParam(value = "page",defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                           @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE) int size
    ){

        return ResponseEntity.ok(higherEducationService.fetchsbursaryapplicationssbyschoolcategory(currentUser,categoryId,page,size));


    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/viewapplicationsbyschoolid/{schoolId}")
    public ResponseEntity<?> viewapplicationsbyschool(@CurrentUser UserPrincipal currentUser, @PathVariable("schoolId") Long schoolId,
                                                      @RequestParam(value = "page",defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                      @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE) int size){

        return ResponseEntity.ok(higherEducationService.findAllBySchoolId(currentUser,schoolId,page,size));
    }

    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/awardbursary")
    public ResponseEntity<?> awardbursaryy(@CurrentUser UserPrincipal currentUser, @RequestBody AwardBursaryHigherEducation awardBursaryHigherEducation, BindingResult result){
        if (result.hasErrors()){
            Map<String,String> errorMap=new HashMap<>();
            for (FieldError err: result.getFieldErrors()){
                errorMap.put(err.getField(),err.getDefaultMessage());
            }
            return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(higherEducationService.awardbursaryy(currentUser,awardBursaryHigherEducation),HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/awardbursaryasindividuall")
    public Object awardbursaryasindividuall(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody List<String> awardbursary, BindingResult result){
       return higherEducationService.awardbursaryasindividual(currentUser,awardbursary);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/fetchawardsbyapplicationperiodyear/viewall")
    PagedResponse<HigherLearningBursaryAwardsResponse> fetchawardsbyapplicationperiodyear(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody BursaryAwardsBySecSchRequest bursaryAwardsBySecSchRequest, @RequestParam(value = "page",defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                                                          @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE) int size){

        return higherEducationService.fetchawardsbyapplicationperiodyear(currentUser,bursaryAwardsBySecSchRequest,page,size);
    }


    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/fetchawardsbyapplicationperiodyearandmonth/viewall")
    PagedResponse<HigherLearningBursaryAwardsResponse> fetchawardsbyapplicationperiodyearandmonth(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody BursaryAwardsBySecSchRequest bursaryAwardsBySecSchRequest, @RequestParam(value = "page",defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                                                    @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE) int size){

        return higherEducationService.fetchawardsbyapplicationperiodyearandmonth(currentUser,bursaryAwardsBySecSchRequest,page,size);
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/fetchbursaryawardsbysecschoolcategory")
    PagedResponse<HigherLearningBursaryAwardsResponse> fetchbursaryawardsbysecschoolcategory(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody BursaryAwardsBySecSchRequest bursaryAwardsBySecSchRequest, @RequestParam(value = "page",defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                                               @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE) int size){

        return higherEducationService.fetchbursaryawardsbysecschoolcategory(currentUser,bursaryAwardsBySecSchRequest,page,size);
    }
    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/getsecondaryschoolstudents")
    public PagedResponse<SecSchoolStudentResponse> getsecondaryschoolstudents(@CurrentUser UserPrincipal currentUser,
                                                                              @RequestParam(value = "page",defaultValue =DEFAULT_PAGE_NUMBER) int page,
                                                                              @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE)  int size
    ){
        return higherEducationService.fetchallsecondaryschoolstudents(currentUser,page,size);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/fetchbursaryawardsbysecschool")
    PagedResponse<HigherLearningBursaryAwardsResponse> fetchbursaryawardsbysecschool(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody BursaryAwardsBySecSchRequest bursaryAwardsBySecSchRequest, @RequestParam(value = "page",defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                                       @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE) int size){

        return higherEducationService.fetchallbursaryawardsbysecschool(currentUser,bursaryAwardsBySecSchRequest,page,size);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/fetchbursaryawardsbysecschoolstudentId")
    PagedResponse<HigherLearningBursaryAwardsResponse> fetchbursaryawardsbysecschoolstudentId(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody BursaryAwardsBySecSchRequest bursaryAwardsBySecSchRequest, @RequestParam(value = "page",defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                                                @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE) int size){

        return higherEducationService.fetchbursaryawardsbysecschoolstudentId(currentUser,bursaryAwardsBySecSchRequest,page,size);
    }
}
