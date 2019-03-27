package gov.va.viers.cdi.emis.ws.builder;

import gov.va.EMISMapperImpl;
import gov.va.viers.cdi.cdi.commonservice.v2.ESSErrorType;
import gov.va.viers.cdi.cdi.commonservice.v2.InputHeaderInfo;
import gov.va.viers.cdi.emis.requestresponse.militaryinfo.v2.ObjectFactory;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryServiceEligibilityResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.soap.SoapHeaderElement;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ESSErrorBuilder {

  private static final Logger LOGGER = LoggerFactory.getLogger(ESSErrorBuilder.class);
  private ObjectFactory objectFactory = new ObjectFactory();
  private InputHeaderInfo requestSoapHeaders;

  public ESSErrorBuilder(SoapHeaderElement soapHeaderElement) {
    this.requestSoapHeaders = getInputHeaderInfo(soapHeaderElement);
  }

  public JAXBElement<EMISmilitaryServiceEligibilityResponseType> generateBadFormatEdipiEssError(
      gov.va.schema.emis.vdrdodadapter.v2.ESSErrorType essErrorTypeVdr) {

    XMLGregorianCalendar xmlGregorianCalendarCurrentTime = xmlGregorianCalendarCurrentTime();

    EMISMapperImpl emisMapper = new EMISMapperImpl();
    ESSErrorType essErrorType = emisMapper.mapESSErrorType(essErrorTypeVdr);

    essErrorType.setCode("MIS-ERR-05");
    essErrorType.setText("EDIPI_BAD_FORMAT");
    essErrorType.setEssCode("gov.va.ess.fault.io.InputOutputFault");
    essErrorType.setEssText("EDIPI incorrectly formatted");
    essErrorType.setTimestamp(xmlGregorianCalendarCurrentTime);
    essErrorType.setServiceName("Veteran Eligibility");
    essErrorType.setUserId(requestSoapHeaders.getUserId());
    essErrorType.setCodePackage("gov.va.viers.emis.milinfo");
    essErrorType.setServiceDomain("Military History");
    essErrorType.setBusinessDomain("Enterprise Military Information");

    EMISmilitaryServiceEligibilityResponseType errorResponse =
        new EMISmilitaryServiceEligibilityResponseType();
    errorResponse.setESSError(essErrorType);
    return objectFactory.createEMISmilitaryServiceEligibilityResponse(errorResponse);
  }

  public JAXBElement<EMISmilitaryServiceEligibilityResponseType>
      generateMissingOrInvalidEdipiEssError(String errorCode, String errorType) {

    XMLGregorianCalendar xmlGregorianCalendarCurrentTime = xmlGregorianCalendarCurrentTime();

    ESSErrorType essErrorType = new ESSErrorType();
    essErrorType.setEssTransactionID(requestSoapHeaders.getTransactionId().toString());
    essErrorType.setESSResponseCode("ERROR");
    essErrorType.setCode(errorCode);
    essErrorType.setText(errorType);
    essErrorType.setEssCode("gov.va.ess.fault.io.InputOutputFault");
    essErrorType.setEssText("Invalid Parameter Identifier");
    essErrorType.setSeverity("Error");
    essErrorType.setTimestamp(xmlGregorianCalendarCurrentTime);
    essErrorType.setServiceName("Veteran Eligibility");
    essErrorType.setUserId(requestSoapHeaders.getUserId());
    essErrorType.setCodePackage("gov.va.viers.emis.milinfo");
    essErrorType.setServiceDomain("Military History");
    essErrorType.setBusinessDomain("Enterprise Military Information");

    EMISmilitaryServiceEligibilityResponseType errorResponse =
        new EMISmilitaryServiceEligibilityResponseType();
    errorResponse.setESSError(essErrorType);
    return objectFactory.createEMISmilitaryServiceEligibilityResponse(errorResponse);
  }

  private XMLGregorianCalendar xmlGregorianCalendarCurrentTime() {
    try {
      GregorianCalendar gregorianCalendar = new GregorianCalendar();
      gregorianCalendar.setTime(new Date());
      return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
    } catch (DatatypeConfigurationException e) {
      LOGGER.error("error creating xmlGregorianCalendar for current time: " + e);
    }
    return null;
  }

  private InputHeaderInfo getInputHeaderInfo(SoapHeaderElement soapHeaderElement) {
    InputHeaderInfo requestSoapHeaders = new InputHeaderInfo();
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
      LOGGER.error("error during unmarshalling of soap header: " + e);
    }
    return requestSoapHeaders;
  }
}
