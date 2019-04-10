package gov.va.viers.cdi.emis.client;

import gov.va.schema.emis.vdrdodadapter.v2.DoDAdapterClient;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class DoDAdapterClientTestConfig {
  @Bean
  @Primary
  public DoDAdapterClient dodAdapterClient() {
    return Mockito.mock(DoDAdapterClient.class);
  }
}
