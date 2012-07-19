package org.blockout.utils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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

public class NetworkLogPlayer extends JFrame {
    private static final long serialVersionUID = 6182193527471497535L;

    /**
     * @param args
     */
    public static void main( String[] args ) {
        NetworkLogPlayer player = new NetworkLogPlayer( args );
        player.setVisible( true );
    }

    public static final int MAX_DELAY_MS = 5000;

    private enum PlayerState {
        PLAYING, PAUSED, STOPPED, NOT_LOADED;
    }
    
    private Graph<String, String>                    graph;
    private VisualizationViewer<String, String> visViewer;

    private JSlider                             delaySlider;
    private JLabel                              currentDelayLabel;
    private JButton                             playButton;
    private JButton                             pauseButton;
    private JButton                             stopButton;

    private PlayerState                         state;

    public NetworkLogPlayer( String[] args ) {
        initGraph();
        initLayout();
        changePlayerState( PlayerState.NOT_LOADED );
    }

    private void initGraph() {
        graph = new SparseMultigraph<String, String>();

        // add sample nodes
        graph.addVertex( "A" );
        graph.addVertex( "B" );
        graph.addVertex( "C" );
        graph.addVertex( "D" );
        graph.addEdge( "a", "A", "B", EdgeType.DIRECTED );
        graph.addEdge( "b", "B", "C", EdgeType.DIRECTED );
        graph.addEdge( "c", "C", "A", EdgeType.DIRECTED );
        graph.addEdge( "d", "D", "A", EdgeType.UNDIRECTED );
    }
    
    private void initLayout() {
        Preconditions.checkNotNull( graph, "No graph to display" );

        setSize( 400, 400 );
        setLocation( 100, 100 );
        setTitle( "NetworkLogPlayer" );
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        
        initMenuBar();
        initToolBar();

        Layout<String, String> layout = new CircleLayout<String, String>( graph );
        layout.setSize( new Dimension( 400, 400 ) );
        visViewer = new VisualizationViewer<String, String>( layout );
        visViewer.setPreferredSize( new Dimension( 400, 400 ) );

        // Render Labels
        visViewer.getRenderContext().setVertexLabelTransformer(
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

            @Override
            public void actionPerformed( ActionEvent e ) {
                onMenuOpen();
            }
        } );
        fileMenu.add( open );

        JMenuItem close = new JMenuItem( new AbstractAction( "Close" ) {

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

        delaySlider = new JSlider( 0, MAX_DELAY_MS, 0 );
        delaySlider.addChangeListener( new ChangeListener() {
            @Override
            public void stateChanged( ChangeEvent e ) {
                JSlider slider = (JSlider) e.getSource();
                currentDelayLabel.setText( String.format( "%04d",
                        slider.getValue() )
                        + " ms" );
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

    private void onMenuOpen() {

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

    private void onPlay() {
        changePlayerState( PlayerState.PLAYING );

    }

    private void onPause() {
        changePlayerState( PlayerState.PAUSED );
    }

    private void onStop() {
        changePlayerState( PlayerState.STOPPED );
    }
}
