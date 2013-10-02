package ananas.jing.lite.android.helper;

import ananas.jing.lite.core.client.JingClient;

public interface CoreAgent {

	JingClient getClient();

	class Factory {

		public static CoreAgent newInstance() {
			return   CoreAgentImpl . newInst  ();
		}

	}

}
