package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import model.DBConnection;

public class StudyTrackerPanel extends JPanel{


private DefaultTableModel tableModel;
private JTable table;

private JTextField dateField;
private JTextField durationField;
private JTextField sleepField;
private JTextField productivityField;
private JTextField searchField;

private JTextArea notesArea;

private JButton addButton;
private JButton updateButton;
private JButton deleteButton;
private JButton searchButton;


private int selectedSessionId=-1;
private int userId;



public StudyTrackerPanel(String email){

setLayout(new BorderLayout());

userId=getUserId(email);



JPanel topPanel=new JPanel();



searchField=new JTextField(20);

searchButton=new JButton("Search");



topPanel.add(searchField);

topPanel.add(searchButton);



add(topPanel,BorderLayout.NORTH);




tableModel=new DefaultTableModel(

new Object[]{

"ID",
"Date",
"Duration",
"Sleep",
"Productivity",
"Notes"

},0

);



table=new JTable(tableModel);



table.getColumnModel()

.getColumn(0)

.setMinWidth(0);



table.getColumnModel()

.getColumn(0)

.setMaxWidth(0);




add(

new JScrollPane(table),

BorderLayout.CENTER

);




JPanel formPanel=

new JPanel(

new GridLayout(

7,
2,
5,
5

)

);




dateField=
new JTextField();


durationField=
new JTextField();


sleepField=
new JTextField();


productivityField=
new JTextField();


notesArea=
new JTextArea(

3,
20

);





addButton=
new JButton(

"Add"

);


updateButton=
new JButton(

"Update"

);


deleteButton=
new JButton(

"Delete"

);




formPanel.add(
new JLabel("Date"));

formPanel.add(dateField);




formPanel.add(
new JLabel("Study Hours"));

formPanel.add(durationField);




formPanel.add(
new JLabel("Sleep Hours"));

formPanel.add(sleepField);




formPanel.add(
new JLabel("Productivity"));

formPanel.add(productivityField);




formPanel.add(
new JLabel("Notes"));



formPanel.add(

new JScrollPane(

notesArea

)

);




formPanel.add(addButton);
formPanel.add(updateButton);
formPanel.add(deleteButton);



add(

formPanel,

BorderLayout.SOUTH

);



loadSessions();




addButton.addActionListener(e->{

addSession();

});



updateButton.addActionListener(e->{


updateSession();

});



deleteButton.addActionListener(e->{


deleteSession();

});




searchButton.addActionListener(e->{


searchSessions();

});




table.getSelectionModel()

.addListSelectionListener(e->{


fillForm();


});



}

private void addSession(){

try(

Connection conn=

DBConnection.getConnection()

){


String sql=

"INSERT INTO study_sessions "

+

"(user_id,"

+

"session_date,"

+

"duration,"

+

"sleep_hours,"

+

"productivity_score,"

+

"notes)"

+

"VALUES(?,?,?,?,?,?)";



PreparedStatement stmt=

conn.prepareStatement(

sql

);



stmt.setInt(

1,

userId

);



stmt.setString(

2,

dateField.getText()

);



stmt.setDouble(

3,

Double.parseDouble(

durationField.getText()

)

);



stmt.setDouble(

4,

Double.parseDouble(

sleepField.getText()

)

);



stmt.setDouble(

5,

Double.parseDouble(

productivityField.getText()

)

);



stmt.setString(

6,

notesArea.getText()

);



stmt.executeUpdate();



JOptionPane.showMessageDialog(

null,

"Session Added"

);



clearForm();



loadSessions();


}


catch(Exception e){

e.printStackTrace();

}


}



private void updateSession(){


if(selectedSessionId==-1){

JOptionPane.showMessageDialog(

null,

"Select Session"

);

return;

}



try(

Connection conn=

DBConnection.getConnection()

){


String sql=


"UPDATE study_sessions "

+

"SET session_date=?,"

+

"duration=?,"

+

"sleep_hours=?,"

+

"productivity_score=?,"

+

"notes=? "

+

"WHERE session_id=?";



PreparedStatement stmt=

conn.prepareStatement(

sql

);



stmt.setString(

1,

dateField.getText()

);



stmt.setDouble(

2,

Double.parseDouble(

durationField.getText()

)

);



stmt.setDouble(

3,

Double.parseDouble(

sleepField.getText()

)

);



stmt.setDouble(

4,

Double.parseDouble(

productivityField.getText()

)

);



stmt.setString(

5,

notesArea.getText()

);



stmt.setInt(

6,

selectedSessionId

);



stmt.executeUpdate();



JOptionPane.showMessageDialog(

null,

"Updated"

);



loadSessions();



clearForm();



}


catch(Exception e){

e.printStackTrace();

}



}



private void deleteSession(){


if(selectedSessionId==-1){

JOptionPane.showMessageDialog(

null,

"Select Session"

);

return;

}



try(

Connection conn=

DBConnection.getConnection()

){


String sql=

"DELETE FROM study_sessions "

+

"WHERE session_id=?";



PreparedStatement stmt=

conn.prepareStatement(

sql

);



stmt.setInt(

1,

selectedSessionId

);



stmt.executeUpdate();



JOptionPane.showMessageDialog(

null,

"Deleted"

);



clearForm();



loadSessions();


}


catch(Exception e){

e.printStackTrace();

}


}

private void loadSessions(){

tableModel.setRowCount(0);


try(

Connection conn=

DBConnection.getConnection()

){


String sql=

"SELECT * "

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



while(

rs.next()

){


tableModel.addRow(


new Object[]{



rs.getInt(

"session_id"

),



rs.getString(

"session_date"

),



rs.getDouble(

"duration"

),



rs.getDouble(

"sleep_hours"

),



rs.getDouble(

"productivity_score"

),



rs.getString(

"notes"

)



}


);


}



}


catch(Exception e){

e.printStackTrace();

}


}






private void fillForm(){


int row=table.getSelectedRow();



if(row==-1){

return;

}



selectedSessionId=


(Integer)


tableModel.getValueAt(

row,

0

);





dateField.setText(

tableModel.getValueAt(

row,

1

).toString()

);




durationField.setText(

tableModel.getValueAt(

row,

2

).toString()

);




sleepField.setText(

tableModel.getValueAt(

row,

3

).toString()

);




productivityField.setText(

tableModel.getValueAt(

row,

4

).toString()

);




notesArea.setText(

tableModel.getValueAt(

row,

5

).toString()

);


}






private void searchSessions(){


String keyword=


searchField.getText();



tableModel.setRowCount(0);



try(

Connection conn=

DBConnection.getConnection()

){



String sql=


"SELECT * "

+

"FROM study_sessions "

+

"WHERE user_id=? "

+

"AND notes LIKE ?";



PreparedStatement stmt=


conn.prepareStatement(

sql

);



stmt.setInt(

1,

userId

);



stmt.setString(

2,

"%"+keyword+"%"

);




ResultSet rs=


stmt.executeQuery();




while(

rs.next()

){



tableModel.addRow(



new Object[]{



rs.getInt(

"session_id"

),



rs.getString(

"session_date"

),



rs.getDouble(

"duration"

),



rs.getDouble(

"sleep_hours"

),



rs.getDouble(

"productivity_score"

),



rs.getString(

"notes"

)



}


);



}




}


catch(Exception e){

e.printStackTrace();

}


}







private void clearForm(){


dateField.setText("");


durationField.setText("");


sleepField.setText("");


productivityField.setText("");


notesArea.setText("");



selectedSessionId=-1;


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
