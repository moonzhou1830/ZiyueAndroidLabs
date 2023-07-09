package algonquin.cst2335.zhou0224;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ChatMessageDAO {

    @Insert
    public void insertMessage(ChatMessage m);
    //query,get all from database:
    @Query("Select * from ChatMessage") //Table name is @Entity
    public List<ChatMessage> getAllMessages();
    @Delete
    public void deleteMessage(ChatMessage m);

}
