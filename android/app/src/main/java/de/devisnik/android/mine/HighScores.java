package de.devisnik.android.mine;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.CursorAdapter;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import java.util.Date;

import de.devisnik.android.mine.data.DBHelper;
import de.devisnik.android.mine.data.Score;
import de.devisnik.android.mine.device.Device;

public class HighScores extends ListActivity {

    private static final int RESULT_NO_HIGHSCORE = RESULT_FIRST_USER;
    private static final String EXTRA_TIME = "time";
    private static final Logger LOGGER = new Logger(HighScores.class);
    private static final int MAX_ENTRIES = 20;
    private static final String BUNDLE_SCORE = "bundle_score";
    private static final int EDIT_SCORE_DIALOG = 101;
    private static final int NEW_SCORE_DIALOG = 102;
    private static final int DELETE_SCORE_DIALOG = 103;

    private class ScoreDialogBuilder extends Builder {

        private final View itsLayout;

        public ScoreDialogBuilder(final int layoutId, final Score score) {
            super(HighScores.this);
            itsLayout = getLayoutInflater().inflate(layoutId, null);
            ScoreBinder.bind(itsLayout, score);
            setView(itsLayout);
        }

        protected TextView findViewById(final int viewId) {
            return (TextView) itsLayout.findViewById(viewId);
        }

        protected void updateList() {
            ((CursorAdapter) getListAdapter()).getCursor().requery();
        }
    }

    private class DeleteScoreDialogBuilder extends ScoreDialogBuilder {

        public DeleteScoreDialogBuilder(final Score score) {
            super(R.layout.score_plain, score);
            setTitle(R.string.score_delete_title);
            setNegativeButton(R.string.another_game_no, null);
            setPositiveButton(R.string.another_game_yes, (dialog, which) -> {
                dbHelper.delete(score);
                updateList();
            });
        }
    }

    private class EditScoreDialogBuilder extends ScoreDialogBuilder {
        public EditScoreDialogBuilder(final Score score) {
            super(R.layout.score_edit, score);
            setPositiveButton(R.string.new_score_confirm, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    TextView text = findViewById(R.id.score_name);
                    String inputName = text.getText().toString().trim();
                    updateName(inputName);
                    updateList();
                }

                private void updateName(final String inputName) {
                    score.name = inputName;
                    dbHelper.updateName(score);
                    settings.setUserNameIfNotSet(inputName);
                }
            });
        }

    }

    private Settings settings;
    private DBHelper dbHelper;
    private Score storedScore;
    private int time;

    public static Intent withTime(Context context, int time) {
        Intent intent = new Intent(context, HighScores.class);
        intent.putExtra(EXTRA_TIME, time);
        return intent;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        debugLog("onCreate");
        super.onCreate(savedInstanceState);
        getDevice().enableHomeButton(this);
        time = getIntent().getIntExtra(EXTRA_TIME, -1);
        getIntent().removeExtra(EXTRA_TIME);

        settings = new Settings(this);
        dbHelper = new DBHelper(this);

        setContentView(R.layout.scores);
        ((MinesApplication) getApplication()).getDevice().setHighScoresTitle(this, getTitle(),
                new GameInfo(settings).createTitle());
        initScoresList();
    }

    private void debugLog(final String msg) {
        LOGGER.d(msg);
    }

    @Override
    protected void onDestroy() {
        debugLog("onDestroy");
        dbHelper.close();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        debugLog("onStart");
        super.onStart();
        if (time < 0)
            // just show list if no score info included in intent
            return;
        storedScore = storeIfHighScore(settings.getUserName(), time);
        time = -1; // make sure we don't store again on restart
        if (storedScore == null) {
            setResult(RESULT_NO_HIGHSCORE);
            finish();
        } else
            showDialog(NEW_SCORE_DIALOG);
    }

    private Cursor createCursor() {
        return dbHelper.createCursor(settings.getBoard(), settings.getLevel());
    }

    public Score storeIfHighScore(final String name, final int seconds) {
        try (Cursor cursor = createCursor()) {
            if (!isHighScore(seconds, cursor))
                return null;
            return storeHighScore(name, seconds, cursor);
        }
    }

    public void clearAll() {
        try (Cursor cursor = createCursor()) {
            while (cursor.moveToNext())
                dbHelper.delete(cursor);
            ((CursorAdapter) getListAdapter()).getCursor().requery();
        }
    }

    private boolean isHighScore(final int seconds, final Cursor cursor) {
        if (seconds <= 0)
            return false;
        if (!cursor.moveToPosition(MAX_ENTRIES - 1)) // adding entries
            // possible
            return true;
        Score lastScore = dbHelper.readScore(cursor);
        return lastScore.time > seconds;
    }

    private Score storeHighScore(final String name, final int seconds, final Cursor cursor) {
        final Score score = createScore(name, seconds);
        long insertId = dbHelper.insert(score);
        cursor.requery();
        deleteObsoleteEntries(cursor);
        Score insertedScore = findScore(insertId, cursor);
        selectEntry(insertedScore);
        return insertedScore;
    }

    private void selectEntry(final Score score) {
        ((CursorAdapter) getListAdapter()).getCursor().requery();
        getListView().setSelectionFromTop(score.rank - 1, 0);
    }

    private void deleteObsoleteEntries(final Cursor cursor) {
        while (cursor.moveToPosition(MAX_ENTRIES)) {
            dbHelper.delete(cursor);
            cursor.requery();
        }
    }

    private Score createScore(final String name, final int seconds) {
        final Score score = new Score();
        score.board = settings.getBoard();
        score.level = settings.getLevel();
        score.name = name;
        score.time = seconds;
        score.date = new Date().getTime();
        return score;
    }

    private Score findScore(final long scoreId, final Cursor cursor) {
        cursor.move(Integer.MIN_VALUE);
        while (cursor.moveToNext()) {
            Score score = dbHelper.readScore(cursor);
            if (score.id == scoreId)
                return score;
        }
        return null;
    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.scores_context, menu);
        ViewGroup title = (ViewGroup) getLayoutInflater().inflate(R.layout.score_plain, null);
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
        Cursor cursor = (Cursor) getListView().getItemAtPosition(info.position);
        Score score = dbHelper.readScore(cursor);
        ScoreBinder.bind(title, score);
        menu.setHeaderView(title);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @SuppressWarnings("boxing")
    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        Cursor cursor = (Cursor) getListView().getItemAtPosition(info.position);
        storedScore = dbHelper.readScore(cursor);
        int i = item.getItemId();
        if (i == R.id.edit_score) {
            showDialog(EDIT_SCORE_DIALOG);
            return true;
        } else if (i == R.id.delete_score) {
            showDialog(DELETE_SCORE_DIALOG);
            return true;
        } else if (i == R.id.share_score) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.score_share_title));
            shareIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    getString(R.string.score_share_message, new GameInfo(settings).createTitle(),
                            storedScore.time));
            startActivity(Intent.createChooser(shareIntent, getString(R.string.score_menu_share)));

            return super.onContextItemSelected(item);
        } else {
            return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        outState.putSerializable(BUNDLE_SCORE, storedScore);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(final Bundle state) {
        storedScore = (Score) state.getSerializable(BUNDLE_SCORE);
        super.onRestoreInstanceState(state);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, MineSweeper.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Dialog onCreateDialog(final int id) {
        switch (id) {
            case NEW_SCORE_DIALOG:
                return onCreateNewScoreDialog();
            case EDIT_SCORE_DIALOG:
                return onCreateEditScoreDialog();
            case DELETE_SCORE_DIALOG:
                return onCreateDeleteScoreDialog();
            default:
                return super.onCreateDialog(id);
        }
    }

    private Dialog onCreateDeleteScoreDialog() {
        AlertDialog dialog = new DeleteScoreDialogBuilder(storedScore).create();
        addRemovingDismissListener(dialog, DELETE_SCORE_DIALOG);
        return dialog;
    }

    private Dialog onCreateEditScoreDialog() {
        AlertDialog dialog = new EditScoreDialogBuilder(storedScore).setTitle(R.string.score_edit_title).create();
        addRemovingDismissListener(dialog, EDIT_SCORE_DIALOG);
        return dialog;
    }

    private Dialog onCreateNewScoreDialog() {
        AlertDialog dialog = new EditScoreDialogBuilder(storedScore).setTitle(R.string.score_new_title).create();
        addRemovingDismissListener(dialog, NEW_SCORE_DIALOG);
        return dialog;
    }

    private void addRemovingDismissListener(final AlertDialog dialog, final int dialogId) {
        dialog.setOnDismissListener(d -> removeDialog(dialogId));
    }

    private void initScoresList() {
        Cursor adapterCursor = createCursor();
        startManagingCursor(adapterCursor);
        ResourceCursorAdapter adapter = new ResourceCursorAdapter(this, R.layout.score, adapterCursor) {

            @Override
            public void bindView(final View view, final Context context, final Cursor cursor) {
                ScoreBinder.bind(view, dbHelper.readScore(cursor));
            }
        };
        setListAdapter(adapter);
        registerForContextMenu(getListView());
    }

    private Device getDevice() {
        return ((MinesApplication) getApplication()).getDevice();
    }

}
