package algonquin.cst2335.zhou0224;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Room;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.zhou0224.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.zhou0224.databinding.SentMessageBinding;
import algonquin.cst2335.zhou0224.databinding.ReceiveMessageBinding;
import algonquin.cst2335.zhou0224.ChatMessage;
import algonquin.cst2335.zhou0224.ChatRoomViewModel;

public class ChatRoom extends AppCompatActivity {
    ActivityChatRoomBinding binding;
    ChatRoomViewModel chatModel ;
    ArrayList<ChatMessage> messages = new ArrayList<>();
    MessageDatabase db;
    ChatMessageDAO myDAO;

    private RecyclerView.Adapter myAdapter;

    private Toolbar theToolbar;

    private int selectedItemPosition = RecyclerView.NO_POSITION;

    ChatMessage chatMessage;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.delete){
            deleteSelectedItem();
        }
        if(item.getItemId() == R.id.about){
            CharSequence text = "Version 1.0, created by Ziyue Zhou";
            Toast toast = Toast.makeText(this,text,Toast.LENGTH_SHORT);
            toast.show();
        }
        return true;
    }

    private void deleteSelectedItem() {
        if (selectedItemPosition != RecyclerView.NO_POSITION) {
            //ChatMessage selectedMessage = messages.get(selectedItemPosition);
            // messages.remove(selectedItemPosition);
            //  myAdapter.notifyItemRemoved(selectedItemPosition);

            ChatMessage removedMessage = messages.remove(selectedItemPosition);
            AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
            builder.setMessage(("Do you want to delete the message: "+ removedMessage.getMessage()))
                    .setTitle("Question: ")
                    .setNegativeButton("No",(dialog,cl)->{})
                    .setPositiveButton("Yes",(dialog,cl)-> {


                        FragmentManager fMgr = getSupportFragmentManager();
                        FragmentTransaction tx = fMgr.beginTransaction();

                        // 移除之前添加的旧 Fragment
                        Fragment oldFragment = fMgr.findFragmentById(R.id.fragmentLocation);
                        if (oldFragment != null) {
                            tx.remove(oldFragment);
                            tx.commit();
                        }

                        Executor thread2 = Executors.newSingleThreadExecutor();
                        thread2.execute(() ->
                        {
                            myDAO.deleteMessage(removedMessage);

                            runOnUiThread(() ->
                                    myAdapter.notifyDataSetChanged());
                        });

                        //    myAdapter.notifyItemRemoved(selectedItemPosition);

                      /* Snackbar.make(, "You deleted message #"+selectedItemPosition,Snackbar.LENGTH_LONG)
                                .setAction("Undo",clk1->{

                                    messages.add(selectedItemPosition,removedMessage);
//                                         myAdapter.notifyDataSetChanged();

                                    Executor thread3 = Executors.newSingleThreadExecutor();
                                    thread3.execute(() -> {
                                        myDAO.insertMessage(removedMessage);
                                        runOnUiThread(() -> myAdapter.notifyDataSetChanged());
                                    });

                            })
                                .show(); */


                    }) .create().show();

    }
        }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        //messages = chatModel.messages.getValue();
        chatModel.selectedMessage.observe(this, newMessageValue -> {
                    MessagesDetailsFragment chatFragment = new MessagesDetailsFragment(newMessageValue);
                   // chatFragment.displayMessage(newValue);
                    FragmentManager fMgr = getSupportFragmentManager();
                    //getSupportFragmentManager().beginTransaction().add(R.id.fragmentLocation, chatFragment).commit();
                    FragmentTransaction tx = fMgr.beginTransaction();
                    tx.replace(R.id.fragmentLocation, chatFragment);
                    tx.addToBackStack("Back to last message");
                    tx.commit();//// This line actually loads the fragment into the specified FrameLayout

                    //getSupportFragmentManager().beginTransaction().add(R.id.messagesFrame,chatFragment).commit();
                    //getSupportFragmentManager().beginTransaction().replace(R.id.messagesFrame,chatFragment).commit();
                    //getSupportFragmentManager().beginTransaction().addToBackStack("Back to last message");
                    //tx.remove();
                });

        messages = chatModel.messages.getValue();

        //access the database:
        db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "MyChatMessageDatabase").build();
        myDAO = db.cmDAO();

        if(messages == null)
        {
            chatModel.messages.setValue( messages = new ArrayList<ChatMessage>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(()-> {
                messages.addAll( myDAO.getAllMessages() ); //Once you get the data from database
                runOnUiThread( () ->  binding.recycleView.setAdapter( myAdapter )); //You can then load the RecyclerView
            });
        }

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //theToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(binding.myToolbar);

        binding.sendButton.setOnClickListener(click -> {
            int type = 1;
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage msg = new ChatMessage(binding.textInput.getText().toString(),currentDateandTime,type);
            //insert into ArrayList
            messages.add(msg);
            //insert into database;
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(()-> {
                //run on a second processor:
                myDAO.insertMessage(msg);//returns the id
                    });
            myAdapter.notifyItemInserted( messages.size()-1 ); //updates the rows
            //clear the previous text:
            binding.textInput.setText("");
        });

        binding.receiveButton.setOnClickListener(click -> {
            int type = 0;
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage msg = new ChatMessage(binding.textInput.getText().toString(),currentDateandTime,type);

            messages.add(msg);
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(()-> {
                //run on a second processor:
                myDAO.insertMessage(msg);//returns the id
            });
            myAdapter.notifyItemInserted( messages.size()-1 );
            //clear the previous text:
            binding.textInput.setText("");
        });

        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {

            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if(viewType == 0) {

                    SentMessageBinding sbinding = SentMessageBinding.inflate(getLayoutInflater(),parent, false);
                    return new MyRowHolder(sbinding.getRoot());
                }
                else {
                    ReceiveMessageBinding rbinding = ReceiveMessageBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(rbinding.getRoot());
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                holder.messageText.setText("");
                holder.messageText.setText("");
                ChatMessage msg = messages.get(position);
                holder.messageText.setText(msg.getMessage());
                holder.timeText.setText(msg.timeSent);
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            public int getItemViewType(int position){
                if(messages.get(position).sendOrReceive == 1){
                    return 0;
                }
                else{
                    return 1;
                }
            }
        });
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        }
    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(click ->{


               /* int position = getAbsoluteAdapterPosition();
                ChatMessage selected = messages.get(position);

                chatModel.selectedMessage.postValue(selected);


                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
                builder.setMessage("Do you want to delete the message:" + messageText.getText())
                       .setTitle("Question:")
                       .setNegativeButton("No",((dialog, cl) -> { }))
                       .setPositiveButton("Yes",((dialog, cl) -> {
                   position = getAbsoluteAdapterPosition();
                   ChatMessage toDelete = messages.get(position);
                   Executor thread = Executors.newSingleThreadExecutor();
                           thread.execute(()-> {
                               //run on a second processor:
                               myDAO.deleteMessage(toDelete);
                           });


                    messages.remove(position);
                    myAdapter.notifyItemRemoved(position);

                    Snackbar.make(messageText,"You deleted message #" + position, Snackbar.LENGTH_LONG)
                            .setAction("Undo",clk ->{
                                messages.add(position,toDelete);

                                Executor thread2 = Executors.newSingleThreadExecutor();
                                thread2.execute(()-> {
                                    //run on a second processor:
                                    myDAO.insertMessage(toDelete);
                                });

                                myAdapter.notifyItemInserted(position);
                            })
                            .show();
                }))
                .create().show();
                */

                int position = getAbsoluteAdapterPosition();
                ChatMessage selected = messages.get(position);
                selectedItemPosition = position;

                //chatModel.selectedMessage.postValue(selected);


            });
            messageText = itemView.findViewById(R.id.theMessage);
            timeText = itemView.findViewById(R.id.theTime);
        }


    }

}


