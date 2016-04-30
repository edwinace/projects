package org.univ7.webapp.quiz.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;
import org.univ7.webapp.exception.UnRecoverableException;

@Component
public class HttpClient {
	private static final String USER_AGENT = "Mozilla/5.0";

	public String sendGet(String url) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("User-Agent", USER_AGENT);
		CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

		if (httpResponse.getStatusLine().getStatusCode() != 200) {
			return null;
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
		StringBuffer response = new StringBuffer();
		String inputLine;

		while ((inputLine = reader.readLine()) != null) {
			response.append(inputLine);
		}
		reader.close();
		httpClient.close();

		return response.toString();
	}

	public String sendJsonPost(String url, Map<String, String> headers, String requestBody) throws UnRecoverableException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);

		if (headers != null) {
			addHeaders(httpPost, headers);
		}

		StringEntity input;
		StringBuffer response = null;
		try {
			input = new StringEntity(requestBody);
			httpPost.setEntity(input);
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

			response = new StringBuffer();

			String inputLine;
			while ((inputLine = reader.readLine()) != null) {
				response.append(inputLine);
			}
			reader.close();
			httpClient.close();
		} catch (IOException e) {
			throw new UnRecoverableException("Push 알림 발송 과정에서 오류가 발생하였습니다");
		}

		return response.toString();
	}

	private void addHeaders(HttpPost httpPost, Map<String, String> headers) {
		httpPost.addHeader("User-Agent", USER_AGENT);
		Iterator<String> keys = headers.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			httpPost.addHeader(key, headers.get(key).toString());
		}
	}
}
