package gov.va.viers.cdi.emis;


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
        assertThat(client.getMilitaryServiceEligibilityResponse("6001010072", "EDIPI").getValue().getMilitaryServiceEligibility()).isNotEmpty();
    }
}
