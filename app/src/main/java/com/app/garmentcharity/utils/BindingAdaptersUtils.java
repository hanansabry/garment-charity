package com.app.garmentcharity.utils;

import android.view.View;
import android.widget.TextView;

import com.app.garmentcharity.data.models.Request;
import com.app.garmentcharity.data.models.RequestItem;

import java.util.List;

import androidx.databinding.BindingAdapter;

public class BindingAdaptersUtils {

    @BindingAdapter({"bind:startDate", "bind:closeDate"})
    public static void bindOrderDates(TextView textView, long startDate, long closeDate) {
        textView.setText(String.format("From: %1s to: %2s",
                Utils.convertMillisecondsToDate(startDate, Constants.DATE_FORMAT),
                Utils.convertMillisecondsToDate(closeDate, Constants.DATE_FORMAT))
        );
    }

    @BindingAdapter("date")
    public static void bindDate(TextView textView, Long milliseconds) {
        if (milliseconds != null) {
            textView.setText(Utils.convertMillisecondsToDate(milliseconds, Constants.DATE_FORMAT));
        }
    }

    @BindingAdapter("time")
    public static void bindTime(TextView textView, Long milliseconds) {
        if (milliseconds != null) {
            textView.setText(Utils.convertMillisecondsToDate(milliseconds, Constants.TIME_FORMAT));
        }
    }

    @BindingAdapter("dateTime")
    public static void bindDateTime(TextView textView, Long milliseconds) {
        if (milliseconds != null) {
            textView.setText(Utils.convertMillisecondsToDate(milliseconds, Constants.DATE_TIME_FORMAT));
        }
    }

    @BindingAdapter({"bind:points", "bind:showPoints"})
    public static void bindDateTime(TextView textView, List<RequestItem> requestItemList, String status) {
        double requestPoints = calculateRequestPoints(requestItemList);
        if (status.equals(Request.RequestStatus.Delivered.name())) {
            textView.setText(String.format("Points: %s", requestPoints));
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    private static Double calculateRequestPoints(List<RequestItem> requestItemList) {
        double points = 0.0;
        for (RequestItem requestItem : requestItemList) {
            points += requestItem.getItem().getPoints() * requestItem.getQuantity();
        }
        return points;
    }

    @BindingAdapter("delivery")
    public static void bindDelivery(TextView textView, boolean isDelivery) {
        if (isDelivery) {
            textView.setText("Delivery");
        } else {
            textView.setText("No Delivery");
        }
    }
}
