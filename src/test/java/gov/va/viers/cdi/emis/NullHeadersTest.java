package gov.va.viers.cdi.emis;

import static org.springframework.ws.test.server.RequestCreators.withSoapEnvelope;
import static org.springframework.ws.test.server.ResponseMatchers.payload;

import javax.xml.transform.Source;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringSource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NullHeadersTest {

  @Autowired private ApplicationContext applicationContext;

  private MockWebServiceClient mockClient;

  @Before
  public void createClient() {
    mockClient = MockWebServiceClient.createClient(applicationContext);
  }

  @Test
  public void testNullHeaders() {
    Source requestEnvelope =
        new StringSource(
            "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:v2=\"http://viers.va.gov/cdi/CDI/commonService/v2\" xmlns:v21=\"http://viers.va.gov/cdi/eMIS/RequestResponse/MilitaryInfo/v2\" xmlns:v22=\"http://viers.va.gov/cdi/eMIS/RequestResponse/v2\" xmlns:v23=\"http://viers.va.gov/cdi/eMIS/commonService/v2\">\n"
                + "   <soap:Body>\n"
                + "      <v21:eMISmilitaryServiceEligibilityRequest>\n"
                + "         <v22:edipiORicn>\n"
                + "          <v23:edipiORicnValue></v23:edipiORicnValue>\n"
                + "            <v23:inputType>ICN</v23:inputType>\n"
                + "         </v22:edipiORicn>\n"
                + "      </v21:eMISmilitaryServiceEligibilityRequest>\n"
                + "   </soap:Body>\n"
                + "</soap:Envelope>");

    Source responseEnvelope =
        new StringSource(
            "<eMISmilitaryServiceEligibilityResponse xmlns=\"http://viers.va.gov/cdi/eMIS/RequestResponse/MilitaryInfo/v2\" xmlns:ns2=\"http://viers.va.gov/cdi/eMIS/commonService/v2\" xmlns:ns3=\"http://viers.va.gov/cdi/eMIS/RequestResponse/v2\" xmlns:ns4=\"http://viers.va.gov/cdi/CDI/commonService/v2\">\n"
                + "    <ns3:ESSError>\n"
                + "        <ns4:ESSResponseCode>ERROR</ns4:ESSResponseCode>\n"
                + "        <ns4:code>MIS-ERR-03</ns4:code>\n"
                + "        <ns4:text>INVALID_IDENTIFIER</ns4:text>\n"
                + "        <ns4:essCode>gov.va.ess.fault.io.InputOutputFault</ns4:essCode>\n"
                + "        <ns4:essText>Invalid Parameter Identifier</ns4:essText>\n"
                + "        <ns4:severity>Error</ns4:severity>\n"
                + "        <ns4:timestamp>2019-04-15T15:28:23.642-04:00</ns4:timestamp>\n"
                + "        <ns4:serviceName>Veteran Eligibility</ns4:serviceName>\n"
                + "        <ns4:codePackage>gov.va.viers.emis.milinfo</ns4:codePackage>\n"
                + "        <ns4:serviceDomain>Military History</ns4:serviceDomain>\n"
                + "        <ns4:businessDomain>Enterprise Military Information</ns4:businessDomain>\n"
                + "    </ns3:ESSError>\n"
                + "</eMISmilitaryServiceEligibilityResponse>");

    mockClient.sendRequest(withSoapEnvelope(requestEnvelope)).andExpect(payload(responseEnvelope));
  }
}
