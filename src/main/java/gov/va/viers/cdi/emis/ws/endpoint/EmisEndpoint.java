package gov.va.viers.cdi.emis.ws.endpoint;

import gov.va.viers.cdi.cdi.commonservice.v2.ESSErrorType;
import gov.va.viers.cdi.emis.requestresponse.v2.InputEdiPiOrIcn;
import gov.va.viers.cdi.emis.ws.builder.ESSErrorBuilder;
import org.springframework.ws.soap.SoapHeader;

import javax.xml.bind.JAXBElement;

abstract class EmisEndpoint {

    //This exists for convenience so that we can check if an error was returned or not
    class ResponseTuple {
        JAXBElement response;
        gov.va.schema.emis.vdrdodadapter.v2.ESSErrorType error;
    }

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
    ResponseTuple emisMapped = makeEmisPayload(request.getEdipiORicn().getEdipiORicnValue());
    if ("ERROR".equals(emisMapped.error.getESSResponseCode()) && "INVALID_EDIPI_INPUT".equals(emisMapped.error.getText())) {
        ESSErrorType essErrorType =
                ESSErrorBuilder.buildEssError(
                        headerInfo, "MIS-ERR-05", "EDIPI_BAD_FORMAT", "EDIPI incorrectly formatted");
        return makeEmisError(essErrorType);
    }
    return emisMapped.response;
  }

  abstract JAXBElement makeEmisError(ESSErrorType error);

  abstract ResponseTuple makeEmisPayload(String edipi);
}
