package frc846.simulator;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Simulation implements ChangeListener, ActionListener {
	
	public class ConnectionSetup
	{
        public static final int encoder = 0;
        public static final int analogChannel = 1;
		public int speedControllerChannel;
		public int connectableChannel;
		public double mass;
		public boolean reverse;
		public SpeedController speedController = null;
		public Connectable connectable = null;
		public int type;
	}
	
	private static Simulation instance = null;
	
	private ArrayList<ConnectionSetup> setups = new ArrayList<ConnectionSetup>();
	private ArrayList<Connection> connections = new ArrayList<Connection>();
	
	private JPanel main = new JPanel();
	private JPanel connectionsPanel = new JPanel();
	private JPanel controls = new JPanel();
	private JToggleButton simulate = new JToggleButton("Simulate");
	private JToggleButton pause = new JToggleButton("Pause");
	private JButton step = new JButton("Step");
	private JSlider speed = new JSlider();
	
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
		c.weightx = 1.0;
		c.weighty = 0.001;
		c.gridx = 0;
		c.anchor = GridBagConstraints.PAGE_START;
	    main.add(new JLabel("Connections"), c);
		c.weighty = 0.9;
	    connectionsPanel.setLayout(new BoxLayout(connectionsPanel, BoxLayout.PAGE_AXIS));
	    main.add(connectionsPanel, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_END;
		c.weighty = 0.001;
	    main.add(new JLabel("Controls"), c);
	    controls.setLayout(new FlowLayout());
	    controls.add(simulate);
	    controls.add(pause);
	    controls.add(step);
	    step.setEnabled(false);
	    main.add(controls, c);
	    speed.setInverted(true);
	    speed.setMaximum(1000);
	    speed.setMinimum(20);
	    speed.setValue(20);
	    speed.setPaintLabels(true);
	    speed.setMajorTickSpacing(980);
	    main.add(speed, c);
	    c.weightx = 0;
	    c.fill = GridBagConstraints.VERTICAL;
	    main.add(new JLabel("Cycle Period (ms)"), c);
	    pause.addChangeListener(this);
	    step.addActionListener(this);
	    readConfig();
	}
	
	private void readConfig()
	{
		try {
			BufferedReader br = new BufferedReader(new FileReader("Connections.config"));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				String[] tokens = strLine.split(" ");
				ConnectionSetup c = new ConnectionSetup();
				c.speedControllerChannel = Integer.parseInt(tokens[0]);
				c.connectableChannel = Integer.parseInt(tokens[1].substring(1));
				c.mass = Double.parseDouble(tokens[2]);
				if (tokens[3] == "-")
					c.reverse = true;
				else
					c.reverse = false;
				char type = tokens[1].charAt(0);
				if (type == 'E')
					c.type = ConnectionSetup.encoder;
				else if (type == 'A')
					c.type = ConnectionSetup.analogChannel;
				setups.add(c);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateConnections(boolean enabled)
	{
		if (simulate.isSelected())
		{
			for(Connection c : connections)
			{
				if (enabled)
					c.update();
				else
					c.disable();
			}
		}
	}
	
	public void registerConnectable(Connectable connectable, int channel, int type)
	{
		for(ConnectionSetup c : setups)
		{
			if (c.connectableChannel == channel && c.type == type)
			{
				c.connectable = connectable;
				if (c.speedController != null)
				{
					Connection connection = new Connection(c.speedController, c.connectable, c.mass, c.reverse);
					connections.add(connection);
					connectionsPanel.add(new JLabel("Speed Controller " + c.speedControllerChannel + " -> " + (c.type == ConnectionSetup.analogChannel ? "Analog" : "Encoder") + " " + c.connectableChannel + " (" + c.mass + ")" + " [" + (c.reverse ? "-" : "+" + "]")));
					main.revalidate();
				}
				break;
			}
		}
	}

	public void registerSpeedController(SpeedController speedController, int channel)
	{
		for(ConnectionSetup c : setups)
		{
			if (c.speedControllerChannel == channel)
			{
				c.speedController = speedController;
				if (c.connectable != null)
				{
					Connection connection = new Connection(c.speedController, c.connectable, c.mass, c.reverse);
					connections.add(connection);
					connectionsPanel.add(new JLabel("Speed Controller " + c.speedControllerChannel + " -> " + (c.type == ConnectionSetup.analogChannel ? "Analog" : "Encoder") + " " + c.connectableChannel + " (" + (c.reverse ? "-" : "+" + ")")));
					main.revalidate();
				}
				break;
			}
		}
	}
	
	public void takeSemaphore()
	{
		try {
			Thread.sleep(speed.getValue());
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
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
