package com.ndirituedwin.Controller.Secskulbursaryapplication;


import com.ndirituedwin.Dto.Requests.Secschool.*;
import com.ndirituedwin.Dto.Requests.Secschool.CheckParentOnparentIdKeyUpRequest;
import com.ndirituedwin.Dto.Responses.SecSchool.BursaryAwardsResponse;
import com.ndirituedwin.Dto.Responses.SecSchool.PagedResponse;
import com.ndirituedwin.Dto.Responses.SecSchool.SchoolResponse;
import com.ndirituedwin.Dto.Responses.SecSchool.SecSchoolStudentResponse;
import com.ndirituedwin.Dto.Responses.SecondarySchoolResponse;
import com.ndirituedwin.Entity.SecondarySchoolCategory;
import com.ndirituedwin.Security.CurrentUser;
import com.ndirituedwin.Security.UserPrincipal;
import com.ndirituedwin.Service.SecondarySchool.SecondarySchoolsService;
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
import java.util.List;
import java.util.Map;

import static com.ndirituedwin.Config.Constants.ApiUtils.DEFAULT_PAGE_NUMBER;
import static com.ndirituedwin.Config.Constants.ApiUtils.DEFAULT_PAGE_SIZE;

@RestController
@RequestMapping("/api/bursary/secondaryschools")
@Slf4j
@AllArgsConstructor
public class SecondarySchoolsController {
    private final SecondarySchoolsService secondarySchoolsService;

    @PostMapping("/save")
    public ResponseEntity<?>  save(@Valid @RequestBody SecondarySchoolsRequest secondarySchoolsRequest, BindingResult result){
        if (result.hasErrors()){
            Map<String ,String> errormap=new HashMap<>();
            for (FieldError error: result.getFieldErrors()){
            errormap.put(error.getField(),error.getDefaultMessage());
                log.info("logging error map {}",errormap);
                return new ResponseEntity<Map<String,String>>(errormap, HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<SecondarySchoolResponse>(secondarySchoolsService.save(secondarySchoolsRequest),HttpStatus.CREATED);
    }


    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/getschools")
    public PagedResponse<SchoolResponse> getallsecschools(@CurrentUser UserPrincipal currentUser,
                                                          @RequestParam(value = "page",defaultValue =DEFAULT_PAGE_NUMBER) int page,
                                                          @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE)  int size
    ){
        return secondarySchoolsService.fetchallsecondaryschools(currentUser,page,size);
    }

    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/getsecondaryschoolstudents")
    public PagedResponse<SecSchoolStudentResponse> getsecondaryschoolstudents(@CurrentUser UserPrincipal currentUser,
                                                                              @RequestParam(value = "page",defaultValue =DEFAULT_PAGE_NUMBER) int page,
                                                                              @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE)  int size
    ){
        return secondarySchoolsService.fetchallsecondaryschoolstudents(currentUser,page,size);
    }
    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/fetchsecschoolbyid")
    public ResponseEntity<?> fetchsecschoolbyid(@CurrentUser UserPrincipal currentUser,@Valid @RequestBody FetchSecSchoolByIdRequest fetchSecSchoolByIdRequest){
        return ResponseEntity.ok(secondarySchoolsService.fetchsecschoolbyid(currentUser,fetchSecSchoolByIdRequest));
    }

    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/applyforabursary")
    public ResponseEntity<?> appybursary(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody Bursaryapplicationrequest bursaryapplicationrequest,BindingResult result){

        if (result.hasErrors()){
            Map<String,String> errorMap=new HashMap<>();
            for (FieldError err: result.getFieldErrors()){
                errorMap.put(err.getField(),err.getDefaultMessage());
            }
            log.info("logging error map {}",errorMap);
            return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(secondarySchoolsService.applybursary(currentUser, bursaryapplicationrequest), HttpStatus.CREATED);
    }
    @PostMapping("/checkparentonkeyup")
    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
   public ResponseEntity<?> getparentonKeyUp(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody CheckParentOnparentIdKeyUpRequest checkParentOnparentIdKeyUpRequest ){
        return ResponseEntity.ok(secondarySchoolsService.checkparentonidnumberkeyup(currentUser,checkParentOnparentIdKeyUpRequest));
    }
    @PostMapping("/checkbystudentadmnoandschool")
    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<?> checkbystudentadmnoandschool(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody Checkstudentbyadmnoandschool checkstudentbyadmnoandschool ){
        return ResponseEntity.ok(secondarySchoolsService.checkbystudentadmnoandschool(currentUser,checkstudentbyadmnoandschool));
    }
    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @PutMapping("/approveordisaprove/")
    public ResponseEntity<?> applybursary(@CurrentUser UserPrincipal currentuser,@Valid @RequestBody UpdateIsApprovedRequest updateIsApprovedRequestd){
        return  ResponseEntity.ok(secondarySchoolsService.apdateisapproved(currentuser,updateIsApprovedRequestd));

    }
    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @PutMapping("/approveordisaprovearray/")
    public Object approveordisaprovearray(@CurrentUser UserPrincipal currentuser,@Valid @RequestBody List<Long> ids){
        return  ResponseEntity.ok(secondarySchoolsService.approveordisaprovearray(currentuser,ids));

    }

    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/viewapplications")
    public ResponseEntity<?> vieapplications(@CurrentUser UserPrincipal currentuser,
                                                                  @RequestParam(value = "page",defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                                  @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE) int size ){

        return ResponseEntity.ok(secondarySchoolsService.getbursaryapplications(currentuser,page,size));

    }
    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/getbursaryapplicationstoupate")
    public ResponseEntity<?> getbursaryapplicationstoupate(@CurrentUser UserPrincipal currentuser,
                                             @RequestParam(value = "page",defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                             @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE) int size ){

        return ResponseEntity.ok(secondarySchoolsService.getbursaryapplicationstoupate(currentuser,page,size));

    }


    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/awardbursaryasindividual")
    public ResponseEntity<?> awardbursary(@CurrentUser UserPrincipal currentUser, @RequestBody Awardbursaryrequest awardbursaryrequest, BindingResult result){
        if (result.hasErrors()){
            Map<String,String> errorMap=new HashMap<>();
            for (FieldError err: result.getFieldErrors()){
                errorMap.put(err.getField(),err.getDefaultMessage());
            }
            log.info("logging error map {}",errorMap);
            return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(secondarySchoolsService.awardbursary(currentUser,awardbursaryrequest),HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/awardbursary")
    public ResponseEntity<?> awardbursaryy(@CurrentUser UserPrincipal currentUser, @RequestBody AwardBursary awardbursary, BindingResult result){
        if (result.hasErrors()){
            Map<String,String> errorMap=new HashMap<>();
            for (FieldError err: result.getFieldErrors()){
                errorMap.put(err.getField(),err.getDefaultMessage());
            }
            log.info("logging error map {}",errorMap);
            return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(secondarySchoolsService.awardbursaryy(currentUser,awardbursary),HttpStatus.CREATED);
    }


    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/awardbursaryasindividuall")
//    public List<String> awardbursaryasindividuall(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody List<String> awardbursary, BindingResult result){
    public Object awardbursaryasindividuall(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody List<String> awardbursary, BindingResult result){
//        if (result.hasErrors()){
//            Map<String,String> errorMap=new HashMap<>();
//            for (FieldError err: result.getFieldErrors()){
//                errorMap.put(err.getField(),err.getDefaultMessage());
//            }
//            log.info("logging error map {}",errorMap);
//            return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
//        }
//        return new ResponseEntity<String>(secondarySchoolsService.awardbursaryasindividual(currentUser,awardbursary),HttpStatus.CREATED);
        return secondarySchoolsService.awardbursaryasindividual(currentUser,awardbursary);
    }




    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/viewdetails/{bursaryapplicationId}")
    public ResponseEntity<?> viewstudentdetails(@CurrentUser UserPrincipal currentUser, @PathVariable("bursaryapplicationId") Long bursaryapplicationId){
        return ResponseEntity.ok(secondarySchoolsService.viewstudentDetails(currentUser,bursaryapplicationId));
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/viewapplicationsbyschoolid/{schoolId}")
    public ResponseEntity<?> viewapplicationsbyschool(@CurrentUser UserPrincipal currentUser, @PathVariable("schoolId") Long schoolId,
                                                      @RequestParam(value = "page",defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                      @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE) int size){

        return ResponseEntity.ok(secondarySchoolsService.findAllBySchoolId(currentUser,schoolId,page,size));
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/viewapplicationsbyapplicationyearandmonth")
    public ResponseEntity<?> viewapplicationsbyapplicationyearandmont(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody FetcBursaryApplicationsBy fetcBursaryApplicationsBy,
                                                      @RequestParam(value = "page",defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                      @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE) int size){

        return ResponseEntity.ok(secondarySchoolsService.findAllByBursaryApplicationsByApplicationYearandmonth(currentUser,fetcBursaryApplicationsBy,page,size));
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/awardbursarybyschool")
    public ResponseEntity<?> awardbursarybysecschools(@CurrentUser UserPrincipal currentUser,@Valid @RequestBody AwardBursarybysecschoolCategoryrequest awardBursarybysecschoolCategoryrequest){
        return new ResponseEntity<>(secondarySchoolsService.awardbursarybysecschoolCategory(currentUser, awardBursarybysecschoolCategoryrequest),HttpStatus.CREATED);
    }


    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/fetchschoolcategories")
    public List<SecondarySchoolCategory> schoolcategories(@CurrentUser UserPrincipal currentUser){
        return secondarySchoolsService.fetchschoolcategories(currentUser);
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/fetchsbursaryapplicationssbyschoolcategory/{categoryId}")
    public ResponseEntity<?> fetchstudentsbyschoolcategory(@CurrentUser UserPrincipal currentUser,@PathVariable("categoryId") Long categoryId,
                                                               @RequestParam(value = "page",defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                               @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE) int size
                                                               ){

        return ResponseEntity.ok(secondarySchoolsService.fetchsbursaryapplicationssbyschoolcategory(currentUser,categoryId,page,size));


    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/fetchbursaryawards")
    PagedResponse<BursaryAwardsResponse> fetchbursaryawards(@CurrentUser UserPrincipal currentUser, @RequestParam(value = "page",defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                            @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE) int size){

        return secondarySchoolsService.fetchallbursaryawards(currentUser,page,size);
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/fetchbursaryawardsbysecschool")
    PagedResponse<BursaryAwardsResponse> fetchbursaryawardsbysecschool(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody BursaryAwardsBySecSchRequest bursaryAwardsBySecSchRequest, @RequestParam(value = "page",defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                            @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE) int size){

        return secondarySchoolsService.fetchallbursaryawardsbysecschool(currentUser,bursaryAwardsBySecSchRequest,page,size);
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/fetchbursaryawardsbysecschoolstudentId")
    PagedResponse<BursaryAwardsResponse> fetchbursaryawardsbysecschoolstudentId(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody BursaryAwardsBySecSchRequest bursaryAwardsBySecSchRequest, @RequestParam(value = "page",defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                                       @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE) int size){

        return secondarySchoolsService.fetchbursaryawardsbysecschoolstudentId(currentUser,bursaryAwardsBySecSchRequest,page,size);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/fetchbursaryawardsbysecschoolcategory")
    PagedResponse<BursaryAwardsResponse> fetchbursaryawardsbysecschoolcategory(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody BursaryAwardsBySecSchRequest bursaryAwardsBySecSchRequest, @RequestParam(value = "page",defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                            @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE) int size){

        return secondarySchoolsService.fetchbursaryawardsbysecschoolcategory(currentUser,bursaryAwardsBySecSchRequest,page,size);
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/fetchawardsbyapplicationperiodyear/viewall")
    PagedResponse<BursaryAwardsResponse> fetchawardsbyapplicationperiodyear(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody BursaryAwardsBySecSchRequest bursaryAwardsBySecSchRequest, @RequestParam(value = "page",defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                                               @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE) int size){

        return secondarySchoolsService.fetchawardsbyapplicationperiodyear(currentUser,bursaryAwardsBySecSchRequest,page,size);
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/fetchawardsbyapplicationperiodyearandmonth/viewall")
    PagedResponse<BursaryAwardsResponse> fetchawardsbyapplicationperiodyearandmonth(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody BursaryAwardsBySecSchRequest bursaryAwardsBySecSchRequest, @RequestParam(value = "page",defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                                               @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE) int size){

        return secondarySchoolsService.fetchawardsbyapplicationperiodyearandmonth(currentUser,bursaryAwardsBySecSchRequest,page,size);
    }


}
