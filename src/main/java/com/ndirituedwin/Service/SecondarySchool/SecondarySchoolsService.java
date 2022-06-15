package com.ndirituedwin.Service.SecondarySchool;

import com.ndirituedwin.Dto.Requests.Secschool.*;
import com.ndirituedwin.Dto.Responses.ApiResponse;
import com.ndirituedwin.Dto.Responses.SecSchool.*;
import com.ndirituedwin.Dto.Responses.SecondarySchoolResponse;
import com.ndirituedwin.Entity.*;
import com.ndirituedwin.Entity.Bursary.Secskuls.*;
import com.ndirituedwin.Exception.AppException;
import com.ndirituedwin.Exception.CouldNotBeFoundException;
import com.ndirituedwin.Repository.*;
import com.ndirituedwin.Security.UserPrincipal;
import com.ndirituedwin.Service.Mapper.ModelMappper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static com.ndirituedwin.Config.Constants.ApiUtils.MAX_PAGE_SIZE;

@Service
@Slf4j
@AllArgsConstructor
public class SecondarySchoolsService {


    private final SecondarySchoolRepository secondarySchoolRepository;
    private final SecondarySchoolCategoryRepository secondarySchoolCategoryRepository;
    private final CountyRepository countyRepository;
    private final Schooltyperepository schooltyperepository;
    private final SecStudentRepo secStudentRepo;
    private final StudentParentRepo studentParentRepo;
    private final BursaryFormSecskulsRepo bursaryFormSecskulsRepo;
    private final WardRepository wardRepository;
    private final BursaryApplicationRepo bursaryApplicationRepo;
    private final ApplicationPeriodRepo applicationPeriodRepo;

    //private final StudentParentBursaryApplicationPeriodsRepo studentParentBursaryApplicationPeriodsRepo;
    public SecondarySchoolResponse save(SecondarySchoolsRequest secondarySchoolsRequest) {

        if (secondarySchoolRepository.existsByCode(secondarySchoolsRequest.getCode())) {
            return SecondarySchoolResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("the given code already exists")
                    .code(secondarySchoolsRequest.getCode())
                    .saved(false)
                    .build();
        }
        if (secondarySchoolRepository.existsBySchoolAndCountyCode(secondarySchoolsRequest.getSchool(), secondarySchoolsRequest.getCountyCode())) {
            return SecondarySchoolResponse.builder()
                    .school(secondarySchoolsRequest.getSchool())
                    .message("The given school exists in the given county")
                    .saved(false)
                    .build();
        }
        try {
            SecondarySchoolCategory category = secondarySchoolCategoryRepository.findById(secondarySchoolsRequest.getCategoryId()).orElseThrow(() -> new UsernameNotFoundException("category not found with given id " + secondarySchoolsRequest.getCategoryId()));
            County county = countyRepository.findByCode(secondarySchoolsRequest.getCountyCode()).orElseThrow(() -> new UsernameNotFoundException("county with the given county id could not be found " + secondarySchoolsRequest.getCountyCode()));
            log.info("logging county {}", county);
            SchoolType schoolType = schooltyperepository.findById(secondarySchoolsRequest.getTypeId()).orElseThrow(() -> new UsernameNotFoundException("school type with the provided id could not be found " + secondarySchoolsRequest.getTypeId()));
            log.info("logging school type {}", schoolType);
            SecondarySchool secondarySchool = new SecondarySchool();
            secondarySchool.setCode(secondarySchoolsRequest.getCode());
            log.info("logging the code {}", secondarySchoolsRequest.getCode());
            secondarySchool.setSchool(secondarySchoolsRequest.getSchool());
            secondarySchool.setCategory(category);
            secondarySchool.setType(schoolType);
            secondarySchool.setCounty(county);
            SecondarySchool savedsecondaryschool = secondarySchoolRepository.save(secondarySchool);
            return SecondarySchoolResponse.builder()
                    .code(savedsecondaryschool.getCode())
                    .school(savedsecondaryschool.getSchool())
                    .message("secondary school saved successfully")
                    .saved(true)
                    .status(HttpStatus.CREATED)
                    .build();
        } catch (Exception ex) {
            return SecondarySchoolResponse.builder()
                    .saved(false)
                    .message("an exception has occured while trying to save school " + ex.getMessage())
                    .build();
        }

    }

    public PagedResponse<SchoolResponse> fetchallsecondaryschools(UserPrincipal currentUser, int page, int size) {
        validatePagenumberandSize(page, size);
        //getting schools as pageable
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "county");
        log.info("logging pageable {}", pageable);
        Page<SecondarySchool> secondarySchools = secondarySchoolRepository.findAll(pageable);
        log.info("logging sec schools from server {}", secondarySchools);
        secondarySchools.forEach(secondarySchool -> {
            log.info("logging pageable secondaryschool {}", secondarySchool);
            System.out.println("Printing secondary schools pageable " + secondarySchool);

        });

        if (secondarySchools.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), secondarySchools.getNumber(), secondarySchools.getSize(), secondarySchools.getTotalElements(), secondarySchools.getTotalPages(), secondarySchools.isLast());
        }
        List<SchoolResponse> schoolResponses = secondarySchools.map(ModelMappper::mapSecondarySchoolToSecondarySchoolResponse).getContent();
        log.info("logging secondary schools {}", schoolResponses);
        return new PagedResponse<>(schoolResponses, secondarySchools.getNumber(), secondarySchools.getSize(), secondarySchools.getTotalElements(), secondarySchools.getTotalPages(), secondarySchools.isLast());

    }

    public ApiResponse validatePagenumberandSize(int page, int size) {

        try {
            if (page < 0) {
                return ApiResponse.builder()
                        .success(false)
                        .message("page number may not be less than zero")
                        .build();

            }
            if (size > MAX_PAGE_SIZE) {
                return ApiResponse.builder().success(false).message("page size may not be greater than " + MAX_PAGE_SIZE).build();
            }

        } catch (Exception e) {
            return ApiResponse.builder()
                    .success(false)
                    .message("an exception has occurred while validating page number and size " + e.getMessage())
                    .build();
        }
        return null;
    }


    @Transactional
    public Object applybursary(UserPrincipal currentUser, Bursaryapplicationrequest bursaryapplicationrequest) {

        SecondarySchool secondarySchool = secondarySchoolRepository.findById(bursaryapplicationrequest.getSecondarySchool()).orElseThrow(() -> new AppException("scool with given id coud not be found"));
        Ward ward = wardRepository.findById(bursaryapplicationrequest.getParentward()).orElseThrow(() -> new CouldNotBeFoundException("ward with the provided id could not be found " + bursaryapplicationrequest.getParentward()));
        ApplicationPeriod applicationPeriod=applicationPeriodRepo.findByYearAndMonth(bursaryapplicationrequest.getApplicationperiodyear(),bursaryapplicationrequest.getApplicationperiodmonth()).orElseThrow(() -> new CouldNotBeFoundException("application period with the given combination of year and month could not be found "+bursaryapplicationrequest.getApplicationperiodyear()+" "+bursaryapplicationrequest.getApplicationperiodmonth()));
        log.info("logging existing application period {}",applicationPeriod);
        //check if the parent exists by phone or id
        try {
            StudentParent existingparent;
            boolean doesparentparentexists = studentParentRepo.existsByPhonenumberOrIdnumber(bursaryapplicationrequest.getParentphonenumber(), bursaryapplicationrequest.getParentidnumber());
            log.info("checking whether the parent exists {}", doesparentparentexists);

            //this executes if the student exists in the database
            if (doesparentparentexists) {
                log.info("the parent exists");
                existingparent = studentParentRepo.findByPhonenumberOrIdnumber(bursaryapplicationrequest.getParentphonenumber(), bursaryapplicationrequest.getParentidnumber()).orElseThrow(() -> new AppException("student parent not found with given phone number or id number"));
                log.info("logging the already existing parent {}", existingparent);
                existingparent.setName(bursaryapplicationrequest.getParentfullname());
                existingparent.setWard(ward);
                existingparent.setCreatedBy(currentUser.getId());
                existingparent.setUpdatedBy(currentUser.getId());
                existingparent = studentParentRepo.save(existingparent);
                log.info("logging saved student parent {}", existingparent);

            } else {
                //this executes if the student does not exist in the server
                log.info("the parent does not exist");
                StudentParent studentParent = new StudentParent();
                studentParent.setIdnumber(bursaryapplicationrequest.getParentidnumber());
                studentParent.setName(bursaryapplicationrequest.getParentfullname());
                studentParent.setPhonenumber(bursaryapplicationrequest.getParentphonenumber());
                studentParent.setWard(ward);
                studentParent.setCreatedBy(currentUser.getId());
                studentParent.setUpdatedBy(currentUser.getId());
                log.info("logging studentparent just before saving {}", studentParent);
                existingparent = studentParentRepo.save(studentParent);

                log.info("logging the saved studentparent object {}", existingparent);
            }

            try {
                log.info("logging the secondary school {}", secondarySchool);
                log.info("logging existing studentparent {}", existingparent);
                //check if student exists by admissionnumber and school;
                String savedstudentadmissionnumber;
                Boolean existsbyadmnoandseschool = secStudentRepo.existsByAdmissionnumberAndSecondaryschool(bursaryapplicationrequest.getAdmissionnumber(), secondarySchool);
                log.info("checking whether the student existsbyadmnoandseschool {}", existsbyadmnoandseschool);

                if (existsbyadmnoandseschool) {
                    log.info("student  exists");

                    SecStudent existingstudent = secStudentRepo.findByAdmissionnumberAndSecondaryschool(bursaryapplicationrequest.getAdmissionnumber(), secondarySchool).orElseThrow(() -> new CouldNotBeFoundException("sec student with given adm no and sec school not found "));
                    log.info("logging the already existing secstudent {}", existingstudent);
                    //if the student exists insert the data to the bursary application entity

//                    boolean existsbyyms = bursaryApplicationRepo.existsBySecStudentAndApplicationYearAndApplicationMonth(existingstudent, bursaryapplicationrequest.getApplicationperiodyear(), bursaryapplicationrequest.getApplicationperiodmonth());
                    boolean existsbyyms = bursaryApplicationRepo.existsBySecStudentAndApplicationPeriod(existingstudent, applicationPeriod);
                    log.info("sec student does they exist {}", existsbyyms);
                    if (existsbyyms) {
                        return Bursaryapplicationresponse.builder()
                                .status(HttpStatus.BAD_REQUEST)
                                .message("Application exists by student " + existingstudent.getAdmissionnumber() + " , application year " + bursaryapplicationrequest.getApplicationperiodyear() + " and by application  month " + bursaryapplicationrequest.getApplicationperiodmonth())
                                .issuccess(false)
                                .build();
                    } else {
                        BursaryApplication bursaryApplication = new BursaryApplication();
                        bursaryApplication.setSecStudent(existingstudent);
                        bursaryApplication.setFormorclass(bursaryapplicationrequest.getFormorclass());

//                        bursaryApplication.setApplicationMonth(bursaryapplicationrequest.getApplicationperiodmonth());
//                        bursaryApplication.setApplicationYear(bursaryapplicationrequest.getApplicationperiodyear());
                        bursaryApplication.setApplicationPeriod(applicationPeriod);
                        bursaryApplication.setCreatedBy(currentUser.getId());
                        bursaryApplication.setUpdatedBy(currentUser.getId());
                        log.info("dddddddddddddddddddddd {}", bursaryApplication);
//                        String savedbursaryapplicationstudentadm = bursaryApplicationRepo.save(bursaryApplication).getSecStudent().getAdmissionnumber();
                        BursaryApplication savedbursaryapplication= bursaryApplicationRepo.save(bursaryApplication);
                        log.info("saved savedbursaryapplication {}",savedbursaryapplication);
                         String savedbursaryapplicationstudentadm=savedbursaryapplication.getSecStudent().getAdmissionnumber();
                        savedstudentadmissionnumber = savedbursaryapplicationstudentadm;
                        log.info("logging savedbursaryapplicationstudentadm of an existing student {}", bursaryApplication);
                    }

                } else {
                    //if the student does not exist, create a new student and insert them to bursary application table
                    log.info("student does not exist create a new student");
                    log.info("logging existing parent wnen student does not exist {}", existingparent);
                    //Insert new record to the server
                    SecStudent secStudent = new SecStudent();
                    secStudent.setFullname(bursaryapplicationrequest.getFullname());
                    secStudent.setSecondaryschool(secondarySchool);
                    secStudent.setAdmissionnumber(bursaryapplicationrequest.getAdmissionnumber());
                    secStudent.setCreatedBy(currentUser.getId());
                    secStudent.setUpdatedBy(currentUser.getId());
                    secStudent.setStudentparent(existingparent);

                    log.info("logging non existing sec student  {}", secStudent);
                    SecStudent savedsecstudent = secStudentRepo.save(secStudent);
                    log.info("logging non existing  saved secstudent {}", savedsecstudent);
                    boolean existsbyyms = bursaryApplicationRepo.existsBySecStudentAndApplicationPeriod(savedsecstudent, applicationPeriod);
                    log.info("logging existsbyyms  {}", existsbyyms);

                    if (existsbyyms) {
                        return Bursaryapplicationresponse.builder()
                                .status(HttpStatus.BAD_REQUEST)
                                .message("Application exists by student " + savedsecstudent.getAdmissionnumber() + " , application  year " + bursaryapplicationrequest.getApplicationperiodyear() + " and by application  month " + bursaryapplicationrequest.getApplicationperiodmonth())
                                .issuccess(false)
                                .build();
                    } else {
                        BursaryApplication nee = new BursaryApplication();
                        nee.setSecStudent(savedsecstudent);
//                        nee.setApplicationYear(bursaryapplicationrequest.getApplicationperiodyear());
//                        nee.setApplicationMonth(bursaryapplicationrequest.getApplicationperiodmonth());
                        nee.setApplicationPeriod(applicationPeriod);
                        nee.setFormorclass(bursaryapplicationrequest.getFormorclass());
                        nee.setCreatedBy(currentUser.getId());
                        nee.setUpdatedBy(currentUser.getId());
//                        String admno = bursaryApplicationRepo.save(nee).getSecStudent().getAdmissionnumber();
                        BursaryApplication saveddd = bursaryApplicationRepo.save(nee);
                        log.info("The the bursary application does not exist {}",saveddd);
                        String admno=saveddd.getSecStudent().getAdmissionnumber();
                        log.info("logging sec student admission number after saving non existing sec student {}", admno);
                        savedstudentadmissionnumber = admno;
                    }
                }


                return Bursaryapplicationresponse.builder().status(HttpStatus.CREATED).message("Bursary application successful for  " + savedstudentadmissionnumber).issuccess(true).build();

            } catch (Exception exception) {
                return Bursaryapplicationresponse.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .message("An exception has occurred while saving student  details " + exception.getMessage())
                        .issuccess(false)
                        .build();
            }


        } catch (Exception exception) {
            return Bursaryapplicationresponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("An exception has occurred while saving student parent details " + exception.getMessage())
                    .issuccess(false)
                    .build();
        }
    }


    @Transactional(readOnly = true)
    public Object getbursaryapplications(UserPrincipal currentuser, int page, int size) {
        LocalDate currentDate=LocalDate.now();
        String currentMonth=String.valueOf(currentDate.getMonth()).toLowerCase();
        String curMonth=currentMonth.substring(0,1).toUpperCase()+currentMonth.substring(1);
        String curYear=String.valueOf(currentDate.getYear());
        log.info("current year {}",curYear);
        log.info("current month {}",curMonth);

        if (currentuser == null) {
            return ApiResponse.builder().message("The user is null");
        }
        validatePagenumberandSize(page, size);
        try {
            log.info("before pageable");
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");
           log.info("after pageable {}",pageable);
//            Page<BursaryApplication> bursaryApplicationPage = bursaryApplicationRepo.findAll(pageable);
//            Page<BursaryApplication> bursaryApplicationPage = bursaryApplicationRepo.findAllByApplicationPeriodYearAndApplicationPeriodMonth(curYear,curMonth,pageable);
            Page<BursaryApplication> bursaryApplicationPage = bursaryApplicationRepo.findAllByIsapprovedEqualsAndApplicationPeriodYearAndApplicationPeriodMonth(true,curYear,curMonth,pageable);
            log.info("logging bursary applications {}", bursaryApplicationPage);
            if (bursaryApplicationPage.getNumberOfElements() == 0) {
                return new PagedResponse<>(Collections.emptyList(), bursaryApplicationPage.getNumber(), bursaryApplicationPage.getSize(), bursaryApplicationPage.getTotalElements(), bursaryApplicationPage.getTotalPages(), bursaryApplicationPage.isLast());
            }
            List<Allbursaryapplications> allbursaryapplications = bursaryApplicationPage.map(ModelMappper::mapbursaryapplicationentitytoallbursaryapplicationsdto).getContent();
            log.info("logging  allbursaryapplications {}", allbursaryapplications);
            return new PagedResponse<>(allbursaryapplications, bursaryApplicationPage.getNumber(), bursaryApplicationPage.getSize(), bursaryApplicationPage.getTotalElements(), bursaryApplicationPage.getTotalPages(), bursaryApplicationPage.isLast());
        } catch (Exception ex) {
            return ApiResponse.builder().message("An error has occurred while fetching bursary applications " + ex.getMessage()).build();
        }
    }
    @Transactional(readOnly = true)
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
        validatePagenumberandSize(page, size);
        try {
            log.info("before pageable");
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");
            log.info("after pageable {}",pageable);
//            Page<BursaryApplication> bursaryApplicationPage = bursaryApplicationRepo.findAll(pageable);
            Page<BursaryApplication> bursaryApplicationPage = bursaryApplicationRepo.findAll(pageable);
            log.info("logging bursary applications {}", bursaryApplicationPage);
            if (bursaryApplicationPage.getNumberOfElements() == 0) {
                return new PagedResponse<>(Collections.emptyList(), bursaryApplicationPage.getNumber(), bursaryApplicationPage.getSize(), bursaryApplicationPage.getTotalElements(), bursaryApplicationPage.getTotalPages(), bursaryApplicationPage.isLast());
            }
            List<Allbursaryapplications> allbursaryapplications = bursaryApplicationPage.map(ModelMappper::mapbursaryapplicationentitytoallbursaryapplicationsdto).getContent();
            log.info("logging  allbursaryapplications {}", allbursaryapplications);
            return new PagedResponse<>(allbursaryapplications, bursaryApplicationPage.getNumber(), bursaryApplicationPage.getSize(), bursaryApplicationPage.getTotalElements(), bursaryApplicationPage.getTotalPages(), bursaryApplicationPage.isLast());
        } catch (Exception ex) {
            return ApiResponse.builder().message("An error has occurred while fetching bursary applications " + ex.getMessage()).build();
        }
    }

    @Transactional(readOnly = true)
    public Object findAllByBursaryApplicationsByApplicationYearandmonth(UserPrincipal currentUser, FetcBursaryApplicationsBy fetcBursaryApplicationsBy, int page, int size) {
        if (currentUser == null) {
            return ApiResponse.builder().success(false).httpStatus(HttpStatus.UNAUTHORIZED).message("The user is null").build();
        }
        log.info("logging fetcBursaryApplicationsBy {}",fetcBursaryApplicationsBy);
        try {
            validatePagenumberandSize(page, size);
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

//            Page<BursaryApplication> bursaryApplicationPage=bursaryApplicationRepo.findAllBySecondarySchool(secondarySchool,pageable);
//            Page<BursaryApplication> bursaryApplicationPage=bursaryApplicationRepo.findAllByApplicationPeriod(applicationPeriod,pageable);
            Page<BursaryApplication> bursaryApplicationPage=bursaryApplicationRepo.findAllByIsapprovedEqualsAndApplicationPeriod(true,applicationPeriod,pageable);
            log.info("bursaryApplicationPage {}",bursaryApplicationPage);
            if (bursaryApplicationPage.getNumberOfElements() == 0) {
                return new PagedResponse<>(Collections.emptyList(), bursaryApplicationPage.getNumber(), bursaryApplicationPage.getSize(), bursaryApplicationPage.getNumberOfElements(), bursaryApplicationPage.getTotalPages(), bursaryApplicationPage.isLast());
            }
            List<Allbursaryapplications> allbursaryapplications = bursaryApplicationPage.map(ModelMappper::mapbursaryapplicationentitytoallbursaryapplicationsdto).getContent();
            log.info("logging viewbursary applications {}", allbursaryapplications);
            return new PagedResponse<>(allbursaryapplications, bursaryApplicationPage.getNumber(), bursaryApplicationPage.getSize(), bursaryApplicationPage.getTotalElements(), bursaryApplicationPage.getTotalPages(), bursaryApplicationPage.isLast());

            }
        } catch (Exception e) {
            return ApiResponse.builder().success(false).httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).message(" An axception has occurred " + e.getMessage()).build();
        }
    }





    @Transactional(readOnly = true)
    public Object findAllBySchoolId(UserPrincipal currentUser, Long schoolId, int page, int size) {
        if (currentUser == null) {
            return ApiResponse.builder().success(false).httpStatus(HttpStatus.UNAUTHORIZED).message("The user is null").build();
        }
        try {
            validatePagenumberandSize(page, size);
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
            SecondarySchool secondarySchool = secondarySchoolRepository.findById(schoolId).orElseThrow(() -> new AppException("school with the provided id " + schoolId + " could not be found"));
            log.info("logging pageable {}", pageable);

//            Page<SecStudent> secStudents = secStudentRepo.findAllBySecondaryschool(pageable, secondarySchool);
           Page<BursaryApplication> bursaryApplicationPage=bursaryApplicationRepo.findAllBySecondarySchool(secondarySchool,pageable);
           log.info("bursaryApplicationPage {}",bursaryApplicationPage);
            if (bursaryApplicationPage.getNumberOfElements() == 0) {
                return new PagedResponse<>(Collections.emptyList(), bursaryApplicationPage.getNumber(), bursaryApplicationPage.getSize(), bursaryApplicationPage.getNumberOfElements(), bursaryApplicationPage.getTotalPages(), bursaryApplicationPage.isLast());
            }
            List<Allbursaryapplications> allbursaryapplications = bursaryApplicationPage.map(ModelMappper::mapbursaryapplicationentitytoallbursaryapplicationsdto).getContent();
            log.info("logging viewbursary applications {}", allbursaryapplications);
            return new PagedResponse<>(allbursaryapplications, bursaryApplicationPage.getNumber(), bursaryApplicationPage.getSize(), bursaryApplicationPage.getTotalElements(), bursaryApplicationPage.getTotalPages(), bursaryApplicationPage.isLast());
        } catch (Exception e) {
            return ApiResponse.builder().success(false).httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).message(" An axception has occurred " + e.getMessage()).build();
        }
    }



        @Transactional
    public Object awardbursary(UserPrincipal currentUser, Awardbursaryrequest awardbursaryrequest) {
       log.info("logging the bursaryapplication request {}",awardbursaryrequest);
      if (awardbursaryrequest.getAmount()==null){
          return AwardBursaryResponse.builder()
                  .status(HttpStatus.BAD_REQUEST)
                  .amountmaynotbenull(" amount may not be null")
                  .build();
      }
       //check if the bursary application exists
       BursaryApplication bursaryApplication=bursaryApplicationRepo.findById(awardbursaryrequest.getApplicationId()).orElseThrow(() -> new CouldNotBeFoundException("the application with the given application id "+awardbursaryrequest.getApplicationId()+" could not be found"));
       SecStudent secStudent=secStudentRepo.findById(awardbursaryrequest.getStudentId()).orElseThrow(() -> new CouldNotBeFoundException("student with the given id could not be found "));
        //check if the bursary application is approved or not
         if (bursaryApplication.isIsapproved()){
             //the bursary is approved yu can go ahead
             //check to see if the application period is open or not
             try {
//             ApplPeriodsPackage applicationPeriod=applicationPeriodRepo.findByYearAndMonth(bursaryApplication.getApplicationYear(),bursaryApplication.getApplicationMonth()).orElseThrow(() -> new CouldNotBeFoundException("The application periods could not be found in the application periods table "));
             ApplicationPeriod applicationPeriod=applicationPeriodRepo.findById(bursaryApplication.getApplicationPeriod().getId()).orElseThrow(() -> new CouldNotBeFoundException("The application periods could not be found in the application periods table "));
              if (applicationPeriod.isOpen()) {
                  //the application period is open
                  //check if the student exists

                  boolean existsbyapplication = bursaryFormSecskulsRepo.existsByBursaryApplication(bursaryApplication);
                  if (existsbyapplication) {
                      //the award bursary exists by the bursary appication
                      BursaryFormSecskuls theexistingawardedbursary = bursaryFormSecskulsRepo.findByBursaryApplication(bursaryApplication).orElseThrow(() -> new CouldNotBeFoundException("bursary award could not be found"));
                      //check to see if the amount in server matches the one to input
                      BigDecimal existingamount=theexistingawardedbursary.getAmount();
                      if (theexistingawardedbursary.getAmount().equals(awardbursaryrequest.getAmount())) {
                          return AwardBursaryResponse.builder()
                                  .status(HttpStatus.CREATED)
                                  .bursaryFormSecskuls(theexistingawardedbursary)
                                  .message("similar bursary application exists")
                                  .build();
                      } else {
                          //amount does not match, go ahead and award new bursary
                          theexistingawardedbursary.setAmount(awardbursaryrequest.getAmount());
                          theexistingawardedbursary.setUpdatedBy(currentUser.getId());
                          BursaryFormSecskuls ss=bursaryFormSecskulsRepo.save(theexistingawardedbursary);
                          return AwardBursaryResponse.builder()
                                  .status(HttpStatus.CREATED)
                                  .bursaryFormSecskuls(ss)
                                  .message("The existing bursary of  " + existingamount + " has been updated to " + ss.getAmount())
                                  .build();
                      }
                  }else {
                      //if the award amount does not exist by application,go ahead and add a new record


                      BursaryFormSecskuls bursaryFormSecskuls = new BursaryFormSecskuls();
                      bursaryFormSecskuls.setSchoolid(awardbursaryrequest.getSchoolId());
                      bursaryFormSecskuls.setStudentid(secStudent.getId());
                      bursaryFormSecskuls.setAmount(awardbursaryrequest.getAmount());
                      bursaryFormSecskuls.setBursaryApplication(bursaryApplication);
                      bursaryFormSecskuls.setCreatedBy(currentUser.getId());
                      bursaryFormSecskuls.setUpdatedBy(currentUser.getId());
                      BursaryFormSecskuls saved = bursaryFormSecskulsRepo.save(bursaryFormSecskuls);
                      log.info("logging saved bursary form sec school awarded bursary {}", saved);
                      return AwardBursaryResponse.builder()
                              .status(HttpStatus.CREATED)
                              .bursaryFormSecskuls(saved)
                              .message("bursary of " + saved.getAmount() + " to " + bursaryApplication.getSecStudent().getAdmissionnumber() + " successfulyy awarded ")
                              .build();

                  }
              }else{
                 //the appplication period is closed
                  return ApiResponse.builder().message("The application period "+bursaryApplication.getApplicationPeriod().getMonth()+","+bursaryApplication.getApplicationPeriod().getYear()+" is closed").build();
              }
         }catch (Exception exception){
                 return ApiResponse.builder().success(false).message("an exception has occurred while awarding bursary "+exception.getMessage()).build();
        }
         }else{
             //the bursary has not yet been approved
             return new ResponseEntity(new ApiResponse (false,"The bursary application  has not yet been approved",HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST);
         }
    }



    @Transactional
    public Object awardbursaryy(UserPrincipal currentUser, AwardBursary awardBursary) {
        log.info("awardbursaryrequest {}",awardBursary);
        final AwardBursaryResponse[] awardBursaryResponse = {null};

        if (awardBursary.getAmount()==null){
            return AwardBursaryResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .amountmaynotbenull(" amount may not be null")
                    .build();
        }
        try {
            awardBursary.getAwardrequest().forEach(awardRequest -> {
                boolean bursaryapplicationexistsbyid=bursaryApplicationRepo.existsById(awardRequest.getId());
                if (!bursaryapplicationexistsbyid){
               log.info("The bursary application with the given application id could not be found");
                }else{
                    BursaryApplication bursaryApplication = bursaryApplicationRepo.findById(awardRequest.getId()).orElseThrow(() -> new CouldNotBeFoundException("The bursary appllication could not be found"));
                    if (!bursaryApplication.isIsapproved()){
                        log.info("Bursary application has not yet been approved ");
                    }else {
                        Boolean applicationPeriod=applicationPeriodRepo.existsByIdAndIsOpenEquals(bursaryApplication.getApplicationPeriod().getId(),true);
                         if (!applicationPeriod){
                             log.info("The application period is either not open or it does not exists");
                         }else{
                             boolean existsbybursaryapplication=bursaryFormSecskulsRepo.existsByBursaryApplication(bursaryApplication);
                             if (existsbybursaryapplication){
                                 //update the existing award
                                  boolean doesstudentexists=secStudentRepo.existsById(bursaryApplication.getSecStudent().getId());
                                  if (!doesstudentexists){
                                     log.info("the student could not be found");
                                  }else{
                                      //the student exists go ahead and update the existig
                                      BursaryFormSecskuls bursaryFormSecskuls=bursaryFormSecskulsRepo.findByBursaryApplication(bursaryApplication).orElseThrow(() -> new CouldNotBeFoundException("Bursary award with provided bursary application could not be found "));
                                      BigDecimal existingamount=bursaryFormSecskuls.getAmount();
                                      if (existingamount.equals(awardBursary.getAmount())){
                                          log.info("bursary award amounts are similar");
                                      }else{
                                          log.info("we are updating ");
                                          bursaryFormSecskuls.setAmount(awardBursary.getAmount());
                                          bursaryFormSecskuls.setUpdatedBy(currentUser.getId());
                                          BursaryFormSecskuls edited=bursaryFormSecskulsRepo.save(bursaryFormSecskuls);
                                          log.info("data successfully updated {}",edited);
                                          awardBursaryResponse[0]=AwardBursaryResponse.builder()
                                                  .status(HttpStatus.CREATED)
                                                  .bursaryFormSecskuls(edited)
                                                  .message("The existing bursary of  " + existingamount + " has been updated to " + edited.getAmount())
                                                  .build();

                                      }

                                  }
                             }else{
                                 //save a new bursary award;

                                 BursaryFormSecskuls bursaryFormSecskuls = new BursaryFormSecskuls();
                                 bursaryFormSecskuls.setSchoolid(awardRequest.getSecondaryschool().getId());
                                 bursaryFormSecskuls.setStudentid(bursaryApplication.getSecStudent().getId());
                                 bursaryFormSecskuls.setAmount(awardBursary.getAmount());
                                 bursaryFormSecskuls.setBursaryApplication(bursaryApplication);
                                 bursaryFormSecskuls.setCreatedBy(currentUser.getId());
                                 bursaryFormSecskuls.setUpdatedBy(currentUser.getId());
                                 BursaryFormSecskuls saved = bursaryFormSecskulsRepo.save(bursaryFormSecskuls);
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
            return new ApiResponse(false,"an exception has ocurre while awarding the bursary "+exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }
    @Transactional
//    public List<String> awardbursaryasindividual(UserPrincipal currentUser, List<String> awardbursary) {
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
                   boolean bursaryapplicationexistsbyid=bursaryApplicationRepo.existsById(Long.parseLong(applid));
                   if (!bursaryapplicationexistsbyid){
                       log.info("bursary application does not exist");
                   }else{
                       BursaryApplication bursaryApplication=bursaryApplicationRepo.findById(Long.parseLong(applid)).orElseThrow(() -> new CouldNotBeFoundException("Bursary application with the provided id "+applid+" could not be found"));
                       if (!bursaryApplication.isIsapproved()){
                           log.info("bursary application is not approved");
                       }else{
                           Boolean applicationPeriod=applicationPeriodRepo.existsByIdAndIsOpenEquals(bursaryApplication.getApplicationPeriod().getId(),true);
                             if(!applicationPeriod){
                                 log.info("The application period is either not open or it does not exists");

                             }else{
                                boolean existsByBursaryApplication=bursaryFormSecskulsRepo.existsByBursaryApplication(bursaryApplication);
                              if(existsByBursaryApplication){

                                  //update the existing award
                                  boolean doesstudentexists=secStudentRepo.existsById(bursaryApplication.getSecStudent().getId());
                                  if (!doesstudentexists){
                                      log.info("the student could not be found");
                                  }else{
                                      //the student exists go ahead and update the existig
                                      BursaryFormSecskuls bursaryFormSecskuls=bursaryFormSecskulsRepo.findByBursaryApplication(bursaryApplication).orElseThrow(() -> new CouldNotBeFoundException("Bursary award with provided bursary application could not be found "));
                                      BigDecimal existingamount=bursaryFormSecskuls.getAmount();
                                      if (existingamount.equals(BigDecimal.valueOf(Long.parseLong(amount)))){
                                          log.info("bursary award amounts are similar");
                                      }else{
                                          log.info("we are updating ");
                                          bursaryFormSecskuls.setAmount(BigDecimal.valueOf(Long.parseLong(amount)));
                                          bursaryFormSecskuls.setUpdatedBy(currentUser.getId());
                                          BursaryFormSecskuls edited=bursaryFormSecskulsRepo.save(bursaryFormSecskuls);
                                          log.info("data successfully updated {}",edited);
                                          awardBursaryResponse[0]=AwardBursaryResponse.builder()
                                                  .status(HttpStatus.CREATED)
                                                  .bursaryFormSecskuls(edited)
                                                  .message("The existing bursary of  " + existingamount + " has been updated to " + edited.getAmount())
                                                  .build();

                                      }

                                  }


                              }else{
                                  //save a new bursary applicatin
                                  //save a new bursary award;

                                  BursaryFormSecskuls bursaryFormSecskuls = new BursaryFormSecskuls();
                                  bursaryFormSecskuls.setSchoolid(bursaryApplication.getSecStudent().getSecondaryschool().getId());
                                  bursaryFormSecskuls.setStudentid(bursaryApplication.getSecStudent().getId());
                                  bursaryFormSecskuls.setAmount(BigDecimal.valueOf(Long.parseLong(amount)));
                                  bursaryFormSecskuls.setBursaryApplication(bursaryApplication);
                                  bursaryFormSecskuls.setCreatedBy(currentUser.getId());
                                  bursaryFormSecskuls.setUpdatedBy(currentUser.getId());
                                  BursaryFormSecskuls saved = bursaryFormSecskulsRepo.save(bursaryFormSecskuls);
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


    @Transactional
    public Object awardbursarybysecschoolCategory(UserPrincipal currentUser, AwardBursarybysecschoolCategoryrequest awardBursarybysecschoolCategoryrequest) {
            log.info("logging awardBursarybysecschoolCategoryrequest.getBursarybysecschool() {}",awardBursarybysecschoolCategoryrequest.getBursarybysecschoolcategory());

           final AwardBursaryResponse[] awardBursaryResponse = {null};
       if (awardBursarybysecschoolCategoryrequest.getAmount()==null){
           return ApiResponse.builder().success(false).message("amount may not be null").httpStatus(HttpStatus.BAD_REQUEST).build();
       }
                       try {
        awardBursarybysecschoolCategoryrequest.getBursarybysecschoolcategory().forEach(awardbyseschoolCategory -> {
                    boolean existsbyid=bursaryApplicationRepo.existsById(awardbyseschoolCategory.getId());
                    if (existsbyid) {
                        BursaryApplication bursaryApplication = bursaryApplicationRepo.findById(awardbyseschoolCategory.getId()).orElseThrow(() -> new CouldNotBeFoundException("The bursary appllication could not be found"));
                           if (bursaryApplication.isIsapproved()) {
                               boolean existsbyapplicationYearadMonth=applicationPeriodRepo.existsByYearAndMonth(awardbyseschoolCategory.getApplicationyear(),awardbyseschoolCategory.getApplicationmonth());
                               if (existsbyapplicationYearadMonth){
                                   ApplicationPeriod applicationPeriod=applicationPeriodRepo.findByYearAndMonth(awardbyseschoolCategory.getApplicationyear(),awardbyseschoolCategory.getApplicationmonth()).orElseThrow(() -> new CouldNotBeFoundException("Application period with "+awardbyseschoolCategory.getApplicationyear()+ " and "+ awardbyseschoolCategory.getApplicationmonth()+" could not be found"));
                                 if (applicationPeriod.isOpen()){
                                     boolean existsbybursaryapplication=bursaryFormSecskulsRepo.existsByBursaryApplication(bursaryApplication);
                                     if (existsbybursaryapplication){
                                         boolean secStudent = secStudentRepo.existsById(bursaryApplication.getSecStudent().getId());
                                         if (secStudent) {
                                             SecStudent secStudent1 = secStudentRepo.findById(bursaryApplication.getSecStudent().getId()).orElseThrow(() -> new CouldNotBeFoundException("Student not found"));
                                        BursaryFormSecskuls bursaryFormSecskuls=bursaryFormSecskulsRepo.findByBursaryApplication(bursaryApplication).orElseThrow(() -> new CouldNotBeFoundException("bursary appliation not found"));
                                         BigDecimal existingamount=bursaryFormSecskuls.getAmount();
                                        if (bursaryFormSecskuls.getAmount().equals(awardBursarybysecschoolCategoryrequest.getAmount())){
                                            log.info("The amount already exists");
                                        }else{
                                            log.info("we are updating ");
                                            bursaryFormSecskuls.setAmount(awardBursarybysecschoolCategoryrequest.getAmount());
                                            bursaryFormSecskuls.setUpdatedBy(currentUser.getId());
                                            BursaryFormSecskuls edited=bursaryFormSecskulsRepo.save(bursaryFormSecskuls);
                                           log.info("data successfully updated {}",edited);
                                            awardBursaryResponse[0]=AwardBursaryResponse.builder()
                                                    .status(HttpStatus.CREATED)
                                                    .bursaryFormSecskuls(edited)
                                                    .message("The existing bursary of  " + existingamount + " has been updated to " + edited.getAmount())
                                                    .build();
                                        }
                                         } else {
                                             log.info("The student could not be found");
                                         }
                                     }else{
                                         //save a new award
                                         BursaryFormSecskuls bursaryFormSecskuls = new BursaryFormSecskuls();
                                         bursaryFormSecskuls.setSchoolid(awardbyseschoolCategory.getSecondaryschool().getId());
                                         bursaryFormSecskuls.setStudentid(bursaryApplication.getSecStudent().getId());
                                         bursaryFormSecskuls.setAmount(awardBursarybysecschoolCategoryrequest.getAmount());
                                         bursaryFormSecskuls.setBursaryApplication(bursaryApplication);
                                         bursaryFormSecskuls.setCreatedBy(currentUser.getId());
                                         bursaryFormSecskuls.setUpdatedBy(currentUser.getId());
                                         BursaryFormSecskuls saved = bursaryFormSecskulsRepo.save(bursaryFormSecskuls);
                                         log.info("logging saved bursary form sec school awarded bursary {}", saved);
                                         awardBursaryResponse[0] = AwardBursaryResponse.builder()
                                                 .status(HttpStatus.CREATED)
//                                .bursaryFormSecskuls(bursaryFormSecskuls)
                                                 .message("Bursary awarded successfully of Kshs" + saved.getAmount() + " to every student")
                                                 .build();
                                     }
                                 }else{
                                     log.info("The application period is closed  {}",awardbyseschoolCategory.getApplicationyear(),awardbyseschoolCategory.getApplicationmonth());
                                 }
                               }else{
                                   log.info("The application period does not exist by application year and month provided");
                               }

                           }else{
                               log.info("Bursary application has not yet been approved ");
                           }
                    }else{
                        log.info("The bursary application with the given application id could not be found");
                    }
        });
                           if (!(awardBursaryResponse[0] == null)) {
                               return awardBursaryResponse[0];
                           }


                           return null;

                       }catch (Exception exception){
                           return new ApiResponse(false,"an exception has ocurre while awarding the bursary "+exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

                       }

    }



    @Transactional(readOnly = true)
    public Object viewstudentDetails(UserPrincipal currentUser, Long applicationId) {
        BursaryApplication bursaryApplication=bursaryApplicationRepo.findById(applicationId).orElseThrow(() -> new CouldNotBeFoundException("Bursary application  with the provided id "+applicationId+" could not be found"));
        log.info("ffddd {}", bursaryApplication);
        try {
            SingleBursaryApplicationDetails singleBursaryApplicationDetails = new SingleBursaryApplicationDetails();
            singleBursaryApplicationDetails.setAdmissionnumber(bursaryApplication.getSecStudent().getAdmissionnumber());
            singleBursaryApplicationDetails.setStudentId(bursaryApplication.getSecStudent().getId());
            singleBursaryApplicationDetails.setFullname(bursaryApplication.getSecStudent().getFullname());
//            singleBursaryApplicationDetails.setApplicationYear(bursaryApplication.getApplicationYear());
//            singleBursaryApplicationDetails.setApplicationMonth(bursaryApplication.getApplicationMonth());
            singleBursaryApplicationDetails.setApplicationYear(bursaryApplication.getApplicationPeriod().getYear());
            singleBursaryApplicationDetails.setApplicationMonth(bursaryApplication.getApplicationPeriod().getMonth());
            singleBursaryApplicationDetails.setSchool(bursaryApplication.getSecStudent().getSecondaryschool());
            singleBursaryApplicationDetails.setStudentParent(bursaryApplication.getSecStudent().getStudentparent());
            singleBursaryApplicationDetails.setFormorclass(bursaryApplication.getFormorclass());
            return singleBursaryApplicationDetails;


        } catch (Exception e) {
            return ApiResponse.builder().success(false).message("an exception has ocurred while fetchings student details " + e);
        }
    }
    @Transactional
    public Object apdateisapproved(UserPrincipal currentuser, UpdateIsApprovedRequest updateIsApprovedRequestd) {

          BursaryApplication bursaryApplication= bursaryApplicationRepo.findById(updateIsApprovedRequestd.getApplicationId()).orElseThrow(() -> new CouldNotBeFoundException("the bursary application with given id "+updateIsApprovedRequestd.getApplicationId()+" could not be found "));
             if (bursaryApplication.isIsapproved()){
                 bursaryApplication.setIsapproved(false);
             }else{
                 bursaryApplication.setIsapproved(true);
             }
             bursaryApplication.setUpdatedBy(currentuser.getId());

             return  bursaryApplicationRepo.save(bursaryApplication).isIsapproved();

    }
    public List<SecondarySchoolCategory> fetchschoolcategories(UserPrincipal currentUser) {

       try {
           List<SecondarySchoolCategory> secondarySchoolCategories=secondarySchoolCategoryRepository.findAll().stream().collect(Collectors.toList());
           log.info("logging school categories {}",secondarySchoolCategories);
           return secondarySchoolCategories;
       }catch (Exception exception){
           throw new RuntimeException("An exception has occurred while trying to fetch school categories");
       }

    }

    @Transactional(readOnly = true)
    public Object fetchsbursaryapplicationssbyschoolcategory(UserPrincipal currentUser, Long categoryId, int page, int size) {
             log.info("logging categoryid {}",categoryId);
        if (currentUser==null){
            return ApiResponse.builder().success(false).httpStatus(HttpStatus.UNAUTHORIZED).message("user are unauthenticated").build();
        }
        try {
              validatePagenumberandSize(page, size);
            Pageable pageable=PageRequest.of(page,size,Sort.Direction.DESC,"createdAt");
              SecondarySchoolCategory secondarySchoolCategory=secondarySchoolCategoryRepository.findById(categoryId).orElseThrow(() -> new AppException("school with category provided could not be found "+categoryId));
              log.info("logging secstudent {}", pageable);
              log.info("logging secondary school category {}",secondarySchoolCategory);
               //fetching secondaryschools for given school category
            Page<SecondarySchool> secondarySchools=secondarySchoolRepository.findByCategory(secondarySchoolCategory,pageable);
              log.info("loggging secondary schools by category {}",secondarySchools);
             if (secondarySchools.getNumberOfElements()==0){
                return new PagedResponse<>(Collections.emptyList(),secondarySchools.getNumber(),secondarySchools.getSize(),secondarySchools.getTotalElements(),secondarySchools.getTotalPages(),secondarySchools.isLast());
            }
            List<Long>secoids=secondarySchools.map(SecondarySchool::getId).getContent();
            log.info("logging secoids {}",secoids);
//            Page<BursaryApplication> bursaryApplicationPage1=bursaryApplicationRepo.findBySecondarySchoolIn(secoids,pageable);
            Page<BursaryApplication> bursaryApplicationPage1= (Page<BursaryApplication>) bursaryApplicationRepo.findBySecondarySchoolIn(secoids,pageable);
            log.info("logging bursaryApplicationPage1 {}", bursaryApplicationPage1);
            bursaryApplicationPage1.forEach(bursaryApplication -> System.out.println("bursaryApplicationPage1hj "+bursaryApplication));
             if (bursaryApplicationPage1.getNumberOfElements()==0){
                 return new PagedResponse<>(Collections.emptyList(),secondarySchools.getNumber(),secondarySchools.getSize(),secondarySchools.getTotalElements(),secondarySchools.getTotalPages(),secondarySchools.isLast());
             }
            List<Allbursaryapplications> allbursaryapplications=bursaryApplicationPage1.map(ModelMappper::mapbursaryapplicationentitytoallbursaryapplicationsdto).getContent();

            log.info("logging viewbursaryapplications {}",allbursaryapplications);
            return new PagedResponse<>(allbursaryapplications,bursaryApplicationPage1.getNumber(),bursaryApplicationPage1.getSize(),bursaryApplicationPage1.getTotalElements(),bursaryApplicationPage1.getTotalPages(),bursaryApplicationPage1.isLast());



        }catch (Exception exception){
           throw new RuntimeException(exception);
//            return ApiResponse.builder().message("An exception has occurred while attempting to fetch students by school category "+exception.getMessage()).build();
        }


    }

    public Object checkparentonidnumberkeyup(UserPrincipal currentUser, CheckParentOnparentIdKeyUpRequest checkParentOnparentIdKeyUpRequest) {

           if (currentUser==null){
               return ApiResponse.builder().success(false).message("you are not authenticated").httpStatus(HttpStatus.UNAUTHORIZED).build();
           }
         try {
//             StudentParent studentParent=studentParentRepo.findByIdnumberLike(checkParentOnparentIdKeyUpRequest.getIdnumber());
//          log.info("logging the found data");
//             StudentParent studentParent=studentParentRepo.findByIdnumberContaining(checkParentOnparentIdKeyUpRequest.getIdnumber());
             boolean studentParen=studentParentRepo.existsByIdnumber(checkParentOnparentIdKeyUpRequest.getIdnumber());

             if (studentParen){
                 StudentParent studentParent=studentParentRepo.findByIdnumber(checkParentOnparentIdKeyUpRequest.getIdnumber()).orElseThrow(() -> new CouldNotBeFoundException("student parent not found"));

                 return ParentResponseOnKeyUp.builder().idnumber(studentParent.getIdnumber()).name(studentParent.getName()).phonenumber(studentParent.getPhonenumber()).build();
             }else{
                 return new ApiResponse(false,"no matches found  for "+checkParentOnparentIdKeyUpRequest.getIdnumber(),HttpStatus.BAD_REQUEST);

             }

         }catch (Exception exception){
             return new ApiResponse(false,"an exception has happened while trying to get parent data "+exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
         }
    }

    @Transactional(readOnly = true)
    public PagedResponse<BursaryAwardsResponse> fetchallbursaryawards(UserPrincipal currentUser, int page, int size) {

        try {
            validatePagenumberandSize(page, size);
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
            Page<BursaryFormSecskuls> bursaryFormSecskuls = bursaryFormSecskulsRepo.findAll(pageable);
            log.info("logging bursary awards {}", bursaryFormSecskuls);
            if (bursaryFormSecskuls.getNumberOfElements() == 0) {
                return new PagedResponse<>(Collections.emptyList(), bursaryFormSecskuls.getNumber(), bursaryFormSecskuls.getSize(), bursaryFormSecskuls.getTotalElements(), bursaryFormSecskuls.getTotalPages(), bursaryFormSecskuls.isLast());
            }
            List<BursaryAwardsResponse> bursaryAwardsResponses = bursaryFormSecskuls.map(ModelMappper::mapBursaryFormSchoolsToBursaryAwardRespons).getContent();
            log.info("logging bursaryawardresponse {}",bursaryAwardsResponses);
            return new PagedResponse<>(bursaryAwardsResponses, bursaryFormSecskuls.getNumber(), bursaryFormSecskuls.getSize(), bursaryFormSecskuls.getTotalElements(), bursaryFormSecskuls.getTotalPages(), bursaryFormSecskuls.isLast());


        } catch (Exception exception) {
            throw new RuntimeException("An exception has happeneed when fetching bursary awards " + exception.getMessage());
        }

    }

    @Transactional(readOnly = true)
    public PagedResponse<BursaryAwardsResponse> fetchallbursaryawardsbysecschool(UserPrincipal currentUser, BursaryAwardsBySecSchRequest bursaryAwardsBySecSchRequest, int page, int size) {
          validatePagenumberandSize(page, size);
          SecondarySchool secondarySchool=secondarySchoolRepository.findById(bursaryAwardsBySecSchRequest.getSchoolId()).orElseThrow(() -> new CouldNotBeFoundException("secondary school with provided id "+bursaryAwardsBySecSchRequest.getSchoolId()+" ould not be found"));
       try {
          Pageable pageable=PageRequest.of(page, size,Sort.Direction.ASC,"createdAt");
          Page<BursaryFormSecskuls> bursaryFormSecskuls=bursaryFormSecskulsRepo.findAllBySecondaryShool(secondarySchool,pageable);

           log.info("logging bursary awards {}", bursaryFormSecskuls);
           if (bursaryFormSecskuls.getNumberOfElements() == 0) {
               return new PagedResponse<>(Collections.emptyList(), bursaryFormSecskuls.getNumber(), bursaryFormSecskuls.getSize(), bursaryFormSecskuls.getTotalElements(), bursaryFormSecskuls.getTotalPages(), bursaryFormSecskuls.isLast());
           }
           List<BursaryAwardsResponse> bursaryAwardsResponses = bursaryFormSecskuls.map(ModelMappper::mapBursaryFormSchoolsToBursaryAwardRespons).getContent();
           log.info("logging bursaryawardresponse {}",bursaryAwardsResponses);
           return new PagedResponse<>(bursaryAwardsResponses, bursaryFormSecskuls.getNumber(), bursaryFormSecskuls.getSize(), bursaryFormSecskuls.getTotalElements(), bursaryFormSecskuls.getTotalPages(), bursaryFormSecskuls.isLast());


       }catch (Exception e){
           throw new RuntimeException("An exception has occurred while fetching bursary awards "+e.getMessage());
       }

    }

    @Transactional(readOnly =true)
    public PagedResponse<BursaryAwardsResponse> fetchbursaryawardsbysecschoolcategory(UserPrincipal currentUser, BursaryAwardsBySecSchRequest bursaryAwardsBySecSchRequest, int page, int size) {
        validatePagenumberandSize(page, size);
        SecondarySchoolCategory secondarySchoolCategory=secondarySchoolCategoryRepository.findById(bursaryAwardsBySecSchRequest.getSchoolCategoryId()).orElseThrow(() -> new CouldNotBeFoundException("secondary school category with provided id "+bursaryAwardsBySecSchRequest.getSchoolCategoryId()+" ould not be found"));
        try {
            Pageable pageable=PageRequest.of(page, size,Sort.Direction.ASC,"createdAt");
            Page<BursaryFormSecskuls> bursaryFormSecskuls=bursaryFormSecskulsRepo.findAllBySecondaryShoolCategory(secondarySchoolCategory,pageable);

            log.info("logging bursary awards {}", bursaryFormSecskuls);
            if (bursaryFormSecskuls.getNumberOfElements() == 0) {
                return new PagedResponse<>(Collections.emptyList(), bursaryFormSecskuls.getNumber(), bursaryFormSecskuls.getSize(), bursaryFormSecskuls.getTotalElements(), bursaryFormSecskuls.getTotalPages(), bursaryFormSecskuls.isLast());
            }
            List<BursaryAwardsResponse> bursaryAwardsResponses = bursaryFormSecskuls.map(ModelMappper::mapBursaryFormSchoolsToBursaryAwardRespons).getContent();
            log.info("logging bursaryawardresponse {}",bursaryAwardsResponses);
            return new PagedResponse<>(bursaryAwardsResponses, bursaryFormSecskuls.getNumber(), bursaryFormSecskuls.getSize(), bursaryFormSecskuls.getTotalElements(), bursaryFormSecskuls.getTotalPages(), bursaryFormSecskuls.isLast());


        }catch (Exception e){
            throw new RuntimeException("An exception has occurred while fetching bursary awards "+e.getMessage());
        }

    }

    @Transactional(readOnly=true)
    public PagedResponse<BursaryAwardsResponse> fetchawardsbyapplicationperiodyear(UserPrincipal currentUser, BursaryAwardsBySecSchRequest bursaryAwardsBySecSchRequest, int page, int size) {
        validatePagenumberandSize(page, size);
        try {
            Pageable pageable=PageRequest.of(page, size,Sort.Direction.ASC,"createdAt");
            Page<BursaryFormSecskuls> bursaryFormSecskuls=bursaryFormSecskulsRepo.findAllByApplicationYear(bursaryAwardsBySecSchRequest.getYear(),pageable);

            log.info("logging bursary awards {}", bursaryFormSecskuls);
            if (bursaryFormSecskuls.getNumberOfElements() == 0) {
                return new PagedResponse<>(Collections.emptyList(), bursaryFormSecskuls.getNumber(), bursaryFormSecskuls.getSize(), bursaryFormSecskuls.getTotalElements(), bursaryFormSecskuls.getTotalPages(), bursaryFormSecskuls.isLast());
            }
            List<BursaryAwardsResponse> bursaryAwardsResponses = bursaryFormSecskuls.map(ModelMappper::mapBursaryFormSchoolsToBursaryAwardRespons).getContent();
            log.info("logging bursaryawardresponse {}",bursaryAwardsResponses);
            return new PagedResponse<>(bursaryAwardsResponses, bursaryFormSecskuls.getNumber(), bursaryFormSecskuls.getSize(), bursaryFormSecskuls.getTotalElements(), bursaryFormSecskuls.getTotalPages(), bursaryFormSecskuls.isLast());


        }catch (Exception e){
            throw new RuntimeException("An exception has occurred while fetching bursary awards "+e.getMessage());
        }

    }

    @Transactional(readOnly = true)
    public PagedResponse<BursaryAwardsResponse> fetchawardsbyapplicationperiodyearandmonth(UserPrincipal currentUser, BursaryAwardsBySecSchRequest bursaryAwardsBySecSchRequest, int page, int size) {
        validatePagenumberandSize(page, size);
        try {
            Pageable pageable=PageRequest.of(page, size,Sort.Direction.ASC,"createdAt");
            Page<BursaryFormSecskuls> bursaryFormSecskuls=bursaryFormSecskulsRepo.findAllByApplicationYearandMonth(bursaryAwardsBySecSchRequest.getYear(),bursaryAwardsBySecSchRequest.getMonth(),pageable);

            log.info("logging bursary awards {}", bursaryFormSecskuls);
            if (bursaryFormSecskuls.getNumberOfElements() == 0) {
                return new PagedResponse<>(Collections.emptyList(), bursaryFormSecskuls.getNumber(), bursaryFormSecskuls.getSize(), bursaryFormSecskuls.getTotalElements(), bursaryFormSecskuls.getTotalPages(), bursaryFormSecskuls.isLast());
            }
            List<BursaryAwardsResponse> bursaryAwardsResponses = bursaryFormSecskuls.map(ModelMappper::mapBursaryFormSchoolsToBursaryAwardRespons).getContent();
            log.info("logging bursaryawardresponse {}",bursaryAwardsResponses);
            return new PagedResponse<>(bursaryAwardsResponses, bursaryFormSecskuls.getNumber(), bursaryFormSecskuls.getSize(), bursaryFormSecskuls.getTotalElements(), bursaryFormSecskuls.getTotalPages(), bursaryFormSecskuls.isLast());


        }catch (Exception e){
            throw new RuntimeException("An exception has occurred while fetching bursary awards "+e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Object fetchsecschoolbyid(UserPrincipal currentUser, FetchSecSchoolByIdRequest fetchSecSchoolByIdRequest) {

        log.info("sss {}",fetchSecSchoolByIdRequest);

       try {
           SecondarySchool secondarySchool=secondarySchoolRepository.findById(fetchSecSchoolByIdRequest.getSchoolId()).orElseThrow(() -> new CouldNotBeFoundException("school with provide id "+fetchSecSchoolByIdRequest.getSchoolId()+" could not be found"));
           log.info("logging secondary {}",secondarySchool);
           return secondarySchool;
       }catch (Exception exception){
           return ApiResponse.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).message("An exception has occurred while fetching school "+exception.getMessage()).success(false).build();
       }
    }

    public PagedResponse<SecSchoolStudentResponse> fetchallsecondaryschoolstudents(UserPrincipal currentUser, int page, int size) {

        validatePagenumberandSize(page, size);
        //getting schools as pageable
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "admissionnumber");
        log.info("logging pageable secschool students {}", pageable);
        Page<SecStudent> secStudents = secStudentRepo.findAll(pageable);
        log.info("logging sec schools students from server {}",  secStudents);
//        secStudents.forEach(secStudent -> {
//            log.info("logging pageable secondaryschool {}", secStudent);
//            System.out.println("Printing secondary schools pageable " + secStudent);
//
//        });
        if (secStudents.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), secStudents.getNumber(), secStudents.getSize(), secStudents.getTotalElements(), secStudents.getTotalPages(), secStudents.isLast());
        }
        List<SecSchoolStudentResponse> secSchoolStudentResponses = secStudents.map(ModelMappper::mapSecondarySchoolStudentToSecondarySchoolStudentResponse).getContent();
        log.info("logging secondary school students {}", secSchoolStudentResponses);
        return new PagedResponse<>(secSchoolStudentResponses, secStudents.getNumber(), secStudents.getSize(), secStudents.getTotalElements(), secStudents.getTotalPages(), secStudents.isLast());

    }

    public PagedResponse<BursaryAwardsResponse> fetchbursaryawardsbysecschoolstudentId(UserPrincipal currentUser, BursaryAwardsBySecSchRequest bursaryAwardsBySecSchRequest, int page, int size) {
        validatePagenumberandSize(page, size);
        SecStudent secStudent=secStudentRepo.findById(bursaryAwardsBySecSchRequest.getStudentId()).orElseThrow(() -> new CouldNotBeFoundException("student with provided id "+bursaryAwardsBySecSchRequest.getStudentId()+" could not be found"));
        try {
            Pageable pageable=PageRequest.of(page, size,Sort.Direction.ASC,"createdAt");
            Page<BursaryFormSecskuls> bursaryFormSecskuls=bursaryFormSecskulsRepo.findAllByStudentid(secStudent.getId(),pageable);

            log.info("logging bursary awards by student Id {}", bursaryFormSecskuls);
            if (bursaryFormSecskuls.getNumberOfElements() == 0) {
                return new PagedResponse<>(Collections.emptyList(), bursaryFormSecskuls.getNumber(), bursaryFormSecskuls.getSize(), bursaryFormSecskuls.getTotalElements(), bursaryFormSecskuls.getTotalPages(), bursaryFormSecskuls.isLast());
            }
            List<BursaryAwardsResponse> bursaryAwardsResponses = bursaryFormSecskuls.map(ModelMappper::mapBursaryFormSchoolsToBursaryAwardRespons).getContent();
            log.info("logging bursaryawardresponse by sec school{}",bursaryAwardsResponses);
            return new PagedResponse<>(bursaryAwardsResponses, bursaryFormSecskuls.getNumber(), bursaryFormSecskuls.getSize(), bursaryFormSecskuls.getTotalElements(), bursaryFormSecskuls.getTotalPages(), bursaryFormSecskuls.isLast());


        }catch (Exception e){
            throw new RuntimeException("An exception has occurred while fetching bursary awards "+e.getMessage());
        }

    }

    public Object approveordisaprovearray(UserPrincipal currentuser, List<Long> ids) {
        final AwardBursaryResponse[] awardBursaryResponse = {null};
       StringBuilder stringBuilder=new StringBuilder();
        if (ids.isEmpty() || ids==null){
            return ApiResponse.builder().success(false).httpStatus(HttpStatus.BAD_REQUEST).message("Empty list ").build();
        }else{
            ids.forEach(aLong -> {
                boolean existsbyid=bursaryApplicationRepo.existsById(aLong);
                if (!existsbyid){
                    log.info("does not exists {}",aLong);
                }else{
                    BursaryApplication bursaryApplication=bursaryApplicationRepo.findById(aLong).orElseThrow(() -> new CouldNotBeFoundException("Bursary application with provided Id could not be found"));
                     stringBuilder.append(" ").append(aLong);
                    if (bursaryApplication.isIsapproved()){
                        bursaryApplication.setIsapproved(false);
                    }else{
                        bursaryApplication.setIsapproved(true);
                    }
                    bursaryApplication.setUpdatedBy(currentuser.getId());
                    bursaryApplicationRepo.save(bursaryApplication);
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

    public Object checkbystudentadmnoandschool(UserPrincipal currentUser,  Checkstudentbyadmnoandschool checkstudentbyadmnoandschool) {

         log.info("checkstudentbyadmnoandschool {}",checkstudentbyadmnoandschool);

        if (currentUser==null){
            return ApiResponse.builder().success(false).message("you are not authenticated").httpStatus(HttpStatus.UNAUTHORIZED).build();
        }
        boolean school=secondarySchoolRepository.existsById(checkstudentbyadmnoandschool.getSchoolId());
         if (!school){
             return false;
         }else {

             try {
                 SecondarySchool secondarySchool=secondarySchoolRepository.findById(checkstudentbyadmnoandschool.getSchoolId()).orElseThrow(() -> new CouldNotBeFoundException("school with the provided id number could not be found "+checkstudentbyadmnoandschool.getSchoolId()));

                 boolean student = secStudentRepo.existsByAdmissionnumberAndSecondaryschool(checkstudentbyadmnoandschool.getStudentAdmno(),secondarySchool);
                 if (student) {
                     SecStudent secStudent=secStudentRepo.findByAdmissionnumberAndSecondaryschool(checkstudentbyadmnoandschool.getStudentAdmno(),secondarySchool).orElseThrow(() -> new CouldNotBeFoundException("student not found"));
                     return Checkstudentbyadmnoandschool.builder().schoolId(secondarySchool.getId()).studentAdmno(secStudent.getAdmissionnumber()).fullname(secStudent.getFullname()).build();
                 } else {
                     return new ApiResponse(false, "no matches found  for " + checkstudentbyadmnoandschool.getStudentAdmno(), HttpStatus.BAD_REQUEST);

                 }

             } catch (Exception exception) {
                 return new ApiResponse(false, "an exception has happened while trying to get parent data " + exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
             }
         }
    }
    }

