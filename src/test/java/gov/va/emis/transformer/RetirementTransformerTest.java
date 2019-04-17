package gov.va.emis.transformer;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.emis.mappers.RetirementMapper;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISretirementResponseType;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXB;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class RetirementTransformerTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(RetirementTransformerTest.class);
  EMISretirementResponseType emisResponse;
  gov.va.schema.emis.vdrdodadapter.v2.EMISretirementResponseType dodResponse;

  public void initializeResponses() {
    try {
      ClassPathResource emisSampleResponse =
          new ClassPathResource("/samples/RetirementEmisResponse.xml");
      ClassPathResource dodSampleResponse =
          new ClassPathResource("/samples/RetirementDodResponse.xml");
      InputStream emisInputStream = emisSampleResponse.getInputStream();
      InputStream dodInputStream = dodSampleResponse.getInputStream();
      emisResponse = JAXB.unmarshal(emisInputStream, EMISretirementResponseType.class);
      dodResponse =
          JAXB.unmarshal(
              dodInputStream, gov.va.schema.emis.vdrdodadapter.v2.EMISretirementResponseType.class);
    } catch (IOException e) {
      LOGGER.debug("File not found, check resources folder", e);
      emisResponse = null;
      dodResponse = null;
    }
  }

  @Test
  public void RetirementTransformersTest() {
    initializeResponses();
    EMISretirementResponseType transformedDodResponse =
        RetirementMapper.INSTANCE.mapEMISretirementResponseType(dodResponse);
    assertThat(transformedDodResponse).isEqualToComparingFieldByFieldRecursively(emisResponse);
  }
}
