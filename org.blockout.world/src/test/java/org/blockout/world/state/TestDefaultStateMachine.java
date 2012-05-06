package org.blockout.world.state;

import static org.mockito.Mockito.mock;

import org.blockout.common.IEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test the default implementation of the state machine.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class TestDefaultStateMachine {

	protected DefaultStateMachine	stateMachine;
	protected IStateMachineListener	listener;

	@Before
	public void setUp() {
		stateMachine = new DefaultStateMachine();
		listener = Mockito.mock( IStateMachineListener.class );
		stateMachine.addIStateMachineListener( listener );
	}

	@Test(expected = NullPointerException.class)
	public void testPushEventNPE() {
		stateMachine.pushEvent( null );
	}

	@Test(expected = NullPointerException.class)
	public void testCommitEventNPE() {
		stateMachine.commitEvent( null );
	}

	@Test(expected = NullPointerException.class)
	public void testDenyEventNPE() {
		stateMachine.rollbackEvent( null );
	}

	/**
	 * Tests whether the state machine allows pushed events when no validators
	 * are defined.
	 */
	@Test
	public void testEventPerformedWithoutValidators() {

		IEvent<?> event = mock( IEvent.class );
		stateMachine.pushEvent( event );

		Mockito.verify( listener ).eventPushed( event );
	}

	/**
	 * Tests whether the state machine denies pushed events if a single
	 * validator denies it.
	 */
	@Test
	public void testSingleValidatorDeniesEvent() {
		IEvent<?> event = mock( IEvent.class );

		IEventValidator validator = Mockito.mock( IEventValidator.class );
		Mockito.doReturn( ValidationResult.Invalid ).when( validator ).validateEvent( event );
		stateMachine.addEventValidator( validator );

		stateMachine.pushEvent( event );

		Mockito.verifyZeroInteractions( listener );
	}

	/**
	 * Tests whether the state machine allows pushed events if a single
	 * validator allows it.
	 */
	@Test
	public void testSingleValidatorAllowsEvent() {
		IEvent<?> event = mock( IEvent.class );

		IEventValidator validator = Mockito.mock( IEventValidator.class );
		Mockito.doReturn( ValidationResult.Valid ).when( validator ).validateEvent( event );
		stateMachine.addEventValidator( validator );

		stateMachine.pushEvent( event );

		Mockito.verify( listener ).eventPushed( event );
	}

	/**
	 * Tests whether the state machine allows pushed events if a single
	 * validator skips it.
	 */
	@Test
	public void testSingleValidatorSkipsEvent() {
		IEvent<?> event = mock( IEvent.class );

		IEventValidator validator = Mockito.mock( IEventValidator.class );
		Mockito.doReturn( ValidationResult.Unknown ).when( validator ).validateEvent( event );
		stateMachine.addEventValidator( validator );

		stateMachine.pushEvent( event );

		Mockito.verify( listener ).eventPushed( event );
	}

	/**
	 * Tests whether the state machine denies pushed events if one validator
	 * allows but another denies it.
	 */
	@Test
	public void testTwoDiscordantValidators() {
		IEvent<?> event = mock( IEvent.class );

		IEventValidator validator = Mockito.mock( IEventValidator.class );
		Mockito.doReturn( ValidationResult.Valid ).when( validator ).validateEvent( event );
		stateMachine.addEventValidator( validator );

		IEventValidator validator2 = Mockito.mock( IEventValidator.class );
		Mockito.doReturn( ValidationResult.Invalid ).when( validator2 ).validateEvent( event );
		stateMachine.addEventValidator( validator2 );

		stateMachine.pushEvent( event );

		Mockito.verifyZeroInteractions( listener );
	}

	/**
	 * Tests whether committed events are preceeded by a perform event
	 * invocations if not present.
	 */
	@Test
	public void testAutoCommitEvent() {
		IEvent<?> event = mock( IEvent.class );

		stateMachine.commitEvent( event );

		Mockito.verify( listener ).eventPushed( event );
		Mockito.verify( listener ).eventCommitted( event );
	}

	/**
	 * Tests whether committed events are preceeded by a perform event
	 * invocations if not present.
	 */
	@Test
	public void testExistingAutoCommitEvent() {
		IEvent<?> event = mock( IEvent.class );

		stateMachine.pushEvent( event );
		Mockito.verify( listener ).eventPushed( event );

		stateMachine.commitEvent( event );

		Mockito.verify( listener ).eventCommitted( event );
		Mockito.verifyNoMoreInteractions( listener );
	}

	@Test
	public void testDenyNonExistingEvent() {
		IEvent<?> event = mock( IEvent.class );

		stateMachine.rollbackEvent( event );

		Mockito.verifyZeroInteractions( listener );
	}

	@Test
	public void testDenyExistingEvent() {
		IEvent<?> event = mock( IEvent.class );

		stateMachine.pushEvent( event );
		Mockito.verify( listener ).eventPushed( event );

		stateMachine.rollbackEvent( event );
		Mockito.verify( listener ).eventRolledBack( event.getInverse() );
	}

	@Test
	public void testDenyCommittedEvent() {
		IEvent<?> event = mock( IEvent.class );

		stateMachine.commitEvent( event );
		Mockito.verify( listener ).eventPushed( event );
		Mockito.verify( listener ).eventCommitted( event );

		stateMachine.rollbackEvent( event );
		Mockito.verifyNoMoreInteractions( listener );
	}
}
