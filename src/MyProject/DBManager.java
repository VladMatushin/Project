package MyProject;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class DBManager {
    private static final String URL = "jdbc:mysql://localhost:3306/project?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "root";

    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    private static final String selectAllSerials = "select * from serials;";
    private static final String selectAllUsers = "select * from users;";
    private static final String selectSerials = "select * from views where ulogin like '%s';";
    private static final String insertSerial = "insert into serials value ('%s',%d,%d,0,0,0);";
    private static final String insertUser = "insert into users value ('%s','%s');";
    private static final String insertView = "insert into views value ('%s','%s',1,1,0);";
    private static final String updateUser = "update users set `password` = '%s' where login like '%s';";
    private static final String updateView = "update views set uSesaon = %d,uEpisod = %d,uRating = %d where ulogin like '%s' and sname like '%s';";
    private static final String updateSerials = "update serials set numberOfSeasons = %d, numberOfEpisodes = %d, sumOfRating = %d, numberOfMarks = %d, numberOfViews = %d where `name` like '%s';";



    public void NewSerial(Serial serial) throws SQLException{
        UpdateBase();
        stmt.executeUpdate(String.format(insertSerial,serial.getName(),serial.getNumberOfSeasons(),serial.getNumberOfEpisodes()));
        CloseUpdate();
    }

    public void ChangeSerials(ArrayList<Serial> serials) throws  SQLException{
        if (!serials.isEmpty()){
            UpdateBase();
            for (Serial serial:serials) {
                UpdateBase();
                stmt.executeUpdate(String.format(updateSerials,serial.getNumberOfSeasons(),serial.getNumberOfEpisodes(),serial.getSumOfRating(),serial.getNumberOfMarks(),serial.getNumberOfViews(),serial.getName()));
                CloseUpdate();
            }
        }
    }

    public ArrayList<Serial> GetAllSerials() throws SQLException {
        ArrayList<Serial> serials = new ArrayList<>();
        ConnectAll(selectAllSerials);

        while (rs.next()) {
            Serial serial = new Serial(rs.getString("name"),rs.getInt("numberOfSeasons"),rs.getInt("numberOfEpisodes"),rs.getInt("sumOfRating"),rs.getInt("numberOfMarks"),rs.getInt("numberOfViews"));
            serials.add(serial);
        }

        CloseAll();
        return serials;
    }

    public void NewUser (User user) throws  SQLException{
        UpdateBase();
        stmt.executeUpdate(String.format(insertUser,user.getLogin(),user.getPassword()));
        CloseUpdate();
    }

    public void ChangeUser(User user) throws  SQLException{
        UpdateBase();
        stmt.executeUpdate(String.format(updateUser,user.getPassword(),user.getLogin()));
        CloseUpdate();
    }

    public User GetMyUser (String login,String password,ArrayList<Serial> serials) throws SQLException {
        ArrayList<Serial> myserials = new ArrayList<>();
        HashMap<String, ArrayList<Integer>> viewedSerials = new HashMap<>();
        ConnectAll(String.format(selectSerials,login));
        while (rs.next()) {
            for (Serial serial:serials) {
                if (rs.getString("sname").equals(serial.getName())){
                    myserials.add(serial);
                    ArrayList<Integer> arr = new ArrayList<>();
                    arr.add(0,rs.getInt("uSesaon"));
                    arr.add(1,rs.getInt("uEpisod"));
                    arr.add(2,rs.getInt("uRating"));
                    viewedSerials.put(rs.getString("sname"),arr);
                    break;
                }
            }

        }
        User user = new User(login,password,myserials,viewedSerials);
        CloseAll();
        return user;
    }

    public HashMap<String,String> GetAllUsers() throws  SQLException{
        HashMap<String,String> users = new HashMap<>();
        ConnectAll(selectAllUsers);
        while (rs.next()) {
            users.put(rs.getString(1),rs.getString(2));
        }
        CloseAll();
        return  users;
    }

    public void  ChangeView(User user) throws SQLException{
        HashMap<String, ArrayList<Integer>> viewedSerials = user.getViewedSerials();
        Set<String> keys = viewedSerials.keySet();
        for (String sname:keys) {
            ArrayList<Integer> arr = viewedSerials.get(sname);
            UpdateBase();
            stmt.executeUpdate(String.format(updateView,arr.get(0),arr.get(1),arr.get(2),user.getLogin(),sname));
            CloseUpdate();
        }

    }

    public void NewView (String login, String sname) throws SQLException{
        UpdateBase();
        stmt.executeUpdate(String.format(insertView,login,sname));
        CloseUpdate();
    }

    private void ConnectAll(String query) throws SQLException{
        con = DriverManager.getConnection(URL, USER, PASS);
        stmt = con.createStatement();
        rs = stmt.executeQuery(query);
    }

    private void UpdateBase() throws SQLException{
        con = DriverManager.getConnection(URL, USER, PASS);
        stmt = con.createStatement();
    }

    private void CloseUpdate()throws  SQLException{
        con.close();
        stmt.close();
    }



    private void CloseAll() throws SQLException{
        con.close();
        stmt.close();
        rs.close();
    }
}
