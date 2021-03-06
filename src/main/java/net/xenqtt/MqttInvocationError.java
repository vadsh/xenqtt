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
package net.xenqtt;

import java.io.PrintStream;

/**
 * Thrown when an {@link Error} occurs during the processing of a command.
 */
public final class MqttInvocationError extends Error {

	private static final long serialVersionUID = 1L;

	private final Error rootCause;

	/**
	 * Create a new instance of this class.
	 * 
	 * @param message
	 *            A textual message describing the situation leading to the throwing of this {@link MqttInvocationError error}
	 * @param rootCause
	 *            The actual {@link Error error} that was thrown
	 */
	public MqttInvocationError(String message, Error rootCause) {
		super(message);
		this.rootCause = rootCause;
	}

	/**
	 * @return The error that occurred while the command was executing. This is not the {@link #getCause()} of this error because it happened on a different
	 *         thread. The stack trace for the {@link #getRootCause() root cause} shows where the problem lies. The stack trace for this
	 *         {@link MqttInvocationError invocation error} shows where the command was called from.
	 */
	public Error getRootCause() {
		return rootCause;
	}

	/**
	 * @see java.lang.Throwable#printStackTrace(java.io.PrintStream)
	 */
	@Override
	public void printStackTrace(PrintStream s) {
		synchronized (s) {
			super.printStackTrace(s);
			s.print("ROOT CAUSE: ");
			if (rootCause == null) {
				s.println("Unknown");
			} else {
				rootCause.printStackTrace(s);
			}
		}
	}
}
