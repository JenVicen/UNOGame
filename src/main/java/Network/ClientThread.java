package Network;

import Server.Request;
import State.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientThread implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(ClientThread.class);
	private final Socket clientSocket;

	private ObjectInputStream inputStream = null;
	private ObjectOutputStream outputStream = null;

	ClientThread(Socket clientSocket) {
		this.clientSocket = clientSocket;
		try {
			logger.info("Object created on thread \"{}\" state {}", Thread.currentThread().getName(), Thread.currentThread().getState());
		} catch (Exception e) {
			logger.error("Error initializing ClientThread from Socket");
		}
	}

	@Override
	public synchronized void run() {
		try {
			logger.info("now running on thread \"{}\" state {}", Thread.currentThread().getName(), Thread.currentThread().getState());
			outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
			inputStream = new ObjectInputStream(clientSocket.getInputStream());
		} catch (Exception ex) {
			logger.error(String.format("Error in establishing client's connection to the server. Details %s", ex.getMessage()));
			terminate();
		}
	}

	synchronized void terminate() {
		try {
			outputStream.close();
			inputStream.close();
			clientSocket.close();
		} catch (IOException ioEx) {
			logger.error(String.format("Error in terminating client's connection to the server. Details %s", ioEx.getMessage()));
		}
	}

	synchronized void send(Request request) {
		try {
			outputStream.writeObject(request);
		} catch (IOException e) {
			logger.error(String.format("Error sending request from client to server. %s", e.getMessage()));
		}
	}

	synchronized State readStateFromServer() {
		try {
			Object inputObject = inputStream.readObject();
			if (inputObject.getClass() == State.class) {
				return (State) inputObject;
			}
		} catch (ClassNotFoundException cnfE) {
			logger.error(String.format("Could not parse state from InputStream %s", cnfE.getMessage()));
		} catch (IOException e) {
			logger.error(String.format("Error sending request from client to server. %s", e.getMessage()));
		}
		return null;
	}

}
