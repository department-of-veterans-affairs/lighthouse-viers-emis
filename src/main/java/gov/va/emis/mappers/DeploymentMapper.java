package gov.va.emis.mappers;

import gov.va.viers.cdi.emis.requestresponse.v2.EMISdeploymentResponseType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface DeploymentMapper {

  DeploymentMapper INSTANCE = Mappers.getMapper(DeploymentMapper.class);

  EMISdeploymentResponseType mapEMISdeploymentResponseType(
      gov.va.schema.emis.vdrdodadapter.v2.EMISdeploymentResponseType emisDeploymentResponseType);
}
