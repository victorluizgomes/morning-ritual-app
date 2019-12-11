package com.example.morningritualtracker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;



public class ShareFragement extends Fragment {
    ArrayList<String> contacts;
    Context containerActivity = null;
    String imagePath;
    String tasksToSend;

    /**
     * Constructor
     * @param activity
     * @param path
     */
    public ShareFragement(Context activity, String path, String tasks) {
        // Required empty public constructor
        containerActivity = activity;
        imagePath = path;
        tasksToSend = tasks;
    }


    /**
     * Grabs Contact information such as the name.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contacts =  new ArrayList<String>();

        Cursor cursor = containerActivity.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            // Do something with the contactId and the name
            contacts.add(name + " :: " + contactId);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(containerActivity, R.layout.contact_row, R.id.row, contacts);
        View inflaterView = inflater.inflate(R.layout.fragment_share_fragement, container, false);
        ListView list = (ListView) inflaterView.findViewById(R.id.contactList);
        list.setAdapter(arrayAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int id = i + 1;

                String email = getEmail(id);

                sendImage(email);

            }
        });
        cursor.close();
        // Inflate the layout for this fragment
        return inflaterView;
    }

    /**
     * Returns the email of a particular contact
     * @param id
     * @return email
     */
    public String getEmail(int id){
        Cursor emails = getActivity().getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + id, null, null);
        if (emails.moveToNext()) {
            String email = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
            // Do something with the email address
            return email;
        }
        emails.close();
        return "";
    }


    /**
     * Starts the intent to send an email message with an image attached.
     * @param email
     */
    public void sendImage(String email){
        System.out.println(imagePath);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("vnd.android.cursor.dir/email");

        intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { email });
        intent.putExtra(Intent.EXTRA_TEXT, tasksToSend);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Tasks I completed today!");

        if(imagePath != null) {
            Uri uri = FileProvider.getUriForFile(containerActivity,
                    BuildConfig.APPLICATION_ID + ".fileprovider", new File(imagePath));
            intent.putExtra(android.content.Intent.EXTRA_STREAM, uri);
        }

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

}
