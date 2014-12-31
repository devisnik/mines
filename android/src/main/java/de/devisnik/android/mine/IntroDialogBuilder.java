package de.devisnik.android.mine;

import android.app.AlertDialog.Builder;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

class IntroDialogBuilder extends Builder {
	private final MineSweeper itsMineSweeper;

	public IntroDialogBuilder(final MineSweeper mineSweeper) {
		super(mineSweeper);
		itsMineSweeper = mineSweeper;
		View view = mineSweeper.getLayoutInflater().inflate(R.layout.help, null);
		setView(view);
		bind(view, R.id.help_text, convertNewlineToBr(R.string.intro_message));
		bind(view, R.id.help_faq, createFaqLink()).setMovementMethod(LinkMovementMethod.getInstance());
		bind(view, R.id.help_about, createAboutHtml());
		setTitle(mineSweeper.getString(R.string.intro_title));
		setPositiveButton(R.string.intro_okay, null);
	}

	private String createAboutHtml() {
		StringBuilder builder = new StringBuilder();
		builder.append(itsMineSweeper.getString(R.string.intro_version) + ": " + getAppVersion());
		builder.append("<br/>");
		builder.append("<br/>");
		builder.append(itsMineSweeper.getString(R.string.intro_thanx));
		builder.append("<br/>");
		builder.append("<br/>");
		builder.append(convertNewlineToBr(R.string.intro_translations));
		return builder.toString();
	}

	private String createFaqLink() {
		StringBuilder builder = new StringBuilder();
		builder.append("<a href=\"");
		builder.append(itsMineSweeper.getString(R.string.faq_url));
		builder.append(new MinesInfo(itsMineSweeper).getQueryString());
		builder.append("\">");
		builder.append(itsMineSweeper.getString(R.string.pref_faq));
		builder.append("</a>");
		return builder.toString();
	}

	private TextView bind(final View parent, final int viewId, final String htmlText) {
		TextView textView = (TextView) parent.findViewById(viewId);
		textView.setText(Html.fromHtml(htmlText));
		return textView;
	}

	private String convertNewlineToBr(final int messageId) {
		String description = itsMineSweeper.getString(messageId);
		return description.replaceAll("\n", "<br/>");
	}

	private String getAppVersion() {
		return new MinesInfo(itsMineSweeper).getVersionInfo();
	}
}
