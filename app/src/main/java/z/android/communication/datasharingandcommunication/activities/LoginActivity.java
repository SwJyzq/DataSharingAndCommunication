package z.android.communication.datasharingandcommunication.activities;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import z.android.communication.datasharingandcommunication.R;
import z.android.communication.datasharingandcommunication.clients.Client;
import z.android.communication.datasharingandcommunication.clients.ClientOutputThread;
import z.android.communication.datasharingandcommunication.interfaces.Constants;
import z.android.communication.datasharingandcommunication.interfaces.MyApplication;
import z.android.communication.datasharingandcommunication.interfaces.User;
import z.android.communication.datasharingandcommunication.service.GetMsgService;
import z.android.communication.datasharingandcommunication.transport.TranObject;
import z.android.communication.datasharingandcommunication.transport.TranObjectType;
import z.android.communication.datasharingandcommunication.utils.DialogFactory;
import z.android.communication.datasharingandcommunication.utils.Encode;
import z.android.communication.datasharingandcommunication.utils.SharedPreferenceUtil;
import z.android.communication.datasharingandcommunication.utils.UserDB;

public class LoginActivity extends MyActivity {
    private Button mRegisterButton,mLoginButton;
    private EditText mAccount,mPassword;
    private CheckBox mAutoSavePassword;
    private MyApplication mApplication;

    private View mMoreView;// “更多登录选项”的view
    private ImageView mMoreImage;// “更多登录选项”的箭头图片
    private View mMoreMenuView;// “更多登录选项”中的内容view
    private MenuInflater menuInflater;// 菜单
    private boolean mShowMenu = false;// “更多登录选项”的内容是否显示

    private Dialog mDialog = null;

    @Override
    public void getMessage(TranObject msg) {
        if (msg != null){
            switch (msg.getType()){
                case LOGIN:{
                    try {
                        List<User> list = (List<User>)msg.getObject();
                        if (msg.getObject()==null){
                            Log.d("login-activity", "getMessage: there is no object received");
                        }
                        if (list==null){
                            Log.d("login-activity", "getMessage: list of user is empty");
                            list = new ArrayList<User>();
                        }
                        if (list.size()>0){
                            SharedPreferenceUtil util = new SharedPreferenceUtil(LoginActivity.this,Constants.SAVE_USER);
                            util.setPassword(mPassword.getText().toString());
                            util.setId(mAccount.getText().toString());
                            util.setEmail(list.get(0).getEmail());
                            util.setName(list.get(0).getName());
                            UserDB userDB = new UserDB(LoginActivity.this);
                            userDB.addUser(list);
                            Intent i = new Intent(LoginActivity.this,
                                    HomePageActivity.class);
                            i.putExtra(Constants.MESSAGE_KEY, msg);
                            startActivity(i);
                            if (mDialog.isShowing()){
                                mDialog.dismiss();
                            }
                            finish();
                            Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_SHORT).show();
                        }else {
                            DialogFactory.ToastDialog(LoginActivity.this,"登录","亲！您的帐号或密码错误哦");
                            if (mDialog.isShowing()){
                                mDialog.dismiss();
                            }
                        }
                    }catch (Exception e){
                        Log.d("login exception", "getMessage: "+e.toString());
                    }
                    break;
                }
                default: break;
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mApplication = (MyApplication)this.getApplicationContext();
        initView();
        initListeners();
        menuInflater = new MenuInflater(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Intent serviceIntent = new Intent(LoginActivity.this,GetMsgService.class);
            startService(serviceIntent);
        }catch (Exception e){
            Toast.makeText(LoginActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("LoginActivity Exception", "onResume: " + e.toString());
        }
    }

    public void initView() {
        mMoreView = findViewById(R.id.more);
        mMoreMenuView = findViewById(R.id.more_menu);
        mMoreImage = findViewById(R.id.more_image);

        mAutoSavePassword = findViewById(R.id.auto_save_password);

        mRegisterButton = findViewById(R.id.register_btn);
        mLoginButton = findViewById(R.id.login_btn);

        mAccount = findViewById(R.id.login_accounts);
        mPassword = findViewById(R.id.login_password);
        if (mAutoSavePassword.isChecked()) {
            SharedPreferenceUtil util = new SharedPreferenceUtil(
                    LoginActivity.this, Constants.SAVE_USER);
            mAccount.setText(util.getId());
            mPassword.setText(util.getPassword());
        }
    }

    public void initListeners(){
        mMoreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreView(!mShowMenu);
            }
        });
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRegisterActivity();
            }
        });
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    public void showMoreView(boolean bShow) {
        if (bShow) {
            mMoreMenuView.setVisibility(View.GONE);
            mMoreImage.setImageResource(R.drawable.login_more_up);
            mShowMenu = true;
        } else {
            mMoreMenuView.setVisibility(View.VISIBLE);
            mMoreImage.setImageResource(R.drawable.login_more);
            mShowMenu = false;
        }
    }

    public void goRegisterActivity(){
        Intent intent = new Intent();
        intent.setClass(this,RegisterActivity.class);
        startActivity(intent);
    }

    private void showRequestDialog(){
        if (mDialog!=null){
            mDialog.dismiss();
            mDialog = null;
        }
        mDialog = DialogFactory.createRequestDialog(this,"正在验证帐号......");
        mDialog.show();
    }

    private void submit() {
        String accounts = mAccount.getText().toString();
        String password = mPassword.getText().toString();
        if (accounts.length() == 0 || password.length() == 0) {
            DialogFactory.ToastDialog(this, "登录", "亲！帐号或密码不能为空哦");
        } else {
            showRequestDialog();
            // 通过Socket验证信息
            if (mApplication.isClientStart()) {
                Client client = mApplication.getClient();
                ClientOutputThread out = client.getClientOutputThread();
                TranObject<User> userTranObject = new TranObject<User>(TranObjectType.LOGIN);
                User user = new User();
                user.setId(Integer.parseInt(accounts));
                user.setPassword(Encode.getEncode("MD5", password));
                userTranObject.setObject(user);
                out.setMsg(userTranObject);
            } else {
                if (mDialog.isShowing())
                    mDialog.dismiss();
                DialogFactory.ToastDialog(LoginActivity.this, "登录",
                        "亲！服务器暂未开放哦");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuInflater.inflate(R.menu.login_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login_menu_setting:
                setDialog();
                break;
            case R.id.login_menu_exit:
                exitDialog(LoginActivity.this, "提示", "亲！您真的要退出吗？");
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        exitDialog(LoginActivity.this, "提示", "亲！您真的要退出吗？");
    }

    private void exitDialog(Context context, String title, String msg) {
        new AlertDialog.Builder(context).setTitle(title).setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mApplication.isClientStart()) {// 如果连接还在，说明服务还在运行
                            // 关闭服务
                            Intent service = new Intent(LoginActivity.this,
                                    GetMsgService.class);
                            stopService(service);
                        }
                        close();// 调用父类自定义的循环关闭方法
                    }
                }).setNegativeButton("取消", null).create().show();
    }

    private void setDialog() {
        final View view = LayoutInflater.from(LoginActivity.this).inflate(
                R.layout.setting_view, null);
        new AlertDialog.Builder(LoginActivity.this).setTitle("设置服务器ip、port")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 把ip和port保存到文件中
                        EditText ipEditText = view.findViewById(R.id.setting_ip);
                        EditText portEditText = view.findViewById(R.id.setting_port);
                        String ip = ipEditText.getText().toString();
                        String port = portEditText.getText().toString();
                        SharedPreferenceUtil util = new SharedPreferenceUtil(
                                LoginActivity.this, Constants.IP_PORT);
                        if (ip.length() > 0 && port.length() > 0) {
                            util.setIp(ip);
                            util.setPort(Integer.valueOf(port));
                            Toast.makeText(getApplicationContext(),
                                    "亲！保存成功，重启生效哦", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),
                                    "亲！ip和port都不能为空哦", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("取消", null).create().show();
    }
}
