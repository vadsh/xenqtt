/**
    Copyright 2013 James McClure

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package net.xenqtt.mock;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import net.xenqtt.message.ConnAckMessage;
import net.xenqtt.message.ConnectMessage;
import net.xenqtt.message.DisconnectMessage;
import net.xenqtt.message.MessageHandler;
import net.xenqtt.message.MessageType;
import net.xenqtt.message.MqttChannel;
import net.xenqtt.message.MqttMessage;
import net.xenqtt.message.PingReqMessage;
import net.xenqtt.message.PingRespMessage;
import net.xenqtt.message.PubAckMessage;
import net.xenqtt.message.PubCompMessage;
import net.xenqtt.message.PubMessage;
import net.xenqtt.message.PubRecMessage;
import net.xenqtt.message.PubRelMessage;
import net.xenqtt.message.SubAckMessage;
import net.xenqtt.message.SubscribeMessage;
import net.xenqtt.message.UnsubAckMessage;
import net.xenqtt.message.UnsubscribeMessage;

/**
 * Thread safe mock implementation of {@link MessageHandler}
 */
public class MockMessageHandler implements MessageHandler {

	private final Map<Object, CountDownLatch> triggers = new ConcurrentHashMap<Object, CountDownLatch>();
	private final List<MqttMessage> messagesReceived = new CopyOnWriteArrayList<MqttMessage>();
	private volatile RuntimeException exceptionToThrow;
	private volatile int channelOpenedCount;
	private volatile int channelClosedCount;
	private volatile int channelAttachedCount;
	private volatile int channelDetachedCount;
	private volatile Throwable lastChannelClosedCause;

	/**
	 * @see net.xenqtt.message.MessageHandler#connect(net.xenqtt.message.MqttChannel, net.xenqtt.message.ConnectMessage)
	 */
	@Override
	public void connect(MqttChannel channel, ConnectMessage message) throws Exception {
		doHandleInvocation(channel, message);
	}

	/**
	 * @see net.xenqtt.message.MessageHandler#connAck(net.xenqtt.message.MqttChannel, net.xenqtt.message.ConnAckMessage)
	 */
	@Override
	public void connAck(MqttChannel channel, ConnAckMessage message) throws Exception {
		doHandleInvocation(channel, message);
	}

	/**
	 * @see net.xenqtt.message.MessageHandler#publish(net.xenqtt.message.MqttChannel, net.xenqtt.message.PubMessage)
	 */
	@Override
	public void publish(MqttChannel channel, PubMessage message) throws Exception {
		doHandleInvocation(channel, message);
	}

	/**
	 * @see net.xenqtt.message.MessageHandler#pubAck(net.xenqtt.message.MqttChannel, net.xenqtt.message.PubAckMessage)
	 */
	@Override
	public void pubAck(MqttChannel channel, PubAckMessage message) throws Exception {
		doHandleInvocation(channel, message);
	}

	/**
	 * @see net.xenqtt.message.MessageHandler#pubRec(net.xenqtt.message.MqttChannel, net.xenqtt.message.PubRecMessage)
	 */
	@Override
	public void pubRec(MqttChannel channel, PubRecMessage message) throws Exception {
		doHandleInvocation(channel, message);
	}

	/**
	 * @see net.xenqtt.message.MessageHandler#pubRel(net.xenqtt.message.MqttChannel, net.xenqtt.message.PubRelMessage)
	 */
	@Override
	public void pubRel(MqttChannel channel, PubRelMessage message) throws Exception {
		doHandleInvocation(channel, message);
	}

	/**
	 * @see net.xenqtt.message.MessageHandler#pubComp(net.xenqtt.message.MqttChannel, net.xenqtt.message.PubCompMessage)
	 */
	@Override
	public void pubComp(MqttChannel channel, PubCompMessage message) throws Exception {
		doHandleInvocation(channel, message);
	}

	/**
	 * @see net.xenqtt.message.MessageHandler#subscribe(net.xenqtt.message.MqttChannel, net.xenqtt.message.SubscribeMessage)
	 */
	@Override
	public void subscribe(MqttChannel channel, SubscribeMessage message) throws Exception {
		doHandleInvocation(channel, message);
	}

	/**
	 * @see net.xenqtt.message.MessageHandler#subAck(net.xenqtt.message.MqttChannel, net.xenqtt.message.SubAckMessage)
	 */
	@Override
	public void subAck(MqttChannel channel, SubAckMessage message) throws Exception {
		doHandleInvocation(channel, message);
	}

	/**
	 * @see net.xenqtt.message.MessageHandler#unsubscribe(net.xenqtt.message.MqttChannel, net.xenqtt.message.UnsubscribeMessage)
	 */
	@Override
	public void unsubscribe(MqttChannel channel, UnsubscribeMessage message) throws Exception {
		doHandleInvocation(channel, message);
	}

	/**
	 * @see net.xenqtt.message.MessageHandler#unsubAck(net.xenqtt.message.MqttChannel, net.xenqtt.message.UnsubAckMessage)
	 */
	@Override
	public void unsubAck(MqttChannel channel, UnsubAckMessage message) throws Exception {
		doHandleInvocation(channel, message);
	}

	/**
	 * @see net.xenqtt.message.MessageHandler#disconnect(net.xenqtt.message.MqttChannel, net.xenqtt.message.DisconnectMessage)
	 */
	@Override
	public void disconnect(MqttChannel channel, DisconnectMessage message) throws Exception {
		doHandleInvocation(channel, message);
	}

	/**
	 * @see net.xenqtt.message.MessageHandler#channelOpened(net.xenqtt.message.MqttChannel)
	 */
	@Override
	public void channelOpened(MqttChannel channel) {
		channelOpenedCount++;
		doHandleInvocation(channel, "channelOpened");
	}

	/**
	 * @see net.xenqtt.message.MessageHandler#channelClosed(net.xenqtt.message.MqttChannel, java.lang.Throwable)
	 */
	@Override
	public void channelClosed(MqttChannel channel, Throwable cause) {
		channelClosedCount++;
		lastChannelClosedCause = cause;
		channel.cancelBlockingCommands();
		doHandleInvocation(channel, "channelClosed");
	}

	/**
	 * @see net.xenqtt.message.MessageHandler#channelAttached(net.xenqtt.message.MqttChannel)
	 */
	@Override
	public void channelAttached(MqttChannel channel) {
		channelAttachedCount++;
		doHandleInvocation(channel, "channelAttached");
	}

	/**
	 * @see net.xenqtt.message.MessageHandler#channelDetached(net.xenqtt.message.MqttChannel)
	 */
	@Override
	public void channelDetached(MqttChannel channel) {
		channelDetachedCount++;
		doHandleInvocation(channel, "channelDetached");
	}

	/**
	 * @see net.xenqtt.message.MessageHandler#messageSent(net.xenqtt.message.MqttChannel, net.xenqtt.message.MqttMessage)
	 */
	@Override
	public void messageSent(MqttChannel channel, MqttMessage message) {
	}

	/**
	 * Called by test channels when a {@link PingReqMessage} is received so it can be handled by this mock like other messages.
	 */
	public void pingReq(MqttChannel channel, PingReqMessage message) throws Exception {
		doHandleInvocation(channel, message);
	}

	/**
	 * Called by test channels when a {@link PingRespMessage} is received so it can be handled by this mock like other messages.
	 */
	public void pingResp(MqttChannel channel, PingRespMessage message) throws Exception {
		doHandleInvocation(channel, message);
	}

	/**
	 * The specified exception will be thrown the next time a {@link MessageHandler} interface method is invoked. It will not be thrown on the following
	 * invocation.
	 */
	public final void setException(RuntimeException e) {
		this.exceptionToThrow = e;
	}

	/**
	 * @return The number of times {@link #channelOpened(MqttChannel)} has been called
	 */
	public final int channelOpenedCalledCount() {
		return channelOpenedCount;
	}

	/**
	 * @return The number of times {@link #channelClosed(MqttChannel, Throwable)} has been called
	 */
	public final int channelClosedCalledCount() {
		return channelClosedCount;
	}

	/**
	 * @return The number of times {@link #channelAttached(MqttChannel)} has been called
	 */
	public final int channelAttachedCalledCount() {
		return channelOpenedCount;
	}

	/**
	 * @return The number of times {@link #channelDetached(MqttChannel)} has been called
	 */
	public final int channelDetachedCalledCount() {
		return channelDetachedCount;
	}

	/**
	 * @return the cause from the most recent invocation of {@link #channelClosed(MqttChannel, Throwable)}
	 */
	public final Throwable lastChannelClosedCause() {
		return lastChannelClosedCause;
	}

	/**
	 * @return The number of messages received
	 */
	public int messageCount() {
		return messagesReceived.size();
	}

	/**
	 * @return The message at the specified index (0=first message, 1=second message,...)
	 */
	public final MqttMessage message(int index) {
		return messagesReceived.get(index);
	}

	/**
	 * clears the received messages list
	 */
	public final void clearMessages() {
		messagesReceived.clear();
	}

	/**
	 * asserts that {@link #channelOpened(MqttChannel)} has been called the specified number of times
	 */
	public final void assertChannelOpenedCount(int value) {
		assertEquals(value, channelOpenedCount);
	}

	/**
	 * asserts that {@link #channelClosed(MqttChannel, Throwable)} has been called the specified number of times
	 */
	public final void assertChannelClosedCount(int value) {
		assertEquals(value, channelClosedCount);
	}

	/**
	 * asserts that {@link #channelAttached(MqttChannel)} has been called the specified number of times
	 */
	public final void assertChannelAttachedCount(int value) {
		assertEquals(value, channelAttachedCount);
	}

	/**
	 * asserts that {@link #channelDetached(MqttChannel)} has been called the specified number of times
	 */
	public final void assertChannelDetachedCount(int value) {
		assertEquals(value, channelDetachedCount);
	}

	/**
	 * asserts that the cause for the last {@link #channelClosed(MqttChannel, Throwable)} call was the specified type of exception
	 */
	public final void assertLastChannelClosedCause(Class<? extends Throwable> exceptionClass) {
		assertTrue(lastChannelClosedCause != null && lastChannelClosedCause.getClass().equals(exceptionClass));
	}

	/**
	 * asserts that the cause for the last {@link #channelClosed(MqttChannel, Throwable)} call equals the specified exception (null verifies it is null).
	 */
	public final void assertLastChannelClosedCause(Throwable e) {
		assertEquals(e, lastChannelClosedCause);
	}

	/**
	 * asserts the specified number of messages was received
	 */
	public final void assertMessageCount(int value) {
		assertEquals(value, messagesReceived.size());
	}

	/**
	 * Asserts the specified messages, and only the specified messages, were received in the specified order. Asserts each received message is equal to, but not
	 * the same object as, the specified message.
	 */
	public final void assertMessages(MqttMessage... messages) {
		assertMessages(Arrays.asList(messages));
	}

	/**
	 * Asserts the specified messages, and only the specified messages, were received in the specified order. Asserts each received message is equal to, but not
	 * the same object as, the specified message.
	 */
	public final void assertMessages(List<? extends MqttMessage> messages) {
		assertEquals(messages, messagesReceived);
	}

	/**
	 * Asserts the specified message was received at the specified index (0=first message, 1=second message, ...). Asserts the received message is equal to, but
	 * not the same object as, the specified message.
	 */
	public final void assertMessage(MqttMessage message, int index) {

		assertTrue(messagesReceived.size() > index);
		assertEquals(message, messagesReceived.get(index));
		assertNotSame(message, messagesReceived.get(index));
	}

	/**
	 * asserts the specified message types, and only the specified types, were received in the specified order
	 */
	public final void assertMessageTypes(MessageType... messageTypes) {

		assertEquals(messageTypes.length, messagesReceived.size());
		for (int i = 0; i < messageTypes.length; i++) {
			assertEquals(messageTypes[i], messagesReceived.get(i).getMessageType());
		}
	}

	/**
	 * Counts down the trigger when {@link #channelOpened(MqttChannel)} is invoked
	 */
	public final void onChannelOpened(CountDownLatch trigger) {
		triggers.put("channelOpened", trigger);
	}

	/**
	 * Counts down the trigger when {@link #channelClosed(MqttChannel, Throwable)} is invoked
	 */
	public final void onChannelClosed(CountDownLatch trigger) {
		triggers.put("channelClosed", trigger);
	}

	/**
	 * Counts down the trigger when {@link #channelAttached(MqttChannel)} is invoked
	 */
	public final void onChannelAttached(CountDownLatch trigger) {
		triggers.put("channelAttached", trigger);
	}

	/**
	 * Counts down the trigger when {@link #channelDetached(MqttChannel)} is invoked
	 */
	public final void onChannelDetached(CountDownLatch trigger) {
		triggers.put("channelDetached", trigger);
	}

	/**
	 * Counts down the trigger when a message of the specified type is received
	 */
	public final void onMessage(MessageType messageType, CountDownLatch trigger) {
		triggers.put(messageType, trigger);
	}

	/**
	 * Called by all {@link MessageHandler} interface methods for common behavior.
	 */
	protected void doHandleInvocation(MqttChannel channel, String action) {
		doHandleInvocation(channel, null, action);
	}

	/**
	 * Called by all {@link MessageHandler} interface methods for common behavior.
	 */
	protected void doHandleInvocation(MqttChannel channel, MqttMessage message) {
		doHandleInvocation(channel, message, null);
	}

	private void doHandleInvocation(MqttChannel channel, MqttMessage message, String action) {

		if (exceptionToThrow != null) {
			RuntimeException e = exceptionToThrow;
			exceptionToThrow = null;
			throw e;
		}

		Object key = action;
		if (message != null) {
			messagesReceived.add(message);
			key = message.getMessageType();
		}

		CountDownLatch latch = triggers.get(key);
		if (latch != null) {
			latch.countDown();
		}
	}
}
