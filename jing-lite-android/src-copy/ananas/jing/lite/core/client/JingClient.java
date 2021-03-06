package ananas.jing.lite.core.client;

import ananas.jing.lite.core.JingEndpoint;
import ananas.jing.lite.core.JingSMSHandler;
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
	JingSMSHandler getSMSHandler();

	void setSMSHandler(JingSMSHandler h);

	JingMessageManager getMessageManager();

}
