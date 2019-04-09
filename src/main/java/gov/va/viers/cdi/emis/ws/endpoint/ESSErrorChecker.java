package gov.va.viers.cdi.emis.ws.endpoint;

import gov.va.viers.cdi.cdi.commonservice.v2.ESSErrorType;
import gov.va.viers.cdi.emis.requestresponse.v2.InputEdiPiOrIcn;
import gov.va.viers.cdi.emis.ws.builder.ESSErrorBuilder;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.soap.server.endpoint.annotation.SoapHeader;

public class ESSErrorChecker {
  public static ESSErrorType checkForPreResponseError(
      @RequestPayload InputEdiPiOrIcn request,
      @SoapHeader("{http://viers.va.gov/cdi/CDI/commonService/v2}inputHeaderInfo")
          org.springframework.ws.soap.SoapHeader soapHeader) {
    if (!request.getEdipiORicn().getInputType().equals("EDIPI")) {
      return ESSErrorBuilder.buildEssError(
          soapHeader, "MIS-ERR-03", "INVALID_IDENTIFIER", "Invalid Parameter Identifier");
    } else if (request.getEdipiORicn().getEdipiORicnValue().isEmpty()) {
      return ESSErrorBuilder.buildEssError(
          soapHeader, "MIS-ERR-02", "MISSING_EDIPI", "Invalid Parameter Identifier");
    }
    return null;
  }

  public static ESSErrorType checkForPostResponseError(
      @SoapHeader("{http://viers.va.gov/cdi/CDI/commonService/v2}inputHeaderInfo")
          org.springframework.ws.soap.SoapHeader soapHeader,
      gov.va.schema.emis.vdrdodadapter.v2.ESSErrorType essErrorType) {
    if ("ERROR".equals(essErrorType.getESSResponseCode())
        && "INVALID_EDIPI_INPUT".equals(essErrorType.getText())) {
      return ESSErrorBuilder.buildEssError(
          soapHeader, "MIS-ERR-05", "EDIPI_BAD_FORMAT", "EDIPI incorrectly formatted");
    }
    return null;
  }
}
