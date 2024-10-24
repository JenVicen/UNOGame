package Network;

import Server.Request;
import GUI.Events.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static Server.Request.Command.SUBSCRIBE;

public class ClientRequestListenerThread implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(ClientRequestListenerThread.class);

	private final Socket clientRequestListenerSocket;

	private ObjectInputStream inputStream = null;
	private ObjectOutputStream outputStream = null;

	private volatile boolean isRunning = true;

	private EventListener eventListener;

	ClientRequestListenerThread(Socket clientRequestListenerSocket) {
		this.clientRequestListenerSocket = clientRequestListenerSocket;

		try {
			logger.info("Object created on thread \"{}\" state {}", Thread.currentThread().getName(),
					Thread.currentThread().getState());
		} catch (Exception e) {
			logger.error("Error initializing ClientThread from Socket");
		}
	}

	void setEventListener(EventListener eventListener) {
		this.eventListener = eventListener;
	}

	@Override
	public synchronized void run() {
		try {
			logger.info("now running on thread \"{}\" state {}", Thread.currentThread().getName(),
					Thread.currentThread().getState());
			outputStream = new ObjectOutputStream(clientRequestListenerSocket.getOutputStream());
			inputStream = new ObjectInputStream(clientRequestListenerSocket.getInputStream());
			long time = System.currentTimeMillis();

			sendListenerConnectedMessage();

			while (isRunning) {
				if (inputStream.available() != 0) {
					continue;
				}
				Request request = readRequestFromServer();
				if (request != null) {
					logger.info("Command {}", request.getCommand());
					this.eventListener.update(request);
				}
				logger.info("Request processed: {}", time);
			}
			terminate();
		} catch (Exception ex) {
			logger.error(
					String.format("Error in executing client's request on client in clientrequestlistener. Details %s",
							ex.getMessage()),
					ex);
			terminate();
		}
	}

	public synchronized void terminate() {
		try {
			isRunning = false;
			outputStream.close();
			inputStream.close();
			clientRequestListenerSocket.close();
		} catch (IOException ioEx) {
			logger.error(String.format("Error in terminating client's connection to the server. Details %s",
					ioEx.getMessage()));
		}
	}

	private void sendListenerConnectedMessage() {
		try {
			outputStream.reset();
			outputStream.writeObject(new Request(SUBSCRIBE, Request.Direction.CLIENT_TO_SERVER));
		} catch (IOException ioEx) {
			logger.error(String.format("Error in executing client's request. Details %s", ioEx.getMessage()));
		}
	}

	private synchronized Request readRequestFromServer() {
		try {
			Object inputObject = inputStream.readObject();
			if (inputObject.getClass() == Request.class) {
				return (Request) inputObject;
			}
		} catch (ClassNotFoundException cnfE) {
			logger.error(String.format("Could not parse request from InputStream %s", cnfE.getMessage()), cnfE);
		} catch (EOFException eofEx) {
			logger.error(String.format("EOFError in readRequestFromServer. %s", eofEx.getMessage()), eofEx);
		} catch (IOException e) {
			logger.error(String.format("General Error in readRequestFromServer. %s", e.getMessage()), e);
		}
		return null;
	}
}
