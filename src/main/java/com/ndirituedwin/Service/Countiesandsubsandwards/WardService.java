package com.ndirituedwin.Service.Countiesandsubsandwards;

import com.ndirituedwin.Dto.Requests.Wardssubountiesandcounties.WardRequest;
import com.ndirituedwin.Dto.Responses.SecSchool.PagedResponse;
import com.ndirituedwin.Dto.Responses.Wardssubountiesandcounties.ViewAllWards;
import com.ndirituedwin.Dto.Responses.Wardssubountiesandcounties.WardResponse;
import com.ndirituedwin.Entity.SubCounty;
import com.ndirituedwin.Entity.Ward;
import com.ndirituedwin.Exception.CouldNotBeFoundException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class WardService {
   private  final SubCountyRepository subCountyRepository;
   private final WardRepository wardRepository;
   private SecondarySchoolsService secondarySchoolsService;

    public WardResponse save(WardRequest wardrequest) {
        SubCounty  subCounty=subCountyRepository.findById(wardrequest.getSubcountyId()).orElseThrow(() -> new CouldNotBeFoundException("subcounty with id "+wardrequest.getSubcountyId() +"could not be found"));
        log.info("logging subcounty {}",subCounty);
         long countwards=wardRepository.countBySubCountyIdAndWard(wardrequest.getWard(),wardrequest.getSubcountyId());
         if(!(countwards>0)){
             log.info("logging Wardssubountiesandcounties {}",countwards);
             Ward ward=new Ward();
             ward.setWard(wardrequest.getWard());
             ward.setSubcounty(subCounty);
             log.info("ward about to be saved {}",ward);
             try {
               Ward savedwared=wardRepository.save(ward);
                return WardResponse.builder()
                        .ward(savedwared.getWard())
                        .subCounty(savedwared.getSubcounty())
                        .message(" ward successfuy saved")
                        .build();
             }catch (Exception exception){
                 throw new RuntimeException("An exception has occurred trying to save ward  "+exception.getMessage());
             }
         }
        return null;
    }


}
