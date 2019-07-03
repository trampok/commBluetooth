package com.example.commbluetooth;

public interface Transceiver {
    interface TransceiverEventListener extends Transceiver {
        void onTransceiverEvent();
    }

    interface TransceiverDataListener extends Transceiver {
        void onTransceiverData();
    }
}
