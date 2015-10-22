package zy.com.leetcode;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import network.LeetCodeSpider;
import utils.MyPreference;

/**
 * Created by zy on 15-10-17.
 */
public class LoginActivity extends AppCompatActivity{

    private EditText userEdit;
    private EditText pwdEdit;

    private Button loginButton;

    private ProgressDialog dialog;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            if (msg.obj == null){
                myFinish(RESULT_CANCELED, null);
                return ;
            }

            String name = (String) msg.obj;
            myFinish(RESULT_OK, name);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init(){
        userEdit = (EditText) this.findViewById(R.id.edit_username);
        pwdEdit = (EditText) this.findViewById(R.id.edit_pwd);

        loginButton = (Button) this.findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(userEdit.getText().toString().trim(),pwdEdit.getText().toString().trim());
//                login("zy0x5f3759df@outlook.com","zhangyi5");
            }
        });

        dialog = new ProgressDialog(this);
        dialog.setMessage("login...");
    }

    public void myFinish(int resCode,String name){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("name",name);
        setResult(resCode, intent);
        finish();
    }

    private void login(final String username, final String pwd){
        if (username == null || pwd == null || username.isEmpty() || pwd.isEmpty()){
            Toast.makeText(this,"fill the mes",Toast.LENGTH_SHORT);
            return ;
        }
        dialog.show();
        new Thread(){
            @Override
            public void run() {
                String name = LeetCodeSpider.getInstance().login(username,pwd);
                MyPreference.getInstance().setNameAndPwd(LoginActivity.this,username,pwd);
                handler.sendMessage(handler.obtainMessage(0, name));
            }
        }.start();
    }
}
