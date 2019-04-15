package gov.va.emis.transformer;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.emis.mappers.UnitInformationMapper;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISunitInformationResponseType;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXB;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class UnitInformationTransformerTest {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(UnitInformationTransformerTest.class);
  EMISunitInformationResponseType emisResponse;
  gov.va.schema.emis.vdrdodadapter.v2.EMISunitInformationResponseType dodResponse;

  public void initializeResponses() {
    try {
      ClassPathResource emisSampleResponse =
          new ClassPathResource("/samples/UnitInformationEmisResponse.xml");
      ClassPathResource dodSampleResponse =
          new ClassPathResource("/samples/UnitInformationDodResponse.xml");
      InputStream emisInputStream = emisSampleResponse.getInputStream();
      InputStream dodInputStream = dodSampleResponse.getInputStream();
      emisResponse = JAXB.unmarshal(emisInputStream, EMISunitInformationResponseType.class);
      dodResponse =
          JAXB.unmarshal(
              dodInputStream,
              gov.va.schema.emis.vdrdodadapter.v2.EMISunitInformationResponseType.class);
    } catch (IOException e) {
      LOGGER.debug("File not found, check resources folder", e);
      emisResponse = null;
      dodResponse = null;
    }
  }

  @Test
  public void UnitInformationTransformersTest() {
    initializeResponses();
    EMISunitInformationResponseType transformedDodResponse =
        UnitInformationMapper.INSTANCE.mapEMISunitInformationResponseType(dodResponse);
    assertThat(transformedDodResponse).isEqualToComparingFieldByFieldRecursively(emisResponse);
  }
}
