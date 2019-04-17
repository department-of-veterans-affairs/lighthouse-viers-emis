package gov.va.emis.mappers;

import gov.va.viers.cdi.emis.commonservice.v2.KeyData;
import gov.va.viers.cdi.emis.commonservice.v2.MilitaryOccupation;
import gov.va.viers.cdi.emis.commonservice.v2.MilitaryOccupationData;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryOccupationResponseType;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface MilitaryOccupationMapper {

  MilitaryOccupationMapper INSTANCE = Mappers.getMapper(MilitaryOccupationMapper.class);

  EMISmilitaryOccupationResponseType mapEMISmilitaryOccupationResponseType(
      gov.va.schema.emis.vdrdodadapter.v2.EMISmilitaryOccupationResponseType
          emiSmilitaryOccupationResponseType);

  List<MilitaryOccupation> mapMilitaryOccupationList(
      List<gov.va.schema.emis.vdrdodadapter.v2.MilitaryOccupation> militaryOccupationList);

  KeyData mapKeyData(gov.va.schema.emis.vdrdodadapter.v2.KeyData keyData);

  MilitaryOccupationData mapMilitaryOccupationData(
      gov.va.schema.emis.vdrdodadapter.v2.MilitaryOccupationData militaryOccupation);
}
