package view;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

import model.DBConnection;


public class AIStudyAssistantPanel extends JPanel{


private int userId;



private JTextArea chatArea;


private JTextField inputField;



private JButton askButton;



public AIStudyAssistantPanel(String email){



setLayout(

new BorderLayout()

);



userId=getUserId(

email

);




chatArea=

new JTextArea();



chatArea.setEditable(

false

);




inputField=

new JTextField();




askButton=

new JButton(

"Ask"

);




JPanel bottom=


new JPanel(

new BorderLayout()

);




bottom.add(

inputField,

BorderLayout.CENTER

);



bottom.add(

askButton,

BorderLayout.EAST

);




add(

new JScrollPane(

chatArea

),

BorderLayout.CENTER

);



add(

bottom,

BorderLayout.SOUTH

);




askButton.addActionListener(e->{


generateReply();


});



}



private void generateReply(){


String question=


inputField.getText()

.toLowerCase();



String answer="";



double avgSleep=getSleep();

double avgStudy=getStudy();

String burnout=getBurnout();





if(

question.contains(

"burnout"

)

){


answer=


burnoutAdvice(

burnout

);


}





else if(

question.contains(

"sleep"

)

){


answer=



"Average Sleep : "

+

avgSleep;



}





else if(

question.contains(

"study"

)

){


answer=


"Average Study : "

+

avgStudy;


}





else{


answer=



generalAdvice(

burnout,

avgSleep,

avgStudy

);



}




chatArea.append(

"\nYou : "

+

question

);



chatArea.append(



"\nAI : "

+

answer

+

"\n"



);



inputField.setText("");



}



private String burnoutAdvice(

String risk

){


if(

risk.equals(

"HIGH RISK"

)

){


return


"Sleep more, reduce workload and take breaks.";


}



if(

risk.equals(

"MEDIUM RISK"

)

){


return


"Manage your schedule and sleep habits.";


}



return


"Maintain current routine.";



}


private String generalAdvice(

String risk,

double sleep,

double study

){



String advice="";




if(

sleep<6

){


advice+=


"Sleep at least 7 hours.\n";


}




if(

study>8

){


advice+=



"Reduce study workload.\n";


}




if(

risk.equals(

"HIGH RISK"

)

){



advice+=


"Take rest days.\n";


}




if(

advice.isEmpty()

){


advice=


"Good job. Continue your routine.";


}




return advice;



}


private double getSleep(){


double value=0;



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


value=


rs.getDouble(

"avgSleep"

);


}



}



catch(Exception e){

e.printStackTrace();

}



return value;



}


private double getStudy(){


double value=0;


try(

Connection conn=

DBConnection.getConnection()

){



String sql=


"SELECT AVG(duration) avgStudy "

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


value=


rs.getDouble(

"avgStudy"

);


}



}



catch(Exception e){

e.printStackTrace();

}



return value;



}


private String getBurnout(){


String risk="LOW RISK";



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


risk=


rs.getString(

"risk_level"

);


}



}



catch(Exception e){

e.printStackTrace();

}



return risk;



}

private int getUserId(String email){

    try(

            Connection conn=

                    DBConnection.getConnection()

    ){


        String sql =

                "SELECT user_id " +

                "FROM users " +

                "WHERE email=?";



        PreparedStatement stmt =

                conn.prepareStatement(

                        sql

                );



        stmt.setString(

                1,

                email

        );



        ResultSet rs =

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