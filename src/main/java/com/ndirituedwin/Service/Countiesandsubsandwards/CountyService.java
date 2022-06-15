package com.ndirituedwin.Service.Countiesandsubsandwards;

import com.ndirituedwin.Dto.Requests.Wardssubountiesandcounties.FetchsubcountiesbycountyRequest;
import com.ndirituedwin.Dto.Requests.Wardssubountiesandcounties.FetchwardsbysubcountyRequest;
import com.ndirituedwin.Dto.Requests.Wardssubountiesandcounties.SaveCountyRequest;
import com.ndirituedwin.Dto.Responses.ApiResponse;
import com.ndirituedwin.Dto.Responses.SecSchool.PagedResponse;
import com.ndirituedwin.Dto.Responses.Wardssubountiesandcounties.CountyResponse;
import com.ndirituedwin.Dto.Responses.Wardssubountiesandcounties.SubCountyResponse;
import com.ndirituedwin.Dto.Responses.Wardssubountiesandcounties.ViewAllWards;
import com.ndirituedwin.Dto.Responses.Wardssubountiesandcounties.WardResponse;
import com.ndirituedwin.Entity.County;
import com.ndirituedwin.Entity.SubCounty;
import com.ndirituedwin.Entity.Ward;
import com.ndirituedwin.Exception.CouldNotBeFoundException;
import com.ndirituedwin.Exception.ResourceNotFoundException;
import com.ndirituedwin.Repository.CountyRepository;
import com.ndirituedwin.Repository.SubCountyRepository;
import com.ndirituedwin.Repository.WardRepository;
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


@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class CountyService {

    private final CountyRepository countyRepository;
   private final SecondarySchoolsService secondarySchoolsService;
   private final SubCountyRepository subCountyRepository;
   private final WardRepository wardRepository;

   @Transactional(readOnly = true)
   public PagedResponse<CountyResponse> getallcounties(UserPrincipal currentUser, int page, int size) {
      secondarySchoolsService.validatePagenumberandSize(page,size);
        Pageable pageable= PageRequest.of(page,size, Sort.Direction.ASC,"code");
        Page<County> counties=countyRepository.findAll(pageable);
        log.info("log c {}",counties);
        if (counties.getNumberOfElements()==0){
            return new PagedResponse<>(Collections.emptyList(),counties.getNumber(),counties.getSize(), counties.getNumberOfElements(),counties.getTotalPages(),counties.isLast());
        }
        List<CountyResponse> countyResponses=counties.map(ModelMappper::mapcountiestocountyResponse).getContent();
        log.info("logging counies {}",countyResponses);
        return new PagedResponse<>(countyResponses,counties.getNumber(),counties.getSize(), counties.getNumberOfElements(), counties.getTotalPages(), counties.isLast());

    }

//    public PagedResponse<SubCountyResponse> fetchcountysubcounties(UserPrincipal currentUser, FetchsubcountiesbycountyRequest fetchsubcountiesbycountyRequest, int page, int size) {
    public Object fetchcountysubcounties(UserPrincipal currentUser, FetchsubcountiesbycountyRequest fetchsubcountiesbycountyRequest, int page, int size) {

      secondarySchoolsService.validatePagenumberandSize(page, size);
      if (fetchsubcountiesbycountyRequest.getCode()==null){
          return new ApiResponse(false,"the request body may not be empty",HttpStatus.BAD_REQUEST);

      }

      boolean countyexistsbycode=countyRepository.existsByCode(fetchsubcountiesbycountyRequest.getCode());
       if (!countyexistsbycode){
           return new ApiResponse(false,"county with given code "+fetchsubcountiesbycountyRequest.getCode()+" could not be found ", HttpStatus.NOT_FOUND);
       }
      try {
          County county=countyRepository.findByCode(fetchsubcountiesbycountyRequest.getCode()).orElseThrow(() -> new CouldNotBeFoundException("the given code could not be found "+fetchsubcountiesbycountyRequest.getCode()));
          Pageable pageable=PageRequest.of(page, size,Sort.Direction.ASC,"subcounty");
          Page<SubCounty> subCounties=subCountyRepository.findAllByCounty(county,pageable);
          subCounties.forEach(subCounty -> log.info("loggging subcounty {}",subCounty));
          if (subCounties.getNumberOfElements()==0){
              return new PagedResponse<>(Collections.emptyList(),subCounties.getNumber(), subCounties.getSize(), subCounties.getNumberOfElements(), subCounties.getTotalPages(), subCounties.isLast());
          }
          List<SubCountyResponse> subCountyResponses=subCounties.map(ModelMappper::mapcountysubcountiestosubcountyresponse).getContent();
          log.info("logging countysubcounties {}",subCountyResponses);
          return new PagedResponse<>(subCountyResponses, subCounties.getNumber(), subCounties.getSize(), subCounties.getNumberOfElements(), subCounties.getTotalPages(), subCounties.isLast());

      }catch (Exception exception){
          return new ApiResponse(false,"an exception has occurred while loading county subcounties "+ exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
      }
         }

    public Object fetchsubcountywards(UserPrincipal currentUser, FetchwardsbysubcountyRequest fetchwardsbysubcountyRequest, int page, int size) {
          secondarySchoolsService.validatePagenumberandSize(page, size);
                boolean existsbyid=subCountyRepository.existsById(fetchwardsbysubcountyRequest.getId());
                if (!existsbyid){
                    return new ApiResponse(false,"subcounty with id "+fetchwardsbysubcountyRequest.getId()+" could not be found ",HttpStatus.NOT_FOUND);
                }
            try {
                SubCounty subCounty=subCountyRepository.findById(fetchwardsbysubcountyRequest.getId()).orElseThrow(() -> new CouldNotBeFoundException("subcounty not found with given id "+fetchwardsbysubcountyRequest.getId()));
                Pageable pageable=PageRequest.of(page, size,Sort.Direction.ASC,"ward");
                Page<Ward> wards=wardRepository.findAllBySubcounty(subCounty,pageable);
                if (wards.getNumberOfElements()==0){
                    return new PagedResponse<>(Collections.emptyList(),wards.getNumber(), wards.getSize(), wards.getNumberOfElements(), wards.getTotalPages(), wards.isLast());

                }
                List<ViewAllWards>  wardResponses=wards.map(ModelMappper::mapwardstoviewallwards).getContent();

                return new PagedResponse<>(wardResponses, wards.getNumber(), wards.getSize(), wards.getNumberOfElements(), wards.getTotalPages(), wards.isLast());

            }catch (Exception e){
                return new ApiResponse(false,"an error has occurred while trying to fetch wards by subcounty "+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            }

            }

            @Transactional
    public Object createcounty(UserPrincipal currentUser, SaveCountyRequest saveCountyRequest) {

          try {
              County county=new County();
              county.setCode(saveCountyRequest.getCode());
              county.setCounty(saveCountyRequest.getCounty());
              county.setArea(saveCountyRequest.getArea());
              county.setRegion(saveCountyRequest.getRegion());
              county.setCapital(saveCountyRequest.getCapital());
              countyRepository.save(county);
              return ApiResponse.builder().success(true).httpStatus(HttpStatus.CREATED).message("county with name "+saveCountyRequest.getCounty()+" has been save");
          }catch (Exception e){
              return ApiResponse.builder().success(false).httpStatus(HttpStatus.BAD_REQUEST).message("an exception has occurred whille saving the county "+e.getMessage()).build();

          }
    }
}
