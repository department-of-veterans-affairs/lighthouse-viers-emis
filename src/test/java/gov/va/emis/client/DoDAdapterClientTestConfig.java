package gov.va.emis.client;

import gov.va.dod.DoDAdapterClient;
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
