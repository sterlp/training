package org.sterl.gcm.android;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(SharedPref.Scope.UNIQUE)
public interface GcmPreferences {
    String token();
    @DefaultBoolean(false)
    boolean registered();
}
