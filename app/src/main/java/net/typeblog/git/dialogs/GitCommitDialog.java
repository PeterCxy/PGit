package net.typeblog.git.dialogs;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.util.ArrayList;
import java.util.List;

import net.typeblog.git.R;
import net.typeblog.git.support.GitProvider;
import net.typeblog.git.support.RepoManager;
import net.typeblog.git.tasks.GitTask;
import static net.typeblog.git.support.Utility.*;

public class GitCommitDialog extends ToolbarDialog
{
	private List<String> mUncommitted = new ArrayList<>();
	private GitProvider mProvider;
	private ListView mList;
	private CheckBox mAll, mAmend;
	private EditText mMessage;
	
	public GitCommitDialog(Context context, GitProvider provider) {
		super(context);
		mProvider = provider;
	}

	@Override
	protected int getLayoutResource() {
		return R.layout.commit;
	}

	@Override
	protected void onInitView() {
		setTitle(R.string.git_commit);
		
		mAll = $(this, R.id.commit_all);
		mAmend = $(this, R.id.commit_amend);
		mMessage = $(this, R.id.commit_message);
		mList = $(this, R.id.commit_file);
		
		try {
			mUncommitted.addAll(mProvider.git().status().call().getUncommittedChanges());
		} catch (GitAPIException e) {
			
		}
		
		if (mUncommitted.size() == 0) {
			Toast.makeText(getContext(), R.string.no_modify, Toast.LENGTH_SHORT).show();
			mList.setVisibility(View.GONE);
			mAll.setVisibility(View.GONE);
			mAmend.setChecked(true);
		}
		
		mList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
		mList.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.item_with_check_multiline, R.id.item_name, mUncommitted));
		
		mAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton button, boolean checked) {
				mList.setVisibility(checked ? View.GONE : View.VISIBLE);
			}
		});
	}

	@Override
	protected void onConfirm() {
		new CommitTask().execute();
	}
	
	private class CommitTask extends GitTask<Void> {
		String message;
		boolean exit = false;
		
		public CommitTask() {
			super(getContext(), mProvider);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			message = mMessage.getText().toString().trim();

			if (message.equals("")) {
				Toast.makeText(getContext(), R.string.no_message, Toast.LENGTH_SHORT).show();
				exit = true;
			}
		}

		@Override
		protected void doGitTask(GitProvider provider, Void... params) throws GitAPIException, RuntimeException {
			if (!exit) {
				CommitCommand commit = provider.git().commit();
				commit.setAmend(mAmend.isChecked());
				commit.setMessage(message);

				boolean all = mAll.isChecked();

				if (all) {
					commit.setAll(true);
				} else {
					commit.setAll(false);

					SparseBooleanArray a = mList.getCheckedItemPositions();
					for (int i = 0; i < a.size(); i++) {
						int pos = a.keyAt(i);
						if (a.valueAt(i)) {
							commit.setOnly(mUncommitted.get(pos));
						}
					}

				}

				// Committer
				RepoManager m = RepoManager.getInstance();
				commit.setCommitter(m.getCommitterName(), m.getCommitterEmail());

				commit.call();
			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dismiss();
		}
	}
}
