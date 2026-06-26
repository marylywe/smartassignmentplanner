package view;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {

    public Dashboard(String userName) {

        setTitle("StudySync Dashboard");
        setSize(700,450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());


        //================ TOP ==================

        JLabel title = new JLabel(

                "Welcome, "+userName+"!",

                JLabel.CENTER
        );

        title.setFont(

                new Font(

                        "Arial",

                        Font.BOLD,

                        24
                )
        );


        add(title,BorderLayout.NORTH);



        //================ CENTER ==================


        JPanel centerPanel = new JPanel();

        centerPanel.setLayout(

                new GridLayout(

                        2,
                        2,
                        20,
                        20
                )
        );



        JButton assignmentBtn =
                new JButton(
                        "Assignments"
                );


        JButton studyBtn =
                new JButton(
                        "Study Tracker"
                );



        JButton burnoutBtn =
                new JButton(
                        "Burnout Analysis"
                );



        JButton analyticsBtn =
                new JButton(
                        "Analytics"
                );




        centerPanel.add(
                assignmentBtn
        );


        centerPanel.add(
                studyBtn
        );


        centerPanel.add(
                burnoutBtn
        );


        centerPanel.add(
                analyticsBtn
        );



        add(
                centerPanel,
                BorderLayout.CENTER
        );



        //================ BOTTOM ==================


        JPanel bottomPanel =
                new JPanel();



        JButton logoutBtn =
                new JButton(
                        "Logout"
                );



        bottomPanel.add(
                logoutBtn
        );


        add(
                bottomPanel,
                BorderLayout.SOUTH
        );



        //================ ACTION ==================



        assignmentBtn.addActionListener(e->{


            JFrame frame =
                    new JFrame(
                            "Assignments"
                    );


            frame.setSize(
                    700,
                    500
            );


            frame.setLocationRelativeTo(null);



            frame.add(

                    new AssignmentPanel(
                            userName
                    )

            );


            frame.setVisible(true);


        });



        studyBtn.addActionListener(e->{


            JFrame frame=new JFrame(


                    "Study Tracker"


            );



            frame.setSize(

                    900,

                    700

            );


            frame.setLocationRelativeTo(

                    null

            );



            frame.add(

                    new StudyTrackerPanel(

                            userName

                    )

            );



            frame.setVisible(

                    true

            );



        });


        burnoutBtn.addActionListener(e->{



            JFrame frame=


                    new JFrame(

                            "Burnout Analysis"

                    );



            frame.setSize(

                    800,

                    600

            );



            frame.setLocationRelativeTo(

                    null

            );



            frame.add(

                    new BurnoutPanel(

                            userName

                    )

            );



            frame.setVisible(

                    true

            );



        });


        analyticsBtn.addActionListener(e->{



            JFrame frame=


                    new JFrame(

                            "Analytics"

                    );



            frame.setSize(

                    800,

                    600

            );



            frame.setLocationRelativeTo(

                    null

            );



            frame.add(

                    new AnalyticsPanel(

                            userName

                    )

            );



            frame.setVisible(

                    true

            );



        });



        logoutBtn.addActionListener(e->{


            dispose();


            new LoginForm().setVisible(true);


        });




    }


}