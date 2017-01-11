package code.caishifu.radiogroup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import code.caishifu.radiogroup.view.CaishifuRadioView;

public class MainActivity extends AppCompatActivity {
    private CaishifuRadioView mTest ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTest = (CaishifuRadioView) findViewById(R.id.test);

        mTest.setOnItemClickListener(new CaishifuRadioView.OnItemClickListener() {
            @Override
            public void clickOnPosition(int position) {
                Toast.makeText(MainActivity.this , "you lick " + position , Toast.LENGTH_LONG ).show();
            }
        });
    }
}
