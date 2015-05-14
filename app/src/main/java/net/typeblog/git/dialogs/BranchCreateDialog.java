package net.typeblog.git.dialogs;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.jgit.api.errors.GitAPIException;

import net.typeblog.git.R;
import net.typeblog.git.support.GitProvider;
import static net.typeblog.git.support.Utility.*;

public class BranchCreateDialog extends ToolbarDialog
{
	private GitProvider mProvider;
	private EditText mText;
	
	public BranchCreateDialog(Context context, GitProvider provider) {
		super(context);
		mProvider = provider;
	}

	@Override
	protected int getLayoutResource() {
		return R.layout.branch_create;
	}

	@Override
	protected void onInitView() {
		setTitle(R.string.git_branch_create);
		
		mText = $(this, R.id.branch_name);
	}

	@Override
	protected void onConfirm() {
		String text = mText.getText().toString().trim().toLowerCase();
		
		if (text.equals("") || text.contains(" ")) {
			Toast.makeText(getContext(), R.string.illegal_name, Toast.LENGTH_SHORT).show();
		} else {
			new BranchCreateTask().execute(text);
		}
	}
	
	private class BranchCreateTask extends AsyncTask<String, Void, Void> {
		ProgressDialog progress;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(getContext());
			progress.setCancelable(false);
			progress.setMessage(getContext().getString(R.string.wait));
			progress.show();
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				mProvider.git().branchCreate()
					.setName(params[0])
					.call();
			} catch (GitAPIException e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			progress.dismiss();
			dismiss();
		}
		
	}

}
