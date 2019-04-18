package gov.va.emis.transformer;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.emis.mappers.DisabilitiesMapper;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISdisabilitiesResponseType;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXB;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class DisabilitiesTransformerTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(DisabilitiesTransformerTest.class);
  EMISdisabilitiesResponseType emisResponse;
  gov.va.schema.emis.vdrdodadapter.v2.EMISdisabilitiesResponseType dodResponse;

  public void initializeResponses() {
    try {
      ClassPathResource emisSampleResponse =
          new ClassPathResource("/samples/DisabilitiesEmisResponse.xml");
      ClassPathResource dodSampleResponse =
          new ClassPathResource("/samples/DisabilitiesDodResponse.xml");
      InputStream emisInputStream = emisSampleResponse.getInputStream();
      InputStream dodInputStream = dodSampleResponse.getInputStream();
      emisResponse = JAXB.unmarshal(emisInputStream, EMISdisabilitiesResponseType.class);
      dodResponse =
          JAXB.unmarshal(
              dodInputStream,
              gov.va.schema.emis.vdrdodadapter.v2.EMISdisabilitiesResponseType.class);
    } catch (IOException e) {
      LOGGER.debug("File not found, check resources folder", e);
      emisResponse = null;
      dodResponse = null;
    }
  }

  @Test
  public void DisabilitiesTransformersTest() {
    initializeResponses();
    EMISdisabilitiesResponseType transformedDodResponse =
        DisabilitiesMapper.INSTANCE.mapEMISdisabilitiesResponseType(dodResponse);
    assertThat(transformedDodResponse).isEqualToComparingFieldByFieldRecursively(emisResponse);
  }
}
