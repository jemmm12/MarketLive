package com.ssafy.rtc.util;

public class constants {

    // redis namespaces
    public static final String NAMESPACAE_ROOM = "room";
    public static final String NAMESPACE_TABLE = "table";
    public static final String NAMESPACE_INFO = "info";
    public static final String NAMESPACE_VIEWERS = "viewers";

    public static String generateRoomInfoKey(String userid) {
        StringBuffer sb = new StringBuffer();
        sb.append(NAMESPACAE_ROOM)
                .append(":")
                .append(NAMESPACE_INFO)
                .append(":")
                .append(userid);
        return sb.toString();
    }

    public static String generateRoomViewersKey(String userid) {
        StringBuffer sb = new StringBuffer();
        sb.append(NAMESPACAE_ROOM)
                .append(":")
                .append(NAMESPACE_VIEWERS)
                .append(":")
                .append(userid);
        return sb.toString();
    }

    // roomDto key
    public static final String ROOMDTO_TITLE = "title";
    public static final String ROOMDTO_CATEGORY = "category";
    public static final String ROOMDTO_INTRODUCE = "introduce";
    public static final String ROOMDTO_STARTTIME = "starttime";
    public static final String ROOMDTO_ENDTIME = "endtime";
    public static final String ROOMDTO_THUMBNAIL = "thumbnail";

}
