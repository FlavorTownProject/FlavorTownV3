package cs.ua.edu.flavortown;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchBarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchBarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchBarFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private boolean checked; //false = restaurant true = food

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SearchBarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchBarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchBarFragment newInstance(String param1, String param2) {
        SearchBarFragment fragment = new SearchBarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_search_bar, container, false);
        final EditText SearchBar = (EditText) v.findViewById(R.id.SearchBar);
        final Button SearchButton = (Button)  v.findViewById(R.id.SearchButton);
        //final TextView a = new TextView(getContext());
        final ListView restaurantList = (ListView) v.findViewById(R.id.restaurantList);

        //ArrayList<ListEntry> items = new ArrayList<ListEntry>();
        final RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton rb =(RadioButton) v.findViewById(checkedId);
                int index = radioGroup.indexOfChild(rb);
                switch (index) {
                    case 0: // Restaurant
                        checked = false;
                        Toast.makeText(v.getContext(), "Selected button  " + rb.getText(),  Toast.LENGTH_SHORT).show();
                        break;
                    case 1: // FOOD
                        checked = true;
                        Toast.makeText(v.getContext(), "Selected button  " + rb.getText(),  Toast.LENGTH_SHORT).show();
                        break;
                }


            }
        });


        SearchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String searchString = SearchBar.getText().toString();
                String[] displayItems = databaseReturn(searchString,checked);
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(),android.R.layout.simple_list_item_1,displayItems);
                restaurantList.setAdapter(adapter);

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
        //return inflater.inflate(R.layout.fragment_search_bar, container, false);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioRestaurant:
                if (checked)
                    break;
            case R.id.radioFood:
                if (checked)
                    break;
        }
    }


    public String[] databaseReturn(String search, boolean choice){
        // choice false=rest true=food
        //Todo: add database query that inserts data into the array
        String[] a  = {"Burger A","Burger B","Burger C","Burger D","Burger E","Burger F","Burger G"};
        return a;
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
