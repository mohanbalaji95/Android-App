package com.garcon.garcon;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONObject;
import us.monoid.web.Resty;

public class MainMenuActivity extends AppCompatActivity {

    public final static String LOG_TAG = MainMenuActivity.class.getSimpleName();
    ListView catListView, itemsListView;
    List categoriesList, itemsList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        OrderSingleton.getInstance();
        setContentView(R.layout.activity_main_menu);

        //TODO intent will send string for location
        String location = "AieMdB5i";
        //old location 8cg4k4kc

        categoriesList = new ArrayList<Category>();
        catRowAdapter = new MenuCategoryAdapter((ArrayList<Category>) categoriesList, this);
        itemsList = new ArrayList<MenuItem>();
        itemRowAdapter = new MenuItemAdapter((ArrayList<MenuItem>) itemsList, this);

        //final String categoryURL = "https://api.omnivore.io/0.1/locations/"+location+"/menu/categories/";
        final String categoryURL = "https://api.omnivore.io/0.1/locations/AieMdB5i/menu/categories/";
        String[] mURL = {categoryURL};
        new RetrievalTask(MainMenuActivity.this).execute(mURL);

        //try{String s = new RetrievalTask(MainMenuActivity.this).execute(categoryURL).get();}
        //catch(CancellationException |ExecutionException |InterruptedException e){Log.i(LOG_TAG,e.toString());}

        //dummy
        /*
            int numCategories = 5;
            int numItemsPer = 5;
            for(int i = 0; i < numCategories; i++){
                String id = String.valueOf(i);
                String name = "Category "+i;
                categoriesList.add(new Category(id,name));
            }
            for(int i = 0; i < numCategories*numItemsPer; i++){
                String id = String.valueOf(i/numItemsPer);
                String name = "MenuItem "+(i/numItemsPer)+"."+(i%numItemsPer+1);
                itemsList.add(new MenuItem(null,name,123,null,false,null,0,id));
            }
        */

        catListView = (ListView) findViewById(R.id.categories);
        catListView.setAdapter(catRowAdapter);

        itemsListView = (ListView) findViewById(R.id.items);
        itemsListView.setAdapter(itemRowAdapter);

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

        //TODO only show if number of items ordered if total > 0
        //final RelativeLayout displayOrderLV = (RelativeLayout) findViewById(R.id.displayViewCart);
        //displayOrderLV.setVisibility(View.GONE);

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
    /*
    Collections.sort(myList, new Comparator<myDataType>(){
        public int compare(myDataType o1, myDataType o2){
            if(o1.attribute == o2.attribute)
                return 0;
            return o1.attribute < o2.attribute ? -1 : 1;
        }
    });
    */
    }

    public void view_order(View view){
        //Log.i(LOG_TAG,"See order");
        Intent intent = new Intent(this, MainViewCartActivity.class);
        this.startActivity(intent);
    }

    private class RetrievalTask extends AsyncTask<String, Integer, String> {

        private ProgressDialog dialog;
        private AppCompatActivity activity;

        public RetrievalTask(AppCompatActivity activity) {
            this.activity = activity;
            context = activity;
            dialog = new ProgressDialog(context);
        }

        protected void onPreExecute() {
            this.dialog.setMessage("Progress start");
            this.dialog.show();
        }
        @Override
        protected String doInBackground(String... urls) {

            Resty r = new Resty();
            // old 2e851bcd49b140eaaef20435f5ce15f1
            r.withHeader("Api-Key", "590c27de8c41473b8014af7553bb85c4");
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
                                int price = itemJson.getInt(PRICE);
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
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

}