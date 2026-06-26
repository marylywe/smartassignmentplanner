package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class PomodoroTimerPanel extends JPanel{


private JLabel timerLabel;


private JButton startButton;
private JButton stopButton;
private JButton resetButton;



private Timer timer;



private int seconds=1500;



public PomodoroTimerPanel(){



setLayout(

new BorderLayout()

);




timerLabel=

new JLabel(

"25:00",

SwingConstants.CENTER

);



timerLabel.setFont(

new Font(

"Arial",

Font.BOLD,

40

)

);





startButton=

new JButton(

"Start"

);


stopButton=

new JButton(

"Stop"

);


resetButton=

new JButton(

"Reset"

);




JPanel buttonPanel=

new JPanel();




buttonPanel.add(

startButton

);


buttonPanel.add(

stopButton

);


buttonPanel.add(

resetButton

);




add(

timerLabel,

BorderLayout.CENTER

);



add(

buttonPanel,

BorderLayout.SOUTH

);



timer=

new Timer(

1000,

e->updateTimer()

);



startButton.addActionListener(e->{


timer.start();


});



stopButton.addActionListener(e->{


timer.stop();


});



resetButton.addActionListener(e->{


resetTimer();


});



}


private void updateTimer(){


if(

seconds>0

){


seconds--;



int minutes=

seconds/60;



int sec=

seconds%60;



timerLabel.setText(



String.format(

"%02d:%02d",

minutes,

sec

)


);



}



else{


timer.stop();



JOptionPane.showMessageDialog(



null,



"Pomodoro Completed!"


);



}





}

private void resetTimer(){



timer.stop();



seconds=1500;



timerLabel.setText(


"25:00"

);



}



}