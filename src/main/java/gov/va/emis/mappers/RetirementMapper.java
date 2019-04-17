package gov.va.emis.mappers;

import gov.va.viers.cdi.emis.commonservice.v2.KeyData;
import gov.va.viers.cdi.emis.commonservice.v2.Retirement;
import gov.va.viers.cdi.emis.commonservice.v2.RetirementData;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISretirementResponseType;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface RetirementMapper {

  RetirementMapper INSTANCE = Mappers.getMapper(RetirementMapper.class);

  EMISretirementResponseType mapEMISretirementResponseType(
      gov.va.schema.emis.vdrdodadapter.v2.EMISretirementResponseType emisRetirementResponseType);

  List<Retirement> mapRetirementList(
      List<gov.va.schema.emis.vdrdodadapter.v2.Retirement> retirementList);

  KeyData mapKeyData(gov.va.schema.emis.vdrdodadapter.v2.KeyData keyData);

  RetirementData retirementData(gov.va.schema.emis.vdrdodadapter.v2.RetirementData retirementData);
}
