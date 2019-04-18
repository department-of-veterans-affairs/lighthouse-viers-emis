package gov.va.emis.mappers;

import gov.va.viers.cdi.emis.requestresponse.v2.EMISretirementResponseType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface RetirementMapper {

  RetirementMapper INSTANCE = Mappers.getMapper(RetirementMapper.class);

  EMISretirementResponseType mapEMISretirementResponseType(
      gov.va.schema.emis.vdrdodadapter.v2.EMISretirementResponseType emisRetirementResponseType);
}
