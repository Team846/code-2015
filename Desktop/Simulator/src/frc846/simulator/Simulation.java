package frc846.simulator;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Semaphore;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Simulation implements ChangeListener, ActionListener {
	private static Simulation instance = null;
	
	private JPanel main = new JPanel();
	private JPanel controls = new JPanel();
	private JToggleButton simulate = new JToggleButton("Simulate");
	private JToggleButton pause = new JToggleButton("Pause");
	private JButton step = new JButton("Step");
	
	private final Semaphore loopWait = new Semaphore(1);

	public static void initialize()
	{
		instance = new Simulation();
	}
	
	public static Simulation getInstance()
	{
		return instance;
	}
	
	public Simulation()
	{
		main.setLayout(new GridBagLayout());
		JFrame parent = new JFrame();
		parent.add(main);
		parent.setVisible(true);
		parent.setTitle("Simulation");
	    parent.setBounds( 1380, 50, 280, 720 );
	    parent.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.9;
		c.weighty = 0.9;
		c.gridx = 0;
		c.anchor = GridBagConstraints.PAGE_START;
	    main.add(new JLabel("Connections"), c);
		c.anchor = GridBagConstraints.PAGE_END;
		c.weighty = 0.001;
	    main.add(new JLabel("Controls"), c);
	    controls.setLayout(new FlowLayout());
	    controls.add(simulate);
	    controls.add(pause);
	    controls.add(step);
	    step.setEnabled(false);
	    main.add(controls, c);
	    pause.addChangeListener(this);
	    step.addActionListener(this);
	}
	
	public void updateConnections()
	{
		
	}
	
	public void registerConnectable(Connectable connectable)
	{
		
	}

	public void registerSpeedController(SpeedController speedController)
	{
		
	}
	
	public void takeSemaphore()
	{
		if (pause.isSelected())
		{
			try {
				loopWait.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		JToggleButton b = (JToggleButton)arg0.getSource();
		if (b.isSelected())
		{
		    step.setEnabled(true);
		}
		else
		{
		    step.setEnabled(false);
			loopWait.release();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		loopWait.release();
	}
}
