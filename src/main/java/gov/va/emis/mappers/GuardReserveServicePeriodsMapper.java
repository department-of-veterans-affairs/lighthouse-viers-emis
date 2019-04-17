package gov.va.emis.mappers;

import gov.va.viers.cdi.cdi.commonservice.v2.ESSErrorType;
import gov.va.viers.cdi.emis.commonservice.v2.GuardReserveServicePeriods;
import gov.va.viers.cdi.emis.commonservice.v2.GuardReserveServicePeriodsData;
import gov.va.viers.cdi.emis.commonservice.v2.KeyData;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISguardReserveServicePeriodsResponseType;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface GuardReserveServicePeriodsMapper {

  GuardReserveServicePeriodsMapper INSTANCE =
      Mappers.getMapper(GuardReserveServicePeriodsMapper.class);

  EMISguardReserveServicePeriodsResponseType mapEMISguardReserveServicePeriodsResponseType(
      gov.va.schema.emis.vdrdodadapter.v2.EMISguardReserveServicePeriodsResponseType
          emiSguardReserveServicePeriodsResponseType);

  List<GuardReserveServicePeriods> mapGuardReserveServicePeriodsList(
      List<gov.va.schema.emis.vdrdodadapter.v2.GuardReserveServicePeriods>
          guardReserveServicePeriodsList);

  KeyData mapKeyData(gov.va.schema.emis.vdrdodadapter.v2.KeyData keyData);

  GuardReserveServicePeriods mapGuardReserveServicePeriods(
      gov.va.schema.emis.vdrdodadapter.v2.GuardReserveServicePeriods guardReserveServicePeriods);

  GuardReserveServicePeriodsData mapGuardReserveServicePeriodsData(
      gov.va.schema.emis.vdrdodadapter.v2.GuardReserveServicePeriodsData
          guardReserveServicePeriodsData);

  ESSErrorType mapEssErrorType(gov.va.schema.emis.vdrdodadapter.v2.ESSErrorType essErrorType);
}
