package net.typeblog.git.adapters;

import android.view.View;
import android.widget.TextView;

import java.util.List;

import net.typeblog.git.R;
import static net.typeblog.git.support.Utility.*;

public class StringArrayAdapter extends BaseListAdapter<String>
{
	public StringArrayAdapter(List<String> list) {
		super(list);
	}

	@Override
	protected int getLayoutResource() {
		return R.layout.file_item;
	}

	@Override
	protected void bindView(View v, String item) {
		TextView text = $(v, R.id.file_name);
		text.setText(item);
	}


}
