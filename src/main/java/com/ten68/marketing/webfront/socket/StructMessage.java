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

    // The input string is a log line, with format of  "1782805743589 hello world"
    public static StructMessage build (String msg) {
        long timestamp = checkAndParse(msg);
        if (timestamp < 0)
            return INVALID;
        String content = msg.substring( msg.indexOf(" ") + 1 ).trim();
        return new StructMessage(timestamp, content);
    }

    private static long checkAndParse (String msg) {
        final long ERROR = -1;

        if (msg == null || msg.isBlank())
            return ERROR;

        String[] token = msg.split("\\s+");
        if (token.length < 1)
            return ERROR;

        try {
            return Long.parseLong(token[0]);
        } catch (NumberFormatException e) {
            return ERROR;
        }
    }


}
