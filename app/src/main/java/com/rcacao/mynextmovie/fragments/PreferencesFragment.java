package com.rcacao.mynextmovie.fragments;

import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.rcacao.mynextmovie.R;

public class PreferencesFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.preferencias);

        EditTextPreference preference = (EditTextPreference) findPreference(getString(R.string.pref_key_num_col));
        preference.setOnPreferenceChangeListener(this);
        changeSummary(preference,preference.getText());

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if(preference.getKey().equals(getString(R.string.pref_key_num_col))){

            Toast toastErro = Toast.makeText(getContext(),"Informe um valor entre 1 e 5.",Toast.LENGTH_SHORT);

            try{
                int numCol = Integer.parseInt((String) newValue);
                if (numCol < 1 || numCol > 5) {
                    toastErro.show();
                    return false;
                }
            }
            catch (NumberFormatException ex){
                toastErro.show();
                return false;
            }

        }
        changeSummary(preference, (String) newValue);
        return true;
    }

    private void changeSummary(Preference preference, String value){
        preference.setSummary(value);
    }


}
