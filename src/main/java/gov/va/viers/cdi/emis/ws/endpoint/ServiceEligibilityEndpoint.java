package gov.va.viers.cdi.emis.ws.endpoint;

import gov.va.EMISMapper;
import gov.va.schema.emis.vdrdodadapter.v2.DoDAdapterClient;
import gov.va.viers.cdi.cdi.commonservice.v2.ESSErrorType;
import gov.va.viers.cdi.emis.requestresponse.militaryinfo.v2.ObjectFactory;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryServiceEligibilityResponseType;
import gov.va.viers.cdi.emis.requestresponse.v2.InputEdiPiOrIcn;
import javax.xml.bind.JAXBElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.server.endpoint.annotation.SoapHeader;

@Endpoint
public class ServiceEligibilityEndpoint extends EmisEndpoint {

  private static final Logger LOGGER = LoggerFactory.getLogger(ServiceEligibilityEndpoint.class);

  private ObjectFactory objectFactory = new ObjectFactory();

  @Autowired private DoDAdapterClient dodClient;

  @Override
  @PayloadRoot(
    namespace = "http://viers.va.gov/cdi/eMIS/RequestResponse/MilitaryInfo/v2",
    localPart = "eMISmilitaryServiceEligibilityRequest"
  )
  @ResponsePayload
  public JAXBElement processRequest(
      @RequestPayload InputEdiPiOrIcn request,
      @SoapHeader(value = "{http://viers.va.gov/cdi/CDI/commonService/v2}inputHeaderInfo")
          org.springframework.ws.soap.SoapHeader headerInfo) {
    return super.processRequest(request, headerInfo);
  }

  @Override
  JAXBElement makeEmisError(ESSErrorType error) {
    EMISmilitaryServiceEligibilityResponseType errorResponse =
        new EMISmilitaryServiceEligibilityResponseType();
    errorResponse.setESSError(error);
    return objectFactory.createEMISmilitaryServiceEligibilityResponse(errorResponse);
  }

  @Override
  ResponseTuple sendRequest(String edipi) {
    JAXBElement<gov.va.schema.emis.vdrdodadapter.v2.EMISmilitaryServiceEligibilityResponseType>
        vadirResponse = dodClient.getMilitaryServiceEligibilityResponse(edipi);

    ResponseTuple tuple = new ResponseTuple();
    /* The null check happens in here to avoid having to pass this back and forth for objectFactory purposes*/
    if (vadirResponse.getValue() == null) {
      tuple.setResponse(
          objectFactory.createEMISmilitaryServiceEligibilityResponse(
              new EMISmilitaryServiceEligibilityResponseType()));
    } else {
      EMISmilitaryServiceEligibilityResponseType mappedResponse =
          EMISMapper.INSTANCE.mapEMISmilitaryServiceEligibilityResponseType(
              vadirResponse.getValue());
      tuple.setResponse(objectFactory.createEMISmilitaryServiceEligibilityResponse(mappedResponse));
    }
    tuple.setError(vadirResponse.getValue().getESSError());
    return tuple;
  }
}
