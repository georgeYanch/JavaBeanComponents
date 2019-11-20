package mybeans;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

import static com.sun.deploy.uitoolkit.ToolkitStore.dispose;

public class DataSheetTable extends JPanel implements Serializable {
    JTable table;
    DataSheetTableModel tableModel;
    DataSheetGraph dataSheetGraph = new DataSheetGraph();
    JButton addButton = new JButton("Add");
    JButton delButton= new JButton("Delete");
    JButton exitButton = new JButton("Exit");
    JButton clearButton = new JButton("Clear");
    JButton saveButton = new JButton("Save");
    JButton readButton = new JButton("Read");

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                try {
                    new DataSheetTable().createGUI();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (TransformerException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public DataSheetTable(){
        table = new JTable();
        tableModel = new DataSheetTableModel();
        table.setModel(tableModel);
    }
    public DataSheetTableModel getTableModel() {
        return tableModel;
    }

    public void setTableModel(DataSheetTableModel tableModel) {
        this.tableModel = tableModel;
        table.revalidate();
    }

    public void revalidate() {
        if (table != null) table.revalidate();
    }

    public void createGUI() throws ParserConfigurationException, TransformerException, SAXException, IOException {
        JFrame frame = new JFrame("Table");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        final DataSheet[] dataSheet = new DataSheet[1];//{DOM.createDOM("points.xml")};
        final JFileChooser fileChooser = new JFileChooser();

        if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(null)) {
            String fileName = fileChooser.getSelectedFile().getPath();
            try {
                dataSheet[0] = DOM.createDOM(fileName);
            } catch (ParserConfigurationException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (SAXException e1) {
                e1.printStackTrace();
            } catch (TransformerException e1) {
                e1.printStackTrace();
            }

            getTableModel().setDataSheet(dataSheet[0]);
            revalidate();

            dataSheetGraph.setDataSheet(dataSheet[0]);
        }

        tableModel.setDataSheet(dataSheet[0]);
        JScrollPane scrollPane = new JScrollPane(table);

        addButton.addActionListener(e -> {
            if(tableModel.getRowCount() > 1) {
                tableModel.setRowCount(tableModel.getRowCount() + 1);
                tableModel.getDataSheet().addElement(tableModel.getDataSheet().newElement("20.24.2019",
                        Math.random() * 15, (Math.random() * 15 + Math.random() * 1.05)));
            }else {
                tableModel.getDataSheet().addElement(tableModel.getDataSheet().newElement("20.24.2019",
                        Math.random() * 15, (Math.random() * 15 + Math.random() * 1.05)));
                tableModel.setRowCount(tableModel.getRowCount() + 1);

            }

            table.revalidate();
            tableModel.fireDataSheetChange();
        });
        delButton.addActionListener(e -> {
            if (tableModel.getRowCount() > 1) {
                tableModel.setRowCount(tableModel.getRowCount() - 1);
                tableModel.getDataSheet().removeElement(tableModel.getDataSheet().
                        getDoc().getDocumentElement().getChildNodes().getLength()-1);
                table.revalidate();
                tableModel.fireDataSheetChange();
            } else {
                tableModel.getDataSheet().removeElement(0);
                table.revalidate();
                table.repaint();
                tableModel.fireDataSheetChange();
            }
        });

        dataSheetGraph.setDataSheet(dataSheet[0]);
        getTableModel().setDataSheet(dataSheet[0]);
        getTableModel().addDataSheetChangeListener(
                new DataSheetChangeListener() {

                    public void dataChanged(DataSheetChangeEvent e) {
                        dataSheetGraph.revalidate();
                        dataSheetGraph.repaint();
                    }
                });



        fileChooser.setCurrentDirectory(new java.io.File("."));
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                try {
                    frame.dispose();
                    dispose();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        clearButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                while(true) {
                    if (tableModel.getRowCount() > 1) {
                        tableModel.setRowCount(tableModel.getRowCount() - 1);
                        tableModel.getDataSheet().removeElement(tableModel.getDataSheet().
                                getDoc().getDocumentElement().getChildNodes().getLength() - 1);
                        table.revalidate();
                        tableModel.fireDataSheetChange();
                    } else {
                        tableModel.getDataSheet().removeElement(0);
                        table.revalidate();
                        table.repaint();
                        tableModel.fireDataSheetChange();
                    break;
                }
                }
                getTableModel().setDataSheet(dataSheet[0]);

                revalidate();
                dataSheetGraph.setDataSheet(dataSheet[0]);

            }
        });
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(null)) {
                    String fileName = fileChooser.getSelectedFile().getPath();
                    try {
                        DOM.saveToXml(dataSheet[0].getDoc(), fileName);
                    } catch (TransformerException e1) {
                        e1.printStackTrace();
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(null, "File " + fileName.trim() + " saved!",
                                                    "Результаты сохранены", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });
        readButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(null)) {
                    String fileName = fileChooser.getSelectedFile().getPath();
                    try {
                        dataSheet[0] = DOM.createDOM(fileName);
                    } catch (ParserConfigurationException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (SAXException e1) {
                        e1.printStackTrace();
                    } catch (TransformerException e1) {
                        e1.printStackTrace();
                    }

                    getTableModel().setDataSheet(dataSheet[0]);
                    revalidate();

                    dataSheetGraph.setDataSheet(dataSheet[0]);
                }

            }
        });

        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();


        scrollPane.add(addButton);
        scrollPane.add(delButton);
        panel3.add(scrollPane, BorderLayout.WEST);

        panel2.add(addButton, BorderLayout.WEST);
        panel2.add(delButton, BorderLayout.EAST);

        panel.add(panel3, BorderLayout.NORTH);
        panel.add(panel2, BorderLayout.SOUTH);

        JPanel bp = new JPanel();

        //frame.getContentPane().add(scrollPane);
        bp.add(readButton, BorderLayout.WEST);
        bp.add(saveButton, BorderLayout.EAST);
        bp.add(exitButton, BorderLayout.AFTER_LAST_LINE);
        bp.add(clearButton, BorderLayout.BEFORE_FIRST_LINE);
        frame.getContentPane().add(bp, BorderLayout.SOUTH);

        frame.getContentPane().add(dataSheetGraph, BorderLayout.CENTER);
        frame.getContentPane().add(panel, BorderLayout.WEST);


        frame.setPreferredSize(new Dimension(1000, 400));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}