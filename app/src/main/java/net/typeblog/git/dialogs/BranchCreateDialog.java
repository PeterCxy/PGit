package net.typeblog.git.dialogs;

import android.content.Context;

import org.eclipse.jgit.api.errors.GitAPIException;

import net.typeblog.git.R;
import net.typeblog.git.support.GitProvider;
import static net.typeblog.git.support.Utility.*;

public class BranchCreateDialog extends BaseRefCreateDialog
{
	public BranchCreateDialog(Context context, GitProvider provider) {
		super(context, provider);
	}

	@Override
	protected void doCreate(String name) throws GitAPIException, RuntimeException {
		mProvider.git().branchCreate()
			.setName(name)
			.call();
	}

	@Override
	protected int getTitle() {
		return R.string.git_branch_create;
	}

}
