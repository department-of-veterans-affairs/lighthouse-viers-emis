package gov.va.viers.cdi.emis.ws.endpoint;

import gov.va.EMISMapper;
import gov.va.schema.emis.vdrdodadapter.v2.DoDAdapterClient;
import gov.va.viers.cdi.cdi.commonservice.v2.InputHeaderInfo;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.TransformerException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.annotation.Endpoint;

import org.slf4j.Logger;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryServiceEligibilityResponseType;
import gov.va.viers.cdi.emis.requestresponse.v2.InputEdiPiOrIcn;
import gov.va.viers.cdi.emis.requestresponse.militaryinfo.v2.ObjectFactory;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

import javax.xml.bind.JAXBElement;
import org.springframework.ws.soap.server.endpoint.annotation.SoapHeader;

@Endpoint
public class MilitaryInfoEndpoint {

  private static final Logger LOGGER = LoggerFactory.getLogger(MilitaryInfoEndpoint.class);

  public static final String NAMESPACE = "http://viers.va.gov/cdi/eMIS/RequestResponse/MilitaryInfo/v2";

  public static final String LOCAL_PART = "eMISmilitaryServiceEligibilityRequest";

  private ObjectFactory milInfoFactory = new ObjectFactory();

  private gov.va.viers.cdi.cdi.commonservice.v2.ObjectFactory commonFactory = new gov.va.viers.cdi.cdi.commonservice.v2.ObjectFactory();

  @Autowired private DoDAdapterClient dodClient;

  @PayloadRoot(
      namespace = NAMESPACE,
      localPart = LOCAL_PART)
  @ResponsePayload
  public JAXBElement<EMISmilitaryServiceEligibilityResponseType> getServiceEligibility(
      @RequestPayload InputEdiPiOrIcn request,
      @SoapHeader(value = "{http://viers.va.gov/cdi/CDI/commonService/v2}inputHeaderInfo")
          SoapHeaderElement soapHeaderElement, MessageContext messageContext) throws JAXBException, TransformerException {
    SaajSoapMessage soapRequest = (SaajSoapMessage) messageContext.getRequest();
    org.springframework.ws.soap.SoapHeader soapRequestHeader = soapRequest.getSoapHeader();

    SaajSoapMessage soapResponse = (SaajSoapMessage) messageContext.getResponse();
    org.springframework.ws.soap.SoapHeader soapResponseHeader = soapResponse.getSoapHeader();

    InputHeaderInfo requestSoapHeaders = new InputHeaderInfo();
    requestSoapHeaders = getInputHeaderInfo(soapHeaderElement, requestSoapHeaders);

    /* This should probably be wrapped in some null checks, not sure what EMIS does in those cases
    though with regards to returning that there was bad input*/
    JAXBElement<gov.va.schema.emis.vdrdodadapter.v2.EMISmilitaryServiceEligibilityResponseType>
        dodResponse =
        dodClient.getMilitaryServiceEligibilityResponse(
            request.getEdipiORicn().getEdipiORicnValue());

    EMISmilitaryServiceEligibilityResponseType noJaxbResponse;
    noJaxbResponse =
        EMISMapper.INSTANCE.mapEMISmilitaryServiceEligibilityResponseType(dodResponse.getValue());
    return milInfoFactory.createEMISmilitaryServiceEligibilityResponse(noJaxbResponse);
  }

  private InputHeaderInfo getInputHeaderInfo(
      SoapHeaderElement soapHeaderElement, InputHeaderInfo requestSoapHeaders) {
    try {
      // create unmarshaller
      JAXBContext context =
          JAXBContext.newInstance(gov.va.viers.cdi.cdi.commonservice.v2.ObjectFactory.class);
      Unmarshaller unmarshaller = context.createUnmarshaller();

      // unmarshall header
      JAXBElement<InputHeaderInfo> headers =
          (JAXBElement<InputHeaderInfo>) unmarshaller.unmarshal(soapHeaderElement.getSource());

      // get header values
      requestSoapHeaders = headers.getValue();

    } catch (JAXBException e) {
      LOGGER.error("error during unmarshalling of soap header", e);
    }
    return requestSoapHeaders;
  }
}
