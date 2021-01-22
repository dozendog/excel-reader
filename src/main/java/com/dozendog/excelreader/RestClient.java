package com.dozendog.excelreader;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class RestClient {
	
	private String responseMessage;

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	public int callRestAPI() throws ClientProtocolException, IOException {
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(Constants.ENDPOINT_URL);
		CloseableHttpResponse response = httpclient.execute(httpGet);
		int status = 500;

		try {
		    System.out.println("# response.getStatusLine:"+response.getStatusLine());
		    HttpEntity entity1 = response.getEntity();
		    
		    status = response.getStatusLine().getStatusCode();
		    this.responseMessage = EntityUtils.toString(response.getEntity());
		    // do something useful with the response body
		    // and ensure it is fully consumed
		    EntityUtils.consume(entity1);
		} finally {
			if(response!=null) {
				response.close();
			}		
		}
		return status;
	}

}
