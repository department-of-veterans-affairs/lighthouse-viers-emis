package gov.va.emis.transformer;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.emis.mappers.GuardReserveServicePeriodsMapper;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISguardReserveServicePeriodsResponseType;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXB;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class GuardReserveServicePeriodsTransformerTest {

  private static final Logger LOGGER =
          LoggerFactory.getLogger(GuardReserveServicePeriodsTransformerTest.class);
  EMISguardReserveServicePeriodsResponseType emisResponse;
  gov.va.schema.emis.vdrdodadapter.v2.EMISguardReserveServicePeriodsResponseType dodResponse;

  public void initializeResponses() {
    try {
      ClassPathResource emisSampleResponse =
              new ClassPathResource("/samples/GuardReserveServicePeriodsEmisResponse.xml");
      ClassPathResource dodSampleResponse =
              new ClassPathResource("/samples/GuardReserveServicePeriodsDodResponse.xml");
      InputStream emisInputStream = emisSampleResponse.getInputStream();
      InputStream dodInputStream = dodSampleResponse.getInputStream();
      emisResponse = JAXB.unmarshal(emisInputStream, EMISguardReserveServicePeriodsResponseType.class);
      dodResponse =
              JAXB.unmarshal(
                      dodInputStream,
                      gov.va.schema.emis.vdrdodadapter.v2.EMISguardReserveServicePeriodsResponseType.class);
    } catch (IOException e) {
      LOGGER.debug("File not found, check resources folder", e);
      emisResponse = null;
      dodResponse = null;
    }
  }

  @Test
  public void GuardReserveServicePeriodsTransformersTest() {
    initializeResponses();
    EMISguardReserveServicePeriodsResponseType transformedDodResponse =
            GuardReserveServicePeriodsMapper.INSTANCE.mapEMISguardReserveServicePeriodsResponseType(dodResponse);
    assertThat(transformedDodResponse).isEqualToComparingFieldByFieldRecursively(emisResponse);
  }
}
