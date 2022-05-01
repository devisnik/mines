package de.devisnik.android.mine;

import android.app.AlertDialog.Builder;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

class IntroDialogBuilder extends Builder {
	private final MineSweeper mineSweeper;

	public IntroDialogBuilder(final MineSweeper mineSweeper) {
		super(mineSweeper);
		this.mineSweeper = mineSweeper;
		FrameLayout root = (FrameLayout) mineSweeper.findViewById(android.R.id.custom);
		View view = mineSweeper.getLayoutInflater().inflate(R.layout.help, root);
		setView(view);
		bind(view, R.id.help_text, convertNewlineToBr(R.string.intro_message));
		bind(view, R.id.help_faq, createFaqLink()).setMovementMethod(LinkMovementMethod.getInstance());
		bind(view, R.id.help_about, createAboutHtml());
		setTitle(mineSweeper.getString(R.string.intro_title));
		setPositiveButton(R.string.intro_okay, null);
	}

	private String createAboutHtml() {
		return mineSweeper.getString(R.string.intro_version) + ": " + getAppVersion() +
				"<br/>" +
				"<br/>" +
				mineSweeper.getString(R.string.intro_thanx) +
				"<br/>" +
				"<br/>" +
				convertNewlineToBr(R.string.intro_translations);
	}

	private String createFaqLink() {
		return "<a href=\"" +
				mineSweeper.getString(R.string.faq_url) +
				new MinesInfo(mineSweeper).getQueryString() +
				"\">" +
				mineSweeper.getString(R.string.pref_faq) +
				"</a>";
	}

	private TextView bind(final View parent, final int viewId, final String htmlText) {
		TextView textView = (TextView) parent.findViewById(viewId);
		textView.setText(Html.fromHtml(htmlText));
		return textView;
	}

	private String convertNewlineToBr(final int messageId) {
		String description = mineSweeper.getString(messageId);
		return description.replaceAll("\n", "<br/>");
	}

	private String getAppVersion() {
		return new MinesInfo(mineSweeper).getVersionInfo();
	}
}
