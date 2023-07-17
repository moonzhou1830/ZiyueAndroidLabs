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

    public MessagesDetailsFragment(ChatMessage m)
    {
        selected = m;
    }

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);

        binding.showMessage.setText(selected.message);
        binding.showTime.setText(selected.timeSent);
        binding.showSendOrReceive.setText(Integer.toString(selected.sendOrReceive));
        binding.showID.setText("ID = " + selected.id);


        return binding.getRoot();
    }

}
