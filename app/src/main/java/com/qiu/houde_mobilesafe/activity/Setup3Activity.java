package com.qiu.houde_mobilesafe.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qiu.houde_mobilesafe.R;
import com.qiu.houde_mobilesafe.utils.Consts;
import com.qiu.houde_mobilesafe.utils.SPUtils;
import com.qiu.houde_mobilesafe.utils.Toasts;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 第3个设置向导页
 *
 * @author Kevin
 */
public class Setup3Activity extends BaseSetupActivity implements View.OnClickListener {

    public static final int REQUEST_CODE = 0x11;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.bt_select_contacts)
    Button btSelectContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        ButterKnife.bind(this);
        if (SPUtils.contains(getApplicationContext(), Consts.SAFE_PHONE)) {
            etPhone.setText((String) SPUtils.get(getApplicationContext(), Consts.SAFE_PHONE, ""));
        }

        btSelectContacts.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        startActivityForResult(new Intent(
                Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            ContentResolver reContentResolverol = getContentResolver();
            Uri contactData = data.getData();
            Cursor cursor = reContentResolverol.query(contactData, null, null, null, null);
            cursor.moveToFirst();
            String username = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            //联系人id
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phoneCursor = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                    null,
                    null);
            while (phoneCursor.moveToNext()) {
                String phoneNum = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phoneNum = phoneNum.replaceAll("-", "").replaceAll(" ", "");
                etPhone.setText(phoneNum);
            }
        }

    }

    @Override
    protected void showPreviousPage() {
        startActivity(new Intent(this, Setup2Activity.class));
        finish();
    }

    @Override
    protected void showNextPage() {
        String phoneNum = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum)) {
            Toasts.showShort(this, "安全号码不能为空!");
            return;
        }
        SPUtils.put(getApplicationContext(), Consts.SAFE_PHONE, phoneNum);
        startActivity(new Intent(this, Setup4Activity.class));
        finish();
    }

}
