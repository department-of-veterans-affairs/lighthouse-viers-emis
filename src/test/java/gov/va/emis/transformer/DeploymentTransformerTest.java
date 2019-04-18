package gov.va.emis.transformer;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.emis.mappers.DeploymentMapper;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISdeploymentResponseType;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXB;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class DeploymentTransformerTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(DeploymentTransformerTest.class);
  EMISdeploymentResponseType emisResponse;
  gov.va.schema.emis.vdrdodadapter.v2.EMISdeploymentResponseType dodResponse;

  public void initializeResponses() {
    try {
      ClassPathResource emisSampleResponse =
          new ClassPathResource("/samples/DeploymentEmisResponse.xml");
      ClassPathResource dodSampleResponse =
          new ClassPathResource("/samples/DeploymentDodResponse.xml");
      InputStream emisInputStream = emisSampleResponse.getInputStream();
      InputStream dodInputStream = dodSampleResponse.getInputStream();
      emisResponse = JAXB.unmarshal(emisInputStream, EMISdeploymentResponseType.class);
      dodResponse =
          JAXB.unmarshal(
              dodInputStream, gov.va.schema.emis.vdrdodadapter.v2.EMISdeploymentResponseType.class);
    } catch (IOException e) {
      LOGGER.debug("File not found, check resources folder", e);
      emisResponse = null;
      dodResponse = null;
    }
  }

  @Test
  public void DeploymentTransformersTest() {
    initializeResponses();
    EMISdeploymentResponseType transformedDodResponse =
        DeploymentMapper.INSTANCE.mapEMISdeploymentResponseType(dodResponse);
    assertThat(transformedDodResponse).isEqualToComparingFieldByFieldRecursively(emisResponse);
  }
}
