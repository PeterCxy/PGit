package net.typeblog.git.dialogs;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.jgit.api.errors.GitAPIException;

import net.typeblog.git.R;
import net.typeblog.git.support.GitProvider;
import net.typeblog.git.tasks.GitTask;
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
	
	private class BranchCreateTask extends GitTask<String> {
		
		public BranchCreateTask() {
			super(getContext(), mProvider);
		}

		@Override
		protected void doGitTask(GitProvider provider, String... params) throws GitAPIException, RuntimeException {
			mProvider.git().branchCreate()
				.setName(params[0])
				.call();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			dismiss();
		}
		
	}

}
