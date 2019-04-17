package gov.va.emis.mappers;

import gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryOccupationResponseType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface MilitaryOccupationMapper {

  MilitaryOccupationMapper INSTANCE = Mappers.getMapper(MilitaryOccupationMapper.class);

  EMISmilitaryOccupationResponseType mapEMISmilitaryOccupationResponseType(
      gov.va.schema.emis.vdrdodadapter.v2.EMISmilitaryOccupationResponseType
          emiSmilitaryOccupationResponseType);
}
