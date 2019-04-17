package gov.va.emis.mappers;

import gov.va.viers.cdi.emis.commonservice.v2.Disability;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISdisabilitiesResponseType;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface DisabilitiesMapper {

  DisabilitiesMapper INSTANCE = Mappers.getMapper(DisabilitiesMapper.class);

  EMISdisabilitiesResponseType mapEMISdisabilitiesResponseType(
      gov.va.schema.emis.vdrdodadapter.v2.EMISdisabilitiesResponseType
          emiSdisabilitiesResponseType);

  List<Disability> mapDisabilitiesList(
      List<gov.va.schema.emis.vdrdodadapter.v2.Disability> disabilitiesList);
}
