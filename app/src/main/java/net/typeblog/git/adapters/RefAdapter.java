package net.typeblog.git.adapters;

import android.view.View;
import android.widget.TextView;

import org.eclipse.jgit.lib.Ref;

import java.util.List;

import net.typeblog.git.R;
import static net.typeblog.git.support.Utility.*;

public class RefAdapter extends BaseListAdapter<Ref>
{
	public RefAdapter(List<Ref> list) {
		super(list);
	}

	@Override
	protected int getLayoutResource() {
		// TODO: Do not share the same layout with FileAdapter
		return R.layout.file_item;
	}

	@Override
	protected void bindView(View v, Ref item) {
		TextView t = $(v, R.id.file_name);
		t.setText(item.getName());
	}
}
