package gov.va.viers.cdi.emis;


import gov.va.viers.cdi.emis.client.MilitaryInfoClient;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryServiceEligibilityResponseType;
import java.util.Optional;
import java.util.stream.Stream;
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
    public void getMilitaryServiceEligibilitySuccess() {
        JAXBElement<gov.va.viers.cdi.emis.requestresponse.v2.EMISmilitaryServiceEligibilityResponseType> response =
            client.getMilitaryServiceEligibilityResponse("6001", "EDIPI");
        assertThat(Optional.ofNullable(response).map(r->r.getValue()).map(v->v.getESSError()).orElse(null)).isNotNull();
    }
}
