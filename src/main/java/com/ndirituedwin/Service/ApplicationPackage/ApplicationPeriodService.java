package com.ndirituedwin.Service.ApplicationPackage;

import com.ndirituedwin.Dto.Requests.ApplicationPeriodRequest;
import com.ndirituedwin.Dto.Requests.ApplicationPeriodStatusrequest;
import com.ndirituedwin.Dto.Requests.ApplicationperiodYearMonthsrequest;
import com.ndirituedwin.Dto.Responses.ApiResponse;
import com.ndirituedwin.Entity.ApplicationPeriod;
import com.ndirituedwin.Exception.CouldNotBeFoundException;
import com.ndirituedwin.Repository.ApplicationPeriodRepo;
import com.ndirituedwin.Security.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Service
@Slf4j
@AllArgsConstructor
public class ApplicationPeriodService {
    private final ApplicationPeriodRepo applicationPeriodRepo;


    public ApiResponse save(UserPrincipal currentUser, ApplicationPeriodRequest applicationPeriodRequest) {

        if (currentUser==null){
            return new ApiResponse(false,"please authenticate first ", HttpStatus.UNAUTHORIZED);
        }else{

            if (applicationPeriodRepo.existsByYearAndMonth(applicationPeriodRequest.getYear(),applicationPeriodRequest.getMonth())){
                return new ApiResponse(false,"failed !, you cannot add same year and month ",HttpStatus.BAD_REQUEST);
            }
            try {
                ApplicationPeriod applicationPeriod=new ApplicationPeriod();
                applicationPeriod.setYear(applicationPeriodRequest.getYear());
                applicationPeriod.setMonth(applicationPeriodRequest.getMonth());
                applicationPeriod.setOpen(true);
                applicationPeriod.setCreatedBy(currentUser.getId());
                applicationPeriod.setUpdatedBy(currentUser.getId());
                applicationPeriodRepo.findAll().forEach(applicationPeriod1 -> {
                    applicationPeriod1.setOpen(false);
                    applicationPeriodRepo.save(applicationPeriod1);
                });
                ApplicationPeriod saved=  applicationPeriodRepo.save(applicationPeriod);

                log.info("logging about to save application {} ",applicationPeriod);
                log.info("logging saved appplication period {}",saved);
                return new ApiResponse(true,"application period  "+saved.getYear()+" "+saved.getMonth()+" saved" ,HttpStatus.CREATED);

            }catch (Exception exception){
                return new ApiResponse(false,"An exception has occured while trying to save application period "+exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }


    }


    public List<ApplicationPeriod> getall(UserPrincipal currentUser) {
        try {
//        List<ApplPeriodsPackage> applicationPeriodRequest= new ArrayList<>(applicationPeriodRepo.findAll());
            List<ApplicationPeriod> applicationPeriodRequest= new ArrayList<>(applicationPeriodRepo.findAllByCreatedAtDesc());
            log.info("logging applicationrequest {}",applicationPeriodRequest);
            return applicationPeriodRequest;
        }catch (Exception exception){
            throw new RuntimeException("an exception has ocurred "+exception);
        }

    }

    @Transactional
    public Object checkopenapplicationperiod(UserPrincipal currentUser, ApplicationPeriodStatusrequest applicationPeriodStatusrequest){
        //checking whether ther exists an open application period
        boolean existsbyisopen=applicationPeriodRepo.existsByIsOpenEquals(true);
        log.info("ApplicationPeriodStatusrequest,{}",applicationPeriodStatusrequest);
        log.info("existsbyisopen,{}",existsbyisopen);
        if (existsbyisopen){
            ApplicationPeriod applicationPeriod1=applicationPeriodRepo.findByIsOpenEquals(true).orElseThrow(() -> new CouldNotBeFoundException("application period could not be found"));
            log.info("open application period ",applicationPeriod1);
            if(applicationPeriod1.getId() !=applicationPeriodStatusrequest.getId()){

                return  ApiResponse.builder().success(false).message("application period "+applicationPeriod1.getYear()+" "+applicationPeriod1.getMonth()+" is Open and must be closed to open another application period.Click yes to close or cancel to cancel").build();
            }else{
                //if there is no open application period
                ApplicationPeriod applicationPeriod=applicationPeriodRepo.findById(applicationPeriodStatusrequest.getId()).orElseThrow(() -> new UsernameNotFoundException("application period with provided id "+applicationPeriodStatusrequest.getId()+" could not be found"));
                log.info("logging application period {}",applicationPeriod);

                applicationPeriod.setOpen(!applicationPeriod.isOpen());
                applicationPeriod.setCreatedBy(currentUser.getId());
                applicationPeriod.setUpdatedBy(currentUser.getId());
                ApplicationPeriod saved=applicationPeriodRepo.save(applicationPeriod);
                log.info("logging saved application{}",saved);


                if (saved.isOpen()){
                    return true;
                }else {
                    return false;
                }
            }
        }else{
            //if there is no open application period
            ApplicationPeriod applicationPeriod=applicationPeriodRepo.findById(applicationPeriodStatusrequest.getId()).orElseThrow(() -> new UsernameNotFoundException("application period with provided id "+applicationPeriodStatusrequest.getId()+" could not be found"));
            log.info("logging application period {}",applicationPeriod);

            applicationPeriod.setOpen(!applicationPeriod.isOpen());
            applicationPeriod.setCreatedBy(currentUser.getId());
            applicationPeriod.setUpdatedBy(currentUser.getId());
            ApplicationPeriod saved=applicationPeriodRepo.save(applicationPeriod);
            log.info("logging saved application{}",saved);


            if (saved.isOpen()){
                return true;
            }else {
                return false;
            }
        }


    }

    @Transactional
    public boolean update(UserPrincipal currentUser, ApplicationPeriodStatusrequest applicationPeriodStatusrequest) {

        log.info("logging id {}",applicationPeriodStatusrequest);
        boolean existsbyisopen=applicationPeriodRepo.existsByIsOpenEquals(true);
        log.info("ApplicationPeriodStatusrequest,{}",applicationPeriodStatusrequest);
        log.info("existsbyisopen,{}",existsbyisopen);
        if (existsbyisopen) {
            ApplicationPeriod applicationPeriod1 = applicationPeriodRepo.findByIsOpenEquals(true).orElseThrow(() -> new CouldNotBeFoundException("application period could not be found"));
            log.info("open application period ", applicationPeriod1);
            applicationPeriod1.setOpen(false);
            applicationPeriod1.setUpdatedBy(currentUser.getId());
            applicationPeriodRepo.save(applicationPeriod1);
        }



        ApplicationPeriod applicationPeriod=applicationPeriodRepo.findById(applicationPeriodStatusrequest.getId()).orElseThrow(() -> new UsernameNotFoundException("application period with provided id "+applicationPeriodStatusrequest.getId()+" could not be found"));
        log.info("logging application period {}",applicationPeriod);

        applicationPeriod.setOpen(!applicationPeriod.isOpen());
        applicationPeriod.setCreatedBy(currentUser.getId());
        applicationPeriod.setUpdatedBy(currentUser.getId());
        ApplicationPeriod saved=applicationPeriodRepo.save(applicationPeriod);
        log.info("logging saved application{}",saved);


        if (saved.isOpen()){
            return true;
        }else {
            return false;
        }
    }

    public Set<?> openapplicationperiods(UserPrincipal currentUser) {
        List<ApplicationPeriod> openApplicationPeriods=applicationPeriodRepo.findAllByIsOpenEquals(true);
        Set<String> uniqueyears=new HashSet<>(0);
        openApplicationPeriods.forEach((applicationPeriod  )->{
            uniqueyears.add(applicationPeriod.getYear());
        });
        log.info("logging open application periods  years {}",openApplicationPeriods);
        log.info("logging the set {}",uniqueyears);
        return uniqueyears;

    }
    public Object openapplicationperiod(UserPrincipal currentUser) {

        boolean applicationPeriod=applicationPeriodRepo.existsByIsOpenEquals(true);
        if (! applicationPeriod){
        }
        try {
            ApplicationPeriod applicationPeriod1=applicationPeriodRepo.findByIsOpenEquals(true).orElseThrow(() -> new CouldNotBeFoundException("No open applicxation perio"));

            return applicationPeriod1;
        }catch (Exception exception){
            return ApiResponse.builder().success(false).httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).message("error during fetch "+exception.getMessage()).build();
        }


    }
    public List<ApplicationPeriod> fetchappliationperiodyearmonths(UserPrincipal currentUser, ApplicationperiodYearMonthsrequest applicationperiodYearMonthsrequest) {

        List<ApplicationPeriod> applicationPeriods=applicationPeriodRepo.findAllByYearAndIsOpenEquals(applicationperiodYearMonthsrequest.getYear(),true);
        log.info("logging all application periods {}",applicationPeriods);
        return applicationPeriods;
    }
    public Set<?> applicationperiodsyears(UserPrincipal currentUser) {
        List<ApplicationPeriod> applicationPeriods= new ArrayList<>(applicationPeriodRepo.findAll());
        Set<String> uniqueyears=new HashSet<>();
        applicationPeriods.forEach(applicationPeriod -> {
            uniqueyears.add(applicationPeriod.getYear());
        });
        return uniqueyears;
    }
    public List<ApplicationPeriod> fetchallappliationperiodyearmonths(UserPrincipal currentUser, ApplicationperiodYearMonthsrequest applicationperiodYearMonthsrequest) {

        List<ApplicationPeriod> applicationPeriods=applicationPeriodRepo.findAllByYear(applicationperiodYearMonthsrequest.getYear());
        log.info("logging all application periods {}",applicationPeriods);
        return applicationPeriods;
    }


}
