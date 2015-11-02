package boulier.remi.whiskers;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class CatsListFragment extends ListFragment implements AdapterView.OnItemClickListener {

    private ArrayAdapter mAdapter;
    private List<Cat> mCats;
    private RequestQueue mQueue;
    private final static String REQUEST_TAG = "boulier.remi.whiskers.catsListFragment.requestTag";
    private View mProgressBar;

    public CatsListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cats_list, container, false);
        mProgressBar = view.findViewById(R.id.progress_bar);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update the menu
        ((AppCompatActivity) getActivity()).getSupportActionBar().invalidateOptionsMenu();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new CatsListDisplayAdapter(getActivity());
        ListView list = getListView();
        list.setAdapter(mAdapter);
        list.setOnItemClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mQueue = Volley.newRequestQueue(getActivity());
        newRequest();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            Log.d("CatsListFragment", "Refresh");
            newRequest();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        Fragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(DetailsFragment.CAT, mCats.get(position));
        fragment.setArguments(args);
        transaction.replace(R.id.fragment_container, fragment, null);
        transaction.commit();
    }

    private void newRequest() {
        String url = getString(R.string.url);
        mQueue.cancelAll(REQUEST_TAG);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String jsonResponse = response.substring(0);
                if (TextUtils.isEmpty(jsonResponse)) return; // TODO: Manage the error
                Map<String, Object> resMap = JsonConverter.getFromJSON(jsonResponse, Map.class);
                if (resMap != null && !resMap.isEmpty()) {
                    Object resFood = resMap.get("food");
                    String innerJson = JsonConverter.toJSON(resFood);
                    List<Food> foods = null;
                    try {
                        Object content = JsonConverter.getFromJSON(innerJson, new TypeToken<ArrayList<Food>>() {
                        }.getType());
                        foods = (List<Food>) content;
                    } catch (JsonParseException e) {
                        Log.e("CAT_LIST_FRAGMENT", e.getMessage());
                    }

                    Object resCats = resMap.get("cats");
                    innerJson = JsonConverter.toJSON(resCats);
                    try {
                        Object content = JsonConverter.getFromJSON(innerJson, new TypeToken<ArrayList<Cat>>() {
                        }.getType());

                        if (content != null) {
                            mAdapter.clear();
                            mCats = (List<Cat>) content;
                            if (foods != null) {
                                for (Cat cat : mCats) {
                                    cat.setFoodPackage(foods);
                                }
                            }
                            mAdapter.addAll(mCats);
                        }
                    } catch (JsonParseException e) {
                        Log.e("CAT_LIST_FRAGMENT", e.getMessage());
                    }
                }
                mProgressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), R.string.server_unreachable, Toast.LENGTH_LONG).show();
            }
        }

        );
        stringRequest.setTag(REQUEST_TAG);
        mQueue.add(stringRequest);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private class CatsListDisplayAdapter extends ArrayAdapter<Cat> {
        private class ViewHolder {
            ImageView image;
            TextView bread;
        }

        private final LayoutInflater mInflater;
        private final Context mContext;

        public CatsListDisplayAdapter(Context context) {
            super(context, R.layout.item_list_cats);
            this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.mContext = context;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            ViewHolder holder;
            Cat cat = getItem(position);
            if (view == null) {
                holder = new ViewHolder();
                view = mInflater.inflate(R.layout.item_list_cats, parent, false);
                holder.image = (ImageView) view.findViewById(R.id.item_image);
                holder.bread = (TextView) view.findViewById(R.id.item_bread);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            String imgResString = cat.image.getImageResString();
            int id = getResources().getIdentifier(imgResString, "drawable", mContext.getPackageName());
            if (id != 0) {
                holder.image.setImageResource(id);
                holder.image.setBackgroundColor(0);
            } else {
                holder.image.setImageDrawable(null);
                holder.image.setBackgroundColor(ContextCompat.getColor(mContext, R.color.grey));
            }
            holder.bread.setText(cat.breed);

            return view;
        }
    }
}
