package cs.ua.edu.flavortown;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private Button top10Button;
    private Button moodButton;
    private Button historyButton;
    private  ArrayAdapter<String> adapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        top10Button = (Button) v.findViewById(R.id.top10Button);
        historyButton =(Button) v.findViewById(R.id.historyButton);
        moodButton = (Button) v.findViewById(R.id.MoodButton);

        moodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getContext(), MoodActivity.class);
                startActivity(intent);
            }
        });


        final ListView restaurantList = (ListView) v.findViewById(R.id.WFlistview);


        top10Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String a = "";
                String[] displayItems = databaseReturn(a);  //TODO: Fill in functions
                adapter = new ArrayAdapter<String>(v.getContext(),android.R.layout.simple_list_item_1,displayItems);
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String b = "";
                String[] displayItems = databaseReturn(b);  //TODO: FIll in functions
                adapter = new ArrayAdapter<String>(v.getContext(),android.R.layout.simple_list_item_1,displayItems);
            }
        });

        restaurantList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            //@Override
            public  void onItemClick(AdapterView<?> parent,View v,int position, long id){
                //Object a = restaurantList.getSelectedItem();
                String a =  (String) parent.getItemAtPosition(position);

                Toast.makeText(v.getContext(), a, Toast.LENGTH_SHORT).show();
            }


        });

        return v;
    }

    public String[] databaseReturn(String query){

        //Todo: add database query that inserts data into the array
        String[] a  = {"Burger A","Burger B","Burger C","Burger D","Burger E","Burger F","Burger G"};
        return a;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
