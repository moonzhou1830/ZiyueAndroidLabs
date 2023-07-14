package algonquin.cst2335.zhou0224;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import algonquin.cst2335.zhou0224.databinding.DetailsLayoutBinding;


public class MessagesDetailsFragment extends Fragment{
    ChatMessage selected;

    public MessagesDetailsFragment(ArrayList<ChatMessage> m)
    {
        selected = m;
    }

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater, container, false);

        binding.messageText.setText( selected.message);
        binding.timeText.setText( selected.timeSent  );
        binding.idText.setText( "ID = " + selected.id );

        return binding.getRoot();
    }

}
