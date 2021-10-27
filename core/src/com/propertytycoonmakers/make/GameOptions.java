package com.propertytycoonmakers.make;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * This class holds persistent options that are changed within the OptionsScreen and PauseScreen classes
 */
public class GameOptions {

    private static final String PREF_MUSIC_VOLUME = "volume";
    private static final String PREF_MUSIC_ENABLED = "music.enabled";
    private static final String PREF_FX_ENABLED = "sound.enabled";
    private static final String PREF_FX_VOL = "sound";
    private static final String PREFS_NAME = "Property Tycoon";
    private static final String PREFS_FULLSCREEN = "fullscreen";
    private static final String PREFS_IS_ABRIDGED = "abridged";
    private static final String PREFS_ABRIDGED_LENGTH = "abridged.length";

    /**
     * @return returns the preferences within GameOptions
     */
    protected Preferences getPrefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    /**
     * @return returns a float for the current volume
     */
    public float getMusicVolume() {
        return getPrefs().getFloat(PREF_MUSIC_VOLUME, 0.5f);
    }

    /**
     * sets the float value for PREF_MUSIC_VOLUME
     * @param volume The desired volume
     */
    public void setMusicVolume(float volume) {
        getPrefs().putFloat(PREF_MUSIC_VOLUME, volume);
        getPrefs().flush();
    }

    /**
     * @return returns a boolean value as to whether music is on
     */
    public boolean isMusicEnabled() {
        return getPrefs().getBoolean(PREF_MUSIC_ENABLED, true);
    }

    /**
     * changes the value of PREF_MUSIC_ENABLED to true or false
     * @param musicEnabled boolean on whether the music should be on or off, true = on
     */
    public void setMusicEnabled(boolean musicEnabled) {
        getPrefs().putBoolean(PREF_MUSIC_ENABLED, musicEnabled);
        getPrefs().flush();
    }

    /**
     * changes the value of PREFS_FULLSCREEN, true = fullscreen
     * @param fullscreen boolean as to whether the window should be fullscreen or not
     */
    public void setPrefsFullscreen(boolean fullscreen){
        getPrefs().putBoolean(PREFS_FULLSCREEN, fullscreen);
        getPrefs().flush();
    }

    /**
     * @return returns the float value of PREF_FX_VOL
     */
    public float getFxVolume() {
        return getPrefs().getFloat(PREF_FX_VOL, 0.5f);
    }

    /**
     * sets the value of PREF_FX_VOL to the desired float
     * @param volume the desired volume, given as a float between 0 and 1
     */
    public void setFxVolume(float volume) {
        getPrefs().putFloat(PREF_FX_VOL, volume);
        getPrefs().flush();
    }

    /**
     * @return boolean as to whether fx are enabled
     */
    public boolean isFxEnabled() {
        return getPrefs().getBoolean(PREF_FX_ENABLED, true);
    }

    /**
     * sets the value of PREF_FX_ENABLED to true or false
     * @param fxEnabled boolean as to whether fx are enabled, true = on
     */
    public void setFxEnabled(boolean fxEnabled) {
        getPrefs().putBoolean(PREF_FX_ENABLED, fxEnabled);
        getPrefs().flush();
    }

    /**
     * sets the value of PREFS_IS_ABRIDGED to true or false
     * @param abridged boolean as to whether game is abridged or not
     */
    public void setAbridged(boolean abridged) {
        getPrefs().putBoolean(PREFS_IS_ABRIDGED, abridged);
    }

    /**
     * @return boolean as to whether game is abridged or not
     */
    public boolean getAbridged() {
        return getPrefs().getBoolean(PREFS_IS_ABRIDGED);
    }

    /**
     * sets the value of PREFS_ABRIDGED_LENGTH to the desired int
     * @param length the length of the game as an integer
     */
    public void setAbridgedLength(int length) {
        getPrefs().putInteger(PREFS_ABRIDGED_LENGTH, length);
    }

    /**
     * @return the length of the game as an integer
     */
    public int getAbridgedLength() {
        return getPrefs().getInteger(PREFS_ABRIDGED_LENGTH);
    }
}