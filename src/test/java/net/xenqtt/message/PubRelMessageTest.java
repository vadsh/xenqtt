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
package net.xenqtt.message;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;

import net.xenqtt.message.MessageType;
import net.xenqtt.message.PubRelMessage;
import net.xenqtt.message.QoS;

import org.junit.Test;

public class PubRelMessageTest {

	ByteBuffer buf = ByteBuffer.wrap(new byte[] { (byte) 0x62, 0x02, 0x00, 0x01 });

	PubRelMessage msg;

	@Test
	public void testCtor_Receive() {

		msg = new PubRelMessage(buf, 0);
		assertMsg();
	}

	@Test
	public void testCtor_Send() {
		msg = new PubRelMessage(1);
		assertMsg();
	}

	@Test
	public void testMessageIds() throws Exception {

		for (int i = 0; i < 0xffff; i++) {
			buf = ByteBuffer.wrap(new byte[] { (byte) 0x62, 0x02, (byte) (i >> 8), (byte) (i & 0xff) });
			msg = new PubRelMessage(i);
			assertEquals(buf, msg.buffer);
			assertEquals(i, msg.getMessageId());
		}

		for (int i = 0; i < 0xffff; i++) {
			buf = ByteBuffer.wrap(new byte[] { (byte) 0x62, 0x02, (byte) (i >> 8), (byte) (i & 0xff) });
			msg = new PubRelMessage(buf, 0);
			assertEquals(i, msg.getMessageId());
		}
	}

	private void assertMsg() {

		assertEquals(buf, msg.buffer);

		assertEquals(MessageType.PUBREL, msg.getMessageType());
		assertFalse(msg.isDuplicate());
		assertEquals(QoS.AT_LEAST_ONCE, msg.getQoS());
		assertFalse(msg.isRetain());
		assertEquals(2, msg.getRemainingLength());

		assertEquals(1, msg.getMessageId());
	}
}
