import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

public class DemoForm {
    private JPanel formPanel;
    private JTextField databaseNameInput;
    private JTextField usernameInput;
    private JTextField passwordInput;
    private JButton connectButton;
    private JTextField status;
    private JLabel title;
    private JLabel databaseNameLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField queryInput;
    private JButton runQueryButton;
    private JLabel queryLabel;
    private JScrollPane queryResultPanel;

    private static JFrame frame;

    private static DatabaseServices dbServices = new DatabaseServices();

    public DemoForm() {
        status.setBorder(null);
        status.setBackground(new Color(0, 255, 0, 0));
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dbServices.connectDatabase(databaseNameInput.getText(), usernameInput.getText(), passwordInput.getText());
                    status.setText("Database Status: CONNECTED\n");
                    status.setForeground(new Color(0, 100, 0));
                } catch(Exception exception) {
                    status.setText("Database Status: FAILED\n");
                    status.setForeground(Color.red);
//                    JOptionPane.showMessageDialog(null, "Failed to connect to database, please check again!");
                }
            }
        });
        runQueryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ResultSet resultSet = dbServices.runQuery(queryInput.getText());
                    JTable queryResultTable = new JTable(buildTableModel(resultSet));
                    queryResultPanel.getViewport().add(queryResultTable);
//                    JOptionPane.showMessageDialog(null, new JScrollPane(queryResultTable));
                    //queryResultTable.setVisible(true);
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Failed to execute query, please check again!");
                }
            }
        });
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(WindowEvent winEvt) {
                dbServices.disconnectDatabase();
            }
        });
        connectButton.addKeyListener(new KeyAdapter() {
        });
    }

    private TableModel buildTableModel(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (resultSet.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(resultSet.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }

    public static void main(String[] args) {
        frame = new JFrame("DemoForm");
        frame.setContentPane(new DemoForm().formPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
