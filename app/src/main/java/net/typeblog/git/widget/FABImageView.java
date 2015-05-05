package net.typeblog.git.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

public class FABImageView extends ImageView
{
	private Drawable mBackground;
	
	public FABImageView(Context context) {
		this(context, null);
	}
	
	public FABImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public FABImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setClickable(true);
		
		if (Build.VERSION.SDK_INT >= 21) {
			setElevation(10.4f);
		}
		
		setOutlineProvider(new ViewOutlineProvider() {
			@Override
			public void getOutline(View view, Outline outline) {
				outline.setOval(0, 0, getWidth(), getHeight());
			}
		});
		setClipToOutline(true);
		
		mBackground = getBackground();
		setBackgroundDrawable(new RippleDrawable(new ColorStateList(new int[][]{{}}, new int[]{Color.WHITE}), mBackground, null));
		
	}
}
