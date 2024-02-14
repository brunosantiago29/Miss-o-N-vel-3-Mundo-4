package com.example.myapp;

import android.content.Context;
import android.media.AudioDeviceInfo;
import android.media.AudioDeviceCallback;
import android.media.AudioManager;

public class AudioCallbackHelper {
    private final AudioManager audioManager;

    public AudioCallbackHelper(Context context) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    // Remova este método se ele não estiver sendo utilizado
    public void registerAudioDeviceCallback() {
        audioManager.registerAudioDeviceCallback(new AudioDeviceCallback() {
            @Override
            public void onAudioDevicesAdded(AudioDeviceInfo[] addedDevices) {
                // Adicione lógica aqui se necessário
            }

            @Override
            public void onAudioDevicesRemoved(AudioDeviceInfo[] removedDevices) {
                // Adicione lógica aqui se necessário
            }
        }, null);
    }

    // Remova este método se ele não estiver sendo utilizado
    private boolean audioOutputAvailable(int type) {
        AudioDeviceInfo[] devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);

        for (AudioDeviceInfo device : devices) {
            if (device.getType() == type) {
                return true;
            }
        }

        return false;
    }
}
