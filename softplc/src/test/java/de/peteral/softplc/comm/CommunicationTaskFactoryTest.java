package de.peteral.softplc.comm;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("javadoc")
public class CommunicationTaskFactoryTest {

	private CommunicationTaskFactory factory;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		factory = new CommunicationTaskFactory();
	}
}
