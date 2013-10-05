package ananas.jing.lite.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ananas.jing.lite.core.Const;
import ananas.jing.lite.core.LocalXGitObject;
import ananas.jing.lite.core.server.JingServer;
import ananas.jing.lite.server.DefaultServerAgent;
import ananas.jing.lite.server.ServerAgent;

/**
 * Servlet implementation class ObjectL
 */
@WebServlet("/ObjectL/*")
public class ObjectL extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final JingServer _server;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ObjectL() {
		super();
		ServerAgent sa = new DefaultServerAgent();
		this._server = sa.getServer();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String method = request.getHeader(Const.XGITP.method);
		if (Const.XGITP.method_head.equals(method)) {
			// to jing
			this.__do_xgitp_head(request, response);
		} else {
			// to html
			this.__do_http_get(request, response);
		}
	}

	private void __do_http_get(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		String kw = ServletConst.path_object_long;
		String url = request.getRequestURL().toString();
		int index = url.lastIndexOf(kw);
		String base_url = url.substring(0, index);
		String sha1 = this.__get_sha1(request);

		response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
		response.setHeader("Location", base_url + ServletConst.path_object_html
				+ "?k=" + sha1);

	}

	private String __get_sha1(HttpServletRequest request) {

		String uri = request.getRequestURI();
		// System.out.print(" head of long-object-url : " + uri);
		int index = uri.lastIndexOf('/');
		String sha1 = uri.substring(index + 1);
		return sha1;
	}

	private void __do_xgitp_head(HttpServletRequest request,
			HttpServletResponse response) {

		String uri = request.getRequestURI();
		// System.out.print(" head of long-object-url : " + uri);
		int index = uri.lastIndexOf('/');
		String sha1 = uri.substring(index + 1);
		LocalXGitObject go = this._server.getRepo().getXGitObject(sha1);
		if (go.exists()) {
			ServletUtil.make_response_headers(request, response, go);
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	}

}
