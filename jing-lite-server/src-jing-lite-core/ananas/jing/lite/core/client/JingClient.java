package ananas.jing.lite.core.client;

import java.util.Properties;

import ananas.jing.lite.core.JingEndpoint;
import ananas.jing.lite.core.LocalXGitObject;
import ananas.jing.lite.core.RemoteXGitObject;

public interface JingClient extends JingEndpoint {

	RemoteXGitObject push(LocalXGitObject go);

	LocalXGitObject pull(LocalXGitObject go);

	LocalXGitObject pull(String ep_url, String sha1);

	LocalXGitObject pull(String url);

	RemoteXGitObject head(String ep_url, String sha1);

	RemoteXGitObject head(String url);

	// sms

	void sendMessage(String to, String overview, Properties prope);

	void receiveMessage(String from, String url);

	void sendMessage();

	void receiveMessage();

}
