package com.bijoykochar.launcher.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bijoykochar.launcher.R;
import com.bijoykochar.launcher.util.Preferences;
import com.bijoykochar.launcher.view.AudioPlayerView;
import com.bijoykochar.launcher.view.CalendarView;
import com.bijoykochar.launcher.view.CallLogsView;
import com.bijoykochar.launcher.view.MessageView;

import java.util.Map;

/**
 * The information page fragment
 * Created by bijoy on 10/17/15.
 */
public class InformationPageFragment extends Fragment {

    CallLogsView callLogsView;
    CalendarView calendarView;
    MessageView messageView;
    AudioPlayerView audioPlayerView;
    Context context;
    Map<String, Boolean> settings;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.information_page, container, false);
        settings = Preferences.getInstance(getActivity()).getSettings();

        setupRefresh(rootView);
        setupCallLogs(rootView);
        setupSearchBox(rootView);
        setupCalendarView(rootView);
        setupMessageView(rootView);
        setupAudioPlayer(rootView);
        return rootView;
    }

    public void setupCallLogs(View rootView) {
        callLogsView = (CallLogsView) rootView.findViewById(R.id.call_logs);
        callLogsView.init(getActivity());
    }

    public void setupAudioPlayer(View rootView) {
        audioPlayerView = (AudioPlayerView) rootView.findViewById(R.id.audio_player);
        audioPlayerView.init(getActivity());
    }

    public void setupCalendarView(View rootView) {
        calendarView = (CalendarView) rootView.findViewById(R.id.calendar_view);
        calendarView.init(getActivity());
    }

    public void setupMessageView(View rootView) {
        messageView = (MessageView) rootView.findViewById(R.id.message_view);
        messageView.init(getActivity());
    }

    public void setupRefresh(View rootView) {
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void refreshData() {
        settings = Preferences.getInstance(getActivity()).getSettings();
        if (settings.get(Preferences.Keys.SETTINGS_SHOW_CALLS)) {
            getCallAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            callLogsView.setVisibility(View.GONE);
        }

        if (settings.get(Preferences.Keys.SETTINGS_SHOW_MUSIC)) {
            getMusicAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            audioPlayerView.setVisibility(View.GONE);
        }

        if (settings.get(Preferences.Keys.SETTINGS_SHOW_CALENDAR)) {
            getCalendarAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            calendarView.setVisibility(View.GONE);
        }

        if (settings.get(Preferences.Keys.SETTINGS_SHOW_MESSAGES)) {
            getMessageAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            messageView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    public void onStart() {
        super.onStart();
        context = getActivity();
    }

    private void setupSearchBox(View rootView) {
        final EditText searchBox = (EditText) rootView.findViewById(R.id.search_box);
        searchBox.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                    intent.putExtra(SearchManager.QUERY, searchBox.getText().toString());
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        ImageView voiceSearch = (ImageView) rootView.findViewById(R.id.voice_search);
        voiceSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sp = new Intent(RecognizerIntent.ACTION_WEB_SEARCH);
                sp.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                sp.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Please");
                startActivity(sp);
            }
        });
    }

    private AsyncTask<Void, Void, Void> getCallAsyncTask() {
        return new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                callLogsView.getData(context);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                callLogsView.setVisibility(View.VISIBLE);
                callLogsView.displayCalls(context);
            }
        };
    }

    private AsyncTask<Void, Void, Void> getMusicAsyncTask() {
        return new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                audioPlayerView.getData(context);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                audioPlayerView.setVisibility(View.VISIBLE);
                audioPlayerView.displayAudios(context);
            }
        };
    }

    private AsyncTask<Void, Void, Void> getCalendarAsyncTask() {
        return new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                calendarView.getData(context);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                calendarView.setVisibility(View.VISIBLE);
                calendarView.displayEvents(context);
            }
        };
    }

    private AsyncTask<Void, Void, Void> getMessageAsyncTask() {
        return new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                messageView.getData(context);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                messageView.setVisibility(View.VISIBLE);
                messageView.displaySms(context);
            }
        };
    }

}
