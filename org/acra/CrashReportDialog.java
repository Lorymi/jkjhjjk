package org.acra;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class CrashReportDialog extends BaseCrashReportDialog implements OnClickListener, OnDismissListener {
    private static final String STATE_COMMENT = "comment";
    private static final String STATE_EMAIL = "email";
    AlertDialog mDialog;
    private SharedPreferences prefs;
    private EditText userComment;
    private EditText userEmail;

    /* access modifiers changed from: protected */
    public View buildCustomView(Bundle bundle) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(10, 10, 10, 10);
        linearLayout.setLayoutParams(new LayoutParams(-1, -2));
        linearLayout.setFocusable(true);
        linearLayout.setFocusableInTouchMode(true);
        ScrollView scrollView = new ScrollView(this);
        linearLayout.addView(scrollView, new LinearLayout.LayoutParams(-1, -1, 1.0f));
        LinearLayout linearLayout2 = new LinearLayout(this);
        linearLayout2.setOrientation(1);
        scrollView.addView(linearLayout2);
        TextView textView = new TextView(this);
        int resDialogText = ACRA.getConfig().resDialogText();
        if (resDialogText != 0) {
            textView.setText(getText(resDialogText));
        }
        linearLayout2.addView(textView);
        int resDialogCommentPrompt = ACRA.getConfig().resDialogCommentPrompt();
        if (resDialogCommentPrompt != 0) {
            TextView textView2 = new TextView(this);
            textView2.setText(getText(resDialogCommentPrompt));
            textView2.setPadding(textView2.getPaddingLeft(), 10, textView2.getPaddingRight(), textView2.getPaddingBottom());
            linearLayout2.addView(textView2, new LinearLayout.LayoutParams(-1, -2));
            this.userComment = new EditText(this);
            this.userComment.setLines(2);
            if (bundle != null) {
                String string = bundle.getString(STATE_COMMENT);
                if (string != null) {
                    this.userComment.setText(string);
                }
            }
            linearLayout2.addView(this.userComment);
        }
        int resDialogEmailPrompt = ACRA.getConfig().resDialogEmailPrompt();
        if (resDialogEmailPrompt != 0) {
            TextView textView3 = new TextView(this);
            textView3.setText(getText(resDialogEmailPrompt));
            textView3.setPadding(textView3.getPaddingLeft(), 10, textView3.getPaddingRight(), textView3.getPaddingBottom());
            linearLayout2.addView(textView3);
            this.userEmail = new EditText(this);
            this.userEmail.setSingleLine();
            this.userEmail.setInputType(33);
            this.prefs = getSharedPreferences(ACRA.getConfig().sharedPreferencesName(), ACRA.getConfig().sharedPreferencesMode());
            String str = null;
            if (bundle != null) {
                str = bundle.getString(STATE_EMAIL);
            }
            if (str != null) {
                this.userEmail.setText(str);
            } else {
                this.userEmail.setText(this.prefs.getString(ACRA.PREF_USER_EMAIL_ADDRESS, ""));
            }
            linearLayout2.addView(this.userEmail);
        }
        return linearLayout;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        String str;
        if (i == -1) {
            String str2 = this.userComment != null ? this.userComment.getText().toString() : "";
            if (this.prefs == null || this.userEmail == null) {
                str = "";
            } else {
                str = this.userEmail.getText().toString();
                Editor edit = this.prefs.edit();
                edit.putString(ACRA.PREF_USER_EMAIL_ADDRESS, str);
                edit.commit();
            }
            sendCrash(str2, str);
        } else {
            cancelReports();
        }
        finish();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Builder builder = new Builder(this);
        int resDialogTitle = ACRA.getConfig().resDialogTitle();
        if (resDialogTitle != 0) {
            builder.setTitle(resDialogTitle);
        }
        int resDialogIcon = ACRA.getConfig().resDialogIcon();
        if (resDialogIcon != 0) {
            builder.setIcon(resDialogIcon);
        }
        builder.setView(buildCustomView(bundle));
        builder.setPositiveButton(getText(ACRA.getConfig().resDialogPositiveButtonText()), this);
        builder.setNegativeButton(getText(ACRA.getConfig().resDialogNegativeButtonText()), this);
        this.mDialog = builder.create();
        this.mDialog.setCanceledOnTouchOutside(false);
        this.mDialog.setOnDismissListener(this);
        this.mDialog.show();
    }

    public void onDismiss(DialogInterface dialogInterface) {
        finish();
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (!(this.userComment == null || this.userComment.getText() == null)) {
            bundle.putString(STATE_COMMENT, this.userComment.getText().toString());
        }
        if (this.userEmail != null && this.userEmail.getText() != null) {
            bundle.putString(STATE_EMAIL, this.userEmail.getText().toString());
        }
    }
}
