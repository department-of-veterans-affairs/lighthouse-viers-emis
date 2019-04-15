package gov.va.viers.cdi.emis;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.schema.emis.vdrdodadapter.v2.DoDAdapterClient;
import gov.va.schema.emis.vdrdodadapter.v2.EMISmilitaryServiceEligibilityResponseType;
import gov.va.schema.emis.vdrdodadapter.v2.ObjectFactory;
import gov.va.viers.cdi.emis.client.MilitaryInfoClient;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBElement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class EmisEndpointTests {

  private static final Logger LOGGER = LoggerFactory.getLogger(EmisEndpointTests.class);

  @Autowired private MilitaryInfoClient emisClient;

  @Autowired private DoDAdapterClient dodClient;

  private void initMock(String sampleResponsePath, String edipi) {
    EMISmilitaryServiceEligibilityResponseType dodResponse;
    try {
      // Grabbing from classpath
      ClassPathResource sampleResponse =
          new ClassPathResource("/vadirResponses/" + sampleResponsePath);
      InputStream inputStream = sampleResponse.getInputStream();
      dodResponse = JAXB.unmarshal(inputStream, EMISmilitaryServiceEligibilityResponseType.class);
    } catch (IOException e) {
      LOGGER.debug("Could not find " + sampleResponsePath + ", check resources folder: " + e);
      dodResponse = null;
    }
    // Have to wrap in JAXBElement to match method signature
    ObjectFactory objectFactory = new ObjectFactory();
    JAXBElement<EMISmilitaryServiceEligibilityResponseType> jaxbDodResponse =
        objectFactory.createEMISmilitaryServiceEligibilityResponse(dodResponse);
    Mockito.doReturn(jaxbDodResponse).when(dodClient).getMilitaryServiceEligibilityResponse(edipi);
  }

  @Test
  public void catchWrongIdentifierType() {
    JAXBElement<gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryServiceEligibilityResponseType>
        response = emisClient.getMilitaryServiceEligibilityResponse("6001010072", "ICN", false);

    assertThat(
            Optional.ofNullable(response)
                .map(r -> r.getValue())
                .map(e -> e.getESSError())
                .map(c -> c.getCode())
                .orElse(null))
        .isEqualTo("MIS-ERR-03");
  }

  @Test
  public void catchMissingEDIPI() {
    JAXBElement<gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryServiceEligibilityResponseType>
        response = emisClient.getMilitaryServiceEligibilityResponse("", "EDIPI", false);

    assertThat(
            Optional.ofNullable(response)
                .map(r -> r.getValue())
                .map(e -> e.getESSError())
                .map(c -> c.getCode())
                .orElse(null))
        .isEqualTo("MIS-ERR-02");
  }

  @Test
  public void catchEDIPIBadFormat() {
    initMock("exampleBadFormatVadirResponse_MilInfoEligSvc.xml", "BADEDIPI01");
    JAXBElement<gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryServiceEligibilityResponseType>
        response = emisClient.getMilitaryServiceEligibilityResponse("BADEDIPI01", "EDIPI", false);

    assertThat(
            Optional.ofNullable(response)
                .map(r -> r.getValue())
                .map(e -> e.getESSError())
                .map(c -> c.getCode())
                .orElse(null))
        .isEqualTo("MIS-ERR-05");
  }

  @Test
  public void catchEDIPIBadFormatUninitializedHeader() {
    initMock("exampleBadFormatVadirResponse_MilInfoEligSvc.xml", "BADEDIPI01");
    JAXBElement<gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryServiceEligibilityResponseType>
        response = emisClient.getMilitaryServiceEligibilityResponse("BADEDIPI01", "EDIPI", true);

    assertThat(
            Optional.ofNullable(response)
                .map(r -> r.getValue())
                .map(e -> e.getESSError())
                .map(c -> c.getCode())
                .orElse(null))
        .isEqualTo("MIS-ERR-05");
  }

  @Test
  public void checkTransactionIdMapped() {
    JAXBElement<gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryServiceEligibilityResponseType>
        response = emisClient.getMilitaryServiceEligibilityResponse("", "EDIPI", false);

    assertThat(
            Optional.ofNullable(response)
                .map(r -> r.getValue())
                .map(e -> e.getESSError())
                .map(c -> c.getEssTransactionID())
                .orElse(null))
        .isEqualTo("transactionIdTest");
  }
}
