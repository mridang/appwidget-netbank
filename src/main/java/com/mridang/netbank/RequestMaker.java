package com.mridang.netbank;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import android.annotation.SuppressLint;
import android.os.Build;

/**
 * This class is used for making the actual request to the bank
 */
@SuppressLint("SimpleDateFormat")
public class RequestMaker {

	/**
	 * This method build a GET request to the Nordea web service and is mainly used for
	 * fetching the list of accounts and cards
	 *
	 * @param strUrl The relative URL to which to make the request
	 * @param strToken The token that should be sent in the request
	 * @return The get request with all the headers and parameters
	 */
	public static HttpGet doGet(String strUrl, String strToken) {

		final HttpGet htpGet = new HttpGet("https://mobilebankingservices.nordea.com" + strUrl);
		htpGet.setHeader("X-Security-Token", strToken);

		final Date localDate = Calendar.getInstance().getTime();
		final String str = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(localDate);

		htpGet.setHeader("X-App-Language", "en");
		htpGet.setHeader("X-App-Name", "MBA-FI");
		htpGet.setHeader("Date", str.substring(0, 22) + ":" + str.substring(22));
		htpGet.setHeader("X-Device-Model", Build.MODEL);
		htpGet.setHeader("X-Platform-Version", Build.VERSION.RELEASE);
		htpGet.setHeader("X-App-Version", "1.4.1");
		htpGet.setHeader("X-Platform-Type", "Android");
		htpGet.setHeader("X-Request-Id", String.valueOf((int) (100000.0D * Math.random())));
		htpGet.setHeader("X-Device-Make", Build.MANUFACTURER);
		htpGet.setHeader("X-App-Country", "FI");
		htpGet.setHeader("X-Region-Code", "11");

		return htpGet;

	}

	/**
	 * This method makes a POST request to the Nordea web service and is mainly used for
	 * logging in and fetching the authentication token.
	 * 
	 * @param strUrl The relative URL to which to make the request
	 * @param strPayload The payload that should be sent in the request
	 * @return The get request with all the headers and parameters
	 */
	public static HttpPost doPost(String strUrl, String strPayload) throws UnsupportedEncodingException {

		final HttpPost htpPost = new HttpPost("https://mobilebankingservices.nordea.com" + strUrl);
		htpPost.setEntity(new StringEntity(strPayload, "UTF-8"));

		final Date localDate = Calendar.getInstance().getTime();
		final String str = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(localDate);
		htpPost.setHeader("X-App-Language", "en");
		htpPost.setHeader("X-App-Name", "MBA-FI");
		htpPost.setHeader("Date", str.substring(0, 22) + ":" + str.substring(22));
		htpPost.setHeader("X-Device-Model", Build.MODEL);
		htpPost.setHeader("X-Platform-Version", Build.VERSION.RELEASE);
		htpPost.setHeader("X-App-Version", "1.4.1");
		htpPost.setHeader("X-Platform-Type", "Android");
		htpPost.setHeader("X-Request-Id", String.valueOf((int) (100000.0D * Math.random())));
		htpPost.setHeader("X-Device-Make", Build.MANUFACTURER);
		htpPost.setHeader("X-App-Country", "FI");
		htpPost.setHeader("X-Region-Code", "11");

		return htpPost;

	}

}