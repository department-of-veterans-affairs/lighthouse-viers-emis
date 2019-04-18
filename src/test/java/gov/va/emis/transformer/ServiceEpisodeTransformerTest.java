package gov.va.emis.transformer;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.emis.mappers.ServiceEpisodeMapper;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISserviceEpisodeResponseType;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXB;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class ServiceEpisodeTransformerTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(ServiceEpisodeTransformerTest.class);
  EMISserviceEpisodeResponseType emisResponse;
  gov.va.schema.emis.vdrdodadapter.v2.EMISserviceEpisodeResponseType dodResponse;

  public void initializeResponses() {
    try {
      ClassPathResource emisSampleResponse =
          new ClassPathResource("/samples/ServiceEpisodeEmisResponse.xml");
      ClassPathResource dodSampleResponse =
          new ClassPathResource("/samples/ServiceEpisodeDodResponse.xml");
      InputStream emisInputStream = emisSampleResponse.getInputStream();
      InputStream dodInputStream = dodSampleResponse.getInputStream();
      emisResponse = JAXB.unmarshal(emisInputStream, EMISserviceEpisodeResponseType.class);
      dodResponse =
          JAXB.unmarshal(
              dodInputStream,
              gov.va.schema.emis.vdrdodadapter.v2.EMISserviceEpisodeResponseType.class);
    } catch (IOException e) {
      LOGGER.debug("File not found, check resources folder", e);
      emisResponse = null;
      dodResponse = null;
    }
  }

  @Test
  public void ServiceEpisodeTransformersTest() {
    initializeResponses();
    EMISserviceEpisodeResponseType transformedDodResponse =
        ServiceEpisodeMapper.INSTANCE.mapEMISserviceEpisodeResponseType(dodResponse);
    assertThat(transformedDodResponse).isEqualToComparingFieldByFieldRecursively(emisResponse);
  }
}
