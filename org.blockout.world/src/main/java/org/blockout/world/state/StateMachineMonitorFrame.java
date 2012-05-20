package org.blockout.world.state;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.SimpleDateFormat;

import javax.inject.Named;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;

import org.blockout.world.event.IEvent;

/**
 * Monitoring frame for debugging the {@link IStateMachine}.
 * 
 * @author Marc-Christian Schulze
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
		txtEventLog.setEditable( false );
		scrollPane.setViewportView( txtEventLog );

		DefaultCaret caret = (DefaultCaret) txtEventLog.getCaret();
		caret.setUpdatePolicy( DefaultCaret.ALWAYS_UPDATE );

		setMinimumSize( new Dimension( 650, 550 ) );
		pack();
		setVisible( true );
	}

	@Override
	public void eventCommitted( final IEvent<?> event ) {
		SwingUtilities.invokeLater( new Runnable() {

			@Override
			public void run() {
				StringBuilder buf = new StringBuilder();
				buf.append( "[" );
				SimpleDateFormat fmt = new SimpleDateFormat( "dd.MM.yyyy HH:mm:ss.SSS" );
				buf.append( fmt.format( event.getLocalTime().getTime() ) );
				buf.append( "] " );
				buf.append( event.toString() );
				buf.append( "\n" );
				txtEventLog.append( buf.toString() );
			}
		} );
	}

	@Override
	public void eventPushed( final IEvent<?> event ) {
	}

	@Override
	public void eventRolledBack( final IEvent<?> event ) {
	}

	@Override
	public void init( final IStateMachine stateMachine ) {
	}
}
