package gov.va.emis.transformer;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.emis.mappers.MilitaryOccupationMapper;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryOccupationResponseType;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXB;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class MilitaryOccupationTransformerTest {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(MilitaryOccupationTransformerTest.class);
  EMISmilitaryOccupationResponseType emisResponse;
  gov.va.schema.emis.vdrdodadapter.v2.EMISmilitaryOccupationResponseType dodResponse;

  public void initializeResponses() {
    try {
      ClassPathResource emisSampleResponse =
          new ClassPathResource("/samples/MilitaryOccupationEmisResponse.xml");
      ClassPathResource dodSampleResponse =
          new ClassPathResource("/samples/MilitaryOccupationDodResponse.xml");
      InputStream emisInputStream = emisSampleResponse.getInputStream();
      InputStream dodInputStream = dodSampleResponse.getInputStream();
      emisResponse = JAXB.unmarshal(emisInputStream, EMISmilitaryOccupationResponseType.class);
      dodResponse =
          JAXB.unmarshal(
              dodInputStream,
              gov.va.schema.emis.vdrdodadapter.v2.EMISmilitaryOccupationResponseType.class);
    } catch (IOException e) {
      LOGGER.debug("File not found, check resources folder", e);
      emisResponse = null;
      dodResponse = null;
    }
  }

  @Test
  public void MilitaryOccupationTransformersTest() {
    initializeResponses();
    EMISmilitaryOccupationResponseType transformedDodResponse =
        MilitaryOccupationMapper.INSTANCE.mapEMISmilitaryOccupationResponseType(dodResponse);
    assertThat(transformedDodResponse).isEqualToComparingFieldByFieldRecursively(emisResponse);
  }
}
