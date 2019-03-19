package gov.va.schema.emis.vdrdodadapter.v2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.WebServiceTemplate;

import javax.xml.bind.JAXBElement;

public class DoDAdapterClient {
  private static final Logger LOGGER = LoggerFactory.getLogger(DoDAdapterClient.class);

  @Autowired private WebServiceTemplate DoDAdapterWebServiceTemplate;

  public JAXBElement<EMISmilitaryServiceEligibilityResponseType>
      getMilitaryServiceEligibilityResponse(String value) {
    ObjectFactory factory = new ObjectFactory();
    InputEdiPi edipi = new InputEdiPi();
    edipi.setEdipi(value);
    JAXBElement<InputEdiPi> request = factory.createEMISmilitaryServiceEligibilityRequest(edipi);
    JAXBElement<EMISmilitaryServiceEligibilityResponseType> response =
        (JAXBElement<EMISmilitaryServiceEligibilityResponseType>)
            DoDAdapterWebServiceTemplate.marshalSendAndReceive(request);

    return response;
  }
}
