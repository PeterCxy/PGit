package net.typeblog.git.activities;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import android.support.v7.widget.AppCompatEditText;

import net.typeblog.git.R;
import net.typeblog.git.dialogs.ToolbarDialog;
import net.typeblog.git.support.RepoManager;
import static net.typeblog.git.support.Utility.*;

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
		private EditText location, url, username, password;
		
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
			
			location = $(this, R.id.local_path);
			url = $(this, R.id.clone_url);
			username = $(this, R.id.username);
			password = $(this, R.id.password);
		}

		@Override
		protected void onConfirm() {
			String location = this.location.getText().toString();
			
			String msg = RepoManager.checkRepo(location);
			if (msg != null) {
				Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
			} else {
				String url = this.url.getText().toString();
				String username = this.username.getText().toString().trim();
				String password = this.password.getText().toString().trim();
				
				RepoManager.getInstance().addRepo(location, url, username, password);
				
				dismiss();
			}
		}
	}
}
