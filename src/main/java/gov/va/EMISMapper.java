package gov.va;

import gov.va.viers.cdi.emis.commonservice.v2.Awards;
import gov.va.viers.cdi.emis.commonservice.v2.AwardsData;
import java.util.List;

import gov.va.viers.cdi.emis.commonservice.v2.MilitaryServiceEligibility;
import gov.va.viers.cdi.emis.commonservice.v2.VeteranStatus;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryServiceEligibilityResponseType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface EMISMapper {

  EMISMapper INSTANCE = Mappers.getMapper( EMISMapper.class );

  EMISmilitaryServiceEligibilityResponseType mapServiceEligibilityResponseType(gov.va.schema.emis.vdrdodadapter.v2.EMISmilitaryServiceEligibilityResponseType serviceRecords);
  List<MilitaryServiceEligibility> mapServiceEligibility(List<gov.va.schema.emis.vdrdodadapter.v2.MilitaryServiceEligibility> eligibilityRecords);
  VeteranStatus mapVetStatus(gov.va.schema.emis.vdrdodadapter.v2.VeteranStatus veteranData);
  List<AwardsData> mapAwardsDataList(List<gov.va.schema.emis.vdrdodadapter.v2.AwardsData> awardsData);
}