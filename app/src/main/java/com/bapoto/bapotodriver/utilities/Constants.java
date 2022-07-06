package com.bapoto.bapotodriver.utilities;

import java.util.HashMap;

public class Constants {

    public static final String KEY_COLLECTION_USERS = "users";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_DATE = "date";
    public static final String KEY_HOUR = "hour";
    public static final String KEY_PICK_UP = "pickUp";
    public static final String KEY_DROP_OFF = "dropOff";
    public static final String KEY_INFOS = "infos";
    public static final String KEY_COLLECTION_RESERVATIONS = "reservations";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PREFERENCE_NAME = "chatAppPreference";
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";
    public static final String KEY_RESERVATION_ID = "reservationId";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_FCM_TOKEN = "fcmToken";
    public static final String KEY_USER = "user";
    public static final String KEY_COLLECTION_CHAT = "chat";
    public static final String KEY_SENDER_ID = "sender_id";
    public static final String KEY_RECEIVER_ID = "receiver_id";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_COLLECTION_CONVERSATIONS = "conversations";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_RECEIVER_NAME = "receiverName";
    public static final String KEY_SENDER_IMAGE = "senderImage";
    public static final String KEY_RECEIVER_IMAGE = "receiverImage";
    public static final String KEY_LAST_MESSAGE = "lastMessage";
    public static final String KEY_AVAILABILITY = "available";
    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT = "Content-Type";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION = "registration_ids";
    public static final String KEY_PRICE = "price";
    public static final String KEY_COLLECTION_ADMIN = "admin";
    public static final String KEY_IS_ADMIN = "isAdmin";
    public static final String KEY_IS_DRIVER = "isDriver";
    public static final String KEY_ACCEPTED_BY = "acceptedBy";
    public static final String IS_ACCEPTED = "isAccepted";
    public static final String KEY_ACCEPTED_THE = "dayAccepted";
    public static final String KEY_DRIVED_BY = "driver";


    public static HashMap<String,String> remoteMsgHeaders = null;
    public static HashMap<String, String> getRemoteMsgHeaders() {
        if(remoteMsgHeaders == null) {
            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(
                    REMOTE_MSG_AUTHORIZATION,
                    "key=AAAAqbR-ZMI:APA91bFvEqfDUjkhJ5ln7PvD3Jtp3ej6i2J_cLR2RT4YTKVmnqlnYZmMgXSXy_uh82KuAkhg-BEimDV7lSTKDxGrNRTzXT7dm1vGIbL1qsoV5IvIKWU58q43bVQkfsnsWHIKt0o4jfSm"
            );
            remoteMsgHeaders.put(
                    REMOTE_MSG_CONTENT,
                    "application/json"
            );
        }
        return remoteMsgHeaders;
    }

}
