package com.example.ThiefBoy;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.widget.Toast;

import com.example.ThiefBoy.EMail.Var;
import com.example.ThiefBoy.EMail.GMail;

import java.io.UnsupportedEncodingException;

//import javax.mail.MessagingException;

//import static android.content.Context.DEVICE_POLICY_SERVICE;

public class MyAdmin extends DeviceAdminReceiver {

    static int atm = 0;
    GMail gMail = new GMail();

    @Override
    public void onEnabled(Context context, Intent intent) {
        Toast.makeText(context, "Enabled", Toast.LENGTH_LONG).show();
    }

    public void onDisabled(Context context, Intent intent) {
        Toast.makeText(context, "Disabled", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPasswordFailed(Context context, Intent intent, UserHandle user) {
        Toast.makeText(context, "Disabled", Toast.LENGTH_LONG).show();
        DevicePolicyManager devicepolicymanager = (DevicePolicyManager) context.getSystemService(context.DEVICE_POLICY_SERVICE);
        int attmp = devicepolicymanager.getCurrentFailedPasswordAttempts();
        atm = attmp;
        System.out.println(attmp);
//        gMail.setEmailBody("Latitude:" + Var.lattitude + "\nLongitude:" + Var.lontitude);
        //gMail.setEmailBody("<p>Your Phones Last Location.</p><a href=\"http://maps.google.com/maps?q="+Var.lontitude+","+Var.lattitude+"\">See phones Location</a>" );
        if (attmp >= 4) {
            gMail.setEmailBody("<a href=\"http://maps.google.com/maps?q=" + Var.lattitude + "," + Var.lontitude + "\">Your Phones Last Location.</a>" + "<br>Unlock Attempts " + attmp + " times");
            gMail.setToEmail(Var.toEmail);
            try {
                gMail.createEmailMessage();
                gMail.sendEmail();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }
}
