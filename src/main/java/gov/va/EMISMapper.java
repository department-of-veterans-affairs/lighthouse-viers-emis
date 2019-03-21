package gov.va;

import java.util.List;


import gov.va.viers.cdi.cdi.commonservice.v2.ESSErrorType;
import gov.va.viers.cdi.emis.commonservice.v2.AwardsData;
import gov.va.viers.cdi.emis.commonservice.v2.CombatPayData;
import gov.va.viers.cdi.emis.commonservice.v2.DentalIndicatorData;
import gov.va.viers.cdi.emis.commonservice.v2.DeploymentEligibilityData;
import gov.va.viers.cdi.emis.commonservice.v2.DeploymentLocationEligibilityData;
import gov.va.viers.cdi.emis.commonservice.v2.GuardReserveServicePeriodsEligibilityData;
import gov.va.viers.cdi.emis.commonservice.v2.KeyData;
import gov.va.viers.cdi.emis.commonservice.v2.MilitaryServiceEligibility;
import gov.va.viers.cdi.emis.commonservice.v2.MilitaryServiceEpisodeEligibilityData;
import gov.va.viers.cdi.emis.commonservice.v2.PayGradeData;
import gov.va.viers.cdi.emis.commonservice.v2.PersonnelDutyStatusCodeType;
import gov.va.viers.cdi.emis.commonservice.v2.PurpleHeartOrMohData;
import gov.va.viers.cdi.emis.commonservice.v2.VeteranStatus;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryServiceEligibilityResponseType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface EMISMapper {

  EMISMapper INSTANCE = Mappers.getMapper(EMISMapper.class);

  // Sub types of EMISmilitaryServiceEligibilityResponseType include:
  // List<MilitaryServiceEligibility>, ESSErrorType
  EMISmilitaryServiceEligibilityResponseType mapEMISmilitaryServiceEligibilityResponseType(
      gov.va.schema.emis.vdrdodadapter.v2.EMISmilitaryServiceEligibilityResponseType
          emiSmilitaryServiceEligibilityResponseType);

  // Sub types of List<MilitaryServiceEligibility> include: VeteranStatus, DentalIndicatorData,
  // List<PurpleHeartOrMohData>, List<MilitaryServiceEpisodeEligibilityData>, List<AwardsData>
  List<MilitaryServiceEligibility> mapMilitaryServiceEligibilityList(
      List<gov.va.schema.emis.vdrdodadapter.v2.MilitaryServiceEligibility>
          militaryServiceEligibilityList);

  // Sub types of VeteranStatus include: PersonnelDutyStatusCodeType
  VeteranStatus mapVeteranStatus(gov.va.schema.emis.vdrdodadapter.v2.VeteranStatus veteranStatus);

  PersonnelDutyStatusCodeType mapPersonnelDutyStatusCodeType(
      gov.va.schema.emis.vdrdodadapter.v2.PersonnelDutyStatusCodeType personnelDutyStatusCodeType);

  DentalIndicatorData mapDentalIndicatorData(
      gov.va.schema.emis.vdrdodadapter.v2.DentalIndicatorData dentalIndicatorData);

  List<PurpleHeartOrMohData> mapPurpleHeartOrMohDataList(
      List<gov.va.schema.emis.vdrdodadapter.v2.PurpleHeartOrMohData> purpleHeartOrMohDataList);

  // Sub types of MilitaryServiceEpisodeEligibilityData include: KeyData,
  // List<GuardReserveServicePeriodsEligibilityData>, List<DeploymentEligibilityData>,
  // List<CombatPayData>, PayGradeData
  List<MilitaryServiceEpisodeEligibilityData> mapMilitaryServiceEpisodeEligibilityDataList(
      List<gov.va.schema.emis.vdrdodadapter.v2.MilitaryServiceEpisodeEligibilityData>
          MilitaryServiceEpisodeEligibilityDataList);

  KeyData mapKeyData(gov.va.schema.emis.vdrdodadapter.v2.KeyData KeyData);

  List<GuardReserveServicePeriodsEligibilityData> mapGuardReserveServicePeriodsEligibilityDataList(
      List<gov.va.schema.emis.vdrdodadapter.v2.GuardReserveServicePeriodsEligibilityData>
          GuardReserveServicePeriodsEligibilityDataList);

  // Sub types of List<DeploymentEligibilityData> include: List<DeploymentLocationEligibilityData>
  List<DeploymentEligibilityData> mapDeploymentEligibilityDataList(
      List<gov.va.schema.emis.vdrdodadapter.v2.DeploymentEligibilityData>
          DeploymentEligibilityData);

  List<DeploymentLocationEligibilityData> mapDeploymentLocationEligibilityDataList(
      List<gov.va.schema.emis.vdrdodadapter.v2.DeploymentLocationEligibilityData>
          DeploymentLocationEligibilityData);

  List<CombatPayData> mapCombatPayDataList(
      List<gov.va.schema.emis.vdrdodadapter.v2.CombatPayData> CombatPayDataList);

  PayGradeData mapPayGradeData(gov.va.schema.emis.vdrdodadapter.v2.PayGradeData payGradeData);

  List<AwardsData> mapAwardsDataList(
      List<gov.va.schema.emis.vdrdodadapter.v2.AwardsData> awardsData);
  ESSErrorType mapESSErrorType(gov.va.schema.emis.vdrdodadapter.v2.ESSErrorType ESSErrorType);
}

