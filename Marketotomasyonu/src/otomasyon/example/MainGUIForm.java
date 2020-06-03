package otomasyon.example;

import jdk.swing.interop.DragSourceContextWrapper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class MainGUIForm extends JFrame {
    private JPanel jpanel;
    private JTextField itemnametext;
    private JTextField itempricetext;
    private JTextField itemnumbertext;
    private JLabel itemnamelabel;
    private JLabel itempricelabel;
    private JLabel itemnumberlabel;
    private JButton deleteitem;
    private JButton saveitem;
    JList<String> list1;
    private JButton additemnumberbutton;
    private JButton subtractitemnamebutton;
    private JButton listeyiGosterButton;
    static ArrayList<String> itemnames;
    static ArrayList<String> itemprices;
    static ArrayList<String> itemnumbers;


    public MainGUIForm() {

        list1.getSelectionModel().addListSelectionListener(e -> {
            try {
                String namethislist = list1.getSelectedValue();
                ResultSet rs = null;
                Connection connection = this.connect();
                String sqlstring = "select * from items where name = " + "'" + namethislist + "'" ;
                Statement statement = null;
                statement = connection.createStatement();
                rs = statement.executeQuery(sqlstring);

                itemnametext.setText(rs.getString("name"));
                itempricetext.setText(rs.getString("price"));
                itemnumbertext.setText(rs.getString("number"));

                    rs.close();
            }catch (Exception asd){
                asd.printStackTrace();
            }
        });
        saveitem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = itemnametext.getText().toString().toUpperCase();
                String price = itempricetext.getText().toString();
                String number = itemnumbertext.getText().toString();
                try{
                    if (!name.matches("")&& !price.matches("")&& !number.matches("")){
                        deletedata(name);
                        insert(name,price,number);
                        getdata();
                        itemnametext.setText("");
                        itemnumbertext.setText("");
                        itempricetext.setText("");
                    }
                }catch (Exception efg){
                    efg.printStackTrace();
                }

            }
        });
        deleteitem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = itemnametext.getText().toString().toUpperCase();
                deletedata(name);
                itemnametext.setText("");
                itemnumbertext.setText("");
                itempricetext.setText("");
                }
        });
        additemnumberbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String number = itemnumbertext.getText().toString();
                if (!number.matches("")){
                    try{  int numberint = Integer.parseInt(number);
                        numberint = numberint + 1;
                        itemnumbertext.setText(String.valueOf(numberint));
                    }catch (Exception ef){
                        ef.printStackTrace();
                    }

                }
            }
        });
        subtractitemnamebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String number = itemnumbertext.getText().toString();
                if (!number.matches("")){
                    try{
                        int numberint = Integer.parseInt(number);
                        numberint = numberint - 1;
                        itemnumbertext.setText(String.valueOf(numberint));
                    }catch (Exception a){
                        a.printStackTrace();
                    }

                }
            }
        });
        listeyiGosterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getdata();
            }
        });
    }
    private Connection connect() {
        Connection  connection = null;
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
        }catch (Exception a){
            a.printStackTrace();
        }
        return connection;
    }
    public void insert(String name, String price, String number) {
        String sql = "INSERT INTO items(name , price , number) VALUES(?,?,?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, price);
            pstmt.setString(3,number);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public  void getdata() {
        ResultSet rs = null;
        Statement statement = null;
        try {
            Connection connection = this.connect();
            statement = connection.createStatement();
            rs = statement.executeQuery("select * from items");
            itemnames.clear();
            itemnumbers.clear();
            itemprices.clear();
            while (rs.next()) {
                itemnames.add(rs.getString("name"));
                itemprices.add(rs.getString("price"));
                itemnumbers.add(rs.getString("number"));
            }
            rs.close();
            DefaultListModel<String> defaultListModel = new DefaultListModel<>();
            for (int i = 0; i < itemnames.size(); i++) {
                defaultListModel.addElement(itemnames.get(i));
            }
            list1.setModel(defaultListModel);
        } catch (Exception de) {
            de.printStackTrace();
        }
    }
    public void deletedata(String name){
        if (!name.matches("")){
            String sql = "delete from items where name = ? ";
            try (Connection conn = connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.executeUpdate();
                getdata();
            } catch (SQLException efg) {
                System.out.println(efg.getMessage());
            }
        }

    }


    public static void main(String[] args) {
        JFrame jFrame = new JFrame("App");
        jFrame.setContentPane(new MainGUIForm().jpanel);
        jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
        itemnames = new ArrayList<>();
        itemnumbers = new ArrayList<>();
        itemprices = new ArrayList<>();


    }
}
