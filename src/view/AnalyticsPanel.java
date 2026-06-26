package view;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

import model.DBConnection;

public class AnalyticsPanel extends JPanel{


private int userId;



private JLabel totalStudyLabel;
private JLabel avgSleepLabel;
private JLabel burnoutLabel;



public AnalyticsPanel(String email){



setLayout(

new GridLayout(

3,
1

)

);



userId=getUserId(email);




totalStudyLabel=

new JLabel(

"Total Study : 0"

);



avgSleepLabel=

new JLabel(

"Average Sleep : 0"

);



burnoutLabel=

new JLabel(

"Latest Burnout : None"

);





add(

totalStudyLabel

);


add(

avgSleepLabel

);


add(

burnoutLabel

);




loadAnalytics();



}

private void loadAnalytics(){


double totalStudy=0;
double avgSleep=0;

String burnout="None";



try(

Connection conn=

DBConnection.getConnection()

){



/* TOTAL STUDY HOURS */


String sql1=


"SELECT SUM(duration) total "

+

"FROM study_sessions "

+

"WHERE user_id=?";




PreparedStatement stmt1=


conn.prepareStatement(

sql1

);



stmt1.setInt(

1,

userId

);



ResultSet rs1=


stmt1.executeQuery();




if(

rs1.next()

){



totalStudy=

rs1.getDouble(

"total"

);


}




/* AVERAGE SLEEP */


String sql2=


"SELECT AVG(sleep_hours) avg_sleep "

+

"FROM study_sessions "

+

"WHERE user_id=?";





PreparedStatement stmt2=


conn.prepareStatement(

sql2

);




stmt2.setInt(

1,

userId

);




ResultSet rs2=


stmt2.executeQuery();




if(

rs2.next()

){



avgSleep=


rs2.getDouble(

"avg_sleep"

);



}





/* LATEST BURNOUT */




String sql3=


"SELECT risk_level "

+

"FROM burnout_analysis "

+

"WHERE user_id=? "

+

"ORDER BY analysis_id DESC "

+

"LIMIT 1";





PreparedStatement stmt3=


conn.prepareStatement(

sql3

);





stmt3.setInt(

1,

userId

);





ResultSet rs3=


stmt3.executeQuery();




if(

rs3.next()

){


burnout=

rs3.getString(

"risk_level"

);



}





}


catch(Exception e){

e.printStackTrace();

}




totalStudyLabel.setText(


"Total Study Hours : "

+

totalStudy

);




avgSleepLabel.setText(


"Average Sleep : "

+

String.format(

"%.2f",

avgSleep

)


);




burnoutLabel.setText(


"Latest Burnout : "

+

burnout


);



}

private int getUserId(

String email

){



try(

Connection conn=

DBConnection.getConnection()

){



String sql=


"SELECT user_id "

+

"FROM users "

+

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
