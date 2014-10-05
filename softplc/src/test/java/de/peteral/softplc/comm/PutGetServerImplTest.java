package de.peteral.softplc.comm;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.peteral.softplc.model.Plc;

@SuppressWarnings("javadoc")
public class PutGetServerImplTest {

	@Mock
	private Plc plc;
	private PutGetServerImpl server;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		server = new PutGetServerImpl();
	}
}
