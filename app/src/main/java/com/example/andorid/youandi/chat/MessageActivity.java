package com.example.andorid.youandi.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.andorid.youandi.R;
import com.example.andorid.youandi.model.ChatModel;
import com.example.andorid.youandi.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MessageActivity extends AppCompatActivity {

    private String destinationUid;
    private Button button;
    private EditText editText;
    private String uid;
    private String chatRoomUid;
    private RecyclerView recyclerView;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        destinationUid = getIntent().getStringExtra("destinationUid");
        button = (Button) findViewById(R.id.messageActivity_button);
        editText = (EditText) findViewById(R.id.messageActivity_editText);
        recyclerView = (RecyclerView) findViewById(R.id.messageActivity_recyclerview);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatModel chatModel = new ChatModel();
                chatModel.users.put(uid, true);
                chatModel.users.put(destinationUid, true);

                if (chatRoomUid == null) {
                    button.setEnabled(false);
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            checkChatRoom();
                        }
                    });

                } else {
                    ChatModel.MessageModel messageModel = new ChatModel.MessageModel();
                    messageModel.uid = uid;
                    messageModel.message = editText.getText().toString();
                    messageModel.timestamp = ServerValue.TIMESTAMP;

                    FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("messages").push().setValue(messageModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            editText.setText("");
                        }
                    });
                }


            }
        });
        checkChatRoom();
    }

    void checkChatRoom() {
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    ChatModel chatModel = item.getValue(ChatModel.class);
                    if (chatModel.users.containsKey(destinationUid)) {
                        chatRoomUid = item.getKey();
                        button.setEnabled(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
                        recyclerView.setAdapter(new RecyclerViewAdapter());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<ChatModel.MessageModel> messageModels;
        UserModel userModel;

        public RecyclerViewAdapter() {
            messageModels = new ArrayList<>();

            FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userModel = dataSnapshot.getValue(UserModel.class);
                    getMessageList();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        void getMessageList() {
            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("messages").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    messageModels.clear();

                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        messageModels.add(item.getValue(ChatModel.MessageModel.class));
                    }
                    notifyDataSetChanged();
                    recyclerView.scrollToPosition(messageModels.size()-1);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);

            return new MessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            MessageViewHolder messageViewHolder = ((MessageViewHolder)holder);

            if (messageModels.get(position).uid.equals(uid)) {
                messageViewHolder.message_textView.setText(messageModels.get(position).message);
                messageViewHolder.message_textView.setTextColor(Color.parseColor("#FFFFFF"));
                messageViewHolder.message_textView.setBackgroundResource(R.drawable.my_message);
                messageViewHolder.linearLayout.setVisibility(View.INVISIBLE);
                messageViewHolder.message_textView.setTextSize(25);
                messageViewHolder.linearLayoutMain.setGravity(Gravity.RIGHT);
            } else {

                messageViewHolder.name_textView.setText(userModel.userName);
                messageViewHolder.message_textView.setBackgroundResource(R.drawable.your_message);
                messageViewHolder.message_textView.setText(messageModels.get(position).message);
                messageViewHolder.linearLayout.setVisibility(View.VISIBLE);
                messageViewHolder.message_textView.setTextSize(25);
                messageViewHolder.linearLayoutMain.setGravity(Gravity.LEFT);
            }
            ((MessageViewHolder)holder).message_textView.setText(messageModels.get(position).message);

            long unixTime = (long) messageModels.get(position).timestamp;
            Date date = new Date(unixTime);
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            String time = simpleDateFormat.format(date);
            messageViewHolder.timestamp_textView.setText(time);
        }

        @Override
        public int getItemCount() {
            return messageModels.size();
        }

        private class MessageViewHolder extends RecyclerView.ViewHolder {
            public TextView message_textView;
            public TextView name_textView;
            public ImageView profile_imageView;
            public LinearLayout linearLayout;
            public LinearLayout linearLayoutMain;
            public TextView timestamp_textView;

            public MessageViewHolder(View view) {
                super(view);
                message_textView = (TextView) view.findViewById(R.id.item_message_textview_message);
                name_textView = (TextView) view.findViewById(R.id.item_message_textview_name);
                profile_imageView = (ImageView) view.findViewById(R.id.item_message_imageview_profile);
                linearLayout = (LinearLayout) view.findViewById(R.id.item_message_linearlayout);
                linearLayoutMain = (LinearLayout) view.findViewById(R.id.item_message_linearlayout_main);
                timestamp_textView = (TextView) view.findViewById(R.id.item_message_textview_timestamp);

            }
        }
    }
}
