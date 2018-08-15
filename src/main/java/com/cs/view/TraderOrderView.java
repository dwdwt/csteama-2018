package com.cs.view;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TraderOrderView {
    public TraderOrderView(DateTime lastOrderTimeStamp, int openedOrderCount, int fulfilledOrderCount, int cancelledOrderCount) {
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        this.lastOrderTimeStamp = dtfOut.print(lastOrderTimeStamp);
        this.opened = openedOrderCount;
        this.fulfilled = fulfilledOrderCount;
        this.cancelled = cancelledOrderCount;
    }

    public String lastOrderTimeStamp;
    public int opened;
    public int fulfilled;
    public int cancelled;

}
