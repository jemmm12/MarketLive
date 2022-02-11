package com.ssafy.rtc.util;

public class GlobalConstants {

    // redis namespaces
    public static final String NAMESPACAE_ROOM = "room";
    public static final String NAMESPACE_TABLE = "table";
    public static final String NAMESPACE_INFO = "info";
    public static final String NAMESPACE_VIEWERS = "viewers";

    // roomDto key
    public static final String ROOMDTO_TITLE = "title";
    public static final String ROOMDTO_CATEGORY = "category";
    public static final String ROOMDTO_INTRODUCE = "introduce";
    public static final String ROOMDTO_STARTTIME = "starttime";
    public static final String ROOMDTO_ENDTIME = "endtime";
    public static final String ROOMDTO_THUMBNAIL = "thumbnail";

    // websocket url
    public static final String SOCKET_URL = "/play/{broadCasterUserId}";

    // rtc constants
    public static final String PROPERTY_ID = "id";
    public static final String PROPERTY_CANDIDATE = "candidate";
    public static final String PROPERTY_RESPONSE = "response";
    public static final String PROPERTY_MESSAGE = "message";
    public static final String PROPERTY_SDPANSWER = "sdpAnswer";
    public static final String VALUE_BROADCASTER = "broadCasterResponse";
    public static final String VALUE_VIEWER = "viewerResponse";
    public static final String VALUE_REJECTED = "rejected";
    public static final String VALUE_ICECANDIDATE = "iceCandidate";
    public static final String VALUE_ACCEPTED = "accepted";

}
