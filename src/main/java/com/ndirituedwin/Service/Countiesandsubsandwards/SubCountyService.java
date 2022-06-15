package com.ndirituedwin.Service.Countiesandsubsandwards;

import com.ndirituedwin.Dto.Requests.Wardssubountiesandcounties.SubCountyRequest;
import com.ndirituedwin.Dto.Responses.ApiResponse;
import com.ndirituedwin.Dto.Responses.SecSchool.PagedResponse;
import com.ndirituedwin.Dto.Responses.SecSchool.SingleBursaryApplicationDetails;
import com.ndirituedwin.Dto.Responses.Wardssubountiesandcounties.CountyResponse;
import com.ndirituedwin.Dto.Responses.Wardssubountiesandcounties.SubCountyResponse;
import com.ndirituedwin.Entity.Bursary.Secskuls.BursaryApplication;
import com.ndirituedwin.Entity.County;
import com.ndirituedwin.Entity.SubCounty;
import com.ndirituedwin.Exception.CouldNotBeFoundException;
import com.ndirituedwin.Exception.CountyCodeNotFoundException;
import com.ndirituedwin.Repository.CountyRepository;
import com.ndirituedwin.Repository.SubCountyRepository;
import com.ndirituedwin.Security.UserPrincipal;
import com.ndirituedwin.Service.Mapper.ModelMappper;
import com.ndirituedwin.Service.SecondarySchool.SecondarySchoolsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class SubCountyService {
    private final SubCountyRepository subCountyRepository;
    private final CountyRepository countyRepository;
    private final SecondarySchoolsService secondarySchoolsService;
    @Transactional
    public Object save(SubCountyRequest subCountyRequest) {

        County county=countyRepository.findByCode(subCountyRequest.getCounty_code()).orElseThrow(() -> new CountyCodeNotFoundException("county "+subCountyRequest.getCounty_code()+" could not be located"));
      //query to check for both countycodeandcountyname

         try {
        long subCountycount=subCountyRepository.countByCountyAndSubcounty(subCountyRequest.getCounty_code(),subCountyRequest.getSubcounty());
          if (!(subCountycount >0)){
        SubCounty subCounty=new SubCounty();
        subCounty.setSubcounty(subCountyRequest.getSubcounty());
        subCounty.setCounty(county);
        log.info("logging the subcounty about to be saved {}",subCounty);
             subCountyRepository.save(subCounty);
            return ApiResponse.builder().success(true).httpStatus(HttpStatus.CREATED).message("subcounty "+subCountyRequest.getSubcounty()+" saved").build();

          }else{
              return ApiResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).success(false).message("subcounty with similar name "+subCountyRequest.getSubcounty()+" and similar county "+county.getCounty()+" exists "+subCountycount).build();
          }
         }catch (Exception e){
             throw new RuntimeException("an exception has occurred while trying to save subcounty "+e.getMessage());
         }

    }

    @Transactional(readOnly = true)
    public PagedResponse<SubCountyResponse> getallsubcounties(UserPrincipal currentUser, int page, int size) {

        secondarySchoolsService.validatePagenumberandSize(page,size);
        Pageable pageable= PageRequest.of(page,size, Sort.Direction.ASC,"subcounty");
        Page<SubCounty> subCounties=subCountyRepository.findAll(pageable);
        log.info("log subcounties {}",subCounties);
        if (subCounties.getNumberOfElements()==0){
            return new PagedResponse<>(Collections.emptyList(),subCounties.getNumber(),subCounties.getSize(), subCounties.getNumberOfElements(),subCounties.getTotalPages(),subCounties.isLast());
        }
        List<SubCountyResponse> countyResponses=subCounties.map(ModelMappper::mapcountysubcountiestosubcountyresponse).getContent();
        log.info("logging subcounties {}",countyResponses);
        return new PagedResponse<>(countyResponses, subCounties.getNumber(),subCounties.getSize(), subCounties.getNumberOfElements(), subCounties.getTotalPages(), subCounties.isLast());

    }

    @Transactional(readOnly = true)
    public Object fetchbyid(UserPrincipal currentUser, Long subcountyId) {
        SubCounty subCounty=subCountyRepository.findById(subcountyId).orElseThrow(() -> new CouldNotBeFoundException("subcounty  with the provided id "+subcountyId+" could not be found"));
        try {

           SubCountyResponse subCountyResponse=new SubCountyResponse();
           subCountyResponse.setId(subCounty.getId());
           subCountyResponse.setSubcounty(subCounty.getSubcounty());
           subCounty.setCounty(subCounty.getCounty());
            return subCountyResponse;


        } catch (Exception e) {
            return ApiResponse.builder().success(false).message("an exception has ocurred while fetchings student details " + e);
        }
    }

    public Object update(SubCountyRequest subCountyRequest) {

       try {
           SubCounty subCounty=subCountyRepository.findById(subCountyRequest.getId()).orElseThrow(() -> new  CountyCodeNotFoundException("subcounty not found"));
           County county=countyRepository.findByCode(subCountyRequest.getCounty_code()).orElseThrow(() -> new  CountyCodeNotFoundException("county not found"));
           subCounty.setSubcounty(subCountyRequest.getSubcounty());
           subCounty.setCounty(county);
           subCountyRepository.save(subCounty);
           return ApiResponse.builder().success(true).httpStatus(HttpStatus.CREATED).message("subcounty updated").build();

       }catch (Exception exception){
           return ApiResponse.builder().message("failed to update subcounty "+exception.getMessage());
       }
    }
}

