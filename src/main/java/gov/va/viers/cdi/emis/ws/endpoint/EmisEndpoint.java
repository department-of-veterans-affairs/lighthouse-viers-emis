package gov.va.viers.cdi.emis.ws.endpoint;

import gov.va.viers.cdi.cdi.commonservice.v2.ESSErrorType;
import gov.va.viers.cdi.emis.requestresponse.v2.InputEdiPiOrIcn;
import gov.va.viers.cdi.emis.ws.builder.ESSErrorBuilder;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.soap.SoapHeader;

import javax.xml.bind.JAXBElement;

abstract class EmisEndpoint {

    String namespace;

    @PayloadRoot(Namespace = namespace)
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
    return makeEmisPayload(request.getEdipiORicn().getEdipiORicnValue());
  }

  abstract JAXBElement makeEmisError(ESSErrorType error);

  abstract JAXBElement makeEmisPayload(String edipi);
}
