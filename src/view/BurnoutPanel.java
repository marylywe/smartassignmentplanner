package view;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

import model.DBConnection;


public class BurnoutPanel extends JPanel{


private int userId;



private JTextField sleepField;
private JTextField stressField;
private JTextField studyField;



private JLabel resultLabel;



private JButton analyzeButton;



public BurnoutPanel(String email){


setLayout(new BorderLayout());


userId=getUserId(email);




JLabel title=

new JLabel(

"Burnout Analysis",

SwingConstants.CENTER

);



title.setFont(

new Font(

"Arial",

Font.BOLD,

20

)

);



add(

title,

BorderLayout.NORTH

);




JPanel centerPanel=

new JPanel(

new GridLayout(

5,
2,
5,
5

)

);





sleepField=

new JTextField();



stressField=

new JTextField();



studyField=

new JTextField();



analyzeButton=

new JButton(

"Analyze"

);



resultLabel=

new JLabel(

"Waiting Analysis"

);






centerPanel.add(

new JLabel(

"Sleep Hours"

)

);

centerPanel.add(

sleepField

);





centerPanel.add(

new JLabel(

"Stress Level (1-10)"

)

);

centerPanel.add(

stressField

);





centerPanel.add(

new JLabel(

"Study Hours"

)

);

centerPanel.add(

studyField

);




centerPanel.add(

analyzeButton

);



centerPanel.add(

resultLabel

);




add(

centerPanel,

BorderLayout.CENTER

);






analyzeButton.addActionListener(e->{


analyzeBurnout();


});



}

private void analyzeBurnout(){


double sleep=

Double.parseDouble(

sleepField.getText()

);



double stress=

Double.parseDouble(

stressField.getText()

);



double study=

Double.parseDouble(

studyField.getText()

);




double score=0;





if(

sleep<6

){

score+=30;

}



else{

score+=10;

}




if(

stress>=8

){

score+=40;

}


else if(

stress>=5

){

score+=20;

}


else{

score+=10;

}




if(

study>8

){

score+=30;

}



else if(

study>5

){

score+=15;

}


else{

score+=5;

}







String level="";

String recommendation="";






if(score>=80){


level="HIGH RISK";


recommendation=

"Take breaks, reduce workload and sleep more.";


}



else if(score>=50){


level="MEDIUM RISK";


recommendation=

"Manage study schedule and improve sleep habits.";


}



else{


level="LOW RISK";


recommendation=

"Keep maintaining healthy routines.";


}





resultLabel.setText(


level


);





saveAnalysis(


score,

level,

recommendation


);




JOptionPane.showMessageDialog(


null,


recommendation


);



}

private void saveAnalysis(

double score,

String level,

String recommendation

){



try(

Connection conn=

DBConnection.getConnection()

){



String sql=


"INSERT INTO burnout_analysis "

+

"(user_id,"

+

"burnout_score,"

+

"risk_level,"

+

"recommendation)"

+

"VALUES(?,?,?,?)";




PreparedStatement stmt=

conn.prepareStatement(

sql

);




stmt.setInt(

1,

userId

);




stmt.setDouble(

2,

score

);




stmt.setString(

3,

level

);




stmt.setString(

4,

recommendation

);




stmt.executeUpdate();



}


catch(Exception e){

e.printStackTrace();

}



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