package gov.va.emis.transformer;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.emis.mappers.ServiceEligibilityMapper;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryServiceEligibilityResponseType;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXB;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class ServiceEligibilityTransformerTest {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(ServiceEligibilityTransformerTest.class);
  EMISmilitaryServiceEligibilityResponseType emisResponse;
  gov.va.schema.emis.vdrdodadapter.v2.EMISmilitaryServiceEligibilityResponseType dodResponse;

  public void initializeResponses() {
    try {
      ClassPathResource emisSampleResponse =
          new ClassPathResource("/samples/ServiceEligibilityEmisResponse.xml");
      ClassPathResource dodSampleResponse =
          new ClassPathResource("/samples/ServiceEligibilityDodResponse.xml");
      InputStream emisInputStream = emisSampleResponse.getInputStream();
      InputStream dodInputStream = dodSampleResponse.getInputStream();
      emisResponse =
          JAXB.unmarshal(emisInputStream, EMISmilitaryServiceEligibilityResponseType.class);
      dodResponse =
          JAXB.unmarshal(
              dodInputStream,
              gov.va.schema.emis.vdrdodadapter.v2.EMISmilitaryServiceEligibilityResponseType.class);
    } catch (IOException e) {
      LOGGER.debug("File not found, check resources folder", e);
      emisResponse = null;
      dodResponse = null;
    }
  }

  @Test
  public void serviceEligibilityTransformersTest() {
    initializeResponses();
    EMISmilitaryServiceEligibilityResponseType transformedDodResponse =
        ServiceEligibilityMapper.INSTANCE.mapEMISmilitaryServiceEligibilityResponseType(
            dodResponse);
    assertThat(transformedDodResponse).isEqualToComparingFieldByFieldRecursively(emisResponse);
  }
}
