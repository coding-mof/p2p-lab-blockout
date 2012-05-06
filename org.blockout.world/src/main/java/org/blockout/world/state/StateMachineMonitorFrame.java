package org.blockout.world.state;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.inject.Named;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import org.blockout.common.IEvent;

/**
 * Monitoring frame for debugging the
 * 
 * @author azrael
 * 
 */
@Named
public class StateMachineMonitorFrame extends JFrame implements IStateMachineListener {

	private static final long	serialVersionUID	= -4664022344505802407L;
	private final JTextArea		txtEventLog;

	public StateMachineMonitorFrame() {
		super( "BlockOut - State Machine Monitor (only committed events)" );
		getContentPane().setLayout( new BorderLayout( 0, 0 ) );

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		getContentPane().add( scrollPane, BorderLayout.CENTER );

		txtEventLog = new JTextArea();
		txtEventLog.setEnabled( false );
		txtEventLog.setEditable( false );
		scrollPane.setViewportView( txtEventLog );

		setMinimumSize( new Dimension( 500, 550 ) );
		pack();
		setVisible( true );
	}

	@Override
	public void eventCommitted( final IEvent<?> event ) {
		SwingUtilities.invokeLater( new Runnable() {

			@Override
			public void run() {
				txtEventLog.append( event.toString() + "\n" );
			}
		} );
	}

	@Override
	public void eventPushed( final IEvent<?> event ) {
	}

	@Override
	public void eventRolledBack( final IEvent<?> event ) {
	}
}
