package com.garcon.garcon;

/**
 * Created by kritikagopalakrishnan on 6/2/16.
 */
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;


public class ThirdFragment extends Fragment implements BeaconConsumer{
    private BeaconManager beaconManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        beaconManager = BeaconManager.getInstanceForApplication(getActivity());
        beaconManager.bind(this);
        return inflater.inflate(R.layout.third_layout, null);


    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                System.out.println("I just saw an beacon for the first time!");
                Toast.makeText(getApplicationContext(),
                        "I just saw an beacon for the first time!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void didExitRegion(Region region) {
                System.out.println( "I no longer see an beacon");
                Toast.makeText(getApplicationContext(),
                        "I no longer see an beacon", Toast.LENGTH_LONG).show();
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                System.out.println( "I have just switched from seeing/not seeing beacons: "+state);
                Toast.makeText(getApplicationContext(),
                        "I have just switched from seeing/not seeing beacons: "+state, Toast.LENGTH_LONG).show();
            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        } catch (RemoteException e) {    }

    }

    @Override
    public Context getApplicationContext() {
        return null;
    }


    @Override
    public void unbindService(ServiceConnection serviceConnection) {

    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }
}