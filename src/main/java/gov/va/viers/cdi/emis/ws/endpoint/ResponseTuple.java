package gov.va.viers.cdi.emis.ws.endpoint;

import gov.va.schema.emis.vdrdodadapter.v2.ESSErrorType;
import javax.xml.bind.JAXBElement;

public class ResponseTuple {

  JAXBElement response;
  ESSErrorType error;

  public JAXBElement getResponse() {
    return response;
  }

  public void setResponse(JAXBElement response) {
    this.response = response;
  }

  public ESSErrorType getError() {
    return error;
  }

  public void setError(ESSErrorType error) {
    this.error = error;
  }
}
