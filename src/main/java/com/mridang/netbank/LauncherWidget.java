package com.mridang.netbank;

import java.io.InputStream;
import java.util.Date;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateFormat;
import android.widget.RemoteViews;

import com.mridang.widgets.BaseWidget;
import com.mridang.widgets.SavedSettings;
import com.mridang.widgets.WidgetHelpers;
import com.mridang.widgets.utils.GzippedClient;

/**
 * This class is the provider for the widget and updates the data
 */
public class LauncherWidget extends BaseWidget {

	/*
	 * @see com.mridang.widgets.BaseWidget#fetchContent(android.content.Context, java.lang.Integer,
	 * com.mridang.widgets.SavedSettings)
	 */
	@Override
	public String fetchContent(Context ctxContext, Integer intInstance, SavedSettings objSettings)
			throws Exception {

		final DefaultHttpClient dhcClient = GzippedClient.createClient();

		InputStream is = ctxContext.getResources().openRawResource(R.raw.login);
		String strLogin = IOUtils.toString(is);
		strLogin = String.format(strLogin, objSettings.get("password"), objSettings.get("username"));
		IOUtils.closeQuietly(is);

		final HttpPost posAuthent = RequestMaker.doPost("/FI/AuthenticationServiceV1.8/SecurityToken", strLogin);
		final HttpResponse resAuthent = dhcClient.execute(posAuthent);

		final Integer intAuthent = resAuthent.getStatusLine().getStatusCode();
		if (intAuthent != HttpStatus.SC_OK) {
			throw new HttpResponseException(intAuthent, "Server responded with code " + intAuthent);
		}

		final String strAuthent = EntityUtils.toString(resAuthent.getEntity(), "UTF-8");
		JSONObject jsoAuthent = new JSONObject(strAuthent);
		jsoAuthent = jsoAuthent.getJSONObject("lightLoginResponse");
		if (jsoAuthent.has("errorMessage")) {
			throw new HttpResponseException(403, jsoAuthent.getString("errorMessage"));
		}

		String strToken = jsoAuthent.getJSONObject("authenticationToken").getJSONObject("token").getString("$");
		final HttpGet getContext = RequestMaker.doGet("/FI/BankingServiceV1.8/initialContext", strToken);
		final HttpResponse resContext = dhcClient.execute(getContext);

		final Integer intContext = resContext.getStatusLine().getStatusCode();
		if (intContext != HttpStatus.SC_OK) {
			throw new HttpResponseException(intContext, "Server responded with code " + intContext);
		}

		final String strContext = EntityUtils.toString(resContext.getEntity(), "UTF-8");
		JSONObject jsoContext = new JSONObject(strContext);
		jsoContext = jsoContext.getJSONObject("getInitialContextOut");
		if (jsoContext.has("errorMessage")) {
			throw new HttpResponseException(403, jsoContext.getString("errorMessage"));
		}

		return jsoContext.getJSONArray("product").toString(2);

	}

	/*
	 * @see com.mridang.widgets.BaseWidget#getIcon()
	 */
	@Override
	public Integer getIcon() {

		return R.drawable.ic_notification;

	}

	/*
	 * @see com.mridang.widgets.BaseWidget#getKlass()
	 */
	@Override
	protected Class<?> getKlass() {

		return getClass();

	}

	/*
	 * @see com.mridang.BaseWidget#getToken()
	 */
	@Override
	public String getToken() {

		return "a1b2c3d4";

	}

	/*
	 * @see com.mridang.widgets.BaseWidget#updateWidget(android.content.Context, java.lang.Integer,
	 * com.mridang.widgets.SavedSettings, java.lang.String)
	 */
	@Override
	public void updateWidget(Context ctxContext, Integer intInstance, SavedSettings objSettings, String strContent)
			throws Exception {

		final RemoteViews remView = new RemoteViews(ctxContext.getPackageName(), R.layout.widget);
		final Intent ittSlides = new Intent(ctxContext, SlideService.class);
		ittSlides.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, intInstance);
		ittSlides.setData(Uri.fromParts("content", String.valueOf(new Random().nextInt()), null));
		ittSlides.putExtra("data", strContent);

		final String strWebpage = "https://solo1.nordea.fi/nsp/login";
		final PendingIntent pitOptions = WidgetHelpers.getIntent(ctxContext, WidgetSettings.class, intInstance);
		final PendingIntent pitWebpage = WidgetHelpers.getIntent(ctxContext, strWebpage);
		remView.setTextViewText(R.id.last_update, DateFormat.format("kk:mm", new Date()));
		remView.setOnClickPendingIntent(R.id.settings_button, pitOptions);
		remView.setOnClickPendingIntent(R.id.widget_icon, pitWebpage);
		remView.setRemoteAdapter(R.id.widget_cards, ittSlides);

		AppWidgetManager.getInstance(ctxContext).updateAppWidget(intInstance, remView);

	}

}