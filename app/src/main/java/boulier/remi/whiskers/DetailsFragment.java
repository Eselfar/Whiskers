package boulier.remi.whiskers;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {
    public static final String CAT = "boulier.remi.whiskers.cat";

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set the home button
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.invalidateOptionsMenu();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle == null) return;

        Cat cat = (Cat) bundle.getParcelable(CAT);

        View view = getView();
        ImageView imageView = (ImageView) view.findViewById(R.id.details_img);
        ImageView bigImageView = (ImageView) view.findViewById(R.id.details_img_big);
        TextView breedTV = (TextView) view.findViewById(R.id.details_breed);
        TextView legsTV = (TextView) view.findViewById(R.id.details_legs);
        TextView sizeTV = (TextView) view.findViewById(R.id.details_size);
        TextView whiskersTV = (TextView) view.findViewById(R.id.details_whiskers);
        TextView foodTV = (TextView) view.findViewById(R.id.details_preferred_food);
        TextView packageTV = (TextView) view.findViewById(R.id.details_package);
        ImageView colorView = (ImageView) view.findViewById(R.id.details_color_img);
        View colorTV = view.findViewById(R.id.details_color_n_a);

        breedTV.setText(cat.breed);
        legsTV.setText(cat.legs.toString());
        sizeTV.setText(cat.size);
        whiskersTV.setText(Integer.toString(cat.whiskers));
        foodTV.setText(cat.preferredFood);
        packageTV.setText(cat.getFoodPackage(getActivity()));
        try {
            colorView.setBackgroundColor(Color.parseColor(cat.color));
        } catch (Exception e) {
            colorView.setVisibility(View.GONE);
            colorTV.setVisibility(View.VISIBLE);
        }

        String imgResString = cat.image.getImageResString();
        int id = getResources().getIdentifier(imgResString, "drawable", getActivity().getPackageName());
        if (id != 0) {
            imageView.setImageResource(id);
            imageView.setBackgroundColor(0);
            bigImageView.setImageResource(id);
            bigImageView.setBackgroundColor(0);
        } else {
            imageView.setImageDrawable(null);
            imageView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.grey));
        }
    }
}
