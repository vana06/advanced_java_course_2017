package edu.technopolis.advanced.boatswain.response;

import com.fasterxml.jackson.annotation.JsonSetter;

public class SendMessageResponse extends Response {
    String recipientId;
    String messageId;
    String error_code;
    String error_msg;
    String error_data;
    String error_field;

    public String getRecipientId() {
        return recipientId;
    }

    @JsonSetter("recipient_id")
    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getMessageId() {
        return messageId;
    }

    @JsonSetter("message_id")
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public String getError_data() {
        return error_data;
    }

    public void setError_data(String error_data) {
        this.error_data = error_data;
    }

    public String getError_field() {
        return error_field;
    }

    public void setError_field(String error_field) {
        this.error_field = error_field;
    }

    @Override
    public String toString() {
        return "SendMessageResponse{" +
                "recipientId='" + recipientId + '\'' +
                ", messageId='" + messageId + '\'' +
                ", error_code='" + error_code + '\'' +
                ", error_msg='" + error_msg + '\'' +
                ", error_data='" + error_data + '\'' +
                ", error_field='" + error_field + '\'' +
                '}';
    }
}
