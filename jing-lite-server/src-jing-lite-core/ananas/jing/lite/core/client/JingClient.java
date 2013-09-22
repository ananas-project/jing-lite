package ananas.jing.lite.core.client;

import ananas.jing.lite.core.JingEndpoint;
import ananas.jing.lite.core.XGitObject;

public interface JingClient extends JingEndpoint {

	Exception push(XGitObject go);

}
