package gov.va.emis.endpoint;

import gov.va.dod.DoDAdapterClient;
import gov.va.emis.mappers.GuardReserveServicePeriodsMapper;
import gov.va.viers.cdi.cdi.commonservice.v2.ESSErrorType;
import gov.va.viers.cdi.emis.requestresponse.militaryinfo.v2.ObjectFactory;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISguardReserveServicePeriodsResponseType;
import gov.va.viers.cdi.emis.requestresponse.v2.InputEdiPiOrIcn;
import javax.xml.bind.JAXBElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.soap.server.endpoint.annotation.SoapHeader;

@Endpoint
public class GuardReserveServicePeriodsEndpoint extends EmisEndpoint {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(GuardReserveServicePeriodsEndpoint.class);

  private ObjectFactory objectFactory = new ObjectFactory();

  @Autowired private DoDAdapterClient dodClient;

  @Override
  @PayloadRoot(
    namespace = "http://viers.va.gov/cdi/eMIS/RequestResponse/MilitaryInfo/v2",
    localPart = "eMISguardReserveServicePeriodsRequest"
  )
  public JAXBElement processRequest(
      @RequestPayload InputEdiPiOrIcn request,
      @SoapHeader(value = "{http://viers.va.gov/cdi/CDI/commonService/v2}inputHeaderInfo")
          org.springframework.ws.soap.SoapHeader headerInfo) {
    return super.processRequest(request, headerInfo);
  }

  @Override
  JAXBElement makeEmisError(ESSErrorType error) {
    EMISguardReserveServicePeriodsResponseType errorResponse =
        new EMISguardReserveServicePeriodsResponseType();
    errorResponse.setESSError(error);
    return objectFactory.createEMISguardReserveServicePeriodsResponse(errorResponse);
  }

  @Override
  ResponseTuple sendRequest(String edipi) {
    JAXBElement<gov.va.schema.emis.vdrdodadapter.v2.EMISguardReserveServicePeriodsResponseType>
        vadirResponse = dodClient.getGuardReserveServicePeriodsResponse(edipi);

    ResponseTuple tuple = new ResponseTuple();

    EMISguardReserveServicePeriodsResponseType mappedResponse =
        GuardReserveServicePeriodsMapper.INSTANCE.mapEMISguardReserveServicePeriodsResponseType(
            vadirResponse.getValue());
    tuple.setResponse(objectFactory.createEMISguardReserveServicePeriodsResponse(mappedResponse));
    tuple.setError(vadirResponse.getValue().getESSError());
    return tuple;
  }
}
