package gov.va.viers.cdi.emis;


import javax.xml.bind.JAXBElement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MilitaryInfoTests {
    @Autowired
    private MilitaryInfoClient client;

    @Test
    public void tryIt() {
        JAXBElement<gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryServiceEligibilityResponseType> response =
            client.getMilitaryServiceEligibilityResponse("6001", "EDIPI");
        assertThat(response.getValue().getMilitaryServiceEligibility().get(0).getVeteranStatus().getPersonFirstName()).isEqualTo("EMIS_FIRST_NM_74");
    }
}
