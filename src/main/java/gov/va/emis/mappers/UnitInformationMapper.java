package gov.va.emis.mappers;

import gov.va.viers.cdi.emis.requestresponse.v2.EMISunitInformationResponseType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface UnitInformationMapper {

  UnitInformationMapper INSTANCE = Mappers.getMapper(UnitInformationMapper.class);

  EMISunitInformationResponseType mapEMISunitInformationResponseType(
      gov.va.schema.emis.vdrdodadapter.v2.EMISunitInformationResponseType
          emiSunitInformationResponseType);
}
