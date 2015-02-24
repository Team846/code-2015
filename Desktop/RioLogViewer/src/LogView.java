import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Console;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import javax.swing.*;
import javax.swing.text.*;

public class LogView {

	private static boolean disabled = false;

	static JTextPane allTab;
	static JTextPane errorTab;
	static JTextPane warnTab;
	static JTextPane infoTab;
	static JTextPane logTab;

	static boolean cmdMode = false;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		cmdMode = args.length > 0 && args[0].equals("-cmd");

		System.out.println(cmdMode);

		if (!cmdMode) {
			UiInit();
		}
		
		networkLoop();
	}

	private static void appendToPane(JTextPane tp, String msg, Color c) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				tp.setEditable(true); // set textArea editable

				StyleContext sc = StyleContext.getDefaultStyleContext();
				AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
						StyleConstants.Foreground, c);

				aset = sc.addAttribute(aset, StyleConstants.FontFamily,
						"Lucida Console");
				aset = sc.addAttribute(aset, StyleConstants.Alignment,
						StyleConstants.ALIGN_JUSTIFIED);

				int len = tp.getDocument().getLength();
				tp.setCaretPosition(len);
				tp.setCharacterAttributes(aset, false);
				tp.replaceSelection(msg.trim() + "\n");

				tp.setEditable(false); // set textArea non-editable

				// check log length and trim if necessary
				int lineCount = tp.getText().split("\n").length;

				try {
					Element root = tp.getDocument().getDefaultRootElement();
					Element first = root.getElement(0);

					if (lineCount > 1000) {
						tp.getDocument().remove(first.getStartOffset(),
								first.getEndOffset());
					}

				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});

	}

	private static void UiInit() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}

		JFrame mainUiFrame = new JFrame("LogView");
		mainUiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTabbedPane tabPanel = new JTabbedPane();

		allTab = createTab("All", tabPanel);
		logTab = createTab("Logs", tabPanel);
		errorTab = createTab("Errors", tabPanel);
		warnTab = createTab("Warnings", tabPanel);
		infoTab = createTab("Info", tabPanel);

		mainUiFrame.add(tabPanel, BorderLayout.CENTER);
		mainUiFrame.setVisible(true);

		JPanel buttons = new JPanel();

		JButton stopStart = new JButton("Stop", null);
		stopStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				disabled = !disabled;

				if (disabled) {
					stopStart.setText("Start");
				} else {
					stopStart.setText("Stop");
				}

				tabPanel.requestFocus();
			}
		});
		buttons.add(stopStart);

		JButton clear = new JButton("Clear Log", null);
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				allTab.setText("");
				errorTab.setText("");
				warnTab.setText("");
				infoTab.setText("");
				logTab.setText("");

				tabPanel.requestFocus();
			}
		});
		buttons.add(clear);

		mainUiFrame.add(buttons, BorderLayout.SOUTH);
		mainUiFrame.setMinimumSize(new Dimension(800, 400));

		mainUiFrame.pack();
		mainUiFrame.setSize(new Dimension(900, 900));
	}

	private static void printCmd(Entry msg) {
		String text = msg.getContent().trim();
		String ANSI_RESET = "\u001B[m";
		switch (msg.getLevel()) {
		case "error":
			System.out.println("\u001B[31m" + text + ANSI_RESET);
			break;
		case "info":
			System.out.println("\u001B[32m" + text + ANSI_RESET);
			break;
		case "warning":
			System.out.println("\u001B[33m" + text + ANSI_RESET);
			break;
		case "log":
			System.out.println(text + ANSI_RESET);
			break;
		}
	}

	private static void networkLoop() {
		while (true) {
			try {
				printCmd(new Entry("[INFO] Connecting to robot log stream"));
				final DatagramSocket socket = new DatagramSocket(null);
				socket.setReuseAddress(true);
				socket.setBroadcast(true);
				socket.bind(new InetSocketAddress(6666));

				final DatagramPacket packet = new DatagramPacket(
						new byte[1500], 1500);

				while (true) {
					socket.receive(packet);

					final String str = new String(packet.getData(),
							packet.getOffset(), packet.getLength());

					if (!disabled) {
						if (packet.getLength() > 1) {
							for (String message : str.split("\n")) {
								if (message.trim().length() > 0) {
									Entry newMessage = new Entry(message);

									if (cmdMode) {
										printCmd(newMessage);
									} else {
										// add to ui
										String content = newMessage
												.getContent();
										Color color = newMessage.getColor();
										String level = newMessage.getLevel();

										appendToPane(allTab, content, color);

										if (level == "error") {
											appendToPane(errorTab, content,
													color);
										} else if (level == "info") {
											appendToPane(infoTab, content,
													color);
										} else if (level == "warning") {
											appendToPane(warnTab, content,
													color);
										} else if (level == "log") {
											appendToPane(logTab, content, color);
										}
									}
								}
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static JTextPane createTab(String name, JTabbedPane parent) {
		JTextPane display = new JTextPane();
		display.setBackground(new Color(50, 50, 50));
		display.setVisible(true);
		display.setEditable(false);

		JPanel tab = new JPanel(new BorderLayout());

		JScrollPane scroll = new JScrollPane(display);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		// Add Textarea in to middle panel
		tab.add(scroll, BorderLayout.CENTER);
		parent.addTab(name, null, tab, null);

		return display;
	}
}
