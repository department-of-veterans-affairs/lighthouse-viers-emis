package gov.va.viers.cdi.emis.ws.endpoint;

import gov.va.EMISMapper;
import gov.va.schema.emis.vdrdodadapter.v2.DoDAdapterClient;
import gov.va.viers.cdi.cdi.commonservice.v2.ESSErrorType;
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
          org.springframework.ws.soap.SoapHeader soapHeader) {

    if (!request.getEdipiORicn().getInputType().equals("EDIPI")) {
      ESSErrorType essErrorType =
          ESSErrorBuilder.buildEssError(
              soapHeader,
              "MIS-ERR-03",
              "INVALID_IDENTIFIER",
              "Invalid Parameter Identifier");
      return getEmisMilitaryServiceEligibilityResponseTypeEssError(essErrorType);
    } else if (request.getEdipiORicn().getEdipiORicnValue().isEmpty()) {
      ESSErrorType essErrorType =
          ESSErrorBuilder.buildEssError(
              soapHeader, "MIS-ERR-02", "MISSING_EDIPI", "Invalid Parameter Identifier");
      return getEmisMilitaryServiceEligibilityResponseTypeEssError(essErrorType);
    }

    /* This should probably be wrapped in some null checks, not sure what EMIS does in those cases
    though with regards to returning that there was bad input*/
    JAXBElement<gov.va.schema.emis.vdrdodadapter.v2.EMISmilitaryServiceEligibilityResponseType>
        dodResponse =
            dodClient.getMilitaryServiceEligibilityResponse(
                request.getEdipiORicn().getEdipiORicnValue());

    if (dodResponse.getValue() == null) {
      return objectFactory.createEMISmilitaryServiceEligibilityResponse(new EMISmilitaryServiceEligibilityResponseType());
    }
    else if (dodResponse.getValue().getESSError() != null) {
      if ("ERROR".equals(dodResponse.getValue().getESSError().getESSResponseCode())
          && "INVALID_EDIPI_INPUT".equals(dodResponse.getValue().getESSError().getText())) {
        ESSErrorType essErrorType =
            ESSErrorBuilder.buildEssError(
                soapHeader, "MIS-ERR-05", "EDIPI_BAD_FORMAT", "EDIPI incorrectly formatted");
        return getEmisMilitaryServiceEligibilityResponseTypeEssError(essErrorType);
      }
    }

    EMISmilitaryServiceEligibilityResponseType noJaxbResponse =
        EMISMapper.INSTANCE.mapEMISmilitaryServiceEligibilityResponseType(dodResponse.getValue());

    return objectFactory.createEMISmilitaryServiceEligibilityResponse(noJaxbResponse);
  }

  private JAXBElement<EMISmilitaryServiceEligibilityResponseType>
      getEmisMilitaryServiceEligibilityResponseTypeEssError(ESSErrorType essErrorType) {
    EMISmilitaryServiceEligibilityResponseType emiSmilitaryServiceEligibilityResponseType =
        new EMISmilitaryServiceEligibilityResponseType();
    emiSmilitaryServiceEligibilityResponseType.setESSError(essErrorType);
    return objectFactory.createEMISmilitaryServiceEligibilityResponse(
        emiSmilitaryServiceEligibilityResponseType);
  }
}
