package gov.va.viers.cdi.emis.serviceEligibility;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.EMISMapper;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryServiceEligibilityResponseType;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXB;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class ServiceEligibilityTransformersTest {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(ServiceEligibilityTransformersTest.class);
  EMISmilitaryServiceEligibilityResponseType emisResponse;
  gov.va.schema.emis.vdrdodadapter.v2.EMISmilitaryServiceEligibilityResponseType dodResponse;

  public void initializeResponses() {
    try {
      ClassPathResource emisSampleResponse =
          new ClassPathResource("/samples/emisSampleResponse.xml");
      ClassPathResource dodSampleResponse = new ClassPathResource("/samples/dodSampleResponse.xml");
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
        EMISMapper.INSTANCE.mapEMISmilitaryServiceEligibilityResponseType(dodResponse);
    assertThat(transformedDodResponse).isEqualToComparingFieldByFieldRecursively(emisResponse);
  }
}
