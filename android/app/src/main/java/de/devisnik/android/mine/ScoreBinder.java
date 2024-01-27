package de.devisnik.android.mine;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import de.devisnik.android.mine.data.Score;

public class ScoreBinder {

    public static void bind(final View view, final Score score) {
        bindText(view, R.id.score_name, getName(score));
        bindText(view, R.id.score_date, getDate(view.getContext(), score));
        bindText(view, R.id.score_time, getTime(score));
        bindText(view, R.id.score_rank, getRank(score));
    }

    private static void bindText(final View view, final int textViewId, final CharSequence text) {
        TextView timeText = (TextView) view.findViewById(textViewId);
        timeText.setText(text);
    }

    private static String getTime(final Score score) {
        return Integer.toString(score.time);
    }

    private static String getName(final Score score) {
        return score.name;
    }

    private static CharSequence getDate(final Context context, final Score score) {
        return DateUtils.getRelativeTimeSpanString(context, score.date, true);
    }

    private static String getRank(final Score score) {
        return score.rank + ".";
    }

}
