package gov.va.viers.cdi.emis.serviceEpisode;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.dod.DoDAdapterClient;
import gov.va.emis.client.MilitaryInfoClient;
import gov.va.schema.emis.vdrdodadapter.v2.EMISserviceEpisodeResponseType;
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
public class ServiceEpisodeTests {

  private static final Logger LOGGER = LoggerFactory.getLogger(ServiceEpisodeTests.class);

  @Autowired private MilitaryInfoClient emisClient;

  @Autowired private DoDAdapterClient dodClient;

  private void initMock(String sampleResponsePath, String edipi) {
    EMISserviceEpisodeResponseType dodResponse;
    try {
      // Grabbing from classpath
      ClassPathResource sampleResponse =
          new ClassPathResource("/vadirResponses/" + sampleResponsePath);
      InputStream inputStream = sampleResponse.getInputStream();
      dodResponse = JAXB.unmarshal(inputStream, EMISserviceEpisodeResponseType.class);
    } catch (IOException e) {
      LOGGER.debug("Could not find " + sampleResponsePath + ", check resources folder: " + e);
      dodResponse = null;
    }
    // Have to wrap in JAXBElement to match method signature
    ObjectFactory objectFactory = new ObjectFactory();
    JAXBElement<EMISserviceEpisodeResponseType> jaxbDodResponse =
        objectFactory.createEMISserviceEpisodeResponse(dodResponse);
    Mockito.doReturn(jaxbDodResponse).when(dodClient).getServiceEpisodeResponse(edipi);
  }

  @Test
  public void getServiceEpisodeEmptyResponse() {
    initMock("emptyVadirResponse_ServiceEpisodeSvc.xml", "1234567890");
    JAXBElement<gov.va.viers.cdi.emis.requestresponse.v2.EMISserviceEpisodeResponseType> response =
        emisClient.getServiceEpisodeResponse("1234567890", "EDIPI", false);

    assertThat(
        Optional.ofNullable(response)
            .map(r -> r.getValue())
            .map(v -> v.getMilitaryServiceEpisode())
            .orElse(null)
            .isEmpty());
  }

  @Test
  public void getServiceEpisodeSuccess() {
    initMock("exampleSuccessVadirResponse_ServiceEpisodeSvc.xml", "6001010072");
    JAXBElement<gov.va.viers.cdi.emis.requestresponse.v2.EMISserviceEpisodeResponseType> response =
        emisClient.getServiceEpisodeResponse("6001010072", "EDIPI", false);

    assertThat(
            Optional.ofNullable(response)
                .map(r -> r.getValue())
                .map(v -> v.getMilitaryServiceEpisode())
                .flatMap(m -> m.stream().findFirst())
                .map(e -> e.getMilitaryServiceEpisodeData())
                .map(s -> s.getPayPlanCode())
                .orElse(null))
        .isEqualTo("MO");
  }
}
