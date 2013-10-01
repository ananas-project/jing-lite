package ananas.jing.lite.core.client;

import java.util.List;
import java.util.Properties;

public interface JingMessageManager {

	void sendMessage(String to, String text, Properties prope);

	void receiveMessage(String from, String text);

	void sendMessage();

	void receiveMessage();

	List<JingMessageOverview> listAll();

}
