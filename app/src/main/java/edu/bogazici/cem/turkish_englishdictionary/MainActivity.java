package edu.bogazici.cem.turkish_englishdictionary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static edu.bogazici.cem.turkish_englishdictionary.Dictionary.eng2turk;
import static edu.bogazici.cem.turkish_englishdictionary.Dictionary.turk2eng;

public class MainActivity extends AppCompatActivity {


    private EditText ed;

    private ListView lv;
    private String[] res;

    Dictionary dict;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed = (EditText) findViewById(R.id.editText);

        lv = (ListView) findViewById(R.id.ListView);

        dict = new Dictionary(this);
    }

    public void onClickButton(View v) {
        Toast.makeText(this, "Button clicked!", Toast.LENGTH_LONG).show();
        String turkWord = ed.getText().toString().toLowerCase(new Locale("tr"));

        List<String> meanings = turk2eng.containsKey(turkWord) ? turk2eng.get(turkWord) : new ArrayList<String>();

        if (meanings.size() == 0) {
            String engWord = turkWord;
            meanings = eng2turk.containsKey(turkWord) ? eng2turk.get(engWord) : new ArrayList<String>();
            if (meanings.size() == 0) {
                Toast.makeText(this, "The word you entered has not been found!", Toast.LENGTH_LONG).show();
                return;
            }
        }

        res = meanings.toArray(new String[meanings.size()]);

        ArrayAdapter<String> arrAd = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, res);

        lv.setAdapter(arrAd);

    }

}