package gov.va.emis.endpoint;

import gov.va.emis.builder.ESSErrorBuilder;
import gov.va.viers.cdi.cdi.commonservice.v2.ESSErrorType;
import gov.va.viers.cdi.emis.requestresponse.v2.InputEdiPiOrIcn;
import javax.xml.bind.JAXBElement;
import org.springframework.ws.soap.SoapHeader;

abstract class EmisEndpoint {

  public JAXBElement processRequest(InputEdiPiOrIcn request, SoapHeader headerInfo) {
    if (!("EDIPI").equals(request.getEdipiORicn().getInputType())) {
      ESSErrorType essErrorType =
          ESSErrorBuilder.buildEssError(
              headerInfo, "MIS-ERR-03", "INVALID_IDENTIFIER", "Invalid Parameter Identifier");
      return makeEmisError(essErrorType);
    } else if (request.getEdipiORicn().getEdipiORicnValue().isEmpty()) {
      ESSErrorType essErrorType =
          ESSErrorBuilder.buildEssError(
              headerInfo, "MIS-ERR-02", "MISSING_EDIPI", "Invalid Parameter Identifier");
      return makeEmisError(essErrorType);
    }
    ResponseTuple emisMapped = sendRequest(request.getEdipiORicn().getEdipiORicnValue());
    if (emisMapped.getError() != null) {
      if ("ERROR".equals(emisMapped.getError().getESSResponseCode())
          && "INVALID_EDIPI_INPUT".equals(emisMapped.getError().getText())) {
        ESSErrorType essErrorType =
            ESSErrorBuilder.buildEssError(
                headerInfo, "MIS-ERR-05", "EDIPI_BAD_FORMAT", "EDIPI incorrectly formatted");
        return makeEmisError(essErrorType);
      }
    }
    return emisMapped.getResponse();
  }

  abstract JAXBElement makeEmisError(ESSErrorType error);

  abstract ResponseTuple sendRequest(String edipi);
}
