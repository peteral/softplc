package de.peteral.softplc.plc;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import de.peteral.softplc.model.Plc;

@SuppressWarnings("javadoc")
public class PlcFactoryTest {
	private PlcFactory factory;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		factory = new PlcFactory();
	}

	@Test
	public void create_ValidFile_ReturnsValidPlcInstance() {
		Plc plc = factory.create("./src/test/resources/config.xml");

		assertEquals(2, plc.getCpuCount());
		// TODO more detailled test of created PLC
	}
}
