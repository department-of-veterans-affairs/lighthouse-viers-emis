package gov.va.emis.endpoint;

import javax.servlet.Servlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

  @Bean
  public ServletRegistrationBean<Servlet> messageDispatcherServlet(
      ApplicationContext applicationContext) {
    MessageDispatcherServlet servlet = new MessageDispatcherServlet();
    servlet.setApplicationContext(applicationContext);

    return new ServletRegistrationBean<>(servlet, "/ws/*");
  }

  @Bean
  public SaajSoapMessageFactory messageFactory() {
    SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
    messageFactory.setSoapVersion(SoapVersion.SOAP_12);
    return messageFactory;
  }

  @Bean(name = "eMISMilitaryInfoRequestResponse")
  public Wsdl11Definition defaultWsdl11Definition() {
    SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
    wsdl11Definition.setWsdl(new ClassPathResource("/wsdl/eMISMilitaryInformationService.wsdl"));

    return wsdl11Definition;
  }
}
