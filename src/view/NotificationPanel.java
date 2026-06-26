package view;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

import model.DBConnection;

public class NotificationPanel extends JPanel {

    private int userId;
    private JTextArea notificationArea;

    public NotificationPanel(String email) {

        setLayout(new BorderLayout());

        userId = getUserId(email);

        JLabel title = new JLabel(
                "Notifications",
                SwingConstants.CENTER
        );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        add(title, BorderLayout.NORTH);

        notificationArea = new JTextArea();

        notificationArea.setEditable(false);

        add(
                new JScrollPane(notificationArea),
                BorderLayout.CENTER
        );

        loadNotifications();
    }


    private void loadNotifications() {

        notificationArea.setText("");

        try (
                Connection conn =
                        DBConnection.getConnection()
        ) {

            /* ================= ASSIGNMENTS ================= */

            String sql1 =

                    "SELECT title,due_date " +

                            "FROM assignments " +

                            "WHERE user_id=? " +

                            "AND status='Pending'";


            PreparedStatement stmt1 =

                    conn.prepareStatement(sql1);


            stmt1.setInt(
                    1,
                    userId
            );


            ResultSet rs1 =

                    stmt1.executeQuery();


            while (rs1.next()) {

                notificationArea.append(

                        "📌 Assignment : "

                                +

                                rs1.getString(
                                        "title"
                                )

                                +

                                "\nDue : "

                                +

                                rs1.getString(
                                        "due_date"
                                )

                                +

                                "\n\n"

                );

            }



            /* ================= BURNOUT ================= */


            String sql2 =

                    "SELECT risk_level " +

                            "FROM burnout_analysis " +

                            "WHERE user_id=? " +

                            "ORDER BY analysis_id DESC " +

                            "LIMIT 1";


            PreparedStatement stmt2 =

                    conn.prepareStatement(sql2);


            stmt2.setInt(

                    1,

                    userId

            );


            ResultSet rs2 =

                    stmt2.executeQuery();


            if (rs2.next()) {

                String risk =

                        rs2.getString(

                                "risk_level"

                        );


                if (risk.equals("HIGH RISK")) {

                    notificationArea.append(

                            "⚠ Burnout Alert\n"

                                    +

                                    "Take rest.\n"

                                    +

                                    "Reduce workload.\n\n"

                    );

                }

            }


            /* ================= EMPTY ================= */


            if(notificationArea.getText().isEmpty()){

                notificationArea.setText(

                        "No notifications available."

                );

            }


        }

        catch(Exception e){

            e.printStackTrace();

        }

    }




    private int getUserId(String email){

        try(

                Connection conn=

                        DBConnection.getConnection()

        ){


            String sql=

                    "SELECT user_id " +

                            "FROM users " +

                            "WHERE email=?";



            PreparedStatement stmt=

                    conn.prepareStatement(

                            sql

                    );



            stmt.setString(

                    1,

                    email

            );



            ResultSet rs=

                    stmt.executeQuery();



            if(rs.next()){

                return rs.getInt(

                        "user_id"

                );

            }


        }

        catch(Exception e){

            e.printStackTrace();

        }


        return -1;

    }

}