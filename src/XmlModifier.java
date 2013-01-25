import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;


public class XmlModifier extends AppDeployerRunnable {
	private String file;
	private String element;
	private String attribute;
	private String value;
	private XmlChangeListener listener;

	private XmlModifier() {}

	public static class Builder {
		protected XmlModifier xmlModifier = new XmlModifier();

		public XmlModifier build() {
			return xmlModifier;
		}

		public Builder withFile(String file) {
			xmlModifier.file = file;
			return this;
		}

		public Builder withElement(String element) {
			xmlModifier.element = element;
			return this;
		}

		public Builder withAttribute(String attribute) {
			xmlModifier.attribute = attribute;
			return this;
		}

		public Builder withValue(String value) {
			xmlModifier.value = value;
			return this;
		}

		public Builder withCanceler(CancelsDeployement canceler) {
			xmlModifier.canceler = canceler;
			return this;
		}

		public Builder withConsole(PrintsMessages console) {
			xmlModifier.console = console;
			return this;
		}

		public Builder withXmlChangeListener(XmlChangeListener listener) {
			xmlModifier.listener = listener;
			return this;
		}
	}

	@Override
	public void run() {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(file);

			Node elementNode = doc.getElementsByTagName(element).item(0);
			Node attributeNode = elementNode.getAttributes().getNamedItem(attribute);
			String originalValue = attributeNode.getTextContent();
			attributeNode.setTextContent(value);


			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(file));
			transformer.transform(source, result);

			console.printlnToConsole("Changed " + attribute + " attribute of the " + element + " element in " + file + " from " + originalValue + " to " + value);
			console.printlnToConsole();
			if (listener != null) {
				listener.onXmlChange(originalValue);
			}
		} catch (Exception e) {
			console.printlnToConsole("ERROR: Failed to change " + attribute + " attribute of the " + element + " element in " + file + " to " + value);
			cancelDeployement("Command Failed:" + e);
		}
	}
}
