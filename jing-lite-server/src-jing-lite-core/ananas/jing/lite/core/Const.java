package ananas.jing.lite.core;

public interface Const {

	interface Jing {

		String direction_rx = "rx";
		String direction_tx = "tx";

		// from-to
		String direction = "jing.direction";
		String tx_addr = "jing.tx.addr";
		String tx_name = "jing.tx.name";
		String tx_time = "jing.tx.time";
		String rx_addr = "jing.rx.addr";
		String rx_name = "jing.rx.name";
		String rx_time = "jing.rx.time";

		// msg
		String message_url = "jing.message.url";
		String message_sha1 = "jing.message.sha1";

		// mime
		String mime_type = "jing.mime.type";
		String mime_uri = "jing.mime.uri";
		String mime_homepage = "jing.mime.homepage";

		// text
		String text_title = "jing.text.title";
		String text_overview = "jing.text.overview";
		String text_detail = "jing.text.detail";

		// adds
		String adds_sha1 = "jing.adds.sha1";
		String adds_mime = "jing.adds.mime.type";
		String adds_type = "jing.adds.type";
		String adds_size = "jing.adds.length";

	}

	interface XGITP {

		String method = "xgitp.method";
		String status_code = "xgitp.status.code";
		String status_message = "xgitp.status.message";
		String version = "xgitp.version";
		String version_current = "1.0";

		String location = "xgitp.location";
		String endpoint = "xgitp.endpoint";

		String object_length = "xgitp.object.length";
		String object_type = "xgitp.object.type";
		String object_sha1 = "xgitp.object.sha1";
		String object_url_short = "xgitp.object.url.short";
		String object_url_full = "xgitp.object.url.full";

		String method_disc = "discovery";
		String method_get = "get";
		String method_put = "put";
		String method_head = "head";

	}

}
