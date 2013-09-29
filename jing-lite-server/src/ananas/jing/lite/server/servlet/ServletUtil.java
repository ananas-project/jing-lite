package ananas.jing.lite.server.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ananas.jing.lite.core.Const;
import ananas.jing.lite.core.LocalXGitObject;

public class ServletUtil {

	public static String getBaseURL(HttpServletRequest request) {

		final String servlet_path = request.getServletPath();

		final String full_url = request.getRequestURL().toString();
		int index;
		index = full_url.indexOf(servlet_path);
		final String servlet_url = full_url.substring(0,
				index + servlet_path.length());
		index = servlet_url.lastIndexOf('/');

		String base_url = full_url.substring(0, index + 1);
		return base_url;
	}

	public static void make_response_headers(HttpServletRequest request,
			HttpServletResponse response, LocalXGitObject go) {

		String base_url = getBaseURL(request);

		if (go != null) {

			final String sha1 = go.getSha1() + "";
			String short_hash = sha1.hashCode() + "";
			response.setHeader(Const.XGITP.object_sha1, sha1);

			response.setHeader(Const.XGITP.object_url_full, base_url
					+ ServletConst.path_object_long + "/" + sha1);
			response.setHeader(Const.XGITP.object_url_short, base_url
					+ ServletConst.path_object_short + "/" + short_hash);

			response.setHeader(Const.XGITP.object_length, go.getLength() + "");
			response.setHeader(Const.XGITP.object_type, go.getType() + "");

			response.setHeader(Const.XGITP.status_code,
					HttpServletResponse.SC_OK + "");

		} else {

			response.setHeader(Const.XGITP.status_code,
					HttpServletResponse.SC_NOT_FOUND + "");

		}

		response.setHeader(Const.XGITP.endpoint, base_url
				+ ServletConst.path_object_hub);

		response.setHeader(Const.XGITP.version, Const.XGITP.version_current);

	}

}
