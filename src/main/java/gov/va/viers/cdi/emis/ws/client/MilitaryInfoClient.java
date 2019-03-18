package gov.va.viers.cdi.emis.ws.client;

import gov.va.viers.cdi.emis.commonservice.v2.InputEdipiIcn;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryServiceEligibilityResponseType;
import gov.va.viers.cdi.emis.requestresponse.v2.InputEdiPiOrIcn;
import gov.va.viers.cdi.emis.requestresponse.militaryinfo.v2.ObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import javax.xml.bind.JAXBElement;

@Component
public class MilitaryInfoClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MilitaryInfoClient.class);

    @Autowired
    private WebServiceTemplate webServiceTemplate;

    public JAXBElement<EMISmilitaryServiceEligibilityResponseType> getMilitaryServiceEligibilityResponse (String value, String type) {
        ObjectFactory factory = new ObjectFactory();
        InputEdiPiOrIcn input = new InputEdiPiOrIcn();
        InputEdipiIcn edi = new InputEdipiIcn();
        edi.setEdipiORicnValue(value);
        edi.setInputType(type);
        input.setEdipiORicn(edi);
        JAXBElement<InputEdiPiOrIcn> request = factory.createEMISmilitaryServiceEligibilityRequest(input);

        JAXBElement<EMISmilitaryServiceEligibilityResponseType> response =
                (JAXBElement<EMISmilitaryServiceEligibilityResponseType>) webServiceTemplate.marshalSendAndReceive(request);

        return response;
    }
}
