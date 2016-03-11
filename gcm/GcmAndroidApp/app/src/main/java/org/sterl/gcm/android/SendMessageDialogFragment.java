package org.sterl.gcm.android;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.UUID;

public class SendMessageDialogFragment extends DialogFragment {

    ProgressDialog progressDialog;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final String to = getString(R.string.gcm_project_number);
        final View view = inflater.inflate(R.layout.send_message_dialog_layout, null);
        builder.setView(view)
                .setTitle("Send Message to " + to)
                // Add action buttons
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        final CharSequence message = ((EditText) view.findViewById(R.id.txtMessage)).getText();


                        progressDialog = ProgressDialog.show(getActivity(), "Sending message", "Sending your message '" + message + "' to " + to);

                        new AsyncTask<Void, Void, String>() {
                            @Override
                            protected String doInBackground(Void... params) {
                                final GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getActivity());
                                String msg = "";
                                try {
                                    Bundle data = new Bundle();
                                    data.putString("message", String.valueOf(message));
                                    String id = UUID.randomUUID().toString();
                                    gcm.send(to + "@gcm.googleapis.com", id, data);
                                    msg = "Sent message to " + to;
                                } catch (IOException ex) {
                                    msg = "Error :" + ex.getMessage();
                                    progressDialog.dismiss();
                                }
                                return msg;
                            }

                            @Override
                            protected void onPostExecute(String msg) {
                                progressDialog.dismiss();
                                dismiss();
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                            }
                        }.execute(null, null, null);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }
}
