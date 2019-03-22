package gov.va.viers.cdi.emis.ws.endpoint;

import gov.va.EMISMapper;
import gov.va.EMISMapperImpl;
import gov.va.schema.emis.vdrdodadapter.v2.DoDAdapterClient;
import gov.va.viers.cdi.cdi.commonservice.v2.ESSErrorType;
import gov.va.viers.cdi.cdi.commonservice.v2.InputHeaderInfo;
import gov.va.viers.cdi.emis.requestresponse.militaryinfo.v2.ObjectFactory;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryServiceEligibilityResponseType;
import gov.va.viers.cdi.emis.requestresponse.v2.InputEdiPiOrIcn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.server.endpoint.annotation.SoapHeader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Endpoint
public class MilitaryInfoEndpoint {

  private static final Logger LOGGER = LoggerFactory.getLogger(MilitaryInfoEndpoint.class);

  private ObjectFactory objectFactory = new ObjectFactory();

  @Autowired private DoDAdapterClient dodClient;

  @PayloadRoot(
      namespace = "http://viers.va.gov/cdi/eMIS/RequestResponse/MilitaryInfo/v2",
      localPart = "eMISmilitaryServiceEligibilityRequest")
  @ResponsePayload
  public JAXBElement<EMISmilitaryServiceEligibilityResponseType> getServiceEligibility(
      @RequestPayload InputEdiPiOrIcn request,
      @SoapHeader(value = "{http://viers.va.gov/cdi/CDI/commonService/v2}inputHeaderInfo")
          SoapHeaderElement soapHeaderElement) {

    InputHeaderInfo requestSoapHeaders = new InputHeaderInfo();
    requestSoapHeaders = getInputHeaderInfo(soapHeaderElement, requestSoapHeaders);

    try {
      if (!request.getEdipiORicn().getInputType().equals("edipi")) {
        return generateMissingOrInvalidEdipiEssError(
            requestSoapHeaders, "MIS-ERR-03", "INVALID_IDENTIFIER");
      } else if (request.getEdipiORicn().getEdipiORicnValue().isEmpty()) {
        return generateMissingOrInvalidEdipiEssError(
            requestSoapHeaders, "MIS-ERR-02", "MISSING_EDIPI");
      }
    } catch (DatatypeConfigurationException e) {
      LOGGER.error("error while generating essError", e);
    }

    /* This should probably be wrapped in some null checks, not sure what EMIS does in those cases
    though with regards to returning that there was bad input*/
    JAXBElement<gov.va.schema.emis.vdrdodadapter.v2.EMISmilitaryServiceEligibilityResponseType>
        dodResponse =
            dodClient.getMilitaryServiceEligibilityResponse(
                request.getEdipiORicn().getEdipiORicnValue());

    EMISmilitaryServiceEligibilityResponseType noJaxbResponse;
    noJaxbResponse =
        EMISMapper.INSTANCE.mapEMISmilitaryServiceEligibilityResponseType(dodResponse.getValue());

    try {
      if (dodResponse.getValue().getESSError().getESSResponseCode().equals("ERROR")) {
        EMISMapperImpl emisMapper = new EMISMapperImpl();
        ESSErrorType essErrorType =
            emisMapper.mapESSErrorType(dodResponse.getValue().getESSError());
        return generateBadFormatEdipiEssError(essErrorType, requestSoapHeaders);
      }
    } catch (DatatypeConfigurationException e) {
      LOGGER.error("error when generating badFormatEdipiEssError", e);
    }

    return objectFactory.createEMISmilitaryServiceEligibilityResponse(noJaxbResponse);
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

  private JAXBElement<EMISmilitaryServiceEligibilityResponseType> generateBadFormatEdipiEssError(
      ESSErrorType essErrorType, InputHeaderInfo inputHeaderInfo)
      throws DatatypeConfigurationException {

    XMLGregorianCalendar xmlGregorianCalendar = xmlGregorianCalendarCurrentTime();

    essErrorType.setCode("MIS-ERR-05");
    essErrorType.setText("EDIPI_BAD_Format");
    essErrorType.setEssCode("gov.va.ess.fault.io.InputOutputFault");
    essErrorType.setEssText("EDIPI incorrectly formatted");
    essErrorType.setTimestamp(xmlGregorianCalendar);
    essErrorType.setServiceName("Veteran Eligibility");
    essErrorType.setUserId(inputHeaderInfo.getUserId());
    essErrorType.setCodePackage("gov.va.viers.emis.milinfo");
    essErrorType.setServiceDomain("Military History");
    essErrorType.setBusinessDomain("Enterprise Military Information");
    return null;
  }

  private JAXBElement<EMISmilitaryServiceEligibilityResponseType>
      generateMissingOrInvalidEdipiEssError(
          InputHeaderInfo header, String errorCode, String errorType)
          throws DatatypeConfigurationException {

    XMLGregorianCalendar xmlGregorianCalendar = xmlGregorianCalendarCurrentTime();

    ESSErrorType essErrorType = new ESSErrorType();
    essErrorType.setEssTransactionID(header.getTransactionId().toString());
    essErrorType.setESSResponseCode("ERROR");
    essErrorType.setCode(errorCode);
    essErrorType.setText(errorType);
    essErrorType.setEssCode("gov.va.ess.fault.io.InputOutputFault");
    essErrorType.setEssText("Invalid Parameter Identifier");
    essErrorType.setSeverity("Error");
    essErrorType.setTimestamp(xmlGregorianCalendar);
    essErrorType.setServiceName("Veteran Eligibility");
    essErrorType.setUserId(header.getUserId());
    essErrorType.setCodePackage("gov.va.viers.emis.milinfo");
    essErrorType.setServiceDomain("Military History");
    essErrorType.setBusinessDomain("Enterprise Military Information");

    EMISmilitaryServiceEligibilityResponseType errorResponse =
        new EMISmilitaryServiceEligibilityResponseType();
    errorResponse.setESSError(essErrorType);
    return objectFactory.createEMISmilitaryServiceEligibilityResponse(errorResponse);
  }

  private XMLGregorianCalendar xmlGregorianCalendarCurrentTime()
      throws DatatypeConfigurationException {
    GregorianCalendar gregorianCalendar = new GregorianCalendar();
    gregorianCalendar.setTime(new Date());
    return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
  }
}
