package com.example.myapp;

import static android.bluetooth.BluetoothAdapter.getDefaultAdapter;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_BLUETOOTH_PERMISSION = 1;

    private TextView textViewStatus;
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewStatus = findViewById(R.id.textViewStatus);

        // Inicializa o adaptador Bluetooth
        bluetoothAdapter = getDefaultAdapter();

        // Verifica se o dispositivo suporta Bluetooth
        if (bluetoothAdapter == null) {
            textViewStatus.setText("Bluetooth não suportado neste dispositivo");
        } else {
            // Registra um receptor de transmissão para detectar dispositivos Bluetooth
            IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(bluetoothReceiver, filter);

            // Verifica permissões Bluetooth
            checkBluetoothPermission();
        }
    }

    private void checkBluetoothPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {
            // Solicita permissão
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.BLUETOOTH},
                    REQUEST_BLUETOOTH_PERMISSION
            );
        } else {
            // Permissão já concedida, inicia a descoberta de dispositivos Bluetooth
            startBluetoothDiscovery();
        }
    }

    private void startBluetoothDiscovery() {
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                // Se o Bluetooth não estiver ativado, solicite ao usuário para ativá-lo
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_BLUETOOTH_PERMISSION);
            } else {
                // Bluetooth ativado, inicia a descoberta de dispositivos Bluetooth
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                bluetoothAdapter.startDiscovery();
            }
        }
    }

    // BroadcastReceiver para detectar mudanças no estado do Bluetooth
    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        textViewStatus.setText("Bluetooth desativado");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        // Bluetooth está sendo desativado...
                        break;
                    case BluetoothAdapter.STATE_ON:
                        // Bluetooth está ativado, inicia a descoberta de dispositivos Bluetooth
                        startBluetoothDiscovery();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        // Bluetooth está sendo ativado...
                        break;
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Certifique-se de desregistrar o receptor de transmissão ao encerrar a atividade
        unregisterReceiver(bluetoothReceiver);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSION
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permissão concedida, inicia a descoberta de dispositivos Bluetooth
            startBluetoothDiscovery();
        } else {
            // Permissão negada, lida com isso conforme necessário
            textViewStatus.setText("Permissão Bluetooth negada.");
        }
    }
}
