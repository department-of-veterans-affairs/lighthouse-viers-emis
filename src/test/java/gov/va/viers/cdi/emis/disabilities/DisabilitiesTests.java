package gov.va.viers.cdi.emis.disabilities;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.dod.DoDAdapterClient;
import gov.va.emis.client.MilitaryInfoClient;
import gov.va.schema.emis.vdrdodadapter.v2.EMISdisabilitiesResponseType;
import gov.va.schema.emis.vdrdodadapter.v2.ObjectFactory;
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
public class DisabilitiesTests {

  private static final Logger LOGGER = LoggerFactory.getLogger(DisabilitiesTests.class);

  @Autowired private MilitaryInfoClient emisClient;

  @Autowired private DoDAdapterClient dodClient;

  private void initMock(String sampleResponsePath, String edipi) {
    EMISdisabilitiesResponseType dodResponse;
    try {
      // Grabbing from classpath
      ClassPathResource sampleResponse =
          new ClassPathResource("/vadirResponses/" + sampleResponsePath);
      InputStream inputStream = sampleResponse.getInputStream();
      dodResponse = JAXB.unmarshal(inputStream, EMISdisabilitiesResponseType.class);
    } catch (IOException e) {
      LOGGER.debug("Could not find " + sampleResponsePath + ", check resources folder: " + e);
      dodResponse = null;
    }
    // Have to wrap in JAXBElement to match method signature
    ObjectFactory objectFactory = new ObjectFactory();
    JAXBElement<EMISdisabilitiesResponseType> jaxbDodResponse =
        objectFactory.createEMISdisabilitiesResponse(dodResponse);
    Mockito.doReturn(jaxbDodResponse).when(dodClient).getDisabilitiesResponse(edipi);
  }

  @Test
  public void getDisabilitiesEmptyResponse() {
    initMock("emptyVadirResponse_DisabilitiesSvc.xml", "1234567890");
    JAXBElement<gov.va.viers.cdi.emis.requestresponse.v2.EMISdisabilitiesResponseType> response =
        emisClient.getDisabilitiesResponse("1234567890", "EDIPI", false);

    assertThat(
        Optional.ofNullable(response)
            .map(r -> r.getValue())
            .map(v -> v.getDisabilities())
            .orElse(null)
            .isEmpty());
  }

  @Test
  public void getDisabilitiesSuccess() {
    initMock("exampleSuccessVadirResponse_DisabilitiesSvc.xml", "6001010002");
    JAXBElement<gov.va.viers.cdi.emis.requestresponse.v2.EMISdisabilitiesResponseType> response =
        emisClient.getDisabilitiesResponse("6001010002", "EDIPI", false);

    assertThat(
            Optional.ofNullable(response)
                .map(r -> r.getValue())
                .map(v -> v.getDisabilities())
                .flatMap(m -> m.stream().findFirst())
                .map(e -> e.getPayAmount())
                .orElse(null))
        .isEqualTo("24573.60");
  }
}
