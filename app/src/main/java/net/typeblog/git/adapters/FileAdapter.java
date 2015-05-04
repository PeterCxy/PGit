package net.typeblog.git.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import net.typeblog.git.R;
import net.typeblog.git.support.GlobalContext;
import static net.typeblog.git.support.Utility.*;

public class FileAdapter extends BaseListAdapter<File>
{
	public FileAdapter(List<File> list) {
		super(list);
	}

	@Override
	protected int getLayoutResource() {
		return R.layout.file_item;
	}

	@Override
	protected void bindView(View v, File f) {
		TextView name = $(v, R.id.file_name);

		name.setText(f.getName());
		name.setTextColor(GlobalContext.get().getResources().getColor(f.isDirectory() ? R.color.color_primary_dark : R.color.white));
	}
}
