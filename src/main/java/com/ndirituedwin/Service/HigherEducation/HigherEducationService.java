package com.ndirituedwin.Service.HigherEducation;

import com.ndirituedwin.Dto.Requests.HigherEducation.*;
import com.ndirituedwin.Dto.Requests.Secschool.*;
import com.ndirituedwin.Dto.Responses.ApiResponse;
import com.ndirituedwin.Dto.Responses.HigherEducationResponse;
import com.ndirituedwin.Dto.Responses.HigherLearning.HigherLearningBursaryAwardsResponse;
import com.ndirituedwin.Dto.Responses.SecSchool.*;
import com.ndirituedwin.Entity.ApplicationPeriod;
import com.ndirituedwin.Entity.Bursary.HigherLearning.*;
import com.ndirituedwin.Entity.Bursary.Secskuls.BursaryApplication;
import com.ndirituedwin.Entity.Bursary.Secskuls.BursaryFormSecskuls;
import com.ndirituedwin.Entity.Bursary.Secskuls.SecStudent;
import com.ndirituedwin.Entity.SecondarySchool;
import com.ndirituedwin.Entity.SecondarySchoolCategory;
import com.ndirituedwin.Exception.AppException;
import com.ndirituedwin.Exception.CouldNotBeFoundException;
import com.ndirituedwin.Repository.*;
import com.ndirituedwin.Security.UserPrincipal;
import com.ndirituedwin.Service.Mapper.ModelMappper;
import com.ndirituedwin.Service.SecondarySchool.SecondarySchoolsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Time;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class HigherEducationService {
    private final SecondarySchoolsService secondarySchoolsService;
    private final HigherEducationRepository higherEducationRepository;
    private final HigherEducationCategoryRepository higherEducationCategoryRepository;
    private final ApplicationPeriodRepo applicationPeriodRepo;
    private final HigherLearningStudentRepo higherLearningStudentRepo;
    private final HigherLearningBursaryApplicationRepo higherLearningBursaryApplicationRepo;
    private final HigherLearningBursaryFormRepo higherLearningBursaryFormRepo;
    public HigherEducationResponse save(UserPrincipal currentUser, HigherEducationRequest higherEducationRequest) {

        if(higherEducationRepository.existsByName( higherEducationRequest.getName())){
//            return new ResponseEntity<>(HigherEducationResponse.builder().message("the name is already taken, choose another name please !").build(),HttpStatus.BAD_REQUEST);
            return HigherEducationResponse.builder().message("the name is already taken, choose another name please !").build();
        }
        try {
            HigherEducationnCategory exists=higherEducationCategoryRepository.findById(higherEducationRequest.getHigheEducationCategoryId()).orElseThrow(() -> new UsernameNotFoundException("an exception has occurred "));
            log.info("logging HigherEducationcategoryexists {}",exists);
            HigherEducationn n=new HigherEducationn();
            n.setName(higherEducationRequest.getName());
            n.setHigherEducationnCategory(exists);
            HigherEducationn saved=higherEducationRepository.save(n);
            log.info("logging saved  highereducation {}",saved);
            return HigherEducationResponse.builder().name(saved.getName()).higherEducationnCategory(saved.getHigherEducationnCategory()).message("saved !"+saved.getName()).build();


//            return new ResponseEntity<>(HigherEducationResponse.builder().higherEducationnCategory(saved.getHigherEducationnCategory()).name(saved.getName()).message("saved"),HttpStatus.CREATED);
        }catch (Exception e){
            return HigherEducationResponse.builder().message("an exception has occurred while saving name "+e.getMessage()).build();
        }
    }

    public List<?> getall() {
        try {
            List<HigherEducationnCategory> higherEducationCategories= new ArrayList<>(higherEducationCategoryRepository.findAll());
            if (higherEducationCategories.isEmpty()){
                return Collections.emptyList();
            }
            return higherEducationCategories;

        }catch (Exception exception){
            throw new RuntimeException("an exception has occurred while getting HigherLearningPackage categories "+exception.getMessage());
        }
    }

    public PagedResponse<HigherEducationResponse> fetchallhigherlearningschools(UserPrincipal currentUser, int page, int size) {

        secondarySchoolsService.validatePagenumberandSize(page, size);
        //getting schools as pageable
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "name");
        log.info("logging higherEducations pageable {}", pageable);
        Page<HigherEducationn> higherEducations = higherEducationRepository.findAll(pageable);
        log.info("logging higherEducations from server {}", higherEducations);


        if (higherEducations.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), higherEducations.getNumber(), higherEducations.getSize(), higherEducations.getTotalElements(), higherEducations.getTotalPages(), higherEducations.isLast());
        }
        List<HigherEducationResponse> higherEducationsresponses = higherEducations.map(ModelMappper::maphighereducationtohighereducationresponse).getContent();
        log.info("logginghigherEducationsresponses {}", higherEducationsresponses);
        return new PagedResponse<>(higherEducationsresponses, higherEducations.getNumber(), higherEducations.getSize(), higherEducations.getTotalElements(), higherEducations.getTotalPages(), higherEducations.isLast());

    }

    @Transactional
    public Object applybursary(UserPrincipal currentUser, HigherEducationApplRequest higherEducationApplRequest) {
   log.info("higherEducationApplRequest {}",higherEducationApplRequest);
        if (currentUser==null){
            return ApiResponse.builder().message("You are Not authenticated").httpStatus(HttpStatus.UNAUTHORIZED).success(false).build();
        }
        HigherEducationn higherEducationn =higherEducationRepository.findById(higherEducationApplRequest.getSchool()).orElseThrow(() -> new AppException("The Higher Institution with The given Id "+higherEducationApplRequest.getSchool()+" coud Not be found"));
        ApplicationPeriod applicationPeriod=applicationPeriodRepo.findByYearAndMonth(higherEducationApplRequest.getApplicationperiodyear(), higherEducationApplRequest.getApplicationperiodmonth()).orElseThrow(() -> new AppException("The application period "+higherEducationApplRequest.getApplicationperiodyear()+" "+higherEducationApplRequest.getApplicationperiodmonth()+" could not be found"));
        /**check whether The student existsbyadmnoandsecschool t**/
         try {
             String  savedstudentadmissionnumber;
             boolean existsbyadmnoandsecschool=higherLearningStudentRepo.existsByAdmissionnumberAndCollege(higherEducationApplRequest.getAdmissionnumber(),higherEducationn);
              if (existsbyadmnoandsecschool){
                  log.info("Goes Here {}",existsbyadmnoandsecschool);
                  HigherLearningStudent existinghigherLearningStudent=higherLearningStudentRepo.findByAdmissionnumberAndCollege(higherEducationApplRequest.getAdmissionnumber(),higherEducationn).orElseThrow(() -> new CouldNotBeFoundException("Could Not be found exception"));
                   boolean existsbystudentandapplicationperiod=higherLearningBursaryApplicationRepo.existsByHigherLearningStudentAndApplicationPeriod(existinghigherLearningStudent,applicationPeriod);
                  if (existsbystudentandapplicationperiod){
                      return Bursaryapplicationresponse.builder()
                                .status(HttpStatus.BAD_REQUEST)
                                .message("Application exists by student " + existinghigherLearningStudent.getAdmissionnumber() + " , application year " + higherEducationApplRequest.getApplicationperiodyear() + " and by application  month " + higherEducationApplRequest.getApplicationperiodmonth())
                                .issuccess(false)
                                .build();
                  }else{
                      HigherLearningBursaryApplication higherLearningBursaryApplication=new HigherLearningBursaryApplication();
                      higherLearningBursaryApplication.setHigherLearningStudent(existinghigherLearningStudent);
                      higherLearningBursaryApplication.setCreatedBy(currentUser.getId());
                      higherLearningBursaryApplication.setUpdatedBy(currentUser.getId());
                      higherLearningBursaryApplication.setApplicationPeriod(applicationPeriod);
                      HigherLearningBursaryApplication savedhigh=higherLearningBursaryApplicationRepo.save(higherLearningBursaryApplication);
                      String admno=savedhigh.getHigherLearningStudent().getAdmissionnumber();
                      savedstudentadmissionnumber=admno;

                  }
              }else{
                  //The student does not exists,insert a new student
                  log.info("The student does not exists,insert a new student {}",higherEducationApplRequest);
                  HigherLearningStudent higherLearningStudent=new HigherLearningStudent();
                  higherLearningStudent.setFullname(higherEducationApplRequest.getFullname());
                  higherLearningStudent.setCollege(higherEducationn);
                  higherLearningStudent.setAdmissionnumber(higherEducationApplRequest.getAdmissionnumber());
                  higherLearningStudent.setIdnumber(higherEducationApplRequest.getIdnumber());
                  higherLearningStudent.setPhonenumber(higherEducationApplRequest.getPhonenumber());
                  higherLearningStudent.setVoterscard(higherEducationApplRequest.getVoterscard());
                  higherLearningStudent.setYearofadmission(higherEducationApplRequest.getYearofadmission());
                  higherLearningStudent.setDurationofcourse(higherEducationApplRequest.getDurationofcourse());
                  higherLearningStudent.setCreatedBy(currentUser.getId());
                  higherLearningStudent.setUpdatedBy(currentUser.getId());
                  log.info("HigherLearnigStudent {}",higherLearningStudent);
                  HigherLearningStudent  saved=higherLearningStudentRepo.save(higherLearningStudent);
                   boolean exists=higherLearningBursaryApplicationRepo.existsByHigherLearningStudentAndApplicationPeriod(saved,applicationPeriod);
                  log.info("saved higherlearning student {}",saved);
                   if (exists){
                       return Bursaryapplicationresponse.builder()
                                .status(HttpStatus.BAD_REQUEST)
                                .message("Application exists by student " + saved.getAdmissionnumber() + " , application  year " + higherEducationApplRequest.getApplicationperiodyear() + " and by application  month " + higherEducationApplRequest.getApplicationperiodmonth())
                                .issuccess(false)
                                .build();
                   }else{
                       HigherLearningBursaryApplication higherLearningBursaryApplication=new HigherLearningBursaryApplication();
                       higherLearningBursaryApplication.setHigherLearningStudent(saved);
                       higherLearningBursaryApplication.setCreatedBy(currentUser.getId());
                       higherLearningBursaryApplication.setUpdatedBy(currentUser.getId());
                       higherLearningBursaryApplication.setApplicationPeriod(applicationPeriod);
                       HigherLearningBursaryApplication savedhigh=higherLearningBursaryApplicationRepo.save(higherLearningBursaryApplication);
                      log.info("saved HigherLearning BursaryApplication {}",savedhigh);
                       String admno=savedhigh.getHigherLearningStudent().getAdmissionnumber();
                       savedstudentadmissionnumber=admno;
                   }

              }
             return Bursaryapplicationresponse.builder().status(HttpStatus.CREATED).message("Bursary application successful for  " + savedstudentadmissionnumber).issuccess(true).build();


         }catch (Exception exception){
             return Bursaryapplicationresponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("An exception has occurred while saving student details " + exception.getMessage())
                    .issuccess(false)
                    .build();
                    }

    }

    public Object checkbyhigherstudentadmnoandschool(UserPrincipal currentUser, CheckhigherStudentByadmnoandschool checkhigherStudentByadmnoandschool) {

        if (currentUser==null){
            return ApiResponse.builder().success(false).message("you are not authenticated").httpStatus(HttpStatus.UNAUTHORIZED).build();
        }
         boolean college=higherEducationRepository.existsById(checkhigherStudentByadmnoandschool.getSchoolId());
        if (!college) {
            return false;
        }else {
          try {
         HigherEducationn higherEducationn=higherEducationRepository.findById(checkhigherStudentByadmnoandschool.getSchoolId()).orElseThrow(() -> new CouldNotBeFoundException("The college with The provided id could not be found "+checkhigherStudentByadmnoandschool.getSchoolId()));
         boolean campusstudent=higherLearningStudentRepo.existsByAdmissionnumberAndCollege(checkhigherStudentByadmnoandschool.getStudentAdmno(),higherEducationn);
          if (campusstudent) {
              HigherLearningStudent higherLearningStudent=higherLearningStudentRepo.findByAdmissionnumberAndCollege(checkhigherStudentByadmnoandschool.getStudentAdmno(),higherEducationn).orElseThrow(() -> new CouldNotBeFoundException("The student could be found"));
               return CheckhigherStudentByadmnoandschool.builder().flag(true)
                       .fullname(higherLearningStudent.getFullname())
                       .idnumber(higherLearningStudent.getIdnumber())
                      .voterscard(higherLearningStudent.getVoterscard())
                      .yearofadmission(higherLearningStudent.getYearofadmission())
                      .phonenumber(higherLearningStudent.getPhonenumber())
                       .durationofcourse(higherLearningStudent.getDurationofcourse())
                       .build();
          }else {
              return new ApiResponse(false, "no matches found  for " + checkhigherStudentByadmnoandschool.getStudentAdmno(), HttpStatus.BAD_REQUEST);

          }
          }catch (Exception ex) {
              return new ApiResponse(false, "an exception has happened while trying to get student details " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

          }
        }
    }

    public Object apdateisapproved(UserPrincipal currentuser, UpdateIsApprovedRequest updateIsApprovedRequestd) {

        HigherLearningBursaryApplication bursaryApplication= higherLearningBursaryApplicationRepo.findById(updateIsApprovedRequestd.getApplicationId()).orElseThrow(() -> new CouldNotBeFoundException("the bursary application with given id "+updateIsApprovedRequestd.getApplicationId()+" could not be found "));
        if (bursaryApplication.isIsapproved()){
            bursaryApplication.setIsapproved(false);
        }else{
            bursaryApplication.setIsapproved(true);
        }
        bursaryApplication.setUpdatedBy(currentuser.getId());

        return  higherLearningBursaryApplicationRepo.save(bursaryApplication).isIsapproved();
    }

    public Object approveordisaprovearray(UserPrincipal currentuser, List<Long> ids) {
        final AwardBursaryResponse[] awardBursaryResponse = {null};
        StringBuilder stringBuilder=new StringBuilder();
        if (ids.isEmpty() || ids==null){
            return ApiResponse.builder().success(false).httpStatus(HttpStatus.BAD_REQUEST).message("Empty list ").build();
        }else{
            ids.forEach(aLong -> {
                boolean existsbyid=higherLearningBursaryApplicationRepo.existsById(aLong);
                if (!existsbyid){
                    log.info("does not exists {}",aLong);
                }else{
                    HigherLearningBursaryApplication bursaryApplication=higherLearningBursaryApplicationRepo.findById(aLong).orElseThrow(() -> new CouldNotBeFoundException("Bursary application with provided Id could not be found"));
                    stringBuilder.append(" ").append(aLong);
                    if (bursaryApplication.isIsapproved()){
                        bursaryApplication.setIsapproved(false);
                    }else{
                        bursaryApplication.setIsapproved(true);
                    }
                    bursaryApplication.setUpdatedBy(currentuser.getId());
                    higherLearningBursaryApplicationRepo.save(bursaryApplication);
                    awardBursaryResponse[0] = AwardBursaryResponse.builder()
                            .status(HttpStatus.CREATED)
                            .message("status updated for "+stringBuilder)
                            .build();                }
            });
            if (!(awardBursaryResponse[0] == null)) {
                return awardBursaryResponse[0];
            }
        }
        return null;
    }

    public Object getbursaryapplicationstoupate(UserPrincipal currentuser, int page, int size) {
        LocalDate currentDate=LocalDate.now();
        String currentMonth=String.valueOf(currentDate.getMonth()).toLowerCase();
        String curMonth=currentMonth.substring(0,1).toUpperCase()+currentMonth.substring(1);
        String curYear=String.valueOf(currentDate.getYear());
        log.info("current year {}",curYear);
        log.info("current month {}",curMonth);

        if (currentuser == null) {
            return ApiResponse.builder().message("The user is null");
        }
        secondarySchoolsService.validatePagenumberandSize(page, size);
        try {
            log.info("before pageable");
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");
            log.info("after pageable {}",pageable);
//            Page<BursaryApplication> bursaryApplicationPage = bursaryApplicationRepo.findAll(pageable);
            Page<HigherLearningBursaryApplication> bursaryApplicationPage = higherLearningBursaryApplicationRepo.findAll(pageable);
            log.info("logging bursary applications {}", bursaryApplicationPage);
            if (bursaryApplicationPage.getNumberOfElements() == 0) {
                return new PagedResponse<>(Collections.emptyList(), bursaryApplicationPage.getNumber(), bursaryApplicationPage.getSize(), bursaryApplicationPage.getTotalElements(), bursaryApplicationPage.getTotalPages(), bursaryApplicationPage.isLast());
            }
            List<AllHigherLearningBursaryapplications> allbursaryapplications = bursaryApplicationPage.map(ModelMappper::maphigherlearningbapplicationstoallhigherlearningbapplications).getContent();
            log.info("logging  allbursaryapplications {}", allbursaryapplications);
            return new PagedResponse<>(allbursaryapplications, bursaryApplicationPage.getNumber(), bursaryApplicationPage.getSize(), bursaryApplicationPage.getTotalElements(), bursaryApplicationPage.getTotalPages(), bursaryApplicationPage.isLast());
        } catch (Exception ex) {
            return ApiResponse.builder().message("An error has occurred while fetching bursary applications " + ex.getMessage()).build();
        }
    }

    public Object getbursaryapplications(UserPrincipal currentuser, int page, int size) {
        LocalDate currentDate=LocalDate.now();
        String currentMonth=String.valueOf(currentDate.getMonth()).toLowerCase();
        String curMonth=currentMonth.substring(0,1).toUpperCase()+currentMonth.substring(1);
        String curYear=String.valueOf(currentDate.getYear());
        if (currentuser == null) {
            return ApiResponse.builder().message("The user is null");
        }
        secondarySchoolsService.validatePagenumberandSize(page, size);
        try {
            log.info("before pageable");
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");
            Page<HigherLearningBursaryApplication> bursaryApplicationPage = higherLearningBursaryApplicationRepo.findAllByIsapprovedEqualsAndApplicationPeriodYearAndApplicationPeriodMonth(true,curYear,curMonth,pageable);
            log.info("logging bursary applications {}", bursaryApplicationPage);
            if (bursaryApplicationPage.getNumberOfElements() == 0) {
                return new PagedResponse<>(Collections.emptyList(), bursaryApplicationPage.getNumber(), bursaryApplicationPage.getSize(), bursaryApplicationPage.getTotalElements(), bursaryApplicationPage.getTotalPages(), bursaryApplicationPage.isLast());
            }
            List<AllHigherLearningBursaryapplications> allbursaryapplications = bursaryApplicationPage.map(ModelMappper::maphigherlearningbapplicationstoallhigherlearningbapplications).getContent();
            log.info("logging  allbursaryapplications {}", allbursaryapplications);
            return new PagedResponse<>(allbursaryapplications, bursaryApplicationPage.getNumber(), bursaryApplicationPage.getSize(), bursaryApplicationPage.getTotalElements(), bursaryApplicationPage.getTotalPages(), bursaryApplicationPage.isLast());
        } catch (Exception ex) {
            return ApiResponse.builder().message("An error has occurred while fetching bursary applications " + ex.getMessage()).build();
        }
    }

    public Object findAllByBursaryApplicationsByApplicationYearandmonth(UserPrincipal currentUser, FetcBursaryApplicationsBy fetcBursaryApplicationsBy, int page, int size) {

        if (currentUser == null) {
            return ApiResponse.builder().success(false).httpStatus(HttpStatus.UNAUTHORIZED).message("The user is null").build();
        }
        log.info("logging fetcBursaryApplicationsBy {}",fetcBursaryApplicationsBy);
        try {
            secondarySchoolsService.validatePagenumberandSize(page, size);
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
            boolean existsbyyearandmonth = applicationPeriodRepo.existsByYearAndMonth(fetcBursaryApplicationsBy.getYear(),fetcBursaryApplicationsBy.getMonth());
            log.info("existsbyyearandmonth {}",existsbyyearandmonth);
            if (!existsbyyearandmonth){
                log.info("does not exists");
                return ApiResponse.builder().success(false).httpStatus(HttpStatus.BAD_REQUEST).message(" application year and months " + fetcBursaryApplicationsBy.getYear()+" "+fetcBursaryApplicationsBy.getMonth()+" provided not found");

            }else{

                ApplicationPeriod applicationPeriod = applicationPeriodRepo.findByYearAndMonth(fetcBursaryApplicationsBy.getYear(),fetcBursaryApplicationsBy.getMonth()).orElseThrow(() -> new AppException("application period with the provided year " + fetcBursaryApplicationsBy.getYear() + " could not be found"));
                log.info("logging pageable {}", pageable);
                log.info("logging applicationPeriod {}", applicationPeriod);

                Page<HigherLearningBursaryApplication> bursaryApplicationPage=higherLearningBursaryApplicationRepo.findAllByIsapprovedEqualsAndApplicationPeriod(true,applicationPeriod,pageable);
                log.info("bursaryApplicationPage {}",bursaryApplicationPage);
                if (bursaryApplicationPage.getNumberOfElements() == 0) {
                    return new PagedResponse<>(Collections.emptyList(), bursaryApplicationPage.getNumber(), bursaryApplicationPage.getSize(), bursaryApplicationPage.getNumberOfElements(), bursaryApplicationPage.getTotalPages(), bursaryApplicationPage.isLast());
                }
                List<AllHigherLearningBursaryapplications> allbursaryapplications = bursaryApplicationPage.map(ModelMappper::maphigherlearningbapplicationstoallhigherlearningbapplications).getContent();
                log.info("logging viewbursary applications {}", allbursaryapplications);
                return new PagedResponse<>(allbursaryapplications, bursaryApplicationPage.getNumber(), bursaryApplicationPage.getSize(), bursaryApplicationPage.getTotalElements(), bursaryApplicationPage.getTotalPages(), bursaryApplicationPage.isLast());

            }
        } catch (Exception e) {
            return ApiResponse.builder().success(false).httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).message(" An axception has occurred " + e.getMessage()).build();
        }

    }

    @Transactional(readOnly = true)
    public Object fetchsbursaryapplicationssbyschoolcategory(UserPrincipal currentUser, Long categoryId, int page, int size) {

        if (currentUser==null){
            return ApiResponse.builder().success(false).httpStatus(HttpStatus.UNAUTHORIZED).message("user are unauthenticated").build();
        }
        try {
            secondarySchoolsService.validatePagenumberandSize(page, size);
            Pageable pageable=PageRequest.of(page,size,Sort.Direction.DESC,"createdAt");
            HigherEducationnCategory secondarySchoolCategory=higherEducationCategoryRepository.findById(categoryId).orElseThrow(() -> new AppException("school with category provided could not be found "+categoryId));
            //fetching secondaryschools for given school category
            Page<HigherEducationn> secondarySchools=higherEducationRepository.findByHigherEducationnCategory(secondarySchoolCategory,pageable);
            if (secondarySchools.getNumberOfElements()==0){
                return new PagedResponse<>(Collections.emptyList(),secondarySchools.getNumber(),secondarySchools.getSize(),secondarySchools.getTotalElements(),secondarySchools.getTotalPages(),secondarySchools.isLast());
            }
            List<Long>secoids=secondarySchools.map(HigherEducationn::getId).getContent();
            Page<HigherLearningBursaryApplication> bursaryApplicationPage1= (Page<HigherLearningBursaryApplication>) higherLearningBursaryApplicationRepo.findByHigherEducationnIn(secoids,pageable);
            if (bursaryApplicationPage1.getNumberOfElements()==0){
                return new PagedResponse<>(Collections.emptyList(),secondarySchools.getNumber(),secondarySchools.getSize(),secondarySchools.getTotalElements(),secondarySchools.getTotalPages(),secondarySchools.isLast());
            }
            List<AllHigherLearningBursaryapplications> allbursaryapplications=bursaryApplicationPage1.map(ModelMappper::maphigherlearningbapplicationstoallhigherlearningbapplications).getContent();

            log.info("logging viewbursaryapplications {}",allbursaryapplications);
            return new PagedResponse<>(allbursaryapplications,bursaryApplicationPage1.getNumber(),bursaryApplicationPage1.getSize(),bursaryApplicationPage1.getTotalElements(),bursaryApplicationPage1.getTotalPages(),bursaryApplicationPage1.isLast());



        }catch (Exception exception){
            throw new RuntimeException(exception);
//            return ApiResponse.builder().message("An exception has occurred while attempting to fetch students by school category "+exception.getMessage()).build();
        }
    }

    @Transactional(readOnly = true)
    public Object findAllBySchoolId(UserPrincipal currentUser, Long schoolId, int page, int size) {

        if (currentUser == null) {
            return ApiResponse.builder().success(false).httpStatus(HttpStatus.UNAUTHORIZED).message("The user is null").build();
        }
        try {
            secondarySchoolsService.validatePagenumberandSize(page, size);
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
            HigherEducationn secondarySchool = higherEducationRepository.findById(schoolId).orElseThrow(() -> new AppException("school with the provided id " + schoolId + " could not be found"));

            Page<HigherLearningBursaryApplication> bursaryApplicationPage=higherLearningBursaryApplicationRepo.findAllByHigherEducationn(secondarySchool,pageable);
            log.info("bursaryApplicationPage {}",bursaryApplicationPage);
            if (bursaryApplicationPage.getNumberOfElements() == 0) {
                return new PagedResponse<>(Collections.emptyList(), bursaryApplicationPage.getNumber(), bursaryApplicationPage.getSize(), bursaryApplicationPage.getNumberOfElements(), bursaryApplicationPage.getTotalPages(), bursaryApplicationPage.isLast());
            }
            List<AllHigherLearningBursaryapplications> allbursaryapplications = bursaryApplicationPage.map(ModelMappper::maphigherlearningbapplicationstoallhigherlearningbapplications).getContent();
            log.info("logging viewbursary applications {}", allbursaryapplications);
            return new PagedResponse<>(allbursaryapplications, bursaryApplicationPage.getNumber(), bursaryApplicationPage.getSize(), bursaryApplicationPage.getTotalElements(), bursaryApplicationPage.getTotalPages(), bursaryApplicationPage.isLast());
        } catch (Exception e) {
            return ApiResponse.builder().success(false).httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).message(" An axception has occurred " + e.getMessage()).build();
        }
    }


    public Object awardbursaryy(UserPrincipal currentUser, AwardBursaryHigherEducation awardBursaryHigherEducation) {
        log.info("awardbursaryrequest {}",awardBursaryHigherEducation);
        final AwardBursaryResponse[] awardBursaryResponse = {null};

        if (awardBursaryHigherEducation.getAmount()==null){
            return AwardBursaryResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .amountmaynotbenull(" amount may not be null")
                    .build();
        }
        try {
            awardBursaryHigherEducation.getAwardrequest().forEach(awardRequest -> {
                boolean bursaryapplicationexistsbyid=higherLearningBursaryApplicationRepo.existsById(awardRequest.getId());
                if (!bursaryapplicationexistsbyid){
                    log.info("The bursary application with the given application id could not be found");
                }else{
                    HigherLearningBursaryApplication bursaryApplication = higherLearningBursaryApplicationRepo.findById(awardRequest.getId()).orElseThrow(() -> new CouldNotBeFoundException("The bursary appllication could not be found"));
                    if (!bursaryApplication.isIsapproved()){
                        log.info("Bursary application has not yet been approved ");
                    }else {
                        Boolean applicationPeriod=applicationPeriodRepo.existsByIdAndIsOpenEquals(bursaryApplication.getApplicationPeriod().getId(),true);
                        if (!applicationPeriod){
                            log.info("The application period is either not open or it does not exists");
                        }else{
                            boolean existsbybursaryapplication=higherLearningBursaryFormRepo.existsByHigherLearningBursaryApplication(bursaryApplication);
                            if (existsbybursaryapplication){
                                //update the existing award
                                boolean doesstudentexists=higherLearningStudentRepo.existsById(bursaryApplication.getHigherLearningStudent().getId());
                                if (!doesstudentexists){
                                    log.info("the student could not be found");
                                }else{
                                    //the student exists go ahead and update the existig
                                    HigherLearningBursaryForm bursaryFormSecskuls=higherLearningBursaryFormRepo.findByHigherLearningBursaryApplication(bursaryApplication).orElseThrow(() -> new CouldNotBeFoundException("Bursary award with provided bursary application could not be found "));
                                    BigDecimal existingamount=bursaryFormSecskuls.getAmount();
                                    if (existingamount.equals(awardBursaryHigherEducation.getAmount())){
                                        log.info("bursary award amounts are similar");
                                    }else{
                                        log.info("we are updating ");
                                        bursaryFormSecskuls.setAmount(awardBursaryHigherEducation.getAmount());
                                        bursaryFormSecskuls.setUpdatedBy(currentUser.getId());
                                        HigherLearningBursaryForm edited=higherLearningBursaryFormRepo.save(bursaryFormSecskuls);
                                        log.info("data successfully updated {}",edited);
                                        awardBursaryResponse[0]=AwardBursaryResponse.builder()
                                                .status(HttpStatus.CREATED)
                                                .higherLearningBursaryForm(edited)
                                                .message("The existing bursary of  " + existingamount + " has been updated to " + edited.getAmount())
                                                .build();

                                    }

                                }
                            }else{
                                //save a new bursary award;

                                HigherLearningBursaryForm bursaryFormSecskuls = new HigherLearningBursaryForm();
                                bursaryFormSecskuls.setSchoolid(awardRequest.getHigherEducationn().getId());
                                bursaryFormSecskuls.setStudentid(bursaryApplication.getHigherLearningStudent().getId());
                                bursaryFormSecskuls.setAmount(awardBursaryHigherEducation.getAmount());
                                bursaryFormSecskuls.setHigherLearningBursaryApplication(bursaryApplication);
                                bursaryFormSecskuls.setCreatedBy(currentUser.getId());
                                bursaryFormSecskuls.setUpdatedBy(currentUser.getId());
                                HigherLearningBursaryForm saved = higherLearningBursaryFormRepo.save(bursaryFormSecskuls);
                                log.info("logging saved bursary form sec school awarded bursary {}", saved);
                                awardBursaryResponse[0] = AwardBursaryResponse.builder()
                                        .status(HttpStatus.CREATED)
//                                .bursaryFormSecskuls(bursaryFormSecskuls)
                                        .message("Bursary awarded successfully of Kshs" + saved.getAmount() + " to every student")
                                        .build();
                            }

                        }
                    }
                }
            });
            if (!(awardBursaryResponse[0] == null)) {
                return awardBursaryResponse[0];
            }


            return null;


        }catch (Exception exception){
            return new ApiResponse(false,"an exception has ocurred while awarding the bursary "+exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

        }


    }



    @Transactional
    public Object awardbursaryasindividual(UserPrincipal currentUser, List<String> awardbursary) {
        log.info("aardbursary {}",awardbursary);
        final AwardBursaryResponse[] awardBursaryResponse = {null};
        if (awardbursary==null || awardbursary.isEmpty()){
            log.info("log info {}",awardbursary);
            return AwardBursaryResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("empty fields")
                    .build();
        }
        List<String> strings= new ArrayList<>(awardbursary);
        log.info("awardbursary {}", awardbursary);
        Map<String,String> map=new HashMap<>();
        strings.forEach(s -> {
            try {
                map.put(s.split("[':']")[0],s.split("[':']")[1]);

            }catch (ArrayIndexOutOfBoundsException exception){
                log.info("exceprion {}",exception.getMessage());
            }
        });
        map.forEach((applid, amount) -> {
            if (applid.equals("") || applid.isEmpty()||applid.isBlank() || amount.equals("")|| amount.isEmpty()|| amount.isBlank()){
                log.info("empty applid {} or empty amount {}",applid,amount);
            }else{
                boolean bursaryapplicationexistsbyid=higherLearningBursaryApplicationRepo.existsById(Long.parseLong(applid));
                if (!bursaryapplicationexistsbyid){
                    log.info("bursary application does not exist");
                }else{
                    HigherLearningBursaryApplication bursaryApplication=higherLearningBursaryApplicationRepo.findById(Long.parseLong(applid)).orElseThrow(() -> new CouldNotBeFoundException("Bursary application with the provided id "+applid+" could not be found"));
                    if (!bursaryApplication.isIsapproved()){
                        log.info("bursary application is not approved");
                    }else{
                        Boolean applicationPeriod=applicationPeriodRepo.existsByIdAndIsOpenEquals(bursaryApplication.getApplicationPeriod().getId(),true);
                        if(!applicationPeriod){
                            log.info("The application period is either not open or it does not exists");

                        }else{
                            boolean existsByBursaryApplication=higherLearningBursaryFormRepo.existsByHigherLearningBursaryApplication(bursaryApplication);
                            if(existsByBursaryApplication){

                                //update the existing award
                                boolean doesstudentexists=higherLearningStudentRepo.existsById(bursaryApplication.getHigherLearningStudent().getId());
                                if (!doesstudentexists){
                                    log.info("the student could not be found");
                                }else{
                                    //the student exists go ahead and update the existig
                                    HigherLearningBursaryForm bursaryFormSecskuls=higherLearningBursaryFormRepo.findByHigherLearningBursaryApplication(bursaryApplication).orElseThrow(() -> new CouldNotBeFoundException("Bursary award with provided bursary application could not be found "));
                                    BigDecimal existingamount=bursaryFormSecskuls.getAmount();
                                    if (existingamount.equals(BigDecimal.valueOf(Long.parseLong(amount)))){
                                        log.info("bursary award amounts are similar");
                                    }else{
                                        log.info("we are updating ");
                                        bursaryFormSecskuls.setAmount(BigDecimal.valueOf(Long.parseLong(amount)));
                                        bursaryFormSecskuls.setUpdatedBy(currentUser.getId());
                                        HigherLearningBursaryForm edited=higherLearningBursaryFormRepo.save(bursaryFormSecskuls);
                                        log.info("data successfully updated {}",edited);
                                        awardBursaryResponse[0]=AwardBursaryResponse.builder()
                                                .status(HttpStatus.CREATED)
                                                .higherLearningBursaryForm(edited)
                                                .message("The existing bursary of  " + existingamount + " has been updated to " + edited.getAmount())
                                                .build();

                                    }

                                }
                            }else{
                                //save a new bursary applicatin
                                //save a new bursary award;

                                HigherLearningBursaryForm bursaryFormSecskuls = new HigherLearningBursaryForm();
                                bursaryFormSecskuls.setSchoolid(bursaryApplication.getHigherLearningStudent().getCollege().getId());
                                bursaryFormSecskuls.setStudentid(bursaryApplication.getHigherLearningStudent().getId());
                                bursaryFormSecskuls.setAmount(BigDecimal.valueOf(Long.parseLong(amount)));
                                bursaryFormSecskuls.setHigherLearningBursaryApplication(bursaryApplication);
                                bursaryFormSecskuls.setCreatedBy(currentUser.getId());
                                bursaryFormSecskuls.setUpdatedBy(currentUser.getId());
                                HigherLearningBursaryForm saved =higherLearningBursaryFormRepo.save(bursaryFormSecskuls);
                                log.info("logging saved bursary form sec school awarded bursary {}", saved);
                                awardBursaryResponse[0] = AwardBursaryResponse.builder()
                                        .status(HttpStatus.CREATED)
//                                .bursaryFormSecskuls(bursaryFormSecskuls)
                                        .message("Bursary awarded successfully of Kshs" + saved.getAmount() + " to every student")
                                        .build();
                            }
                        }
                    }
                }

            }
        });
        if (!(awardBursaryResponse[0] == null)) {
            return awardBursaryResponse[0];
        }



        return null;



    }

    public PagedResponse<HigherLearningBursaryAwardsResponse> fetchawardsbyapplicationperiodyear(UserPrincipal currentUser, BursaryAwardsBySecSchRequest bursaryAwardsBySecSchRequest, int page, int size) {
        secondarySchoolsService.validatePagenumberandSize(page, size);
        try {
            Pageable pageable=PageRequest.of(page, size,Sort.Direction.ASC,"createdAt");
            Page<HigherLearningBursaryForm> bursaryFormSecskuls=higherLearningBursaryFormRepo.findAllByApplicationYear(bursaryAwardsBySecSchRequest.getYear(),pageable);

            log.info("logging bursary awards {}", bursaryFormSecskuls);
            if (bursaryFormSecskuls.getNumberOfElements() == 0) {
                return new PagedResponse<>(Collections.emptyList(), bursaryFormSecskuls.getNumber(), bursaryFormSecskuls.getSize(), bursaryFormSecskuls.getTotalElements(), bursaryFormSecskuls.getTotalPages(), bursaryFormSecskuls.isLast());
            }
            List<HigherLearningBursaryAwardsResponse> bursaryAwardsResponses = bursaryFormSecskuls.map(ModelMappper::mapHigherLearningBursaryFormSchoolsToHigherBursaryAwardRespons).getContent();
            log.info("logging bursaryawardresponse {}",bursaryAwardsResponses);
            return new PagedResponse<>(bursaryAwardsResponses, bursaryFormSecskuls.getNumber(), bursaryFormSecskuls.getSize(), bursaryFormSecskuls.getTotalElements(), bursaryFormSecskuls.getTotalPages(), bursaryFormSecskuls.isLast());


        }catch (Exception e){
            throw new RuntimeException("An exception has occurred while fetching bursary awards "+e.getMessage());
        }

    }

    public PagedResponse<HigherLearningBursaryAwardsResponse> fetchawardsbyapplicationperiodyearandmonth(UserPrincipal currentUser, BursaryAwardsBySecSchRequest bursaryAwardsBySecSchRequest, int page, int size) {

        secondarySchoolsService.validatePagenumberandSize(page, size);
        try {
            Pageable pageable=PageRequest.of(page, size,Sort.Direction.ASC,"createdAt");
            Page<HigherLearningBursaryForm> bursaryFormSecskuls=higherLearningBursaryFormRepo.findAllByApplicationYearandMonth(bursaryAwardsBySecSchRequest.getYear(),bursaryAwardsBySecSchRequest.getMonth(),pageable);

            log.info("logging bursary awards {}", bursaryFormSecskuls);
            if (bursaryFormSecskuls.getNumberOfElements() == 0) {
                return new PagedResponse<>(Collections.emptyList(), bursaryFormSecskuls.getNumber(), bursaryFormSecskuls.getSize(), bursaryFormSecskuls.getTotalElements(), bursaryFormSecskuls.getTotalPages(), bursaryFormSecskuls.isLast());
            }
            List<HigherLearningBursaryAwardsResponse> bursaryAwardsResponses = bursaryFormSecskuls.map(ModelMappper::mapHigherLearningBursaryFormSchoolsToHigherBursaryAwardRespons).getContent();
            log.info("logging bursaryawardresponse {}",bursaryAwardsResponses);
            return new PagedResponse<>(bursaryAwardsResponses, bursaryFormSecskuls.getNumber(), bursaryFormSecskuls.getSize(), bursaryFormSecskuls.getTotalElements(), bursaryFormSecskuls.getTotalPages(), bursaryFormSecskuls.isLast());


        }catch (Exception e){
            throw new RuntimeException("An exception has occurred while fetching bursary awards "+e.getMessage());
        }
    }

    public PagedResponse<HigherLearningBursaryAwardsResponse> fetchbursaryawardsbysecschoolcategory(UserPrincipal currentUser, BursaryAwardsBySecSchRequest bursaryAwardsBySecSchRequest, int page, int size) {
        secondarySchoolsService.validatePagenumberandSize(page, size);
        HigherEducationnCategory secondarySchoolCategory=higherEducationCategoryRepository.findById(bursaryAwardsBySecSchRequest.getSchoolCategoryId()).orElseThrow(() -> new CouldNotBeFoundException("secondary school category with provided id "+bursaryAwardsBySecSchRequest.getSchoolCategoryId()+" ould not be found"));
        try {
            Pageable pageable=PageRequest.of(page, size,Sort.Direction.ASC,"createdAt");
            Page<HigherLearningBursaryForm> bursaryFormSecskuls=higherLearningBursaryFormRepo.findAllByHigherEducationCategory(secondarySchoolCategory,pageable);

            log.info("logging bursary awards {}", bursaryFormSecskuls);
            if (bursaryFormSecskuls.getNumberOfElements() == 0) {
                return new PagedResponse<>(Collections.emptyList(), bursaryFormSecskuls.getNumber(), bursaryFormSecskuls.getSize(), bursaryFormSecskuls.getTotalElements(), bursaryFormSecskuls.getTotalPages(), bursaryFormSecskuls.isLast());
            }
            List<HigherLearningBursaryAwardsResponse> bursaryAwardsResponses = bursaryFormSecskuls.map(ModelMappper::mapHigherLearningBursaryFormSchoolsToHigherBursaryAwardRespons).getContent();
            log.info("logging bursaryawardresponse {}",bursaryAwardsResponses);
            return new PagedResponse<>(bursaryAwardsResponses, bursaryFormSecskuls.getNumber(), bursaryFormSecskuls.getSize(), bursaryFormSecskuls.getTotalElements(), bursaryFormSecskuls.getTotalPages(), bursaryFormSecskuls.isLast());


        }catch (Exception e){
            throw new RuntimeException("An exception has occurred while fetching bursary awards "+e.getMessage());
        }

    }

    public PagedResponse<SecSchoolStudentResponse> fetchallsecondaryschoolstudents(UserPrincipal currentUser, int page, int size) {

        secondarySchoolsService.validatePagenumberandSize(page, size);
        //getting schools as pageable
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "admissionnumber");
        log.info("logging pageable secschool students {}", pageable);
        Page<HigherLearningStudent> secStudents = higherLearningStudentRepo.findAll(pageable);

        if (secStudents.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), secStudents.getNumber(), secStudents.getSize(), secStudents.getTotalElements(), secStudents.getTotalPages(), secStudents.isLast());
        }
        List<SecSchoolStudentResponse> secSchoolStudentResponses = secStudents.map(ModelMappper::mapcampusstudentTocampusStudentResponse).getContent();
        log.info("logging secondary school students {}", secSchoolStudentResponses);
        return new PagedResponse<>(secSchoolStudentResponses, secStudents.getNumber(), secStudents.getSize(), secStudents.getTotalElements(), secStudents.getTotalPages(), secStudents.isLast());

    }

    public PagedResponse<HigherLearningBursaryAwardsResponse> fetchallbursaryawardsbysecschool(UserPrincipal currentUser, BursaryAwardsBySecSchRequest bursaryAwardsBySecSchRequest, int page, int size) {
        secondarySchoolsService.validatePagenumberandSize(page, size);
        HigherEducationn secondarySchool=higherEducationRepository.findById(bursaryAwardsBySecSchRequest.getSchoolId()).orElseThrow(() -> new CouldNotBeFoundException("secondary school with provided id "+bursaryAwardsBySecSchRequest.getSchoolId()+" ould not be found"));
        try {
            Pageable pageable=PageRequest.of(page, size,Sort.Direction.ASC,"createdAt");
            Page<HigherLearningBursaryForm> bursaryFormSecskuls=higherLearningBursaryFormRepo.findAllByHigherEducationn(secondarySchool,pageable);

            log.info("logging bursary awards {}", bursaryFormSecskuls);
            if (bursaryFormSecskuls.getNumberOfElements() == 0) {
                return new PagedResponse<>(Collections.emptyList(), bursaryFormSecskuls.getNumber(), bursaryFormSecskuls.getSize(), bursaryFormSecskuls.getTotalElements(), bursaryFormSecskuls.getTotalPages(), bursaryFormSecskuls.isLast());
            }
            List<HigherLearningBursaryAwardsResponse> bursaryAwardsResponses = bursaryFormSecskuls.map(ModelMappper::mapHigherLearningBursaryFormSchoolsToHigherBursaryAwardRespons).getContent();
            log.info("logging bursaryawardresponse {}",bursaryAwardsResponses);
            return new PagedResponse<>(bursaryAwardsResponses, bursaryFormSecskuls.getNumber(), bursaryFormSecskuls.getSize(), bursaryFormSecskuls.getTotalElements(), bursaryFormSecskuls.getTotalPages(), bursaryFormSecskuls.isLast());


        }catch (Exception e){
            throw new RuntimeException("An exception has occurred while fetching bursary awards "+e.getMessage());
        }


    }

    public PagedResponse<HigherLearningBursaryAwardsResponse> fetchbursaryawardsbysecschoolstudentId(UserPrincipal currentUser, BursaryAwardsBySecSchRequest bursaryAwardsBySecSchRequest, int page, int size) {
        secondarySchoolsService.validatePagenumberandSize(page, size);
        HigherLearningStudent secStudent=higherLearningStudentRepo.findById(bursaryAwardsBySecSchRequest.getStudentId()).orElseThrow(() -> new CouldNotBeFoundException("student with provided id "+bursaryAwardsBySecSchRequest.getStudentId()+" could not be found"));
        try {
            Pageable pageable=PageRequest.of(page, size,Sort.Direction.ASC,"createdAt");
            Page<HigherLearningBursaryForm> bursaryFormSecskuls=higherLearningBursaryFormRepo.findAllByStudentid(secStudent.getId(),pageable);

            log.info("logging bursary awards by student Id {}", bursaryFormSecskuls);
            if (bursaryFormSecskuls.getNumberOfElements() == 0) {
                return new PagedResponse<>(Collections.emptyList(), bursaryFormSecskuls.getNumber(), bursaryFormSecskuls.getSize(), bursaryFormSecskuls.getTotalElements(), bursaryFormSecskuls.getTotalPages(), bursaryFormSecskuls.isLast());
            }
            List<HigherLearningBursaryAwardsResponse> bursaryAwardsResponses = bursaryFormSecskuls.map(ModelMappper::mapHigherLearningBursaryFormSchoolsToHigherBursaryAwardRespons).getContent();
            log.info("logging bursaryawardresponse by sec school{}",bursaryAwardsResponses);
            return new PagedResponse<>(bursaryAwardsResponses, bursaryFormSecskuls.getNumber(), bursaryFormSecskuls.getSize(), bursaryFormSecskuls.getTotalElements(), bursaryFormSecskuls.getTotalPages(), bursaryFormSecskuls.isLast());


        }catch (Exception e){
            throw new RuntimeException("An exception has occurred while fetching bursary awards "+e.getMessage());
        }

    }
}
