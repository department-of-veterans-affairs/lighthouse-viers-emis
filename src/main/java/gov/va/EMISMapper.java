package gov.va;

import gov.va.viers.cdi.emis.commonservice.v2.AwardsData;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface EMISMapper {

  EMISMapper INSTANCE = Mappers.getMapper( EMISMapper.class );

  AwardsData mapAwardsData(gov.va.schema.emis.vdrdodadapter.v2.AwardsData awards);
}