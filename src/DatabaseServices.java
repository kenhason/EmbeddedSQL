import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DatabaseServices {
    private Connection con;
    public DatabaseServices() {
        con = null;
    }
    protected void connectDatabase(String databaseName, String username, String password) throws Exception {
            if (databaseName.isEmpty() || username.isEmpty())
                throw new Exception();
            Class.forName("com.mysql.jdbc.Driver");
            String conString = "jdbc:mysql://localhost:3306/" + databaseName;
            con= DriverManager.getConnection(conString , username, password);
    }

    protected void disconnectDatabase() {
        try {
            con.close();
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    protected ResultSet runQuery(String query) throws SQLException {
        if (!con.isClosed()) {
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery(query);
            return rs;
        } else {
            throw new SQLException("Can not execute query!");
        }
    }

//    public List<Set<String>> transformToTransactionList(List<List<String>> dataSet) {
//        List<Set<String>> table = new ArrayList<Set<String>>();
//        List<String> idList = getIdListFromTable(dataSet);
//
//        idList.forEach(entry -> {
//            table.add(new HashSet<>());
//        });
//
//        dataSet.forEach(row -> {
//            table.get(idList.indexOf(row.get(0))).add(row.get(1));
//        });
//        return table;
//    }

    private List<String> getIdListFromTable(List<List<String>> dataSet) {
        List<String> idList = new ArrayList<>();
        dataSet.forEach(row -> {
            if (!idList.contains(row.get(0)))
                idList.add(row.get(0));
        });
        return idList;
    }
}
