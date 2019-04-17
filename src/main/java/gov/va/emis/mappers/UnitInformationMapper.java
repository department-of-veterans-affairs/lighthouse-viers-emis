package gov.va.emis.mappers;

import gov.va.viers.cdi.emis.commonservice.v2.KeyData;
import gov.va.viers.cdi.emis.commonservice.v2.UnitInformation;
import gov.va.viers.cdi.emis.commonservice.v2.UnitInformationData;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISunitInformationResponseType;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface UnitInformationMapper {

  UnitInformationMapper INSTANCE = Mappers.getMapper(UnitInformationMapper.class);

  EMISunitInformationResponseType mapEMISunitInformationResponseType(
      gov.va.schema.emis.vdrdodadapter.v2.EMISunitInformationResponseType
          emiSunitInformationResponseType);

  List<UnitInformation> mapUnitInformationList(
      List<gov.va.schema.emis.vdrdodadapter.v2.UnitInformation> unitInformationList);

  KeyData mapKeyData(gov.va.schema.emis.vdrdodadapter.v2.KeyData keyData);

  UnitInformationData mapUnitInformationData(
      gov.va.schema.emis.vdrdodadapter.v2.UnitInformationData unitInformationData);
}
