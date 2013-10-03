package ananas.jing.lite.core.client;

public interface JingMessageOverview {

	JingMessageDetail getDetail();

	void load();

	void save();

	long getTimeRx();

	long getTimeTx();

	String getTitle();

	String getOverview();

	boolean isWrite();

	boolean isRead();

	boolean isSend();

	boolean isReceive();

	boolean isRx();

	boolean isTx();

	void delete();

	String getFromAddress();

	String getToAddress();

}
