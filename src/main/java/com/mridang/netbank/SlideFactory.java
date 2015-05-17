package com.mridang.netbank;

import org.acra.ACRA;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

/**
 * This class is used to provide the stacks for the widget
 */
public class SlideFactory implements RemoteViewsFactory {

	/* This is the array containing the the list of accounts */
	private JSONArray jsoBalances;
	/* The context of the calling activity */
	private final Context ctxContext;
	/* The view that is used for each of the slides */
	private RemoteViews remView;

	/*
	 * 
	 */
	public SlideFactory(Context ctxContext, Intent ittIntent) {

		remView = new RemoteViews(ctxContext.getPackageName(), R.layout.slide);
		this.ctxContext = ctxContext;
		try {
			jsoBalances = new JSONArray(ittIntent.getStringExtra("data"));
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

	}

	/*
	 * @see android.widget.RemoteViewsService.RemoteViewsFactory#getCount()
	 */
	@Override
	public int getCount() {

		return jsoBalances != null ? jsoBalances.length() : 0;

	}

	/*
	 * @see android.widget.RemoteViewsService.RemoteViewsFactory#getItemId(int)
	 */
	@Override
	public long getItemId(int intPosition) {

		return intPosition;

	}

	/*
	 * @see android.widget.RemoteViewsService.RemoteViewsFactory#getLoadingView()
	 */
	@Override
	public RemoteViews getLoadingView() {

		return new RemoteViews(ctxContext.getPackageName(), R.layout.loading);

	}

	/*
	 * @see android.widget.RemoteViewsService.RemoteViewsFactory#getViewAt(int)
	 */
	@Override
	public RemoteViews getViewAt(int intPosition) {

		try {

			JSONObject jsoSlide = jsoBalances.getJSONObject(intPosition);
			if (jsoSlide.getJSONObject("productType").getString("$").equalsIgnoreCase("card")) {

				if (jsoSlide.getJSONObject("productNumber").getString("$").startsWith("4")) {
					remView.setImageViewResource(R.id.widget_icon, R.drawable.ic_visa);
				} else if (jsoSlide.getJSONObject("productNumber").getString("$").startsWith("54")) {
					remView.setImageViewResource(R.id.widget_icon, R.drawable.ic_master);
				} else {
					remView.setImageViewResource(R.id.widget_icon, R.drawable.ic_launcher);
				}

				if (jsoSlide.getJSONObject("nickName").has("$")) {

					String strNickname = jsoSlide.getJSONObject("nickName").getString("$");
					remView.setTextViewText(R.id.account_name, strNickname);

				} else {

					String strProduct = jsoSlide.getJSONObject("productType").getString("$");
					remView.setTextViewText(R.id.account_name, strProduct.replace("*", "\u2022"));

				}

			} else {

				remView.setImageViewResource(R.id.widget_icon, R.drawable.ic_launcher);
				if (jsoSlide.getJSONObject("nickName").has("$")) {

					String strNickname = jsoSlide.getJSONObject("nickName").getString("$");
					remView.setTextViewText(R.id.account_name, strNickname);

				} else {

					String strProduct = jsoSlide.getJSONObject("productType").getString("$");
					remView.setTextViewText(R.id.account_name, strProduct);

				}

			}

			String strNumber = jsoSlide.getJSONObject("productNumber").getString("$");
			remView.setTextViewText(R.id.account_iban, strNumber);
			if (jsoSlide.getJSONObject("fundsAvailable").has("$")) {

				String strBalance = jsoSlide.getJSONObject("fundsAvailable").getString("$");
				remView.setTextViewText(R.id.account_balance, strBalance + " \u20ac");

			} else {
				remView.setTextViewText(R.id.account_balance, "0 \u20ac");
			}

		} catch (final JSONException e) {
			Log.e("SlideFactory", "Unknown error encountered", e);
			ACRA.getErrorReporter().handleSilentException(e);
		} catch (final Exception e) {
			Log.e("SlideFactory", "Unknown error encountered", e);
			ACRA.getErrorReporter().handleSilentException(e);
		}

		return remView;

	}

	/*
	 * @see android.widget.RemoteViewsService.RemoteViewsFactory#getViewTypeCount()
	 */
	@Override
	public int getViewTypeCount() {

		return 1;

	}

	/*
	 * @see android.widget.RemoteViewsService.RemoteViewsFactory#hasStableIds()
	 */
	@Override
	public boolean hasStableIds() {

		return true;

	}

	/*
	 * @see android.widget.RemoteViewsService.RemoteViewsFactory#onCreate()
	 */
	@Override
	public void onCreate() {

		return;

	}

	/*
	 * @see android.widget.RemoteViewsService.RemoteViewsFactory#onDataSetChanged()
	 */
	@Override
	public void onDataSetChanged() {

		return;

	}

	/*
	 * @see android.widget.RemoteViewsService.RemoteViewsFactory#onDestroy()
	 */
	@Override
	public void onDestroy() {

		jsoBalances = null;

	}

}