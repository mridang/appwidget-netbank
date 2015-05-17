package com.mridang.netbank;

import android.graphics.drawable.Drawable;

import com.mridang.widgets.SettingsActivity;

/**
 * This class provides the settings activity for the widget
 */
public class WidgetSettings extends SettingsActivity {

	/*
	 * @see com.mridang.widgets.SettingsActivity#getIcon()
	 */
	@Override
	public Drawable getIcon() {

		return getApplicationContext().getResources().getDrawable(R.drawable.ic_launcher);

	}

	/*
	 * @see com.mridang.widgets.BaseWidget#getKlass()
	 */
	@Override
	protected Class<?> getKlass() {

		return LauncherWidget.class;

	}

	/*
	 * @see com.mridang.widgets.SettingsActivity#getPreferences()
	 */
	@Override
	public Integer getPreferences() {

		return R.xml.preferences;

	}

}
