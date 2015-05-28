package de.peteral.softplc.plc;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import de.peteral.softplc.model.Plc;
import de.peteral.softplc.plc.PlcFactory;
import de.peteral.softplc.plc.PlcTransformer;

@SuppressWarnings("javadoc")
public class PlcTransformerTest {
	private static final String SOURCE = "./src/test/resources/config.xml";
	private PlcTransformer transformer;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		transformer = new PlcTransformer();
	}

	@Test
	public void transform_ValidPlc_CreatesCorrectDocument()
			throws TransformerException, IOException,
			ParserConfigurationException, SAXException {
		Plc plc = new PlcFactory().create(SOURCE);

		Document doc = transformer.transform(plc);

		byte[] contents = Files.readAllBytes(Paths.get(SOURCE));
		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document originalDocument = builder.parse(new ByteArrayInputStream(
				contents));

		assertEquals(docToString(originalDocument), docToString(doc));
	}

	private String docToString(Document doc)
			throws TransformerFactoryConfigurationError,
			TransformerConfigurationException, TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "softplc.dtd");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "4");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(doc), new StreamResult(writer));
		return writer.toString().replaceAll("\t", "    ")
				.replaceAll("^\\s*\\n", "");
	}
}
