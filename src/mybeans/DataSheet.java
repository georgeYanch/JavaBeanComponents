package mybeans;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.management.Attribute;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;

public class DataSheet implements Serializable {
    private Document doc;
    public DataSheet(Document doc) {
        super();
        this.doc = doc;
    }
    public DataSheet() {
        this(null);
    }
    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }
    public int numData() {
        return doc.getDocumentElement().getElementsByTagName("data").getLength();
    }
    public double getX(int pos) {
        String s = doc.getDocumentElement().getElementsByTagName("x").item(pos).getTextContent();
        return Double.parseDouble(s);
    }
    public void setX(int pos, double val) {
        doc.getDocumentElement().getElementsByTagName("x").item(pos).setTextContent(val+"");
    }
    public double getY(int pos) {
        String s = doc.getDocumentElement().getElementsByTagName("y").item(pos).getTextContent();
        return Double.parseDouble(s);
    }
    public void setDate(int pos, String val){
        doc.getDocumentElement().getElementsByTagName("dataPoint").item(pos).getAttributes().item(0).setNodeValue(val);
    }
    public Element getDataItem(int index){
        return (Element) doc.getDocumentElement().getChildNodes().item(index);
    }
    public void setY(int pos, double val) {
        doc.getDocumentElement().getElementsByTagName("y").item(pos).setTextContent(val+"");
    }
    public Element newElement(String date, double x, double y) {
        Element data = doc.createElement("dataPoint");
        Attr attr = doc.createAttribute("date");
        attr.setValue(date.trim());
        data.setAttributeNode(attr);
        Element elemX = doc.createElement("x");
        elemX.appendChild(doc.createTextNode(String.valueOf(x)));
        data.appendChild(elemX);
        Element elemY = doc.createElement("y");
        elemY.appendChild(doc.createTextNode(String.valueOf(y)));
        data.appendChild(elemY);
        return data;
    }
    public void addElement(Element data) {
        this.doc.getDocumentElement().appendChild(data);
    }
    public void removeElement(int pos) {
        Node el = doc.getDocumentElement().getElementsByTagName("dataPoint").item(pos);
        doc.getDocumentElement().removeChild(el);
    }
    public void insertElementAt(int pos, Node nd) {
        Node el = doc.getDocumentElement().getElementsByTagName("dataPoint").item(pos);
        doc.getDocumentElement().insertBefore(nd, el);
    }
    public void replaceElementAt(int pos, Node nd) {
        Node el = doc.getDocumentElement().getElementsByTagName("data").item(pos);
        doc.getDocumentElement().replaceChild(nd, el);
    }

}