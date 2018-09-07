package mbaas.com.nifcloud.androidsegmentpushapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nifcloud.mbaas.core.DoneCallback;
import com.nifcloud.mbaas.core.NCMB;
import com.nifcloud.mbaas.core.NCMBException;
import com.nifcloud.mbaas.core.NCMBInstallation;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    TextView _objectId;
    TextView _appversion;
    Spinner _channels;
    TextView _devicetoken;
    TextView _sdkversion;
    TextView _timezone;
    TextView _createdate;
    TextView _updatedate;
    EditText _txtPrefectures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //**************** APIキーの設定とSDKの初期化 **********************
        NCMB.initialize(this.getApplicationContext(), "YOUR_APPLICATION_KEY", "YOUR_CLIENT_KEY");

        final NCMBInstallation installation = NCMBInstallation.getCurrentInstallation();

        setContentView(R.layout.activity_main);

        //表示する端末情報のデータを反映
        _objectId = (TextView) findViewById(R.id.txtObject);
        _appversion = (TextView) findViewById(R.id.txtAppversion);
        _channels = (Spinner) findViewById(R.id.spinChannel);
        _devicetoken = (TextView) findViewById(R.id.txtDevicetoken);
        _sdkversion = (TextView) findViewById(R.id.txtSdkversion);
        _timezone = (TextView) findViewById(R.id.txtTimezone);
        _createdate = (TextView) findViewById(R.id.txtCreatedate);
        _updatedate = (TextView) findViewById(R.id.txtUpdatedate);
        _txtPrefectures = (EditText) findViewById(R.id.txtPrefecture);

        //表示する端末情報を指定
        _objectId.setText(installation.getObjectId());
        _appversion.setText(installation.getAppVersion());
        try {
            if (installation.getChannels() != null) {
                String selectChannel = installation.getChannels().get(0).toString();
                String[] channelArray = new String[]{"A", "B", "C", "D"};
                Integer selectId = Arrays.asList(channelArray).indexOf(selectChannel);
                _channels.setSelection(selectId);
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        _devicetoken.setText(installation.getDeviceToken());
        _sdkversion.setText(installation.getSDKVersion());
        _timezone.setText(installation.getTimeZone());
        try {
            _createdate.setText(installation.getCreateDate().toString());
            _updatedate.setText(installation.getUpdateDate().toString());
        } catch (NCMBException e) {
            e.printStackTrace();
        }
        if (installation.getString("Prefectures") != null) {
            _txtPrefectures.setText(installation.getString("Prefectures"));
        }

        Button _btnSave = (Button) findViewById(R.id.btnSave);
        _btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _channels = (Spinner) findViewById(R.id.spinChannel);
                _txtPrefectures = (EditText) findViewById(R.id.txtPrefecture);
                String prefectures = _txtPrefectures.getText().toString();
                String item = (String) _channels.getSelectedItem();
                JSONArray tmpArray = new JSONArray();
                tmpArray.put(item);
                installation.setChannels(tmpArray);
                try {
                    installation.put("Prefectures", prefectures);
                } catch (NCMBException e) {
                    e.printStackTrace();
                }
                installation.saveInBackground(new DoneCallback() {
                    @Override
                    public void done(NCMBException e) {
                        if (e != null) {
                            //保存失敗
                            Toast.makeText(MainActivity.this, "端末情報の保存に失敗しました。" + e.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            //保存成功
                            Toast.makeText(MainActivity.this, "端末情報の保存に成功しました。", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }


}
