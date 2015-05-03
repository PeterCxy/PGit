package net.typeblog.git.activities;

import android.view.View;

import android.support.v7.widget.AppCompatEditText;

import net.typeblog.git.R;
import net.typeblog.git.dialogs.ToolbarDialog;

public class HomeActivity extends ToolbarActivity
{

	@Override
	protected int getLayoutResource() {
		return R.layout.main;
	}

	@Override
	protected void onInitView() {
		new AddRepoDialog().show();
	}
	
	private class AddRepoDialog extends ToolbarDialog {
		AddRepoDialog() {
			super(HomeActivity.this);
		}

		@Override
		protected int getLayoutResource() {
			return R.layout.add_repo;
		}

		@Override
		protected void onInitView() {
			setTitle(R.string.add_repo);
		}
	}
}
