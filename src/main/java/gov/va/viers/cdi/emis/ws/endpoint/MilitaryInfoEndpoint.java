package gov.va.viers.cdi.emis.ws.endpoint;

import gov.va.EMISMapper;
import gov.va.schema.emis.vdrdodadapter.v2.DoDAdapterClient;
import gov.va.viers.cdi.cdi.commonservice.v2.ESSErrorType;
import gov.va.viers.cdi.emis.requestresponse.militaryinfo.v2.ObjectFactory;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryServiceEligibilityResponseType;
import gov.va.viers.cdi.emis.requestresponse.v2.InputEdiPiOrIcn;
import gov.va.viers.cdi.emis.ws.builder.ESSErrorBuilder;
import javax.xml.bind.JAXBElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class MilitaryInfoEndpoint {

  private static final Logger LOGGER = LoggerFactory.getLogger(MilitaryInfoEndpoint.class);

  private ObjectFactory objectFactory = new ObjectFactory();

  @Autowired private DoDAdapterClient dodClient;

  public JAXBElement<EMISmilitaryServiceEligibilityResponseType> getServiceEligibility(
      InputEdiPiOrIcn request, org.springframework.ws.soap.SoapHeader soapHeader) {

    if (!("EDIPI").equals(request.getEdipiORicn().getInputType())) {
      ESSErrorType essErrorType =
          ESSErrorBuilder.buildEssError(
              soapHeader, "MIS-ERR-03", "INVALID_IDENTIFIER", "Invalid Parameter Identifier");
      return makeEmisEssError(essErrorType);
    } else if (request.getEdipiORicn().getEdipiORicnValue().isEmpty()) {
      ESSErrorType essErrorType =
          ESSErrorBuilder.buildEssError(
              soapHeader, "MIS-ERR-02", "MISSING_EDIPI", "Invalid Parameter Identifier");
      return makeEmisEssError(essErrorType);
    }

    /* This should probably be wrapped in some null checks, not sure what EMIS does in those cases
    though with regards to returning that there was bad input*/
    JAXBElement<gov.va.schema.emis.vdrdodadapter.v2.EMISmilitaryServiceEligibilityResponseType>
        dodResponse =
            dodClient.getMilitaryServiceEligibilityResponse(
                request.getEdipiORicn().getEdipiORicnValue());

    if (dodResponse.getValue() == null) {
      return objectFactory.createEMISmilitaryServiceEligibilityResponse(
          new EMISmilitaryServiceEligibilityResponseType());
    } else if (dodResponse.getValue().getESSError() != null) {
      if ("ERROR".equals(dodResponse.getValue().getESSError().getESSResponseCode())
          && "INVALID_EDIPI_INPUT".equals(dodResponse.getValue().getESSError().getText())) {
        ESSErrorType essErrorType =
            ESSErrorBuilder.buildEssError(
                soapHeader, "MIS-ERR-05", "EDIPI_BAD_FORMAT", "EDIPI incorrectly formatted");
        return makeEmisEssError(essErrorType);
      }
    }

    EMISmilitaryServiceEligibilityResponseType noJaxbResponse =
        EMISMapper.INSTANCE.mapEMISmilitaryServiceEligibilityResponseType(dodResponse.getValue());

    return objectFactory.createEMISmilitaryServiceEligibilityResponse(noJaxbResponse);
  }

  private JAXBElement<EMISmilitaryServiceEligibilityResponseType> makeEmisEssError(
      ESSErrorType essErrorType) {
    EMISmilitaryServiceEligibilityResponseType emiSmilitaryServiceEligibilityResponseType =
        new EMISmilitaryServiceEligibilityResponseType();
    emiSmilitaryServiceEligibilityResponseType.setESSError(essErrorType);
    return objectFactory.createEMISmilitaryServiceEligibilityResponse(
        emiSmilitaryServiceEligibilityResponseType);
  }
}
