package z.android.communication.datasharingandcommunication.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import z.android.communication.datasharingandcommunication.R;
import z.android.communication.datasharingandcommunication.clients.Client;
import z.android.communication.datasharingandcommunication.clients.ClientOutputThread;
import z.android.communication.datasharingandcommunication.interfaces.MyApplication;
import z.android.communication.datasharingandcommunication.interfaces.User;
import z.android.communication.datasharingandcommunication.transport.TranObject;
import z.android.communication.datasharingandcommunication.transport.TranObjectType;
import z.android.communication.datasharingandcommunication.utils.DialogFactory;
import z.android.communication.datasharingandcommunication.utils.Encode;

public class RegisterActivity extends MyActivity {
    private Button mRegisterButton,mBackButton;
    private EditText mEmail,mNickname,mPassword,mPasswordConfirm;

    private MyApplication mApplication;

    private Dialog mDialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        mApplication = (MyApplication)this.getApplication();
        initWidgets();
        initListeners();
    }

    private void showRequestDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        mDialog = DialogFactory.createRequestDialog(this, "正在注册中...");
        mDialog.show();
    }

    private void initWidgets(){
        mRegisterButton = findViewById(R.id.register_btn);
        mBackButton = findViewById(R.id.reg_back_btn);
        mEmail = findViewById(R.id.reg_email);
        mNickname = findViewById(R.id.reg_name);
        mPassword = findViewById(R.id.reg_password);
        mPasswordConfirm = findViewById(R.id.reg_password2);
    }

    private void initListeners(){
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast(RegisterActivity.this);
            }
        });
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    estimate();
                }catch (Exception e){
                    Toast.makeText(RegisterActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void estimate() {
        String email = mEmail.getText().toString();
        String name = mNickname.getText().toString();
        String password = mPassword.getText().toString();
        String password2 = mPasswordConfirm.getText().toString();
        if (email.equals("") || name.equals("") || password.equals("")
                || password2.equals("")) {
            DialogFactory.ToastDialog(RegisterActivity.this, "注册",
                    "亲！带*项是不能为空的哦");
        } else {
            if (password.equals(password2)) {
                showRequestDialog();
                // 提交注册信息
                if (mApplication.isClientStart()) {// 如果已连接上服务器
                    Client client = mApplication.getClient();
                    ClientOutputThread out = client.getClientOutputThread();
                    TranObject<User> userTranObject = new TranObject<User>(TranObjectType.REGISTER);
                    User user = new User();
                    user.setEmail(email);
                    user.setName(name);
                    user.setPassword(Encode.getEncode("MD5", password));
                    userTranObject.setObject(user);
                    out.setMsg(userTranObject);
                } else {
                    if (mDialog.isShowing())
                        mDialog.dismiss();
                    DialogFactory.ToastDialog(this, "注册", "亲！服务器暂未开放哦");
                }
            } else {
                DialogFactory.ToastDialog(RegisterActivity.this, "注册",
                        "亲！您两次输入的密码不同哦");
            }
        }
    }

    private void toast(Context context) {
        new AlertDialog.Builder(context).setTitle("注册")
                .setMessage("亲！您真的不注册了吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("取消", null).create().show();
    }
    @Override
    public void getMessage(TranObject msg) {
        switch (msg.getType()) {
            case REGISTER:
                User u = (User) msg.getObject();
                int id = u.getId();
                if (id > 0) {
                    if (mDialog != null) {
                        mDialog.dismiss();
                        mDialog = null;
                    }
                    DialogFactory.ToastDialog(RegisterActivity.this, "注册",
                            "亲！请牢记您的登录帐号哦：" + id);
                } else {
                    if (mDialog != null) {
                        mDialog.dismiss();
                        mDialog = null;
                    }
                    DialogFactory.ToastDialog(RegisterActivity.this, "注册",
                            "亲！很抱歉！暂时无法注册哦");
                }
                break;
            default:
                break;
        }
    }
}
