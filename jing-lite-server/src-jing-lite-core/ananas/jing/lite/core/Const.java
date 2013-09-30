package ananas.jing.lite.core;

public interface Const {

	interface Jing {

		String direction_rx = "rx";
		String direction_tx = "tx";

		String direction = "jing.direction";
		String addr_from = "jing.from";
		String addr_to = "jing.to";
		String message_url = "jing.message.url";
		String message_sha1 = "jing.message.sha1";
		String text_overview = "jing.text.overview";
		String text_detail = "jing.text.detail";

		String time_tx = "jing.time.tx";
		String time_rx = "jing.time.rx";

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
