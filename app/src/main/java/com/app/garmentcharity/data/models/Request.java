package com.app.garmentcharity.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import androidx.annotation.NonNull;

public class Request implements Parcelable {

    private String id;
    private String title;
    private String clientId;
    private String clientName;
    private String organizationId;
    private String organizationName;
    private String status;
    private long dateTime;
    private List<RequestItem> requestItemList;

    public Request() {
    }

    protected Request(Parcel in) {
        id = in.readString();
        title = in.readString();
        clientId = in.readString();
        clientName = in.readString();
        organizationId = in.readString();
        organizationName = in.readString();
        status = in.readString();
        dateTime = in.readLong();
    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public List<RequestItem> getRequestItemList() {
        return requestItemList;
    }

    public void setRequestItemList(List<RequestItem> requestItemList) {
        this.requestItemList = requestItemList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(clientId);
        dest.writeString(clientName);
        dest.writeString(organizationId);
        dest.writeString(organizationName);
        dest.writeString(status);
        dest.writeLong(dateTime);
    }

    public enum RequestStatus {
        New, Confirmed, Delivered
    }
}
