package net.typeblog.git.dialogs;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;

import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;

import net.typeblog.git.R;
import static net.typeblog.git.support.Utility.*;

public abstract class ToolbarDialog extends AppCompatDialog
{
	private Toolbar mToolbar;
	private String mTitle = "";
	//private LayoutInflater mInflater;
	
	public ToolbarDialog(Context context) {
		super(context, R.style.AppTheme_Dialog);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutResource());
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		
		// Toolbar
		mToolbar = $(this, R.id.toolbar);
		
		if (mToolbar == null)
			throw new IllegalStateException("No Toolbar");
			
		if (Build.VERSION.SDK_INT >= 21) {
			mToolbar.setElevation(15.6f);
		}
		
		mToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});
		mToolbar.setTitle(mTitle);
		
		onInitView();
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title.toString();
		
		if (mToolbar != null) {
			mToolbar.setTitle(mTitle);
		}
	}
	
	protected abstract int getLayoutResource();
	protected abstract void onInitView();
}
