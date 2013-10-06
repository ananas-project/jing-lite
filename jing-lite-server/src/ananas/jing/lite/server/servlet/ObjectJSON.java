package ananas.jing.lite.server.servlet;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ananas.jing.lite.core.LocalXGitObject;
import ananas.jing.lite.core.server.JingServer;
import ananas.jing.lite.core.xgit.XGitCheckout;
import ananas.jing.lite.server.DefaultServerAgent;
import ananas.jing.lite.server.ServerAgent;

/**
 * Servlet implementation class ObjectJSON
 */
@WebServlet("/ObjectJSON")
public class ObjectJSON extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final JingServer _server;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ObjectJSON() {
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

		String sha1 = null;
		{
			String query = request.getQueryString();
			String k = "k=";
			int index = query.indexOf(k);
			sha1 = query.substring(index + k.length());
		}

		// out.println("query = " + query);
		// out.println(" sha1 = " + sha1);

		LocalXGitObject go = _server.getRepo().getXGitObject(sha1);
		if (!go.exists()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		long length = go.getLength();
		if (length > 102400) {
			response.setStatus(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
			return;
		}

		XGitCheckout co = go.getRepo().getApiL().checkout(go);
		try {

			Properties prop = new Properties();
			prop.load(co.getInputStream());

			response.setStatus(HttpServletResponse.SC_OK);
			Set<Object> keys = prop.keySet();
			int index = 0;
			StringBuilder sb = new StringBuilder();
			sb.append("{");
			for (Object k : keys) {
				if (index > 0)
					sb.append(",");
				index++;

				String key = k.toString();
				String value = prop.getProperty(key);

				key = ServletUtil.toJSONString(key);
				value = ServletUtil.toJSONString(value);

				sb.append(key + ":" + value);

			}
			sb.append("}");

			ServletOutputStream out = response.getOutputStream();
			out.write(sb.toString().getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		co.close();

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
