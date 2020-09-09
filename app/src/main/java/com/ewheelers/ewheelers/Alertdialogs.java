package com.ewheelers.ewheelers;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ewheelers.ewheelers.Activities.LoginScreenActivity;

import pl.droidsonroids.gif.GifImageView;

public class Alertdialogs {

    public void showSuccessAlert(final Context context, String smsg) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_success_dialog_layout);
        dialog.setCancelable(false);
        Button done = (Button) dialog.findViewById(R.id.done);
        GifImageView gifImageView = (GifImageView) dialog.findViewById(R.id.gif_image);
        gifImageView.setVisibility(View.VISIBLE);
        TextView textView = (TextView)dialog.findViewById(R.id.response_text);
        textView.setText(smsg);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(context, LoginScreenActivity.class);
                context.startActivity(i);
            }
        });
        dialog.show();

    }

    public void showFailedAlert(Context context,String smsg){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_success_dialog_layout);
        dialog.setCancelable(false);
        GifImageView gifImageView = (GifImageView) dialog.findViewById(R.id.gif_image);
        gifImageView.setVisibility(View.GONE);
        Button done = (Button) dialog.findViewById(R.id.done);
        TextView textView = (TextView)dialog.findViewById(R.id.response_text);
        textView.setText(smsg);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
