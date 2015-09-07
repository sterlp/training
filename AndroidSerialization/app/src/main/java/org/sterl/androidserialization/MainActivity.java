package org.sterl.androidserialization;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.sterl.performancetest.AbstractTest;
import org.sterl.performancetest.SerializationJacksonTest;
import org.sterl.performancetest.SerializationProtoTest;
import org.sterl.performancetest.SerializationWireTest;
import org.sterl.performancetest.TestRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {


    @Click(R.id.cmdRunTest)
    @Background
    public void runTests() {
        final int cycles = 100000;
        Log.d("TestRunner", "started " + cycles + " cycles ...");
        try {
            List<AbstractTest> tests = new ArrayList();
            tests.add(new SerializationJacksonTest(cycles));
            tests.add(new SerializationProtoTest(cycles));
            tests.add(new SerializationWireTest(cycles));

            Iterator var2 = tests.iterator();
            while(var2.hasNext()) {
                System.gc();
                Thread.sleep(500);
                StringBuilder result = new StringBuilder();
                AbstractTest test = (AbstractTest)var2.next();
                test.doTest(result);

                Log.d(var2.getClass().getSimpleName(), result.toString());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Log.d("TestRunner", "done.");
    }
}
