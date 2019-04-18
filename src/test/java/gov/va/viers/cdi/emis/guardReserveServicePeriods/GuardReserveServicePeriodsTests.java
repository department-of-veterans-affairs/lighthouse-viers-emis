package gov.va.viers.cdi.emis.guardReserveServicePeriods;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.dod.DoDAdapterClient;
import gov.va.emis.client.MilitaryInfoClient;
import gov.va.schema.emis.vdrdodadapter.v2.EMISguardReserveServicePeriodsResponseType;
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
public class GuardReserveServicePeriodsTests {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(GuardReserveServicePeriodsTests.class);

  @Autowired private MilitaryInfoClient emisClient;

  @Autowired private DoDAdapterClient dodClient;

  private void initMock(String sampleResponsePath, String edipi) {
    EMISguardReserveServicePeriodsResponseType dodResponse;
    try {
      // Grabbing from classpath
      ClassPathResource sampleResponse =
          new ClassPathResource("/vadirResponses/" + sampleResponsePath);
      InputStream inputStream = sampleResponse.getInputStream();
      dodResponse = JAXB.unmarshal(inputStream, EMISguardReserveServicePeriodsResponseType.class);
    } catch (IOException e) {
      LOGGER.debug("Could not find " + sampleResponsePath + ", check resources folder: " + e);
      dodResponse = null;
    }
    // Have to wrap in JAXBElement to match method signature
    ObjectFactory objectFactory = new ObjectFactory();
    JAXBElement<EMISguardReserveServicePeriodsResponseType> jaxbDodResponse =
        objectFactory.createEMISguardReserveServicePeriodsResponse(dodResponse);
    Mockito.doReturn(jaxbDodResponse).when(dodClient).getGuardReserveServicePeriodsResponse(edipi);
  }

  @Test
  public void getGuardReserveServicePeriodsEmptyResponse() {
    initMock("emptyVadirResponse_GuardReserveSvc.xml", "1234567890");
    JAXBElement<gov.va.viers.cdi.emis.requestresponse.v2.EMISguardReserveServicePeriodsResponseType>
        response = emisClient.getGuardReserveServicePeriodsResponse("1234567890", "EDIPI", false);

    assertThat(
        Optional.ofNullable(response)
            .map(r -> r.getValue())
            .map(v -> v.getGuardReserveServicePeriods())
            .orElse(null)
            .isEmpty());
  }

  @Test
  public void getGuardReserveServicePeriodsSuccess() {
    initMock("exampleSuccessVadirResponse_GuardReserveSvc.xml", "6001010072");
    JAXBElement<gov.va.viers.cdi.emis.requestresponse.v2.EMISguardReserveServicePeriodsResponseType>
        response = emisClient.getGuardReserveServicePeriodsResponse("6001010072", "EDIPI", false);

    assertThat(
            Optional.ofNullable(response)
                .map(r -> r.getValue())
                .map(v -> v.getGuardReserveServicePeriods())
                .flatMap(m -> m.stream().findFirst())
                .map(e -> e.getGuardReserveServicePeriodsData())
                .map(s -> s.getGuardReservePeriodProjectCode())
                .orElse(null))
        .isEqualTo("A22");
  }
}
