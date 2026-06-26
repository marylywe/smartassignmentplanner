package view;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

import model.DBConnection;

public class RecommendationPanel extends JPanel{


private int userId;



private JTextArea recommendationArea;



private JButton generateButton;




public RecommendationPanel(String email){



setLayout(new BorderLayout());



userId=getUserId(email);




recommendationArea=

new JTextArea();



recommendationArea.setEditable(

false

);




generateButton=

new JButton(

"Generate Recommendation"

);




add(

new JScrollPane(

recommendationArea

),

BorderLayout.CENTER

);




add(

generateButton,

BorderLayout.SOUTH

);




generateButton.addActionListener(e->{


generateRecommendation();


});



}

private void generateRecommendation(){

double study=0;

double sleep=0;

String burnout="";


try(

Connection conn=

DBConnection.getConnection()

){



String sql1=


"SELECT AVG(duration) avgStudy "

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



if(rs1.next()){


study=


rs1.getDouble(

"avgStudy"

);


}






String sql2=


"SELECT AVG(sleep_hours) avgSleep "

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



if(rs2.next()){


sleep=


rs2.getDouble(

"avgSleep"

);


}







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



String recommendation="";



if(

burnout.equals(

"HIGH RISK"

)

){


recommendation=


"Reduce study hours.\n"

+

"Sleep at least 7 hours.\n"

+

"Take breaks every 50 minutes.\n"

+

"Exercise 3 times weekly.";


}



else if(

burnout.equals(

"MEDIUM RISK"

)

){


recommendation=


"Improve sleep habits.\n"

+

"Maintain study schedule.\n"

+

"Take regular breaks.";



}



else{


recommendation=


"Great job.\n"

+

"Maintain healthy routines.\n"

+

"Continue tracking progress.";


}



recommendationArea.setText(


recommendation

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