package gov.va.viers.cdi.emis.client;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

@Configuration
@PropertySource("classpath:client.properties")
public class ClientConfig {

    @Value("${client.default-uri}")  String defaultUri;
    @Value("${client.context-path}") String contextPath;


    @Bean
    Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath(contextPath);

        return jaxb2Marshaller;
    }

    @Bean
    public WebServiceTemplate webServiceTemplate() throws SOAPException {
        MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        SaajSoapMessageFactory saajSoapMessageFactory = new SaajSoapMessageFactory(messageFactory);

        WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
        webServiceTemplate.setMarshaller(jaxb2Marshaller());
        webServiceTemplate.setUnmarshaller(jaxb2Marshaller());
        webServiceTemplate.setDefaultUri(defaultUri);
        webServiceTemplate.setMessageFactory(saajSoapMessageFactory);

        return webServiceTemplate;
    }
}
