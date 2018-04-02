package org.eliconcepts.geekking;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.eliconcepts.geekking.loadingChat.my_emo_my_username;


public class Chat extends AppCompatActivity {

    private EditText message;
    private Button send;
    private String GetMessage;
    private MessageAdapter messageAdapter;
    private ListView messagesView;
    private MemberData memberData;
    private MemberData me;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        message = (EditText) findViewById(R.id.edittext_chatbox);
        send = (Button) findViewById(R.id.button_chatbox_send);

        messageAdapter = new MessageAdapter(this);
        messagesView = (ListView) findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);

        //get the data from intent extra - data about the other user
        String data;
        String new_message;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                data = null;
                new_message = null;
            } else {
                data = extras.getString("USER_MATCH");
                new_message = extras.getString("NEW_MESSAGE");
            }
        } else {
            data = (String) savedInstanceState.getSerializable("USER_MATCH");
            new_message = (String) savedInstanceState.getSerializable("NEW_MESSAGE");
        }
        //the data i get: variable - "data" - String - Thread: ["user_name2", "emotion2"] -
        // (of the other user)
        if (data != null) {
            Pattern p = Pattern.compile("Thread: (.*), (.*)");
            Matcher m = p.matcher(data);
            String emo = "";
            String username = "";
            if (m.find()) {
                MatchResult mr = m.toMatchResult();
                username = mr.group(1);
                emo = mr.group(2);
            }
            memberData = new MemberData(username, emo); //member data of the other user.
        }

        if (new_message != null){ //"username: " + username + ", msg: " + msg
            Pattern p = Pattern.compile("username: (.*)\\, msg: (.*)");
            Matcher m = p.matcher(new_message);
            String sender = "";
            String msg = "";
            if (m.find()) {
                MatchResult mr = m.toMatchResult();
                sender = mr.group(1);
                msg = mr.group(2);
            }
            MemberData new_display = new MemberData(sender); //member data of the other user.
            onMessage(GetMessage, new_display, false);
        }

        //take data of the current user from static variable - "my_emo_my_username" that contains:
        // username: username, emotion: my_emo
        Pattern p1 = Pattern.compile("username: (.*)\\, emotion: (.*)");
        Matcher m1 = p1.matcher(my_emo_my_username);
        String my_emo ="";
        String my_username = "";
        if(m1.find()){
            MatchResult mr1 = m1.toMatchResult();
            my_username = mr1.group(1);
            my_emo = mr1.group(2);
        }

        me = new MemberData(my_username, my_emo); //the data of the current user

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetMessage = message.getText().toString();
                if (!TextUtils.isEmpty(GetMessage)){
                    //if this user pressed on send button we need to
                    // send to the server a message with the client details.
                    String msg;
                    MyTaskParams.Client my_client = new MyTaskParams.Client(Chat.this);
                    msg = my_client.Chat(me.getName(), memberData.getName(), GetMessage);
                    MyTaskParams params = new MyTaskParams(msg, null, null);
                    my_client.execute(params);

                    onMessage(GetMessage, me, true);
                    message.getText().clear();
                }
            }
        });

    }

    public void onMessage(String msg, MemberData data, boolean belongsToCurrentUser) {
        final Message message = new Message(msg, data, belongsToCurrentUser);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageAdapter.add(message);
                messagesView.setSelection(messagesView.getCount() - 1);
            }
        });
    }
}

class MemberData {
    private String name;
    private String emotion = null;

    public MemberData(String name, String emotion) {
        this.name = name;
        this.emotion = emotion;
    }

    public MemberData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getEmotion() {
        return emotion;
    }

    @Override
    public String toString() {
        return "MemberData{" +
                "name='" + name + '\'' +
                ", emotion='" + emotion + '\'' +
                '}';
    }
}
