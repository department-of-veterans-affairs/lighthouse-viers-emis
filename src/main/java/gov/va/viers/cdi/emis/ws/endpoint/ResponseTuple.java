package gov.va.viers.cdi.emis.ws.endpoint;

import gov.va.schema.emis.vdrdodadapter.v2.ESSErrorType;
import javax.xml.bind.JAXBElement;
import lombok.Data;

@Data
public class ResponseTuple {
  JAXBElement response;
  ESSErrorType error;
}
