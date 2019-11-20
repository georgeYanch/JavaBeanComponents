package mybeans;

import com.sun.xml.internal.ws.org.objectweb.asm.Attribute;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;

public class DOM implements Serializable {
    public static DataSheet createDOM(String fileName) throws ParserConfigurationException, IOException, SAXException, TransformerException {

        Schema schema = null;
        try {
            String xsd = "D:\\Софт\\IntelliJ IDEA\\IntelliJ IDEA 2018.3.5\\XMLtoObject\\src\\XSD.xsd";
            String dtd = "DTD.dtd";
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            schema = schemaFactory.newSchema(new StreamSource(new File(xsd)));
            Validator validator = schema.newValidator();
            StreamSource xmlFile = new StreamSource(new File(fileName));
            validator.validate(xmlFile);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//=============================Task 5=============================================
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setSchema(schema);
        factory.setValidating(true);                                        //setting validaion
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();

        ErrorHandler errorHandler = new ErrorHandler() {
            @Override
            public void warning(SAXParseException e) throws SAXException { System.out.println(e.getMessage()); }
            @Override
            public void error(SAXParseException e) throws SAXException {  System.out.println(e.getMessage()); }
            @Override
            public void fatalError(SAXParseException e) throws SAXException {  System.out.println(e.getMessage()); }
        };

        builder.setErrorHandler(errorHandler);
        Document doc = builder.parse(new File(fileName));

        Element element = doc.getDocumentElement();
        //System.out.println(element.getNodeName());
        //printElements(element.getChildNodes());

        DataSheet dataSheet = new DataSheet(doc);
//        Element e = dataSheet.newElement("06.06.2000", 8.0,8.45);
//        dataSheet.insertElementAt(doc.getElementsByTagName("dataPoint").getLength(), e);
//        for(int i = 0; i < doc.getElementsByTagName("dataPoint").getLength()+1; i++) {
//            Element elem = doc.createElement("number");
//            elem.setTextContent(String.valueOf(i + 1));
//            dataSheet.insertElementAt(i, elem);
//        }
        return dataSheet;
    }

    static void saveToXml(Document doc, String fileName) throws TransformerException, FileNotFoundException {
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = transFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        File newXMLFile = new File(fileName);
        FileOutputStream fos = new FileOutputStream(newXMLFile);
        StreamResult result = new StreamResult(fos);
        transformer.transform(source, result);
    }

    static void printElements(NodeList nodeList){
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if(node instanceof Element) {
                if((node.hasAttributes())) {
                    System.out.println(node.getNodeName() + " " + ((Element) node).getAttribute("date"));

                }else if(node.getNodeName().equals("x") || node.getNodeName().equals("y")) {
                    System.out.println(node.getNodeName() + ": " + node.getTextContent());
                }else System.out.println(node.getNodeName());
            }
            if(nodeList.item(i).hasChildNodes()){
                printElements(node.getChildNodes());
            }
        }
    }
}
