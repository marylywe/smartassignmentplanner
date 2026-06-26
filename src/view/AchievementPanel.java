package view;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

import model.DBConnection;


public class AchievementPanel extends JPanel{


private int userId;



private JTextArea badgeArea;



public AchievementPanel(String email){



setLayout(new BorderLayout());



userId=getUserId(email);



badgeArea=new JTextArea();

badgeArea.setEditable(false);



add(

new JScrollPane(

badgeArea

),

BorderLayout.CENTER

);



loadBadges();



}


private void loadBadges(){


badgeArea.setText("");



studyBadge();


sleepBadge();


burnoutBadge();



}

private void studyBadge(){


try(

Connection conn=

DBConnection.getConnection()

){



String sql=



"SELECT SUM(duration) total "

+

"FROM study_sessions "

+

"WHERE user_id=?";




PreparedStatement stmt=

conn.prepareStatement(

sql

);



stmt.setInt(

1,

userId

);



ResultSet rs=

stmt.executeQuery();




if(rs.next()){


double total=

rs.getDouble(

"total"

);




if(total>=100){


badgeArea.append(


"📚 Study Beast\n"


);


}



}



}


catch(Exception e){

e.printStackTrace();

}



}

private void sleepBadge(){



try(

Connection conn=

DBConnection.getConnection()

){



String sql=



"SELECT AVG(sleep_hours) avgSleep "

+

"FROM study_sessions "

+

"WHERE user_id=?";





PreparedStatement stmt=

conn.prepareStatement(

sql

);




stmt.setInt(

1,

userId

);




ResultSet rs=

stmt.executeQuery();




if(

rs.next()

){



double sleep=


rs.getDouble(

"avgSleep"

);




if(

sleep>=7

){


badgeArea.append(



"🌙 Sleep Master\n"



);



}



}



}


catch(Exception e){

e.printStackTrace();

}



}

private void burnoutBadge(){



try(

Connection conn=

DBConnection.getConnection()

){



String sql=



"SELECT risk_level "


+

"FROM burnout_analysis "


+

"WHERE user_id=? "


+

"ORDER BY analysis_id DESC "


+

"LIMIT 1";





PreparedStatement stmt=


conn.prepareStatement(

sql

);




stmt.setInt(

1,

userId

);




ResultSet rs=


stmt.executeQuery();




if(

rs.next()

){



String risk=


rs.getString(

"risk_level"

);





if(

risk.equals(

"LOW RISK"

)

){



badgeArea.append(



"🧠 Burnout Survivor\n"



);



}





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


"SELECT user_id FROM users WHERE email=?";



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



if(

rs.next()

){


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