package gov.va;

import gov.va.viers.cdi.emis.commonservice.v2.Awards;
import gov.va.viers.cdi.emis.commonservice.v2.AwardsData;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface EMISMapper {

  EMISMapper INSTANCE = Mappers.getMapper( EMISMapper.class );

  List<AwardsData> mapAwardsDataList(List<gov.va.schema.emis.vdrdodadapter.v2.AwardsData> awardsData);
}