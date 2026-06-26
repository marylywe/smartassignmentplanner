package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import model.DBConnection;

public class AssignmentPanel extends JPanel {

	private DefaultTableModel tableModel;
	private JTable table;

	private JTextField titleField;
	private JTextField dueDateField;
	private JTextField searchField;

	private JTextArea descriptionArea;

	private JComboBox<String> priorityBox;
	private JComboBox<String> filterBox;

	private JButton addButton;
	private JButton updateButton;
	private JButton deleteButton;
	private JButton doneButton;
	private JButton searchButton;

	private int selectedAssignmentId = -1;

	private int userId;

    public AssignmentPanel(String userEmail) {

        setLayout(new BorderLayout());
        userId = getUserId(userEmail);
        JLabel title = new JLabel("Your Assignments", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        // Filter dropdown
        String[] filterOptions = {"Show All", "Pending Only"};

        filterBox = new JComboBox<>(filterOptions);
        searchField = new JTextField(15);
        searchButton = new JButton("Search"); 
        searchButton.addActionListener(event ->{

            String keyword =
                    searchField.getText();


            searchAssignments(keyword);


        });
        
        filterBox.addActionListener(e -> {
            String selected = (String) filterBox.getSelectedItem();

            if ("Pending Only".equals(selected)) {
                loadAssignments(userEmail, true);
            } else {
                loadAssignments(userEmail, false);
                table.getSelectionModel()

                .addListSelectionListener(event->{



                int row=table.getSelectedRow();



                if(row>=0){


                titleField.setText(

                tableModel.getValueAt(

                row,

                0

                ).toString()

                );




                descriptionArea.setText(

                tableModel.getValueAt(

                row,

                1

                ).toString()

                );




                priorityBox.setSelectedItem(

                tableModel.getValueAt(

                row,

                2

                )

                );





                dueDateField.setText(

                tableModel.getValueAt(

                row,

                3

                ).toString()

                );




                selectedAssignmentId=row;



                }



                });
                
                table.getSelectionModel().addListSelectionListener(event ->{


                	int row=table.getSelectedRow();



                	if(row>=0){


                	titleField.setText(

                	tableModel.getValueAt(

                	row,

                	0

                	).toString()

                	);




                	descriptionArea.setText(

                	tableModel.getValueAt(

                	row,

                	1

                	).toString()

                	);





                	priorityBox.setSelectedItem(

                	tableModel.getValueAt(

                	row,

                	2

                	)

                	);




                	dueDateField.setText(

                	tableModel.getValueAt(

                	row,

                	3

                	).toString()

                	);



                	selectedAssignmentId=row;



                	}



                	});
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel rightPanel = new JPanel();

        rightPanel.add(searchField);
        rightPanel.add(searchButton);
        rightPanel.add(filterBox);

        topPanel.add(title,BorderLayout.CENTER);
        topPanel.add(rightPanel,BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);


        // Table
        tableModel = new DefaultTableModel(

        		new Object[]{

        		"Title",

        		"Description",

        		"Priority",

        		"Due Date",

        		"Status"

        		}

        		,0);

        table = new JTable(tableModel);

        add(new JScrollPane(table), BorderLayout.CENTER);


        // Form panel
        JPanel formPanel = new JPanel(

        		new GridLayout(7,2,5,5)

        		);



        		titleField = new JTextField();

        		dueDateField = new JTextField();



        		descriptionArea = new JTextArea(3,20);



        		priorityBox =

        		new JComboBox<>(

        		new String[]{

        		"Low",

        		"Medium",

        		"High"

        		}

        		);



        		addButton = new JButton("Add");

        		updateButton = new JButton("Update");

        		doneButton = new JButton("Done");

        		deleteButton = new JButton("Delete");




        		formPanel.add(new JLabel("Title"));
        		formPanel.add(titleField);



        		formPanel.add(new JLabel("Description"));

        		formPanel.add(

        		new JScrollPane(

        		descriptionArea

        		)

        		);



        		formPanel.add(

        		new JLabel("Priority")

        		);

        		formPanel.add(priorityBox);


        		formPanel.add(

        		new JLabel("Due Date")

        		);

        		formPanel.add(

        		dueDateField

        		);

        		formPanel.add(addButton);

        		formPanel.add(updateButton);


        		formPanel.add(doneButton);

        		formPanel.add(deleteButton);

        add(formPanel, BorderLayout.SOUTH);


        // Add Assignment
        addButton.addActionListener(e -> {

            String newTitle = titleField.getText().trim();
            String newDue = dueDateField.getText().trim();

            if (newTitle.isEmpty() || newDue.isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "Please fill in all fields."
                );
                return;
            }

            try (Connection conn = DBConnection.getConnection()) {

            	String query =

            			"INSERT INTO assignments "

            			+

            			"(user_id,title,description,priority,due_date,status)"

            			+

            			"VALUES(?,?,?,?,?,'Pending')";
            	
                PreparedStatement stmt =
                        conn.prepareStatement(query);

                stmt.setInt(1, userId);

                stmt.setString(2, newTitle);

                stmt.setString(3,
                descriptionArea.getText()
                );


                stmt.setString(4,
                priorityBox.getSelectedItem().toString()
                );


                stmt.setString(5,
                newDue
                ); 
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(
                        null,
                        "Assignment added!"
                );

                titleField.setText("");
                dueDateField.setText("");

                loadAssignments(userEmail, false);

            } catch(SQLException ex){

JOptionPane.showMessageDialog(

null,

ex.getMessage()

);

ex.printStackTrace();

}
        });
        updateButton.addActionListener(event->{



if(selectedAssignmentId<0){


JOptionPane.showMessageDialog(

null,

"Select Assignment"

);


return;


}




try(Connection conn=


DBConnection.getConnection()){




String sql=


"UPDATE assignments " +

"SET title=?," +

"description=?," +

"priority=?," +

"due_date=? " +

"WHERE assignment_id=?";




PreparedStatement stmt=

conn.prepareStatement(sql);



stmt.setString(

1,

titleField.getText()

);



stmt.setString(

2,

descriptionArea.getText()

);



stmt.setString(

3,

priorityBox.getSelectedItem().toString()

);



stmt.setString(

4,

dueDateField.getText()

);




stmt.setInt(

5,

selectedAssignmentId

);




stmt.executeUpdate();



JOptionPane.showMessageDialog(

null,

"Updated"

);



loadAssignments(

userEmail,

false

);



}

catch(Exception ex){

ex.printStackTrace();

}



});
        deleteButton.addActionListener(event ->{

            if(selectedAssignmentId < 0){

                JOptionPane.showMessageDialog(

                        null,

                        "Select Assignment"

                );

                return;

            }


            try(Connection conn = DBConnection.getConnection()){


                String sql =


                        "UPDATE assignments " +

                        "SET status='Deleted' " +

                        "WHERE assignment_id=?";



                PreparedStatement stmt =

                        conn.prepareStatement(sql);



                stmt.setInt(

                        1,

                        selectedAssignmentId

                );



                stmt.executeUpdate();



                JOptionPane.showMessageDialog(

                        null,

                        "Assignment Deleted"

                );



                loadAssignments(

                        userEmail,

                        false

                );


            }


            catch(Exception ex){

                ex.printStackTrace();

            }


        });


        	// TAMBAH SINI
        doneButton.addActionListener(event->{


            if(selectedAssignmentId<0){


                JOptionPane.showMessageDialog(

                        null,

                        "Select Assignment"

                );

                return;

            }



            try(Connection conn=

                        DBConnection.getConnection()){


                String sql=


                        "UPDATE assignments " +

                        "SET status='Done' " +

                        "WHERE assignment_id=?";



                PreparedStatement stmt=

                        conn.prepareStatement(sql);



                stmt.setInt(

                        1,

                        selectedAssignmentId

                );



                stmt.executeUpdate();



                JOptionPane.showMessageDialog(

                        null,

                        "Marked Done"

                );



                loadAssignments(

                        userEmail,

                        false

                );


            }



            catch(Exception ex){

                ex.printStackTrace();

            }


        });



        	loadAssignments(userEmail,false);
    
        doneButton.addActionListener(event->{



    	JOptionPane.showMessageDialog(

    	null,

    	"Coming Soon"

    	);



    	});


        // Load assignments initially
        loadAssignments(userEmail, false);
    }

    private void loadAssignments(String userEmail,
            boolean pendingOnly) {

tableModel.setRowCount(0);

try (Connection conn = DBConnection.getConnection()) {

String query =
"SELECT * FROM assignments " +
"WHERE user_id=?";

if (pendingOnly) {
query += " AND status='Pending'";
}

PreparedStatement stmt =
conn.prepareStatement(query);

stmt.setInt(1, userId);

ResultSet rs = stmt.executeQuery();

while (rs.next()) {

String title =
   rs.getString("title");

String description =
   rs.getString("description");

String priority =
   rs.getString("priority");

String due =
   rs.getString("due_date");

String status =
   rs.getString("status");


tableModel.addRow(

   new Object[]{

           title,

           description,

           priority,

           due,

           status

   }

);

}

}

catch (SQLException e) {

e.printStackTrace();

}

}

    private void searchAssignments(String keyword){




    	tableModel.setRowCount(0);



    	try(Connection conn=
    	DBConnection.getConnection()){


    	String sql=


    	"SELECT * FROM assignments " +

    	"WHERE user_id=? " +

    	"AND title LIKE ?";



    	PreparedStatement stmt=

    	conn.prepareStatement(sql);



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



    	while(rs.next()){


    	tableModel.addRow(

    	new Object[]{


    	rs.getString("title"),


    	rs.getString("description"),


    	rs.getString("priority"),


    	rs.getString("due_date"),


    	rs.getString("status")


    	}

    	);


    	}



    	}

    	catch(Exception ex){

    	ex.printStackTrace();

    	}


    	}


private int getUserId(String email){

try(Connection conn =
DBConnection.getConnection()){


String sql =

"SELECT user_id " +

"FROM users " +

"WHERE email=?";



PreparedStatement stmt =

conn.prepareStatement(sql);



stmt.setString(1,email);



ResultSet rs =

stmt.executeQuery();



if(rs.next()){


return rs.getInt("user_id");


}



}


catch(Exception e){


e.printStackTrace();


}



return -1;



}


}