package com.ndirituedwin.Service.Mapper;


import com.ndirituedwin.Dto.Requests.HigherEducation.AllHigherLearningBursaryapplications;
import com.ndirituedwin.Dto.Responses.HigherEducationResponse;
import com.ndirituedwin.Dto.Responses.HigherLearning.HigherLearningBursaryAwardsResponse;
import com.ndirituedwin.Dto.Responses.SecSchool.*;
import com.ndirituedwin.Dto.Responses.Wardssubountiesandcounties.CountyResponse;
import com.ndirituedwin.Dto.Responses.Wardssubountiesandcounties.SubCountyResponse;
import com.ndirituedwin.Dto.Responses.Wardssubountiesandcounties.ViewAllWards;
import com.ndirituedwin.Entity.*;
import com.ndirituedwin.Entity.Bursary.HigherLearning.HigherEducationn;
import com.ndirituedwin.Entity.Bursary.HigherLearning.HigherLearningBursaryApplication;
import com.ndirituedwin.Entity.Bursary.HigherLearning.HigherLearningBursaryForm;
import com.ndirituedwin.Entity.Bursary.HigherLearning.HigherLearningStudent;
import com.ndirituedwin.Entity.Bursary.Secskuls.BursaryApplication;
import com.ndirituedwin.Entity.Bursary.Secskuls.BursaryFormSecskuls;
import com.ndirituedwin.Entity.Bursary.Secskuls.SecStudent;
import com.ndirituedwin.Entity.Bursary.Secskuls.StudentParent;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class ModelMappper {

 public static SchoolResponse mapSecondarySchoolToSecondarySchoolResponse(SecondarySchool secondarySchool){
     SchoolResponse schoolResponse=new SchoolResponse();
     schoolResponse.setId(secondarySchool.getId());
     schoolResponse.setCode(secondarySchool.getCode());
     schoolResponse.setSchool(secondarySchool.getSchool());
     schoolResponse.setCategory(secondarySchool.getCategory());
     schoolResponse.setType(secondarySchool.getType());
     schoolResponse.setCounty(secondarySchool.getCounty());
     return schoolResponse;

 }
 public static Viewbursaryapplications mapSecstudenttoviewbursaryapplications(SecStudent secStudent){
     Viewbursaryapplications viewbursaryapplications=new Viewbursaryapplications();
     viewbursaryapplications.setId(secStudent.getId());
     viewbursaryapplications.setAdmissionnumber(secStudent.getAdmissionnumber());
     viewbursaryapplications.setFullname(secStudent.getFullname());
//     viewbursaryapplications.setFormorclas(secStudent.getFormorclass());
     viewbursaryapplications.setSecondaryschool(secStudent.getSecondaryschool());
     viewbursaryapplications.setStudentparent(secStudent.getStudentparent());
     viewbursaryapplications.setCreatedAt(secStudent.getCreatedAt());
//     viewbursaryapplications.setIsapproved(secStudent.isIsapproved());
//     viewbursaryapplications.setIdnumber(secStudent.getStudentparent().getIdnumber());
//     viewbursaryapplications.setParentname(secStudent.getStudentparent().getName());
//     viewbursaryapplications.setPhonenumber(secStudent.getStudentparent().getPhonenumber());
     return viewbursaryapplications;
 }
 public static ViewAllWards mapwardstoviewallwards(Ward ward){
     ViewAllWards viewallwards=new ViewAllWards();
     viewallwards.setId(ward.getId());
     viewallwards.setWard(ward.getWard());
     viewallwards.setSubCounty(ward.getSubcounty());
     return viewallwards;
 }
 public static CountyResponse mapcountiestocountyResponse(County county){
     CountyResponse countyResponse=new CountyResponse();
     countyResponse.setCode(county.getCode());
     countyResponse.setCounty(county.getCounty());
  return countyResponse;
 }

    public static SubCountyResponse mapcountysubcountiestosubcountyresponse(SubCounty subCounty) {
        SubCountyResponse subCountyResponse=new SubCountyResponse();
        subCountyResponse.setId(subCounty.getId());
        subCountyResponse.setSubcounty(subCounty.getSubcounty());
        subCountyResponse.setCounty(subCounty.getCounty());
    return subCountyResponse;

 }

    public static Allbursaryapplications mapbursaryapplicationentitytoallbursaryapplicationsdto(BursaryApplication bursaryApplication) {
     Allbursaryapplications allbursaryapplications=new Allbursaryapplications();
    allbursaryapplications.setId(bursaryApplication.getId());
    allbursaryapplications.setAdmissionnumber(bursaryApplication.getSecStudent().getAdmissionnumber());
    allbursaryapplications.setFullname(bursaryApplication.getSecStudent().getFullname());
    allbursaryapplications.setFormorclas(bursaryApplication.getFormorclass());
//    allbursaryapplications.setApplicationyear(bursaryApplication.getApplicationYear());
//    allbursaryapplications.setApplicationmonth(bursaryApplication.getApplicationMonth());
     allbursaryapplications.setApplicationyear(bursaryApplication.getApplicationPeriod().getYear());
    allbursaryapplications.setApplicationmonth(bursaryApplication.getApplicationPeriod().getMonth());
    allbursaryapplications.setSecondaryschool(bursaryApplication.getSecStudent().getSecondaryschool());
//    allbursaryapplications.setSecondaryschool(bursaryApplication.getSecStudent().getSecondaryschool().getSchool());
    allbursaryapplications.setStudentparent(bursaryApplication.getSecStudent().getStudentparent());
//    allbursaryapplications.setPhonenumber(bursaryApplication.getSecStudent().getStudentparent().getPhonenumber());
//    allbursaryapplications.setCounty(bursaryApplication.getSecStudent().getStudentparent().getWard().getSubcounty().getCounty().getCounty());
//    allbursaryapplications.setWard(bursaryApplication.getSecStudent().getStudentparent().getWard().getWard());
    allbursaryapplications.setIsapproved(bursaryApplication.isIsapproved());
    allbursaryapplications.setSecStudentId(bursaryApplication.getSecStudent().getId());
    return  allbursaryapplications;
 }

    public static BursaryAwardsResponse mapBursaryFormSchoolsToBursaryAwardRespons(BursaryFormSecskuls bursaryFormSecskuls) {
     BursaryAwardsResponse bursaryAwardsResponse=new BursaryAwardsResponse();
     bursaryAwardsResponse.setId(bursaryFormSecskuls.getId());
     bursaryAwardsResponse.setFullname(bursaryFormSecskuls.getBursaryApplication().getSecStudent().getFullname());
     bursaryAwardsResponse.setAdmno(bursaryFormSecskuls.getBursaryApplication().getSecStudent().getAdmissionnumber());
     bursaryAwardsResponse.setFormorclass(bursaryFormSecskuls.getBursaryApplication().getFormorclass());
     bursaryAwardsResponse.setSecondarySchool(bursaryFormSecskuls.getBursaryApplication().getSecStudent().getSecondaryschool().getSchool());
     bursaryAwardsResponse.setSecondaryschoolcategory(bursaryFormSecskuls.getBursaryApplication().getSecStudent().getSecondaryschool().getCategory().getCategory());
     bursaryAwardsResponse.setSchoollocation(bursaryFormSecskuls.getBursaryApplication().getSecStudent().getSecondaryschool().getCounty().getCounty());
     bursaryAwardsResponse.setSchoollocationcountycode(bursaryFormSecskuls.getBursaryApplication().getSecStudent().getSecondaryschool().getCounty().getCode());

     bursaryAwardsResponse.setApplicationYear(bursaryFormSecskuls.getBursaryApplication().getApplicationPeriod().getYear());
     bursaryAwardsResponse.setApplicationMonth(bursaryFormSecskuls.getBursaryApplication().getApplicationPeriod().getMonth());
     bursaryAwardsResponse.setStudentParentname(bursaryFormSecskuls.getBursaryApplication().getSecStudent().getStudentparent().getName());
     bursaryAwardsResponse.setStudentParentidnumber(bursaryFormSecskuls.getBursaryApplication().getSecStudent().getStudentparent().getIdnumber());
     bursaryAwardsResponse.setStudentParentphonenumber(bursaryFormSecskuls.getBursaryApplication().getSecStudent().getStudentparent().getPhonenumber());
     bursaryAwardsResponse.setStudentparentcounty(bursaryFormSecskuls.getBursaryApplication().getSecStudent().getStudentparent().getWard().getSubcounty().getCounty().getCounty());
     bursaryAwardsResponse.setStudentparentcountycode(bursaryFormSecskuls.getBursaryApplication().getSecStudent().getStudentparent().getWard().getSubcounty().getCounty().getCode());
     bursaryAwardsResponse.setStudentparentward(bursaryFormSecskuls.getBursaryApplication().getSecStudent().getStudentparent().getWard().getWard());
     bursaryAwardsResponse.setAmount(bursaryFormSecskuls.getAmount());
     return bursaryAwardsResponse;
 }

    public static SecSchoolStudentResponse mapSecondarySchoolStudentToSecondarySchoolStudentResponse(SecStudent secStudent) {
      SecSchoolStudentResponse secSchoolStudentResponse=new SecSchoolStudentResponse();
      secSchoolStudentResponse.setId(secStudent.getId());
      secSchoolStudentResponse.setAdmissionnumber(secStudent.getAdmissionnumber());
      secSchoolStudentResponse.setFullname(secStudent.getFullname());
      secSchoolStudentResponse.setSecondaryschool(secStudent.getSecondaryschool());
      return secSchoolStudentResponse;
    }

    public static HigherEducationResponse maphighereducationtohighereducationresponse(HigherEducationn higherEducationn) {

    HigherEducationResponse higherEducationResponse=new HigherEducationResponse();
    higherEducationResponse.setId(higherEducationn.getId());
    higherEducationResponse.setName(higherEducationn.getName());
    higherEducationResponse.setHigherEducationnCategory(higherEducationn.getHigherEducationnCategory());
    return higherEducationResponse;

 }

    public static AllHigherLearningBursaryapplications maphigherlearningbapplicationstoallhigherlearningbapplications(HigherLearningBursaryApplication higherLearningBursaryApplication) {
  AllHigherLearningBursaryapplications allHigherLearningBursaryapplications=new AllHigherLearningBursaryapplications();
  allHigherLearningBursaryapplications.setId(higherLearningBursaryApplication.getId());
  allHigherLearningBursaryapplications.setAdmissionnumber(higherLearningBursaryApplication.getHigherLearningStudent().getAdmissionnumber());
  allHigherLearningBursaryapplications.setFullname(higherLearningBursaryApplication.getHigherLearningStudent().getFullname());
  allHigherLearningBursaryapplications.setApplicationyear(higherLearningBursaryApplication.getApplicationPeriod().getYear());
  allHigherLearningBursaryapplications.setApplicationmonth(higherLearningBursaryApplication.getApplicationPeriod().getMonth());
  allHigherLearningBursaryapplications.setHigherEducationn(higherLearningBursaryApplication.getHigherLearningStudent().getCollege());
  allHigherLearningBursaryapplications.setPhonenumber(higherLearningBursaryApplication.getHigherLearningStudent().getPhonenumber());
  allHigherLearningBursaryapplications.setIdnumber(higherLearningBursaryApplication.getHigherLearningStudent().getIdnumber());
  allHigherLearningBursaryapplications.setYearofadmission(higherLearningBursaryApplication.getHigherLearningStudent().getYearofadmission());
  allHigherLearningBursaryapplications.setDurationofcourse(higherLearningBursaryApplication.getHigherLearningStudent().getDurationofcourse());
  allHigherLearningBursaryapplications.setIsapproved(higherLearningBursaryApplication.isIsapproved());
  return allHigherLearningBursaryapplications;
 }

    public static HigherLearningBursaryAwardsResponse mapHigherLearningBursaryFormSchoolsToHigherBursaryAwardRespons(HigherLearningBursaryForm higherLearningBursaryForm) {
        HigherLearningBursaryAwardsResponse higherLearningBursaryAwardsResponse=new HigherLearningBursaryAwardsResponse();
        higherLearningBursaryAwardsResponse.setAdmissionnumber(higherLearningBursaryForm.getHigherLearningBursaryApplication().getHigherLearningStudent().getAdmissionnumber());
        higherLearningBursaryAwardsResponse.setApplicationmonth(higherLearningBursaryForm.getHigherLearningBursaryApplication().getApplicationPeriod().getMonth());
        higherLearningBursaryAwardsResponse.setApplicationyear(higherLearningBursaryForm.getHigherLearningBursaryApplication().getApplicationPeriod().getYear());
        higherLearningBursaryAwardsResponse.setDurationofcourse(higherLearningBursaryForm.getHigherLearningBursaryApplication().getHigherLearningStudent().getDurationofcourse());
        higherLearningBursaryAwardsResponse.setFullname(higherLearningBursaryForm.getHigherLearningBursaryApplication().getHigherLearningStudent().getFullname());
        higherLearningBursaryAwardsResponse.setHigherEducationn(higherLearningBursaryForm.getHigherLearningBursaryApplication().getHigherLearningStudent().getCollege());
        higherLearningBursaryAwardsResponse.setId(higherLearningBursaryForm.getId());
        higherLearningBursaryAwardsResponse.setIdnumber(higherLearningBursaryForm.getHigherLearningBursaryApplication().getHigherLearningStudent().getIdnumber());
        higherLearningBursaryAwardsResponse.setIsapproved(higherLearningBursaryForm.getHigherLearningBursaryApplication().isIsapproved());
        higherLearningBursaryAwardsResponse.setPhonenumber(higherLearningBursaryForm.getHigherLearningBursaryApplication().getHigherLearningStudent().getPhonenumber());
        higherLearningBursaryAwardsResponse.setYearofadmission(higherLearningBursaryForm.getHigherLearningBursaryApplication().getHigherLearningStudent().getYearofadmission());
        higherLearningBursaryAwardsResponse.setAmount(higherLearningBursaryForm.getAmount());
        higherLearningBursaryAwardsResponse.setCollege(higherLearningBursaryForm.getHigherLearningBursaryApplication().getHigherLearningStudent().getCollege().getName());
        return higherLearningBursaryAwardsResponse;

 }

    public static SecSchoolStudentResponse mapcampusstudentTocampusStudentResponse(HigherLearningStudent higherLearningStudent) {

     SecSchoolStudentResponse secSchoolStudentResponse=new SecSchoolStudentResponse();
     secSchoolStudentResponse.setFullname(higherLearningStudent.getFullname());
     secSchoolStudentResponse.setAdmissionnumber(higherLearningStudent.getAdmissionnumber());
     secSchoolStudentResponse.setHigherEducationn(higherLearningStudent.getCollege());
     secSchoolStudentResponse.setId(higherLearningStudent.getId());
     return secSchoolStudentResponse;
 }
}
