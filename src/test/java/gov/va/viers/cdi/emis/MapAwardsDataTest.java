package gov.va.viers.cdi.emis;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.EMISMapper;
import gov.va.viers.cdi.emis.commonservice.v2.AwardsData;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.Test;

public class MapAwardsDataTest {
  private static DatatypeFactory datatypeFactory;

  private static void datatypeFactoryInit(){
    try {
      datatypeFactory = DatatypeFactory.newInstance();
    } catch (DatatypeConfigurationException e) {
      throw new RuntimeException("Init Error!", e);
    }
  }

  @Test
  public void mapAwardsDataTest() {
    datatypeFactoryInit();
    gov.va.schema.emis.vdrdodadapter.v2.AwardsData dodAwardsData = new gov.va.schema.emis.vdrdodadapter.v2.AwardsData();
    dodAwardsData.setAwardCode("code it up");
    dodAwardsData.setAwardDescription("good job coding it up");
    XMLGregorianCalendar date = datatypeFactory.newXMLGregorianCalendar();
    date.setDay(1);
    date.setMonth(1);
    date.setYear(1999);
    dodAwardsData.setAwardDate(date);
    AwardsData emisAwardsData = EMISMapper.INSTANCE.mapAwardsData(dodAwardsData);

    assertThat(emisAwardsData.getAwardCode()).isEqualTo("code it up");
    assertThat(emisAwardsData.getAwardDate()).isEqualTo(date);
    assertThat(emisAwardsData.getAwardDescription()).isEqualTo("good job coding it up");

}

}
