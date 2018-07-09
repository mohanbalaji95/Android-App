package com.garcon.garcon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.garcon.garcon.Payment.CheckoutandPay;

public class AppBarActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Intent checkoutIntent = new Intent(getApplicationContext(), CheckoutandPay.class);
        Intent cartIntent = new Intent(getApplicationContext(), MainViewCartActivity.class);
        Intent homeIntent= new Intent(getApplicationContext(), homeactivity.class);
        Intent profileIntent = new Intent(getApplicationContext(), manageProfileActivity.class);
        Intent favIntent = new Intent(getApplicationContext(), FavoriteActivity.class);
        Intent historyIntent = new Intent(getApplicationContext(), OrderHistoryActivity.class);
        Intent settingsIntent = new Intent(getApplicationContext(), Settings.class);

        switch (id) {
            case R.id.cartMenu:
                startActivity(cartIntent);
                //To Do
                break;
            case R.id.pay:
                startActivity(checkoutIntent);
                break;

            case R.id.nav_home:
                startActivity(homeIntent);
                break;
            case R.id.nav_profilesettings:
                startActivity(profileIntent);
                break;
            case R.id.nav_item_sent:
                startActivity(favIntent);
                break;
            case R.id.nav_history:
                startActivity(historyIntent);
                break;
            case R.id.nav_settings:
                startActivity(settingsIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
