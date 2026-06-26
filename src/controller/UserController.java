package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.DBConnection;

public class UserController {

    // ================= LOGIN =================

    public boolean login(String email, String password) {

        String sql =
                "SELECT * FROM users " +
                "WHERE email=? AND password=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        }

        catch (SQLException e) {

            e.printStackTrace();
        }

        return false;
    }



    // ================= REGISTER =================

    public boolean registerUser(String name,
                                String email,
                                String password) {


        String sql =

                "INSERT INTO users " +
                "(name,email,password) " +
                "VALUES (?,?,?)";


        try(Connection conn=
                    DBConnection.getConnection();

            PreparedStatement stmt=
                    conn.prepareStatement(sql)){



            stmt.setString(1,name);
            stmt.setString(2,email);
            stmt.setString(3,password);


            stmt.executeUpdate();


            System.out.println(
                    "User Registered Successfully"
            );


            return true;



        }


        catch(SQLException e){


            if(e.getMessage().contains(
                    "Duplicate entry")){


                System.out.println(
                        "Email already exists"
                );

            }

            else{

                e.printStackTrace();

            }


        }


        return false;


    }




    // ================= GET USER ID =================

    public int getUserId(String email){


        String sql=

                "SELECT user_id " +
                "FROM users " +
                "WHERE email=?";



        try(Connection conn=
                    DBConnection.getConnection();

            PreparedStatement stmt=
                    conn.prepareStatement(sql)){



            stmt.setString(1,email);



            ResultSet rs=
                    stmt.executeQuery();



            if(rs.next()){


                return rs.getInt("user_id");

            }


        }


        catch(SQLException e){

            e.printStackTrace();

        }


        return -1;

    }



    // ================= GET USER NAME =================


    public String getUserName(String email){


        String sql=

                "SELECT name " +
                "FROM users " +
                "WHERE email=?";



        try(Connection conn=
                    DBConnection.getConnection();

            PreparedStatement stmt=
                    conn.prepareStatement(sql)){



            stmt.setString(1,email);



            ResultSet rs=
                    stmt.executeQuery();



            if(rs.next()){


                return rs.getString("name");

            }



        }


        catch(SQLException e){

            e.printStackTrace();

        }



        return "";



    }



}