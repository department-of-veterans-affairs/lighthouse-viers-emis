package gov.va.viers.cdi.emis.ws.endpoint;

import gov.va.EMISMapper;
import gov.va.schema.emis.vdrdodadapter.v2.DoDAdapterClient;
import gov.va.viers.cdi.cdi.commonservice.v2.InputHeaderInfo;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;

import org.slf4j.Logger;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryServiceEligibilityResponseType;
import gov.va.viers.cdi.emis.requestresponse.v2.InputEdiPiOrIcn;
import gov.va.viers.cdi.emis.requestresponse.militaryinfo.v2.ObjectFactory;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

import javax.xml.bind.JAXBElement;

@Endpoint
public class MilitaryInfoEndpoint {

  private static final Logger LOGGER = LoggerFactory.getLogger(MilitaryInfoEndpoint.class);

  public static final String NAMESPACE = "http://viers.va.gov/cdi/eMIS/RequestResponse/MilitaryInfo/v2";

  public static final String LOCAL_PART = "eMISmilitaryServiceEligibilityRequest";

  private ObjectFactory milInfoFactory = new ObjectFactory();

  private gov.va.viers.cdi.cdi.commonservice.v2.ObjectFactory commonFactory = new gov.va.viers.cdi.cdi.commonservice.v2.ObjectFactory();

  @Autowired private DoDAdapterClient dodClient;

  @PayloadRoot(
      namespace = NAMESPACE,
      localPart = LOCAL_PART)
  @ResponsePayload
  public JAXBElement<EMISmilitaryServiceEligibilityResponseType> getServiceEligibility(
      @RequestPayload InputEdiPiOrIcn request) {
    /*     This should probably be wrapped in some null checks, not sure what EMIS does in those cases
    though with regards to returning that there was bad input*/
    JAXBElement<gov.va.schema.emis.vdrdodadapter.v2.EMISmilitaryServiceEligibilityResponseType>
        dodResponse =
            dodClient.getMilitaryServiceEligibilityResponse(
                request.getEdipiORicn().getEdipiORicnValue());

    EMISmilitaryServiceEligibilityResponseType noJaxbResponse;
    noJaxbResponse = EMISMapper.INSTANCE.mapEMISmilitaryServiceEligibilityResponseType(dodResponse.getValue());

    return milInfoFactory.createEMISmilitaryServiceEligibilityResponse(noJaxbResponse);
  }
}
