package com.mp.poc.s37uberandroid.network

import com.mp.poc.s37mapsimulator.WebSocket
import com.mp.poc.s37mapsimulator.WebSocketListener

class NetworkService {

    fun createWebSocket(webSocketListener: WebSocketListener): WebSocket {
        return WebSocket(webSocketListener)
    }

}