package com.angryburg.uapp.utils;

import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.angryburg.uapp.R;
import com.angryburg.uapp.application.United;
import com.angryburg.uapp.fragments.GenericAlertDialogFragment;

/**
 * Stores app-wide properties that should be saved between launches of the app
 */

public final class PropertiesSingleton {
    private static final String TAG = PropertiesSingleton.class.getSimpleName();
    /**
     * used to be "united4_config.json" but it's not called /u/nited anymore
     */
    private static final String CONFIG = "launcher_config.json";
    /**
     * generic singleton
     */
    private static PropertiesSingleton singleton;
    static {
        singleton = new PropertiesSingleton();
    }

    /**
     * gets the singleton
     * @return the singleton
     */
    public static PropertiesSingleton get() {
        return singleton;
    }


    /**
     * the actual properties
     */
    private Map<String, String> properties;


    /**
     * Reads in all the properties from the json file or some generic default properties if that failed
     */
    private PropertiesSingleton() {
        properties = new HashMap<>();
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(United.getContext().openFileInput(CONFIG)));
            reader.beginObject();
            int read = 0;
            while (reader.hasNext()) {
                read++;
                String key = reader.nextName();
                String value = reader.nextString();
                Log.d(TAG, "Read prop " + key);
                properties.put(key, value);
            }
            resetForAppStart();
            if (read > 0) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // default properties that should only be set on the very first run of the app
        properties.put("startup_music", "false");
        properties.put("theme", "normal");
        properties.put("looping", "false");
        properties.put("shuffle", "false");
        properties.put("current_song", "");
        properties.put("debug", "false");
        resetForAppStart();
    }


    /**
     * static properties that never change
     */
    private void resetForAppStart() {
        // the default value for infinite scrolling is set by the userscript, so it doesn't need to be included here
        if (getProperty("userscript").isEmpty()) properties.put("userscript", "true");
        if (getProperty("force_show_back_btn").isEmpty()) properties.put("force_show_back_btn", "true");
        if (getProperty("window_bar_color").isEmpty()) properties.put("window_bar_color", "-25");
        if (getProperty("watch_on_reply").isEmpty()) properties.put("watch_on_reply", "true");
        // Pull the current version and put it in the main screen
        String version = "error";
        try {
            PackageInfo packageInfo = United.getContext().getPackageManager().getPackageInfo(United.getContext().getPackageName(), 0);
            version = String.valueOf(packageInfo.versionName);
        } catch (Exception ignored) {
            //
        }
        properties.put("version_notes", "Version " + version + "!\nTap for Patch Notes");
        // index.html expects is_playing to be false on first load so it can play startup music
        // if it was told to. It doesn't start the music if `is_playing` is set to true, so if you go
        // to music.html, change the song, rotate and come back, it won't override the song you just picked
        // with the startup music again
        properties.put("is_playing", "false");
        //List<String> themes = Arrays.asList("normal", "dotted", "steam", "kira", "meme", "vaporwave");
        // rip the meme theme, it was my favorite
        List<String> themes = Arrays.asList("normal", "vaporwave", "noir", "burg", "unity", "empire", "classy");
        String str = new JSONArray(themes).toString();
        properties.put("all_themes", str);
        HashMap<String, String> map = new HashMap<>();
        ArrayList<String> ordered_songs = new ArrayList<>();
        put(map, ordered_songs, R.raw.hopes_and_dreams, "Hopes and Dreams");
        put(map, ordered_songs, R.raw.a_neon_glow_lights_the_way, "A Neon Glow Lights the Way");
        put(map, ordered_songs, R.raw.welcome_to_va_11_hall_a, "Welcome To VA-11 HALL-A");
        put(map, ordered_songs, R.raw.every_day_is_night, "Every Day is Night");
        put(map, ordered_songs, R.raw.commencing_simulation, "Commencing Simulation");
        put(map, ordered_songs, R.raw.drive_me_wild, "Drive Me Wild");
        put(map, ordered_songs, R.raw.good_for_health_bad_for_education, "Good for Health, Bad for Education");
        put(map, ordered_songs, R.raw.who_was_i, "Who Was I?");
        put(map, ordered_songs, R.raw.troubling_news, "Troubling News");
        put(map, ordered_songs, R.raw.a_gaze_that_invited_disaster, "A Gaze That Invited Disaster");
        put(map, ordered_songs, R.raw.friendly_conversation, "Friendly Conversation");
        put(map, ordered_songs, R.raw.youve_got_me, "You've Got Me");
        put(map, ordered_songs, R.raw.umemoto, "Umemoto");
        put(map, ordered_songs, R.raw.jc_eltons, "JC Elton's");
        put(map, ordered_songs, R.raw.go_go_streaming_chan, "Go! Go! Streaming-chan!");
        put(map, ordered_songs, R.raw.all_systems_go, "All Systems, Go!");
        put(map, ordered_songs, R.raw.where_do_i_go_from_here, "Where Do I Go From Here?");
        put(map, ordered_songs, R.raw.will_you_remember_me, "Will You Remember Me?");
        put(map, ordered_songs, R.raw.everything_will_be_okay, "Everything Will Be Okay");
        put(map, ordered_songs, R.raw.march_of_the_white_knights, "March of the White Knights");
        put(map, ordered_songs, R.raw.a_rene, "A. Rene");
        put(map, ordered_songs, R.raw.neo_avatar, "Neo Avatar");
        put(map, ordered_songs, R.raw.those_who_dwell_in_the_shadows, "Those Who Dwell in The Shadows");
        put(map, ordered_songs, R.raw.nightime_maneuvers, "Nighttime Manuvers");
        put(map, ordered_songs, R.raw.a_star_pierces_the_darkness, "A Star Pierces the Darkness");
        put(map, ordered_songs, R.raw.your_love_is_a_drug, "Your Love is a Drug");
        put(map, ordered_songs, R.raw.through_the_storm_we_will_find_a_way, "Through the Storm We Will Find a Way");
        put(map, ordered_songs, R.raw.synthestitch, "Synthestitch");
        put(map, ordered_songs, R.raw.snowfall, "Snowfall");
        put(map, ordered_songs, R.raw.the_answer_lies_within, "The Answer Lies Within");
        put(map, ordered_songs, R.raw.dawn_approaches, "Dawn Approaches");
        put(map, ordered_songs, R.raw.with_renewed_hope_we_continue_forward, "With Renewed Hope, We Continue Forward");
        put(map, ordered_songs, R.raw.last_call, "Last Call");
        put(map, ordered_songs, R.raw.reminescence, "Reminiscence");
        put(map, ordered_songs, R.raw.believe_in_me_who_believes_in_you, "Believe in Me Who Believes in You");
        put(map, ordered_songs, R.raw.final_result, "Final Result");
        put(map, ordered_songs, R.raw.until_we_meet_again, "Until We Meet Again");
        put(map, ordered_songs, R.raw.digital_drive, "Digital Drive");
        put(map, ordered_songs, R.raw.safe_haven, "Safe Haven");
        put(map, ordered_songs, R.raw.takeshi_ino_okaeri, "O-KA-E-RI");
        put(map, ordered_songs, R.raw.takeshi_ino_the_sciences_dream, "The Science's Dream");
        put(map, ordered_songs, R.raw.takeshi_ino_messenger_from_zero, "Messenger from zero");
        put(map, ordered_songs, R.raw.takeshi_ino_karma, "Karma");
        put(map, ordered_songs, R.raw.takeshi_ino_time_leap_machine, "Time Leap Machine");
        put(map, ordered_songs, R.raw.steins_gate_0_bgm05, "STEINS;GATE 0 - BGM05");
        put(map, ordered_songs, R.raw.steins_gate_0_bgm09, "STEINS;GATE 0 - BGM09");
        put(map, ordered_songs, R.raw.steins_gate_0_bgm11, "STEINS;GATE 0 - BGM11");
        put(map, ordered_songs, R.raw.steins_gate_0_bgm16, "STEINS;GATE 0 - BGM16");
        put(map, ordered_songs, R.raw.steins_gate_0_bgm21, "STEINS;GATE 0 - BGM21");
        put(map, ordered_songs, R.raw.steins_gate_0_bgm22, "STEINS;GATE 0 - BGM22");
        put(map, ordered_songs, R.raw.steins_gate_0_bgm23, "STEINS;GATE 0 - BGM23");
        put(map, ordered_songs, R.raw.steins_gate_0_bgm25, "STEINS;GATE 0 - BGM25");
        put(map, ordered_songs, R.raw.steins_gate_0_bgm26, "STEINS;GATE 0 - BGM26");
        put(map, ordered_songs, R.raw.aythar_return_to_the_moon, "Return to the Moon");
        put(map, ordered_songs, R.raw.elite_dangerous_soundtrack_supercruise_2, "Supercruise 2");
        put(map, ordered_songs, R.raw.elite_dangerous_soundtrack_a_tale_of_five_systems, "A Tale of Five Systems");
        put(map, ordered_songs, R.raw.elite_dangerous_soundtrack_menu, "Elite Dangerous - Menu OST");
        str = new JSONObject(map).toString();
        properties.put("songs", str);
        properties.put("ordered_songs", new JSONArray(ordered_songs).toString());
        properties.put("awoo_endpoint", "https://boards.dangeru.us");
    }


    /**
     * helper function for putting things in both `songs` and `ordered_songs`
     * @param map a map from song name to resource ID
     * @param ordered_songs the list of songs
     * @param id The resource ID
     * @param s The song name
     */
    private static void put(Map<String, String> map, @SuppressWarnings("TypeMayBeWeakened") List<String> ordered_songs, int id, String s) {
        map.put(s, Integer.toString(id));
        ordered_songs.add(s);
    }

    /**
     * Gets a property, or an empty string if it wasn't set
     * @param key the key for the property
     * @return the value of that property or an empty string if it isn't set
     */
    public String getProperty(String key) {
        String res = properties.get(key);
        if (res == null) return "";
        return res;
    }


    /**
     * sets a property, then writes the entire json file out to the disk
     * @param key The property to set
     * @param value the value for that property
     */
    public void setProperty(String key, String value) {
        // If we're playing or pausing the music, reload index.html
        //noinspection EqualsReplaceableByObjectsCall
        if (key.equals("is_playing")) {
            if (!getProperty(key).equalsIgnoreCase(value)) {
                //Thread.dumpStack();
                NotifierService.notify(NotifierService.NotificationType.RELOAD_INDEX);
            }
        }
        properties.put(key, value);
        try {
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(United.getContext().openFileOutput(CONFIG, Context.MODE_PRIVATE)));
            writer.beginObject();
            for (Map.Entry<String, String> i : properties.entrySet()) {
                Log.d(TAG, "Writing prop " + i.getKey());
                writer.name(i.getKey());
                writer.value(i.getValue());
            }
            writer.endObject();
            writer.close();
        } catch (Exception ignored) {
            //
        }
    }

    /**
     * Resets all properties and forcibly closes the program to prevent them from being rewritten
     * on the next setProperty call
     * @param fragman a fragment manager used to show an error message, if applicable
     */
    public static void resetAllAndExit(FragmentManager fragman) {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(United.getContext().openFileOutput(CONFIG, Context.MODE_PRIVATE));
            writer.write("{}");
            writer.flush();
            writer.close();
            System.exit(0);
        } catch (Exception e) {
            GenericAlertDialogFragment.newInstance("Unexpected error - " + e, fragman);
        }
    }
}
