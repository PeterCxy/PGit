package net.typeblog.git.dialogs;

import android.content.Context;

import org.eclipse.jgit.api.errors.GitAPIException;

import net.typeblog.git.R;
import net.typeblog.git.support.GitProvider;
import net.typeblog.git.support.RepoManager;

public class TagCreateDialog extends BaseRefCreateDialog
{
	public TagCreateDialog(Context context, GitProvider provider) {
		super(context, provider);
	}

	@Override
	protected int getTitle() {
		return R.string.git_tag_create;
	}

	@Override
	protected void doCreate(String name) throws GitAPIException, RuntimeException {
		mProvider.git().tag()
			.setName(name)
			.setTagger(RepoManager.getInstance().getCommitterIdent())
			.call();
	}

	
}
