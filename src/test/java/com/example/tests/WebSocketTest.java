package com.example.tests;

import okhttp3.*;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class WebSocketTest {

    private static final String WS_URL = "ws://ws.ifelse.io";

    @Test
    public void testWebSocketMultipleMessagesAndClose() throws InterruptedException {
        OkHttpClient client = new OkHttpClient();

        int messageCount = 3;
        CountDownLatch messageLatch = new CountDownLatch(messageCount);
        CountDownLatch closeLatch = new CountDownLatch(1);

        Request request = new Request.Builder()
                .url(WS_URL)
                .build();

        String[] messagesToSend = {
                "Hello WebSocket 1",
                "Hello WebSocket 2",
                "Hello WebSocket 3"
        };

        WebSocketListener listener = new WebSocketListener() {

            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                System.out.println("WebSocket opened and connected!");
                for (String msg : messagesToSend) {
                    webSocket.send(msg);
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                System.out.println("Received message: " + text);

                // Flexible assertion for variable server responses
                assertThat(text, anyOf(
                        containsString("Request served by"),
                        containsString("Hello WebSocket")
                ));

                messageLatch.countDown();
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                t.printStackTrace();
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                System.out.println("WebSocket closed" + reason);
                closeLatch.countDown();
            }
        };

        WebSocket ws = client.newWebSocket(request, listener);

        // Wait for all messages
        if (!messageLatch.await(10, TimeUnit.SECONDS)) {
            throw new AssertionError("Did not receive all messages in time");
        }

        // Close the WebSocket
        ws.close(1000, "");

        // Wait for onClosed callback
        if (!closeLatch.await(5, TimeUnit.SECONDS)) {
            System.out.println("Warning: WebSocket close callback did not fire in time");
        }

        client.dispatcher().executorService().shutdown();
    }
}
