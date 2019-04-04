package gov.va.viers.cdi.emis.ws.builder;

import gov.va.viers.cdi.cdi.commonservice.v2.ESSErrorType;
import gov.va.viers.cdi.cdi.commonservice.v2.InputHeaderInfo;
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

  public static ESSErrorType buildEssError(
      SoapHeaderElement soapHeaderElement, String errorCode, String errorType, String essText) {

    XMLGregorianCalendar xmlGregorianCalendarCurrentTime = xmlGregorianCalendarCurrentTime();

    ESSErrorType essErrorType = new ESSErrorType();

    if (soapHeaderElement != null) {
      if (soapHeaderElement.getResult() != null) {
        InputHeaderInfo requestSoapHeaders = getInputHeaderInfo(soapHeaderElement);
        if (requestSoapHeaders != null) {
          essErrorType.setUserId(requestSoapHeaders.getUserId());
          if (requestSoapHeaders.getTransactionId() != null) {
            essErrorType.setEssTransactionID(requestSoapHeaders.getTransactionId().toString());
          }
          //TODO else generate transaction id and set it.
        }
      }
    }
    essErrorType.setESSResponseCode("ERROR");
    essErrorType.setCode(errorCode);
    essErrorType.setText(errorType);
    essErrorType.setEssCode("gov.va.ess.fault.io.InputOutputFault");
    essErrorType.setEssText(essText);
    essErrorType.setSeverity("Error");
    essErrorType.setTimestamp(xmlGregorianCalendarCurrentTime);
    essErrorType.setServiceName("Veteran Eligibility");
    essErrorType.setCodePackage("gov.va.viers.emis.milinfo");
    essErrorType.setServiceDomain("Military History");
    essErrorType.setBusinessDomain("Enterprise Military Information");

    return essErrorType;
  }

  private static XMLGregorianCalendar xmlGregorianCalendarCurrentTime() {
    try {
      GregorianCalendar gregorianCalendar = new GregorianCalendar();
      gregorianCalendar.setTime(new Date());
      return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
    } catch (DatatypeConfigurationException e) {
      LOGGER.error("error creating xmlGregorianCalendar for current time: " + e);
    }
    return null;
  }

  private static InputHeaderInfo getInputHeaderInfo(SoapHeaderElement soapHeaderElement) {
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
