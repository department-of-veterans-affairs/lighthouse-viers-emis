package gov.va.viers.cdi.emis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan({"gov.va."})
public class SpringWsApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringWsApplication.class, args);
  }
}
