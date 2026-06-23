package com.ten68.marketing.webfront.socket;

public record StructMessage (long timestamp, String message) implements Comparable<StructMessage> {
    public static final StructMessage INVALID = new StructMessage(-1L, "invalid");

    @Override
    public int compareTo(StructMessage structMessage) {
        return Long.compare(this.timestamp, structMessage.timestamp);
    }

    public boolean isLater (StructMessage structMessage) {
        return compareTo(structMessage) > 0;
    }

    public static StructMessage build (String msg) {
        long timestamp = checkAndParse(msg);
        return
                timestamp < 0 ?
                INVALID :
                new StructMessage(timestamp, msg);
    }

    private static long checkAndParse (String msg) {
        if (msg == null || msg.isBlank())
            return -1;

        String[] token = msg.split("\\s+");
        if (token.length < 1)
            return -1;

        try {
            return Long.parseLong(token[0]);
        } catch (NumberFormatException e) {
            return -1;
        }
    }


}
