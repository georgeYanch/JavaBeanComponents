package mybeans;

import mybeans.DataSheet;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Iterator;

public class DataSheetTableModel extends AbstractTableModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private int columnCount = 3;

    private int rowCount = 1;
    private DataSheet dataSheet = null;

    String[] columnNames = {"Date", "X Value", "Y Value"};


    public DataSheet getDataSheet() {
        return dataSheet;
    }

    // список
    private ArrayList<DataSheetChangeListener> listenerList = new ArrayList<DataSheetChangeListener>();

    // !
    private DataSheetChangeEvent event = new DataSheetChangeEvent(this);

    public void addDataSheetChangeListener(DataSheetChangeListener l) {
        listenerList.add(l);
    }

    public void removeDataSheetChangeListener(DataSheetChangeListener l) {
        listenerList.remove(l);
    }


    protected void fireDataSheetChange() {
        Iterator<DataSheetChangeListener> i = listenerList.iterator();
        while (i.hasNext())
            (i.next()).dataChanged(event);
    }


    public void setDataSheet(DataSheet dataSheet) {
        this.dataSheet = dataSheet;
        if(dataSheet != null || dataSheet.getDoc() != null) {
            rowCount = this.dataSheet.getDoc().getDocumentElement().getChildNodes().getLength();
            fireDataSheetChange();
        }
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) { // TODO Auto-generated method stub
        super.setValueAt(value, rowIndex, columnIndex);
        try {
            double d;
            if (dataSheet != null) {
                if (columnIndex == 0) {
                    dataSheet.getDataItem(rowIndex).setAttribute("date", (String) value);
                } else if (columnIndex == 1) {
                    d = Double.parseDouble((String) value);
                    dataSheet.getDataItem(rowIndex).setTextContent(String.valueOf(d));
                } else if (columnIndex == 2) {
                    d = Double.parseDouble((String) value);
                    dataSheet.getDataItem(rowIndex).setTextContent((String) value);
                }
            }
            fireDataSheetChange();
        } catch (Exception ex) {
        }

    }
    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex >= 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
    // TODO Auto-generated method stub
        if (dataSheet != null && dataSheet.getDoc().getDocumentElement().hasChildNodes() && dataSheet.getDataItem(rowIndex)!=null) {
            if (columnIndex == 0 && dataSheet.getDataItem(rowIndex)!=null)
                return dataSheet.getDataItem(rowIndex).getAttributeNode("date").getValue();

                else if(columnIndex == 1)
                    return Double.parseDouble(dataSheet.getDataItem(rowIndex).getElementsByTagName("x").item(0).getTextContent());

                else if (columnIndex == 2)
                    return Double.parseDouble(dataSheet.getDataItem(rowIndex).getElementsByTagName("y").item(0).getTextContent());
        }
        return null;
    }

    public void setRowCount(int rowCount) {
        if (rowCount >= 0)
        this.rowCount = rowCount;
    }
}

class DataSheetChangeEvent extends EventObject {

    private static final long serialVersionUID = 1L;
    public DataSheetChangeEvent(Object source) {
        super(source);
    }
}

interface DataSheetChangeListener extends EventListener {
    public void dataChanged(DataSheetChangeEvent e);
}

//------------------------------------------------------------------------

class DataSheetGraph extends JComponent implements Serializable {
    private static final long serialVersionUID = 1L;


    private DataSheet dataSheet = null;
    private boolean isConnected;

    private int deltaX;
    private int deltaY;

    transient private Color color;

    public DataSheet getDataSheet() {
        return dataSheet;
    }

    public void setDataSheet(DataSheet dataSheet) {
        this.dataSheet = dataSheet;

    }

    public boolean isConnected() {
        return isConnected;

    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
        repaint();
    }

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }

    public void setDeltaX(int deltaX) {
        this.deltaX = deltaX;
        repaint();
    }

    public void setDeltaY(int deltaY) {
        this.deltaY = deltaY;
        repaint();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        repaint();
    }

    public DataSheetGraph() {
        super();
        initialize();
    }

    private void initialize() {
        isConnected = false;

        deltaX = 5;
        deltaY = 5;
        color = Color.red;

        this.setSize(300, 400);
    }

    private double minX() {

        double result = 0;

        if (dataSheet != null) {
            int size = dataSheet.getDoc().getDocumentElement().getChildNodes().getLength();
            for (int i = 0; i < size; i++)
                if (Double.parseDouble(dataSheet.getDataItem(i).getElementsByTagName("x").item(0).getTextContent()) < result)
                    result = Double.parseDouble(dataSheet.getDataItem(i).getElementsByTagName("x").item(0).getTextContent());
        }
        return result;
    }

    private double maxX() {
        double result = 0;

        if (dataSheet != null) {
            int size = dataSheet.getDoc().getDocumentElement().getChildNodes().getLength();
            for (int i = 0; i < size; i++)
                if (Double.parseDouble(dataSheet.getDataItem(i).getElementsByTagName("x").item(0).getTextContent()) > result)
                    result = Double.parseDouble(dataSheet.getDataItem(i).getElementsByTagName("x").item(0).getTextContent());
        }
        return result;
    }

    private double minY() {
        double result = 0;

        if (dataSheet != null) {

            int size = dataSheet.getDoc().getDocumentElement().getChildNodes().getLength();
            for (int i = 0; i < size; i++)
                if (Double.parseDouble(dataSheet.getDataItem(i).getElementsByTagName("y").item(0).getTextContent()) < result)
                    result = Double.parseDouble(dataSheet.getDataItem(i).getElementsByTagName("y").item(0).getTextContent());
        }
        return result;
    }

    private double maxY() {
        double result = 0;

        if (dataSheet != null) {
            int size = dataSheet.getDoc().getDocumentElement().getChildNodes().getLength();
            for (int i = 0; i < size; i++)

                if (Double.parseDouble(dataSheet.getDataItem(i).getElementsByTagName("y").item(0).getTextContent()) > result)
                    result = Double.parseDouble(dataSheet.getDataItem(i).getElementsByTagName("y").item(0).getTextContent());
        }
        return result;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        showGraph(g2);
    }

    public void showGraph(Graphics2D gr) {

        double xMin, xMax, yMin, yMax;
        double width = getWidth();
        double height = getHeight();

        xMin = minX() - deltaX;
        xMax = maxX() + deltaX;

        yMin = minY() - deltaY;
        yMax = maxY() + deltaY;

        double xScale = width / (xMax - xMin);
        double yScale = height / (yMax - yMin);
        double x0 = -xMin * xScale;
        double y0 = yMax * xScale;

        Paint oldColor = gr.getPaint();
        gr.setPaint(Color.WHITE);
        gr.fill(new Rectangle2D.Double(0.0, 0.0, width, height));

        Stroke oldStroke = gr.getStroke();
        Font oldFont = gr.getFont();


        float[] dashPattern = {10, 10};
        gr.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0));
        gr.setFont(new Font("Serif", Font.BOLD, 14));

        double xStep = 1;
        for (double dx = xStep; dx < xMax; dx += xStep) {
            double x = x0 + dx * xScale;
            gr.setPaint(Color.LIGHT_GRAY);
            gr.draw(new Line2D.Double(x, 0, x, height));
            gr.setPaint(Color.BLACK);
            gr.drawString(Math.round(dx / xStep) * xStep + "", (int) x + 2, 10);
        }

        for (double dx = -xStep; dx >= xMin; dx -= xStep) {
            double x = x0 + dx * xScale;
            gr.setPaint(Color.LIGHT_GRAY);
            gr.draw(new Line2D.Double(x, 0, x, height));
            gr.setPaint(Color.BLACK);
            gr.drawString(Math.round(dx / xStep) * xStep + "", (int) x + 2, 10);
        }


        double yStep = 1;
        for (double dy = yStep; dy < yMax; dy += yStep) {
            double y = y0 - dy * yScale;
            gr.setPaint(Color.LIGHT_GRAY);
            gr.draw(new Line2D.Double(0, y, width, y));
            gr.setPaint(Color.BLACK);
            gr.drawString(Math.round(dy / yStep) * yStep + "", 2, (int) y - 2);
        }

        for (double dy = -yStep; dy >= yMin; dy -= yStep) {
            double y = y0 - dy * yScale;
            gr.setPaint(Color.LIGHT_GRAY);
            gr.draw(new Line2D.Double(0, y, width, y));
            gr.setPaint(Color.BLACK);
            gr.drawString(Math.round(dy / yStep) * yStep + "", 2, (int) y - 2);
        }

        gr.setPaint(Color.BLACK);
        gr.setStroke(new BasicStroke(3.0f));
        gr.draw(new Line2D.Double(x0, 0, x0, height));
        gr.draw(new Line2D.Double(0, y0, width, y0));
        gr.drawString("X", (int) width - 10, (int) y0 - 2);
        gr.drawString("Y", (int) x0 + 2, 10);

        if (!isConnected) {

            for (int i = 0; i < dataSheet.getDoc().getDocumentElement().getChildNodes().getLength(); i++) {
                double x = x0 + (Double.parseDouble(dataSheet.getDataItem(i).getElementsByTagName("x").item(0).getTextContent()) * xScale);
                double y = y0 - (Double.parseDouble(dataSheet.getDataItem(i).getElementsByTagName("y").item(0).getTextContent()) * yScale);
                gr.setColor(Color.white);
                gr.fillOval((int) (x - 5 / 2), (int) (y - 5 / 2), 5, 5);
                gr.setColor(color);
                gr.drawOval((int) (x - 5 / 2), (int) (y - 5 / 2), 5, 5);
            }
        } else {

            gr.setPaint(color);
            gr.setStroke(new BasicStroke(2.0f));
            double xOld = x0 + Double.parseDouble(dataSheet.getDataItem(0).getElementsByTagName("x").item(0).getTextContent()) * xScale;
            double yOld = y0 - Double.parseDouble(dataSheet.getDataItem(0).getElementsByTagName("x").item(0).getTextContent()) * yScale;
            for (int i = 1; i < dataSheet.getDoc().getDocumentElement().getChildNodes().getLength(); i++) {

                double x = x0 + Double.parseDouble(dataSheet.getDataItem(i).getElementsByTagName("x").item(0).getTextContent()) * xScale;
                double y = y0 - Double.parseDouble(dataSheet.getDataItem(i).getElementsByTagName("y").item(0).getTextContent()) * yScale;
                gr.draw(new Line2D.Double(xOld, yOld, (double) x, y));
                xOld = x;
                yOld = y;

            }
        }

        gr.setPaint(oldColor);
        gr.setStroke(oldStroke);
        gr.setFont(oldFont);
    }


    public class DataSheetGraphBeanInfo extends SimpleBeanInfo implements Serializable {
        private PropertyDescriptor[] propertyDescriptors;

        public DataSheetGraphBeanInfo() {
            try {

                propertyDescriptors = new PropertyDescriptor[]{
                        new PropertyDescriptor("color", DataSheetGraph.class),
                        new PropertyDescriptor("filled", DataSheetGraph.class),
                        new PropertyDescriptor("deltaX", DataSheetGraph.class),
                        new PropertyDescriptor("deltaY", DataSheetGraph.class)
                };
            } catch (IntrospectionException e) {
            }
        }
        @Override
        public PropertyDescriptor[] getPropertyDescriptors() {
            return propertyDescriptors;
        }

    }
}

