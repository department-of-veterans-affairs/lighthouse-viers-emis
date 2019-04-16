package gov.va.emis.mappers;

import gov.va.viers.cdi.emis.commonservice.v2.GuardReserveServicePeriods;
import gov.va.viers.cdi.emis.commonservice.v2.GuardReserveServicePeriodsData;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISguardReserveServicePeriodsResponseType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface GuardReserveServicePeriodsMapper {

  GuardReserveServicePeriodsMapper INSTANCE =
      Mappers.getMapper(GuardReserveServicePeriodsMapper.class);

  EMISguardReserveServicePeriodsResponseType mapEMISguardReserveServicePeriodsResponseType(
      gov.va.schema.emis.vdrdodadapter.v2.EMISguardReserveServicePeriodsResponseType
          emiSguardReserveServicePeriodsResponseType);

  GuardReserveServicePeriods mapGuardReserveServicePeriods(
      gov.va.schema.emis.vdrdodadapter.v2.GuardReserveServicePeriods guardReserveServicePeriods);

  GuardReserveServicePeriodsData mapGuardReserveServicePeriodsData(
      gov.va.schema.emis.vdrdodadapter.v2.GuardReserveServicePeriods guardReserveServicePeriods);
}
