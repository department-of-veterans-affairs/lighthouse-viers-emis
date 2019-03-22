package gov.va.viers.cdi.emis.ws.endpoint;

import gov.va.viers.cdi.cdi.commonservice.v2.InputHeaderInfo;
import gov.va.viers.cdi.cdi.commonservice.v2.ObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;

public class MilInfoInterceptor implements EndpointInterceptor {

    private ObjectFactory commonFactory = new ObjectFactory();

    private static final Logger LOGGER = LoggerFactory.getLogger(MilInfoInterceptor.class);

    @Override
    public boolean handleRequest(MessageContext messageContext, Object o) throws Exception {
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext, Object endpoint) throws Exception {

        WebServiceMessage message = messageContext.getResponse();
        try {
            SoapHeader soapHeader = ((SoapMessage) message).getSoapHeader();
            InputHeaderInfo headerInfo = commonFactory.createInputHeaderInfo();
            headerInfo.setTransactionId("omgpleasework");
            JAXBElement<InputHeaderInfo> header = commonFactory.createInputHeaderInfo(headerInfo);

            JAXBContext context = JAXBContext.newInstance(InputHeaderInfo.class);
            Marshaller marshaller = context.createMarshaller();

            marshaller.marshal(header, soapHeader.getResult());
        } catch (Exception e) {
            System.out.println("SOMETHING WENT SUPER WRONG");
        }
        LOGGER.debug("Response handler is here, why can't I do things");
        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext, Object o) throws Exception {
        return true;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Object o, Exception e) throws Exception {

    }
}
