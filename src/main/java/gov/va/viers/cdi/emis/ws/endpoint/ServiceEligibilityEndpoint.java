package gov.va.viers.cdi.emis.ws.endpoint;

import gov.va.viers.cdi.cdi.commonservice.v2.ESSErrorType;
import gov.va.viers.cdi.emis.requestresponse.militaryinfo.v2.ObjectFactory;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryServiceEligibilityResponseType;

import javax.xml.bind.JAXBElement;

public class ServiceEligibilityEndpoint extends EmisEndpoint {

    public static final String NAMESPACE = "http://viers.va.gov/cdi/eMIS/RequestResponse/MilitaryInfo/v2";
    private ObjectFactory objectFactory = new ObjectFactory();



    @Override
    JAXBElement makeEmisError(ESSErrorType error) {
        EMISmilitaryServiceEligibilityResponseType errorResponse = new EMISmilitaryServiceEligibilityResponseType();
        errorResponse.setESSError(error);
        return objectFactory.createEMISmilitaryServiceEligibilityResponse(errorResponse);
    }

    @Override
    JAXBElement makeEmisPayload(String edipi) {
        //call dodAdapter.getServiceEligibility
        //objectfactory.result.toEmisResponse()
        return null;
    }
}
