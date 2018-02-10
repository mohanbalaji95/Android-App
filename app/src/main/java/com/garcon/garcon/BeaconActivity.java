package com.garcon.garcon;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.garcon.garcon.R;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
/**
 * iBeacon implementation.
 * No longer used.
 */
public class BeaconActivity extends AppCompatActivity implements BeaconConsumer{

    private BeaconManager beaconManager;
    private TextView text_UUID;
    private TextView text_major;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        text_UUID = (TextView) findViewById(R.id.text_uuid_holder);
        text_major = (TextView) findViewById(R.id.text_major_holder);
        setSupportActionBar(toolbar);



    }

    @Override
    public void onBeaconServiceConnect() {
        // Set the two identifiers below to null to detect any beacon regardless of identifiers
        //Identifier myBeaconNamespaceId = Identifier.parse("0x2f234454f4911ba9ffa6");
        //Identifier myBeaconInstanceId = Identifier.parse("0x000000000001");

        //Region region = new Region("my-beacon-region", null, null, null);
        final Region region = new Region("myBeacons", Identifier.parse("E2C56DB5-DFFB-48D2-B060-D0F5A71096E0"), null, null);
        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                try {
                    System.out.println( "didEnterRegion");
                    beaconManager.startRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didExitRegion(Region region) {
                try {
                    //Log.d(TAG, "didExitRegion");
                    System.out.println( "didExitRegion");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            text_UUID.setText("");
                            text_major.setText("");
                        }
                    });
                    beaconManager.stopRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {

            }
        });

        beaconManager.setRangeNotifier(new RangeNotifier() {
            String uuid=null;
            String major=null;
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                for(Beacon oneBeacon : beacons) {
                    System.out.println( "distance: " + oneBeacon.getDistance() + " id:" + oneBeacon.getId1() + "/" + oneBeacon.getId2() + "/" + oneBeacon.getId3());
                    uuid=oneBeacon.getId1().toString();
                    major=oneBeacon.getId2().toString();
                    final ArrayList<String> restList = new ArrayList<String>();
                    if(major.equals("1")){
                        restList.add("Virtual POS");
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String d= returnData();
                            text_UUID.setText(d.split(":")[0]);
                            text_major.setText(d.split(":")[1]);
                            ListView lv = (ListView) findViewById(R.id.restaurant_list);
                            lv.setAdapter(new ArrayAdapter<String>(BeaconActivity.this,android.R.layout.simple_list_item_1,(String[])restList.toArray()));
                        }
                    });

                }
            }
            public String returnData(){
                return uuid+":"+major;
            }

        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        beaconManager = BeaconManager.getInstanceForApplication(this);

        beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        beaconManager.bind(this);
    }



    @Override
    public void onPause() {
        super.onPause();
        beaconManager.unbind(this);
    }
}
