package com.comparison.xrv.service;

final class MessageDiffService {
    private MessageDiffService() {
    }

    static String normalizeMessage(final String message) {
        return message == null ? "" : message.toLowerCase().replaceAll("\\s+", "");
    }

    static String buildDiff(final String xrvMessage, final String gatewayMessage) {
        final String left = xrvMessage == null ? "" : xrvMessage;
        final String right = gatewayMessage == null ? "" : gatewayMessage;

        if (left.equals(right)) {
            return "";
        }

        int start = 0;
        final int minLength = Math.min(left.length(), right.length());
        while (start < minLength && left.charAt(start) == right.charAt(start)) {
            start++;
        }

        int leftEnd = left.length() - 1;
        int rightEnd = right.length() - 1;
        while (leftEnd >= start && rightEnd >= start && left.charAt(leftEnd) == right.charAt(rightEnd)) {
            leftEnd--;
            rightEnd--;
        }

        final String leftChunk = left.substring(start, leftEnd + 1);
        final String rightChunk = right.substring(start, rightEnd + 1);

        return "XRV:'" + leftChunk + "' | JQA:'" + rightChunk + "'";
    }
}
