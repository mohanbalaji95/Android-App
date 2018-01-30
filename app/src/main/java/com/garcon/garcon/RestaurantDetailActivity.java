package com.garcon.garcon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.garcon.Constants.Constants;
import com.google.android.gms.maps.GoogleMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONObject;
import us.monoid.web.Resty;

//import android.view.MenuItem;


public class RestaurantDetailActivity extends AppCompatActivity {
    private static final String TAG = RestaurantDetailActivity.class.getSimpleName();
    TextView tvName, tvPrice, tvLocation, tvHours, tvType,tvHoursAffected;

    Button btn_Menu;
    Button btn_Call;
    Button btn_DineIn;
    Button btn_TakeOut;
    Button btn_Info;
    String locationName;
    String phoneNumber;
    double lat, longt;
    GoogleMap Map;
    private View restaurantLayout;


    //from main menu activity
    public final static String LOG_TAG = RestaurantDetailActivity.class.getSimpleName();
    ListView catListView, itemsListView;
    ImageView img,extraInfo;
    // ScrollView sv1, sv2;

    private Toolbar toolbar;
    List<Category> categoriesList;
    List<MenuItem> itemsList;
    List<Pair<Category,ArrayList<MenuItem>>> imported_list = new ArrayList<>();
    private boolean loaded = false;
    private Context context;

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String PRICE = "price";
    private static final String PRICE_LEVELS = "price_levels";
    private static final String IN_STOCK = "in_stock";
    private static final String MODIFIER_GROUPS_COUNT = "modifier_groups_count";


    private MenuCategoryAdapter catRowAdapter;
    private MenuItemAdapter itemRowAdapter;
    private String modifier_ref;
    private JSONObject mod_resource;

    private Intent i;
    public String data;


    private static final String subPath = "/menu/categories/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OrderSingleton.getInstance();
        setContentView(R.layout.floating_layout);

        initializeUI();
        i=getIntent();
        dataSetup((Restaurant)i.getSerializableExtra("RestaurantObject"));

        //copied from main menu activity
        //View mainmenuview1 = inflater.inflate(R.layout.floating_layout, container, false);
        catListView = (ListView) findViewById(R.id.categories);
        itemsListView = (ListView) findViewById(R.id.items);
        categoriesList = new ArrayList<>();
        catRowAdapter = new MenuCategoryAdapter(categoriesList, getApplicationContext());
        itemsList = new ArrayList<>();
        
        img = (ImageView) findViewById((R.id.BackArrow));
        extraInfo = (ImageView) findViewById(R.id.extra_info);
        extraInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestaurantDetailActivity.this,InfoActivity.class);
                Restaurant restaurant = (Restaurant)i.getSerializableExtra("RestaurantObject");
                Bundle bundle = new Bundle();
                bundle.putSerializable("RestaurantObject",restaurant);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //sv1 = (ScrollView) findViewById(R.id.sv1);
        //sv2 = (ScrollView) findViewById(R.id.sv2);
		
		//Commented out from "this" to Siddhi's thing
		//itemRowAdapter = new MenuItemAdapter(itemsList, this);
        itemRowAdapter = new MenuItemAdapter(itemsList, getApplicationContext(),tvName.getText().toString());

        //final String categoryURL = "https://api.omnivore.io/0.1/locations/"+location+"/menu/categories/";
        //final String categoryURL = "https://api.omnivore.io/0.1/locations/7TAprKdT/menu/categories/";
        final String categoryURL = Constants.OMNIVORE_API_BASE_URL+Constants.LOCATION_KEY+subPath;

        String[] mURL = {categoryURL};
        //  new MainMenuActivity.RetrievalTask(MainMenuActivity.this).execute(mURL);
        //new RetrievalTask().execute(mURL);

        new RestaurantDetailActivity.RetrievalTask().execute(mURL);
        //aync1.execute(mURL);


        catListView.setAdapter(catRowAdapter);
        itemsListView.setAdapter(itemRowAdapter);
        //sv2.addView(catListView);


        //synchronizes catListView with itemsListView
        itemsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            //private int currentVisibleItemCount, currentScrollState, currentFirstVisibleItem, totalItem;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                checkSync();
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //checkSync();
                //too much computation
            }
            private void checkSync() {
                //attribute (id) is the position of the category in the left listview
                //each item in the right listview has a string name and corresponding category's position
                if(loaded){

                    int leftTop = catListView.getFirstVisiblePosition();
                    String leftTopAttribute = ((Category) categoriesList.get(leftTop)).getId();

                    int rightTop = itemsListView.getFirstVisiblePosition();
                    String rightTopAttribute = ((MenuItem) itemsList.get(rightTop)).getCategoryID();

                    //Log.i(LOG_TAG,"scroll "+leftTopAttribute+" "+rightTopAttribute);

                    if(!leftTopAttribute.equals(rightTopAttribute)) {
                        catListView.requestFocusFromTouch();
                        for(int i = 0; i < categoriesList.size(); i++){
                            if(((Category) categoriesList.get(i)).getId().equals(rightTopAttribute)){
                                catListView.setSelection(i);
                                break;
                            }
                        }
                        catListView.requestFocus();
                    }
                }
            }
        });

        //   return view;
    }



    public void dataSetup(Restaurant restaurant) {

        if(restaurant!=null) {
            restaurantLayout.setVisibility(View.VISIBLE);
            tvName.setText(restaurant.getName());
            tvPrice.setText(restaurant.getPrice());
            tvLocation.setText(restaurant.getLocation());
            tvHours.setText(parseHours(restaurant.getHours()));
            //tvHours.setText(restaurant.getHours());
            tvType.setText(restaurant.getType());
            phoneNumber = restaurant.getPhone();
            locationName = restaurant.getName();
            lat = restaurant.getLat();
            longt = restaurant.getLongt();
        }
    }

    private void initializeUI()
    {
        restaurantLayout =  findViewById(R.id.restaurantLayout);
        tvName = (TextView) findViewById(R.id.tvName);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvHours = (TextView) findViewById(R.id.tvHours);
        tvType = (TextView) findViewById(R.id.tvType);
        tvHoursAffected = (TextView) findViewById(R.id.tvHoursAffected);




//commenting out to add menu item UI
   /*     btn_Menu = (Button) v.findViewById(R.id.menu);
        //btn_Call = (Button) v.findViewById(R.id.btnCall);
        btn_DineIn = (Button) v.findViewById(R.id.btnDineIn);
        btn_TakeOut = (Button) v.findViewById(R.id.btbTakeOut);

        btn_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startCheckoutActivity = new Intent(getContext(), MainMenuActivity.class);
                startCheckoutActivity.putExtra("ticketnumber","oTpbBkqT");
                startCheckoutActivity.putExtra("locationID","AieMdB5i");
                startActivity(startCheckoutActivity);
            }
        });

       // btn_Call.setOnClickListener(new View.OnClickListener(){
       //     @Override
       //     public  void onClick(View v){
       //         Intent callActivity = new Intent(Intent.ACTION_CALL);
       //         callActivity.setData(Uri.parse("tel:"+phoneNumber));
       //         if (callActivity.resolveActivity(getActivity().getPackageManager()) != null)
       //         {
       //             startActivity(callActivity);
       //         }
       //         //Snackbar.make(v,phoneNumber,Snackbar.LENGTH_LONG).show();
       //     }
       //  });
        btn_DineIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent startCheckoutActivity = new Intent(getContext(), MainMenuActivity.class);
                startCheckoutActivity.putExtra("ticketnumber","oTpbBkqT");
                startCheckoutActivity.putExtra("locationID","AieMdB5i");
                startActivity(startCheckoutActivity);
            }

        });

        btn_TakeOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent startCheckoutActivity = new Intent(getContext(), MainMenuActivity.class);
                startCheckoutActivity.putExtra("ticketnumber","oTpbBkqT");
                startCheckoutActivity.putExtra("locationID","AieMdB5i");
                startActivity(startCheckoutActivity);
            }
        }); */
        tvLocation.setOnClickListener(new View.OnClickListener(){
            //int zoomLevel = 12;
            @Override
            public  void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setPackage("com.google.android.apps.maps");
                String data = String.format("geo:%s,%s",+lat , +longt);
                data = String.format("%s?q=%s,%s", data, +lat,+longt+"("+locationName+")");
                intent.setData(Uri.parse(data));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }


        });
    }

    public String parseHours(String hours) {
        String data = hours;
        // String header = "Hours:Today ";
        String[] weekday = data.split("[;]");
        String header = "Hours: ";
        String Monday = weekday[0].substring(8);
        String Tuesday = weekday[1].substring(9);
        String Wednesday = weekday[2].substring(11);
        String Thursday = weekday[3].substring(10);
        String Friday = (weekday[4]).substring(8);
        String Saturday = weekday[5].substring(10);
        String Sunday = weekday[6].substring(8);
        //String finalhour = header + newFriday;
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        String key = month + "-" + date;
        Log.d(TAG,"Today's day key "+key);
        HashMap<String,String> holidayCalendar = HolidayCalendarSingleton.getInstance().getHolidayCalendar();
        if(holidayCalendar!=null && holidayCalendar.size()>0){
            if(holidayCalendar.containsKey(key)){
                tvHoursAffected.setVisibility(View.VISIBLE);
            }
        }


        switch (day) {
            case Calendar.MONDAY:
                return header + Monday;

            case Calendar.TUESDAY:
                return header + Tuesday;

            case Calendar.WEDNESDAY:
                return header + Wednesday;
            case Calendar.THURSDAY:
                return header + Thursday;
            case Calendar.FRIDAY:
                return header + Friday;
            case Calendar.SATURDAY:
                return header + Saturday;
            case Calendar.SUNDAY:
                return header + Sunday;

        }
        return null;

    }
    public void addToFav(View view){
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE);
        String value = sharedPreferences.getString("fav_string",null);
        if(value!=null){
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            favcardview favcardarray[] = gson.fromJson(value, favcardview[].class);

            List<favcardview> favcardviews;
            favcardviews = new ArrayList<>();
            for(int i =0 ;i<favcardarray.length;i++){
                favcardviews.add(new favcardview(favcardarray[i].name,R.drawable.la_vic,favcardarray[i].extra));
            }
            favcardviews.add(new favcardview(locationName,R.drawable.la_vic,tvLocation.getText().toString()));
            SharedPreferences.Editor spEditor = sharedPreferences.edit();
            spEditor.putString("fav_string",gson.toJson(favcardviews));
            spEditor.commit();
        }
        else{
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            List<favcardview> favcardviews;
            favcardviews = new ArrayList<>();
            favcardviews.add(new favcardview(locationName,R.drawable.la_vic,tvLocation.getText().toString()));
            SharedPreferences.Editor spEditor = sharedPreferences.edit();
            spEditor.putString("fav_string",gson.toJson(favcardviews));
            spEditor.commit();

        }

        Toast.makeText(this,"Added to your Favorites List",Toast.LENGTH_LONG).show();
    }

    public void categorySelectedSync(int position){
        int itemsListPos = position;
        for(int i = 0; i < itemsList.size(); i++){
            MenuItem p = (MenuItem) itemsList.get(i);
            if(p.getCategoryID().equals(((Category) categoriesList.get(position)).getId())){
                itemsListPos = i;
                break;
            }
        }
        catListView.requestFocusFromTouch();
        catListView.setSelection(position);
        catListView.requestFocus();
        itemsListView.requestFocusFromTouch();
        itemsListView.setSelection(itemsListPos);
        itemsListView.requestFocus();
    }

    public void sort(){
        //TODO based on more feedback

    }

    public void view_order(View view){
        //Log.i(LOG_TAG,"See order");
        //Intent intent = new Intent(this, MainViewCartActivity.class);
		Intent intent = new Intent(getApplicationContext(), MainViewCartActivity.class);
        this.startActivity(intent);
    }

    public void BackButton(View v)
    {
//img.setOnClickListener(new View.OnClickListener() {
        //  @Override
        //public void onClick(View v) {

        onBackPressed();
        //  }
//});
    }



    //changed private to public
    public class RetrievalTask extends AsyncTask<String, Integer, String> {

        private ProgressDialog dialog;
        private AppCompatActivity activity;
//29 June commenting out constructor
        //public RetrievalTask(AppCompatActivity activity) {
        //  this.activity = activity;
        //context = activity;
        //dialog = new ProgressDialog(context);
        //}

        //        protected void onPreExecute() {
//            this.dialog.setMessage("Progress start");
//            this.dialog.show();
//        }
        @Override
        protected String doInBackground(String... urls) {

            Resty r = new Resty();
            // old 2e851bcd49b140eaaef20435f5ce15f1
            r.withHeader("api-key", Constants.API_KEY);
            JSONObject categoriesObject;

            try {
                // getting array of categories
                categoriesObject = r.json(urls[0]).object();
                JSONArray categoriesList = categoriesObject.
                        getJSONObject("_embedded").getJSONArray("categories");
                for (int nCat = 0; nCat < categoriesList.length(); nCat++) {

                    try {
                        //new category
                        JSONObject category = categoriesList.getJSONObject(nCat);
                        Category c = new Category(category.getString(ID), category.getString(NAME));
                        imported_list.add(new Pair(c, new ArrayList<MenuItem>()));

                        JSONArray category_array = category.getJSONObject("_embedded").
                                getJSONArray("items");

                        for (int nItem = 0; nItem < category_array.length(); nItem++) {

                            try {
                                //new menu item
                                JSONObject itemJson = category_array.getJSONObject(nItem);
                                String id = itemJson.getString(ID);
                                String name = itemJson.getString(NAME);
                                Double price = itemJson.getDouble(PRICE);
                                boolean in_stock = itemJson.getBoolean(IN_STOCK);
                                int modifier_groups_count = itemJson.getInt(MODIFIER_GROUPS_COUNT);
                                String categoryID = (String) imported_list.get(nCat).first.getId();
                                ArrayList<MenuItem.PriceLevel> list = new ArrayList<MenuItem.PriceLevel>();
                                JSONArray price_levels_array = itemJson.getJSONArray(PRICE_LEVELS);
                                for (int nMod = 0; nMod < price_levels_array.length(); nMod++) {
                                    JSONObject pl = price_levels_array.getJSONObject(nMod);
                                    list.add(new MenuItem.PriceLevel(pl.getString("id"),
                                            pl.getInt("price")));
                                }

                                try {

                                    modifier_ref = itemJson.getJSONObject("_links").
                                            getJSONObject("modifier_groups").getString("href");
                                    mod_resource = r.json(modifier_ref).object();
                                }
                                catch(Exception e){
                                    Log.i("error with ref link", modifier_ref);
                                }

                                try {

                                    int num_mod_groups = mod_resource.getInt("count");
                                    JSONArray group_arr;
                                    ArrayList<MenuItem.ModifierGroup> modifierGroupArrayList = new ArrayList<>();

                                    if (num_mod_groups > 0) {
                                        group_arr = mod_resource.getJSONObject("_embedded").getJSONArray("modifier_groups");

                                        Log.i("modifier reference", modifier_ref);

                                        for (int nGrp = 0; nGrp < num_mod_groups; nGrp++) {

                                            JSONObject mod_group = group_arr.getJSONObject(nGrp);

                                            String id_mgroup = mod_group.getString("id");
                                            String name_mgroup = mod_group.getString("name");

                                            int max_mgroup;
                                            if(mod_group.isNull("maximum"))
                                                max_mgroup = Integer.MAX_VALUE;
                                            else
                                                max_mgroup = mod_group.getInt("maximum");

                                            int min_mgroup = mod_group.getInt("minimum");
                                            boolean required_mgroup = mod_group.getBoolean("required");

                                            ArrayList<MenuItem.ModifierGroup.ItemModifier> modlist =
                                                    new ArrayList<MenuItem.ModifierGroup.ItemModifier>();
                                            JSONArray optionsArray = mod_group.getJSONObject("_embedded").getJSONArray("options");

                                            //each modifier

                                            try {

                                                for (int nMod = 0; nMod < optionsArray.length(); nMod++) {
                                                    JSONObject mod = optionsArray.getJSONObject(nMod);
                                                    String id_mod = mod.getString("id");
                                                    String name_mod = mod.getString("name");
                                                    int price_per_unit_mod = mod.getInt("price_per_unit");
                                                    JSONArray price_levels_array_mod = itemJson.getJSONArray(PRICE_LEVELS);

                                                    ArrayList<MenuItem.PriceLevel> price_levels_mod = new ArrayList<MenuItem.PriceLevel>();
                                                    for (int x = 0; x < price_levels_array_mod.length(); x++) {
                                                        JSONObject pl = price_levels_array_mod.getJSONObject(x);
                                                        price_levels_mod.add(new MenuItem.PriceLevel(pl.getString("id"), pl.getInt("price")));
                                                    }
                                                    modlist.add(new MenuItem.ModifierGroup.ItemModifier(id_mod, name_mod,
                                                            price_per_unit_mod, price_levels_mod));
                                                }

                                                MenuItem.ModifierGroup mGroup = new MenuItem.ModifierGroup(id_mgroup,
                                                        name_mgroup, min_mgroup, max_mgroup, required_mgroup, modlist);

                                                modifierGroupArrayList.add(mGroup);

                                            } catch (Exception e) {
                                                Log.i(LOG_TAG, e.toString() + "FAILURE5");
                                            }
                                        }
                                    }

                                    MenuItem m = new MenuItem(id, name, price, list, in_stock, modifierGroupArrayList, modifier_groups_count, categoryID);
                                    imported_list.get(nCat).second.add(m);

                                }
                                catch(Exception e){
                                    Log.i(LOG_TAG, e.toString() + "FAILURE4");
                                }

                            }
                            catch (Exception e) {
                                Log.i(LOG_TAG, e.toString() + "FAILURE3");
                            }

                        }
                    }
                    catch (Exception e) {
                        Log.i(LOG_TAG, e.toString() + "FAILURE2");
                    }
                }
            } catch (Exception e) {
                Log.i(LOG_TAG, e.toString() + "FAILURE1");
            }
            Log.i(LOG_TAG, "imported_list "+imported_list.size());
            return "DONE"; //if meaningful string is required, it can be done
        }
        @Override
        protected void onPostExecute(String result) {
            //categoriesList and itemsList instantiated
            for(Pair p : imported_list){
                Category c = (Category) p.first;
                categoriesList.add(new Category(c.getId(), c.getName()));
                for(MenuItem m : (ArrayList<MenuItem>) p.second){
                    itemsList.add(m);
                }
            }
            Log.i("onPostExecute",
                    "number of categories "+String.valueOf(imported_list.size())+
                            "categories "+String.valueOf(categoriesList.size())+
                            " items "+String.valueOf(itemsList.size()));
            catRowAdapter.notifyDataSetChanged();
            itemRowAdapter.notifyDataSetChanged();
            loaded = true;
            //if (dialog.isShowing()) {
            //  dialog.dismiss();
            //   }
        }
    }
}

