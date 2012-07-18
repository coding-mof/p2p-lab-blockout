package org.blockout.utils;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

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

    
    private Graph<String, String>                    graph;
    private VisualizationViewer<String, String> visViewer;

    public NetworkLogPlayer( String[] args ) {
        initGraph();
        initLayout();
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
        setTitle( "NetworkLogPlayer" );
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        
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

        getContentPane().add( new JScrollPane( visViewer ) );
        pack();
    }
}
