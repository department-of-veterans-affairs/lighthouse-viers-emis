package gov.va.dod;

import gov.va.schema.emis.vdrdodadapter.v2.EMISdeploymentResponseType;
import gov.va.schema.emis.vdrdodadapter.v2.EMISguardReserveServicePeriodsResponseType;
import gov.va.schema.emis.vdrdodadapter.v2.EMISmilitaryServiceEligibilityResponseType;
import gov.va.schema.emis.vdrdodadapter.v2.EMISretirementResponseType;
import gov.va.schema.emis.vdrdodadapter.v2.EMISunitInformationResponseType;
import gov.va.schema.emis.vdrdodadapter.v2.InputEdiPi;
import gov.va.schema.emis.vdrdodadapter.v2.ObjectFactory;
import javax.xml.bind.JAXBElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

@Component
public class DoDAdapterClient {
  private static final Logger LOGGER = LoggerFactory.getLogger(DoDAdapterClient.class);

  @Qualifier("dodAdapterWebServiceTemplate")
  @Autowired
  private WebServiceTemplate DoDAdapterWebServiceTemplate;

  private ObjectFactory factory = new ObjectFactory();

  public JAXBElement<EMISmilitaryServiceEligibilityResponseType>
      getMilitaryServiceEligibilityResponse(String value) {
    InputEdiPi edipi = new InputEdiPi();
    edipi.setEdipi(value);
    JAXBElement<InputEdiPi> request = factory.createEMISmilitaryServiceEligibilityRequest(edipi);

    JAXBElement<EMISmilitaryServiceEligibilityResponseType> response =
        (JAXBElement<EMISmilitaryServiceEligibilityResponseType>)
            DoDAdapterWebServiceTemplate.marshalSendAndReceive(request);

    if (response.getValue() == null) {
      response.setValue(new EMISmilitaryServiceEligibilityResponseType());
    }

    return response;
  }

  public JAXBElement<EMISunitInformationResponseType> getUnitInformationResponse(String value) {
    InputEdiPi edipi = new InputEdiPi();
    edipi.setEdipi(value);
    JAXBElement<InputEdiPi> request = factory.createEMISunitInformationRequest(edipi);

    JAXBElement<EMISunitInformationResponseType> response =
        (JAXBElement<EMISunitInformationResponseType>)
            DoDAdapterWebServiceTemplate.marshalSendAndReceive(request);

    if (response.getValue() == null) {
      response.setValue(new EMISunitInformationResponseType());
    }

    return response;
  }

  public JAXBElement<EMISguardReserveServicePeriodsResponseType>
      getGuardReserveServicePeriodsResponse(String value) {
    InputEdiPi ediPi = new InputEdiPi();
    ediPi.setEdipi(value);
    JAXBElement<InputEdiPi> request = factory.createEMISguardReserveServicePeriodsRequest(ediPi);

    JAXBElement<EMISguardReserveServicePeriodsResponseType> response =
        (JAXBElement<EMISguardReserveServicePeriodsResponseType>)
            DoDAdapterWebServiceTemplate.marshalSendAndReceive(request);

    if (response.getValue() == null) {
      response.setValue(new EMISguardReserveServicePeriodsResponseType());
    }
    return response;
  }

  public JAXBElement<EMISretirementResponseType> getRetirementResponse(String value) {
    InputEdiPi edipi = new InputEdiPi();
    edipi.setEdipi(value);
    JAXBElement<InputEdiPi> request = factory.createEMISretirementRequest(edipi);

    JAXBElement<EMISretirementResponseType> response =
        (JAXBElement<EMISretirementResponseType>)
            DoDAdapterWebServiceTemplate.marshalSendAndReceive(request);

    if (response.getValue() == null) {
      response.setValue(new EMISretirementResponseType());
    }
    return response;
  }

  public JAXBElement<EMISdeploymentResponseType> getDeploymentResponse(String value) {
    InputEdiPi edipi = new InputEdiPi();
    edipi.setEdipi(value);
    JAXBElement<InputEdiPi> request = factory.createEMISdeploymentRequest(edipi);

    JAXBElement<EMISdeploymentResponseType> response =
        (JAXBElement<EMISdeploymentResponseType>)
            DoDAdapterWebServiceTemplate.marshalSendAndReceive(request);

    if (response.getValue() == null) {
      response.setValue(new EMISdeploymentResponseType());
    }
    return response;
  }
}
