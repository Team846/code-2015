/*
 * TabbedPaneDemo.java requires one additional file: images/middle.gif.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;


public class LogViewer extends JPanel
{
    JTabbedPane pane;
    JList<String> allList;
    JList<String> errorList;
    JList<String> warningList;
    JList<String> infoList;
    JTabbedPane tabbedPane;
    JScrollPane[] panes;
    DefaultListModel<String> all;
    DefaultListModel<String> error;
    DefaultListModel<String> warning;
    DefaultListModel<String> info;
    public LogViewer( JMenuItem item )
    {
        super( new BorderLayout() );
        panes = new JScrollPane[4];
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab( "All", panes[0] );
        tabbedPane.addTab( "Errors", panes[1] );
        tabbedPane.addTab( "Warnings", panes[2] );
        tabbedPane.addTab( "Info", panes[3] );
        setBackground( Color.DARK_GRAY );
        add( tabbedPane );

        item.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                final JFileChooser fc = new JFileChooser();
                FileFilter filter = new FileNameExtensionFilter( "Text file", "txt" );
                fc.setFileFilter( filter );
                int returnVal = fc.showOpenDialog( LogViewer.this );
                if ( returnVal == JFileChooser.APPROVE_OPTION )
                {
                    File file = fc.getSelectedFile();
                    populate( file );
                    tabbedPane.removeAll();
                    tabbedPane.addTab( "All", panes[0] );
                    tabbedPane.addTab( "Errors", panes[1] );
                    tabbedPane.addTab( "Warnings", panes[2] );
                    tabbedPane.addTab( "Info", panes[3] );
                }
            }
        } );
    }


    public void populate( File file )
    {

        Scanner scan = null;
        int cutOff = 25;
        try
        {
            scan = new Scanner( file );
        }
        catch ( FileNotFoundException e )
        {
            e.printStackTrace();
            return;
        }
        String next;
        all = new DefaultListModel<String>();
        error = new DefaultListModel<String>();
        warning = new DefaultListModel<String>();
        info = new DefaultListModel<String>();

        while ( scan.hasNext() )
        {
            next = scan.nextLine();
            if ( next.length() > cutOff )
            {
                if ( next.substring( 0, cutOff ).toLowerCase().contains( "error" ) )
                {
                    all.addElement( "<html><font color=" + "red" + ">" + next + "</font></html>" );
                    error.addElement( "<html><font color=" + "red" + ">" + next + "</font></html>" );
                }
                else if ( next.substring( 0, cutOff ).toLowerCase().contains( "warning" ) )
                {
                    all.addElement( "<html><font color=" + "yellow" + ">" + next + "</font></html>" );
                    warning.addElement( "<html><font color=" + "yellow" + ">" + next + "</font></html>" );
                }
                else if ( next.substring( 0, cutOff ).toLowerCase().contains( "info" ) )
                {
                    all.addElement( "<html><font color=" + "white" + ">" + next + "</font></html>" );
                    info.addElement( "<html><font color=" + "white" + ">" + next + "</font></html>" );
                }
                else
                    all.addElement( "<html><font color=" + "white" + ">" + next + "</font></html>" );
            }

        }
        scan.close();
        allList = new JList<String>( all );
        allList.setBackground( Color.DARK_GRAY );
        errorList = new JList<String>( error );
        errorList.setBackground( Color.DARK_GRAY );
        warningList = new JList<String>( warning );
        warningList.setBackground( Color.DARK_GRAY );
        infoList = new JList<String>( info );
        infoList.setBackground( Color.DARK_GRAY );
        allList.setVisibleRowCount( all.getSize() );
        errorList.setVisibleRowCount( error.getSize() );
        warningList.setVisibleRowCount( warning.getSize() );
        infoList.setVisibleRowCount( info.getSize() );
        panes[0] = new JScrollPane( allList );
        panes[0].setBackground( Color.DARK_GRAY );
        panes[1] = new JScrollPane( errorList );
        panes[1].setBackground( Color.DARK_GRAY );
        panes[2] = new JScrollPane( warningList );
        panes[2].setBackground( Color.DARK_GRAY );
        panes[3] = new JScrollPane( infoList );
        panes[3].setBackground( Color.DARK_GRAY );
    }


    public static void main( String[] args )
    {
        JFrame frame = new JFrame( "LogViewer" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize( screenSize.width, screenSize.height );
        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu( "File" );
        JMenuItem item = new JMenuItem( "Choose" );
        menu.add( item );
        menubar.add( menu );
        frame.setJMenuBar( menubar );
        frame.add( new LogViewer( item ), BorderLayout.CENTER );
        frame.setVisible( true );
    }
}