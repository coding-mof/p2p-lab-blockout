package org.blockout.utils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import org.blockout.common.NetworkLogMessage;
import org.blockout.utils.NetworkLogPlayer.IMessageProcessor;

import com.google.common.base.Preconditions;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

public class NetworkLogPlayerFrame extends JFrame implements IMessageProcessor {
    private static final long serialVersionUID = 6182193527471497535L;

    /**
     * @param args
     */
    public static void main( String[] args ) {
        NetworkLogPlayerFrame player = new NetworkLogPlayerFrame( args );
        player.setVisible( true );
    }

    public static final int MAX_DELAY_MS = 5000;

    private enum PlayerState {
        PLAYING, PAUSED, STOPPED, NOT_LOADED;
    }
    
    private Graph<String, String>                    graph;
    private VisualizationViewer<String, String> visViewer;
    private Layout<String, String>              layout;

    private JSlider                             delaySlider;
    private JLabel                              currentDelayLabel;
    private JButton                             playButton;
    private JButton                             pauseButton;
    private JButton                             stopButton;

    private PlayerState                         state;
    private NetworkLogPlayer                    player;

    public NetworkLogPlayerFrame( String[] args ) {
        initGraph();
        initLayout();
        changePlayerState( PlayerState.NOT_LOADED );
    }

    private void initGraph() {
        graph = new SparseMultigraph<String, String>();

        // add sample nodes
        // graph.addVertex( "A" );
        // graph.addVertex( "B" );
        // graph.addVertex( "C" );
        // graph.addVertex( "D" );
        // graph.addEdge( "a", "A", "B", EdgeType.DIRECTED );
        // graph.addEdge( "b", "B", "C", EdgeType.DIRECTED );
        // graph.addEdge( "c", "C", "A", EdgeType.DIRECTED );
        // graph.addEdge( "d", "D", "A", EdgeType.UNDIRECTED );
    }
    
    private void initLayout() {
        Preconditions.checkNotNull( graph, "No graph to display" );

        setSize( 400, 400 );
        setLocation( 100, 100 );
        setTitle( "NetworkLogPlayer" );
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        
        initMenuBar();
        initToolBar();

        layout = new CircleLayout<String, String>( graph );
        layout.setSize( new Dimension( 400, 400 ) );
        visViewer = new VisualizationViewer<String, String>( layout );
        visViewer.setPreferredSize( new Dimension( 400, 400 ) );

        // Render Labels
        visViewer.getRenderContext().setVertexLabelTransformer(
                new ToStringLabeller<String>() );
        visViewer.getRenderContext().setEdgeLabelTransformer(
                new ToStringLabeller<String>() );
        // visViewer.getRenderer().getVertexLabelRenderer()
        // .setPosition( Position.CNTR );

        // Add mouse controls for zooming and panning
        DefaultModalGraphMouse<String, String> gm = new DefaultModalGraphMouse<String, String>();
        gm.setMode( ModalGraphMouse.Mode.TRANSFORMING );
        visViewer.setGraphMouse( gm );

        getContentPane()
                .add( new JScrollPane( visViewer ), BorderLayout.CENTER );
        pack();
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu( "File" );
        fileMenu.setMnemonic( KeyEvent.VK_F );
        menuBar.add( fileMenu );

        JMenuItem open = new JMenuItem( new AbstractAction( "Open" ) {
            private static final long serialVersionUID = -2668865400061916586L;

            @Override
            public void actionPerformed( ActionEvent e ) {
                onMenuOpen();
            }
        } );
        fileMenu.add( open );

        JMenuItem close = new JMenuItem( new AbstractAction( "Close" ) {
            private static final long serialVersionUID = -4601171569276502914L;

            @Override
            public void actionPerformed( ActionEvent e ) {
                System.exit( 0 );
            }
        } );
        fileMenu.add( close );
        setJMenuBar( menuBar );
    }

    private void initToolBar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setRollover( true );
        toolbar.setFloatable( false );

        toolbar.add( new JLabel( "Delay:" ) );

        delaySlider = new JSlider( 0, MAX_DELAY_MS, 1000 );
        delaySlider.addChangeListener( new ChangeListener() {
            @Override
            public void stateChanged( ChangeEvent e ) {
                JSlider slider = (JSlider) e.getSource();
                currentDelayLabel.setText( String.format( "%04d",
                        slider.getValue() )
                        + " ms" );

                if( null != player )
                    player.setDelay( slider.getValue() );
            }
        } );

        toolbar.add( delaySlider );

        currentDelayLabel = new JLabel( String.format( "%04d",
                delaySlider.getValue() )
                + " ms" );
        toolbar.add( currentDelayLabel );
        toolbar.addSeparator();


        playButton = new JButton( new ImageIcon(
 "icons/play_18x24.png" ) );
        playButton.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                onPlay();
            }
        } );

        toolbar.add( playButton );
        toolbar.addSeparator();

        pauseButton = new JButton( new ImageIcon(
                "icons/pause_18x24.png" ) );
        pauseButton.setEnabled( false );
        pauseButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                onPause();
            }
        } );
        toolbar.add( pauseButton );
        toolbar.addSeparator();

        stopButton = new JButton(
                new ImageIcon( "icons/stop_24x24.png" ) );
        stopButton.setEnabled( false );
        stopButton.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                onStop();
            }
        } );

        toolbar.add( stopButton );

        getContentPane().add( toolbar, BorderLayout.NORTH );
    }

    private void changePlayerState(final PlayerState newState){
        if(state == newState)
            return;
        
        state = newState;
        
        switch(state){
        case PLAYING:
            playButton.setEnabled( false );
            pauseButton.setEnabled( true );
            stopButton.setEnabled( true );
            break;
        case PAUSED:
            playButton.setEnabled( true );
            pauseButton.setEnabled( false );
            stopButton.setEnabled( true );
            break;
        case STOPPED:
            playButton.setEnabled( true );
            pauseButton.setEnabled( false );
            stopButton.setEnabled( false );
            break;
        default:
            playButton.setEnabled( false );
            pauseButton.setEnabled( false );
            stopButton.setEnabled( false );
            break;
        }
    }

    protected void onMenuOpen() {
        JFileChooser fileChooser = new JFileChooser( new File( "." ) );
        fileChooser.addChoosableFileFilter( new FileFilter() {

            @Override
            public String getDescription() {
                return "Logfiles (*.log)";
            }

            @Override
            public boolean accept( File f ) {
                return f.getName().endsWith( ".log" ) || f.isDirectory();
            }
        } );
        fileChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
        fileChooser.setMultiSelectionEnabled( true );
        int status = fileChooser.showOpenDialog( this );

        if( JFileChooser.APPROVE_OPTION == status ) {
            File[] files = fileChooser.getSelectedFiles();
            List<NetworkLogMessage> messages = new LinkedList<NetworkLogMessage>();

            NetworkLogReader reader;
            for ( File file : files ) {
                try {
                    reader = new NetworkLogReader( new FileReader( file ) );
                } catch ( FileNotFoundException e1 ) {
                    e1.printStackTrace();
                    continue;
                }

                NetworkLogMessage msg;

                try {
                    while ( null != ( msg = reader.readMessage() ) ) {
                        messages.add( msg );
                    }
                } catch ( IOException e1 ) {
                    e1.printStackTrace();
                    continue;
                }
            }

            Collections.sort( messages, new Comparator<NetworkLogMessage>() {
                public int compare( NetworkLogMessage o1, NetworkLogMessage o2 ) {
                    return new Long( o1.getTimestamp() ).compareTo( o2
                            .getTimestamp() );
                };
            } );

            player = new NetworkLogPlayer( messages, NetworkLogPlayerFrame.this );
            player.setDelay( delaySlider.getValue() );
            changePlayerState( PlayerState.STOPPED );
        }
    }

    private void onPlay() {
        changePlayerState( PlayerState.PLAYING );
        if( null != player )
            player.play();
    }

    private void onPause() {
        changePlayerState( PlayerState.PAUSED );
        player.stop();
    }

    private void onStop() {
        player.stop();
        graph = new SparseMultigraph<String, String>();
        updateGraph();
        player.reset();

        changePlayerState( PlayerState.STOPPED );
    }

    @Override
    public void process( NetworkLogMessage message ) {
        System.out.println( message );

        if( message.hasExtra( "chord" ) ) {
            String type = (String) message.getExtra( "chord" );
            if( "predecessor".equals( type ) ) {
                String id = (String) message.getExtra( "id" );
                String pred = (String) message.getExtra( "predid" );

                if( !graph.containsVertex( id ) )
                    graph.addVertex( id );

                if( !graph.containsVertex( pred ) )
                    graph.addVertex( pred );

                Collection<String> edges = graph.getIncidentEdges( id );
                for(String edge : edges){
                    if( ( "predecessor of " + id ).equals( edge ) ) {
                        graph.removeEdge( edge );
                    }
                }
                
                if( !id.equals( pred ) )
                    graph.addEdge( "predecessor of " + id, pred, id,
                            EdgeType.DIRECTED );
            }
        }

        updateGraph();
    }

    private void updateGraph() {
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
                // need to create new layout to update node positions
                visViewer
.setGraphLayout( new CircleLayout<String, String>(
                                graph ) );
                visViewer.repaint();
                NetworkLogPlayerFrame.this.repaint();
            }
        } );
    }
}
