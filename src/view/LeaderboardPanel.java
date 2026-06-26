package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

import model.DBConnection;

public class LeaderboardPanel extends JPanel{


private JTable table;

private DefaultTableModel model;



public LeaderboardPanel(){



setLayout(

new BorderLayout()

);



model=

new DefaultTableModel(

new Object[]{

"Rank",
"Name",
"Hours"

},

0

);




table=

new JTable(

model

);



add(

new JScrollPane(

table

),

BorderLayout.CENTER

);




loadLeaderboard();



}

private void loadLeaderboard(){


model.setRowCount(0);



try(

Connection conn=

DBConnection.getConnection()

){



String sql=


"SELECT users.username,"

+

"SUM(duration) total "


+

"FROM study_sessions "


+

"JOIN users "


+

"ON users.user_id=study_sessions.user_id "


+

"GROUP BY users.user_id "


+

"ORDER BY total DESC";




PreparedStatement stmt=


conn.prepareStatement(

sql

);




ResultSet rs=


stmt.executeQuery();




int rank=1;




while(

rs.next()

){



model.addRow(



new Object[]{



rank,



rs.getString(

"username"

),



rs.getDouble(

"total"

)



}



);



rank++;



}



}



catch(Exception e){

e.printStackTrace();

}



    }
}