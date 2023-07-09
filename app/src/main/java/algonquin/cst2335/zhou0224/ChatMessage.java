package algonquin.cst2335.zhou0224;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity //A table called ChatMessage
public class ChatMessage{
    @PrimaryKey(autoGenerate = true) //increment the ids for us
    @ColumnInfo(name="id")
    public int id;

    @ColumnInfo(name="message")
    String message;
    @ColumnInfo(name="TimeSent")
    String timeSent;
    @ColumnInfo(name="SendOrReceive")
    int sendOrReceive;

    ChatMessage(String m, String t, int type)
    {
        message = m;
        timeSent = t;
        sendOrReceive = type;
    }

    ChatMessage()
    {    }
    public String getMessage(){
        return message;
    }
    public String getTimeSent(){
        return timeSent;
    }
    public int getIsSentButton(){
        return sendOrReceive;
    }
}
