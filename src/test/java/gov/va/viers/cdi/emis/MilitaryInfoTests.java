package gov.va.viers.cdi.emis;

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
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MilitaryInfoTests {

  private static final Logger LOGGER = LoggerFactory.getLogger(MilitaryInfoTests.class);

  @Autowired private MilitaryInfoClient emisClient;

  @Autowired private DoDAdapterClient dodClient;

  public void initSuccessMock() {
    String sampleResponsePath = "exampleSuccessVadirResponse_MilInfoEligSvc.xml";
    JAXBElement<EMISmilitaryServiceEligibilityResponseType> jaxbDodResponse =
        getSampleJaxbDodResponse(sampleResponsePath);
    Mockito.doReturn(jaxbDodResponse)
        .when(dodClient)
        .getMilitaryServiceEligibilityResponse("6001010072");
  }

  public void initBadFormatMock() {
    String sampleResponsePath = "exampleBadFormatVadirResponse_MilInfoEligSvc.xml";
    JAXBElement<EMISmilitaryServiceEligibilityResponseType> jaxbDodResponse =
        getSampleJaxbDodResponse(sampleResponsePath);
    Mockito.doReturn(jaxbDodResponse)
        .when(dodClient)
        .getMilitaryServiceEligibilityResponse("BADEDIPI01");
  }

  private JAXBElement<EMISmilitaryServiceEligibilityResponseType> getSampleJaxbDodResponse(
      String sampleResponsePath) {
    EMISmilitaryServiceEligibilityResponseType dodResponse;
    try {
      // Grabbing from classpath
      ClassPathResource sampleResponse =
          new ClassPathResource("/vadirResponses/" + sampleResponsePath);
      InputStream inputStream = sampleResponse.getInputStream();
      dodResponse = JAXB.unmarshal(inputStream, EMISmilitaryServiceEligibilityResponseType.class);
    } catch (IOException e) {
      LOGGER.debug("File not found, check resources folder");
      dodResponse = null;
    }
    // Have to wrap in JAXBElement to match method signature
    ObjectFactory objectFactory = new ObjectFactory();
    JAXBElement<EMISmilitaryServiceEligibilityResponseType> jaxbDodResponse =
        objectFactory.createEMISmilitaryServiceEligibilityResponse(dodResponse);
    return jaxbDodResponse;
  }

  @Test
  public void getMilitaryServiceEligibilityInvalidIdentifier() {
    JAXBElement<gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryServiceEligibilityResponseType>
        response = emisClient.getMilitaryServiceEligibilityResponse("6001010072", "ICN");

    assertThat(
            Optional.ofNullable(response)
                .map(r -> r.getValue())
                .map(e -> e.getESSError())
                .map(c -> c.getCode())
                .orElse(null))
        .isEqualTo("MIS-ERR-03");
  }

  @Test
  public void getMilitaryServiceEligibilityMissingEdipi() {
    JAXBElement<gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryServiceEligibilityResponseType>
        response = emisClient.getMilitaryServiceEligibilityResponse("", "EDIPI");

    assertThat(
            Optional.ofNullable(response)
                .map(r -> r.getValue())
                .map(e -> e.getESSError())
                .map(c -> c.getCode())
                .orElse(null))
        .isEqualTo("MIS-ERR-02");
  }

  @Test
  public void getMilitaryServiceEligibilityBadFormat() {
    initBadFormatMock();
    JAXBElement<gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryServiceEligibilityResponseType>
        response = emisClient.getMilitaryServiceEligibilityResponse("BADEDIPI01", "EDIPI");

    assertThat(
            Optional.ofNullable(response)
                .map(r -> r.getValue())
                .map(e -> e.getESSError())
                .map(c -> c.getCode())
                .orElse(null))
        .isEqualTo("MIS-ERR-05");
  }

  @Test
  public void getMilitaryServiceEligibilitySuccess() {
    initSuccessMock();
    JAXBElement<gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryServiceEligibilityResponseType>
        response = emisClient.getMilitaryServiceEligibilityResponse("6001010072", "EDIPI");

    assertThat(
            Optional.ofNullable(response)
                .map(r -> r.getValue())
                .map(v -> v.getMilitaryServiceEligibility())
                .flatMap(m -> m.stream().findFirst())
                .map(e -> e.getVeteranStatus())
                .map(s -> s.getPersonFirstName())
                .orElse(null))
        .isEqualTo("EMIS_FIRST_NM_74");
  }
}
