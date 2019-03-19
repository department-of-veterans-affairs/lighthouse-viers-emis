package gov.va.viers.cdi.emis.ws.endpoint;

import gov.va.viers.cdi.cdi.commonservice.v2.ESSErrorType;
import org.slf4j.LoggerFactory;
import org.springframework.ws.server.endpoint.annotation.Endpoint;

import org.slf4j.Logger;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryServiceEligibilityResponseType;
import gov.va.viers.cdi.emis.requestresponse.v2.InputEdiPiOrIcn;
import gov.va.viers.cdi.emis.requestresponse.militaryinfo.v2.ObjectFactory;

import javax.xml.bind.JAXBElement;

@Endpoint
public class MilitaryInfoEndpoint {

  private static final Logger LOGGER = LoggerFactory.getLogger(MilitaryInfoEndpoint.class);

  @PayloadRoot(
      namespace = "http://viers.va.gov/cdi/eMIS/RequestResponse/MilitaryInfo/v2",
      localPart = "eMISmilitaryServiceEligibilityRequest")
  @ResponsePayload
  public JAXBElement<EMISmilitaryServiceEligibilityResponseType> getServiceEligibility(
      @RequestPayload InputEdiPiOrIcn request) {
    // hoping I'm allowed to return an empty soap response
    ObjectFactory factory = new ObjectFactory();
    EMISmilitaryServiceEligibilityResponseType response =
        new EMISmilitaryServiceEligibilityResponseType();
    gov.va.viers.cdi.cdi.commonservice.v2.ESSErrorType errorType = new ESSErrorType();
    response.setESSError(errorType);
    JAXBElement<EMISmilitaryServiceEligibilityResponseType> jaxbElement =
        factory.createEMISmilitaryServiceEligibilityResponse(response);
    return jaxbElement;
  }
}
