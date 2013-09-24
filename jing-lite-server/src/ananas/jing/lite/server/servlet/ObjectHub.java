package ananas.jing.lite.server.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ananas.jing.lite.core.Const;
import ananas.jing.lite.core.JingRepo;
import ananas.jing.lite.core.LocalXGitObject;
import ananas.jing.lite.core.server.JingServer;
import ananas.jing.lite.core.xgit.XGitApiL;
import ananas.jing.lite.server.DefaultServerAgent;
import ananas.jing.lite.server.ServerAgent;

/**
 * Servlet implementation class ObjectHub
 */
public class ObjectHub extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final JingServer _server;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ObjectHub() {
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

		this.__doProc(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		this.__doProc(request, response);
	}

	private void __doProc(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String method = this.__getProperty(request, Const.XGITP.method);
		String sha1 = this.__getProperty(request, Const.XGITP.object_sha1);

		if (sha1 == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		System.out.println("XGITP:" + method + " " + sha1);

		if (method == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

		} else if (method.equals(Const.XGITP.method_get)) {
			this.__do_xgitp_get(request, response);

		} else if (method.equals(Const.XGITP.method_put)) {
			this.__do_xgitp_put(request, response);

		} else if (method.equals(Const.XGITP.method_head)) {
			this.__do_xgitp_head(request, response);

		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

	}

	private void __do_xgitp_head(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String sha1 = this.__getProperty(request, Const.XGITP.object_sha1);

		JingRepo repo = this._server.getRepo();
		LocalXGitObject go = repo.getXGitObject(sha1);
		if (go.exists()) {
			ServletUtil.make_response_headers(request, response, go);
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}

	}

	private void __do_xgitp_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String sha1 = this.__getProperty(request, Const.XGITP.object_sha1);

		JingRepo repo = this._server.getRepo();
		LocalXGitObject go = repo.getXGitObject(sha1);
		if (go.exists()) {
			ServletUtil.make_response_headers(request, response, go);
			response.setStatus(HttpServletResponse.SC_OK);
			OutputStream out = response.getOutputStream();
			XGitApiL lapi = repo.getApiL();
			lapi.getZippedObject(go, out);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void __do_xgitp_put(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String sha1 = this.__getProperty(request, Const.XGITP.object_sha1);

		JingRepo repo = this._server.getRepo();
		LocalXGitObject go = repo.getXGitObject(sha1);
		if (go.exists()) {
			ServletUtil.make_response_headers(request, response, go);
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			InputStream in = request.getInputStream();
			XGitApiL lapi = repo.getApiL();
			if (lapi.addZippedObject(go, in)) {
				ServletUtil.make_response_headers(request, response, go);
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
			}
		}

	}

	private String __getProperty(HttpServletRequest request, String key) {
		return request.getHeader(key);
	}

}
