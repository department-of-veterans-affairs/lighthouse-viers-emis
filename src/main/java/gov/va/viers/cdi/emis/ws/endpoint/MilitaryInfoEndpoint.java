package gov.va.viers.cdi.emis.ws.endpoint;

import gov.va.EMISMapper;
import gov.va.schema.emis.vdrdodadapter.v2.DoDAdapterClient;
import gov.va.viers.cdi.emis.requestresponse.militaryinfo.v2.ObjectFactory;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryServiceEligibilityResponseType;
import gov.va.viers.cdi.emis.requestresponse.v2.InputEdiPiOrIcn;
import gov.va.viers.cdi.emis.ws.builder.ESSErrorBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.server.endpoint.annotation.SoapHeader;

import javax.xml.bind.JAXBElement;

@Endpoint
public class MilitaryInfoEndpoint {

  private static final Logger LOGGER = LoggerFactory.getLogger(MilitaryInfoEndpoint.class);

  private ObjectFactory objectFactory = new ObjectFactory();

  @Autowired private DoDAdapterClient dodClient;

  @PayloadRoot(
      namespace = "http://viers.va.gov/cdi/eMIS/RequestResponse/MilitaryInfo/v2",
      localPart = "eMISmilitaryServiceEligibilityRequest")
  @ResponsePayload
  public JAXBElement<EMISmilitaryServiceEligibilityResponseType> getServiceEligibility(
      @RequestPayload InputEdiPiOrIcn request,
      @SoapHeader(value = "{http://viers.va.gov/cdi/CDI/commonService/v2}inputHeaderInfo")
          SoapHeaderElement soapHeaderElement) {

    ESSErrorBuilder essErrorBuilder = new ESSErrorBuilder(soapHeaderElement);

    if (!request.getEdipiORicn().getInputType().equals("EDIPI")) {
      return essErrorBuilder.generateMissingOrInvalidEdipiEssError(
          "MIS-ERR-03", "INVALID_IDENTIFIER");
    } else if (request.getEdipiORicn().getEdipiORicnValue().isEmpty()) {
      return essErrorBuilder.generateMissingOrInvalidEdipiEssError("MIS-ERR-02", "MISSING_EDIPI");
    }

    /* This should probably be wrapped in some null checks, not sure what EMIS does in those cases
    though with regards to returning that there was bad input*/
    JAXBElement<gov.va.schema.emis.vdrdodadapter.v2.EMISmilitaryServiceEligibilityResponseType>
        dodResponse =
            dodClient.getMilitaryServiceEligibilityResponse(
                request.getEdipiORicn().getEdipiORicnValue());

    if (dodResponse.getValue().getESSError() != null) {
      if ("ERROR".equals(dodResponse.getValue().getESSError().getESSResponseCode())
          && "INVALID_EDIPI_INPUT".equals(dodResponse.getValue().getESSError().getText())) {
        return essErrorBuilder.generateBadFormatEdipiEssError(dodResponse.getValue().getESSError());
      }
    }

    EMISmilitaryServiceEligibilityResponseType noJaxbResponse =
        EMISMapper.INSTANCE.mapEMISmilitaryServiceEligibilityResponseType(dodResponse.getValue());

    return objectFactory.createEMISmilitaryServiceEligibilityResponse(noJaxbResponse);
  }
}
