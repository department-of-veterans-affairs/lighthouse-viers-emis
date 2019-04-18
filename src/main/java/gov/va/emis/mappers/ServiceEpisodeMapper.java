package gov.va.emis.mappers;

import gov.va.viers.cdi.emis.requestresponse.v2.EMISserviceEpisodeResponseType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface ServiceEpisodeMapper {

  ServiceEpisodeMapper INSTANCE = Mappers.getMapper(ServiceEpisodeMapper.class);

  EMISserviceEpisodeResponseType mapEMISserviceEpisodeResponseType(
      gov.va.schema.emis.vdrdodadapter.v2.EMISserviceEpisodeResponseType
          emiSserviceEpisodeResponseType);
}
