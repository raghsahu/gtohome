package com.grocery.gtohome.model.slot_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 12-May-20.
 */
public class ModulePincodedays {
    @SerializedName("error")
    @Expose
    private List<Object> error = null;
//    @SerializedName("timeslots_today")
//    @Expose
//    private TimeslotsToday timeslotsToday;
    @SerializedName("pincodedays")
    @Expose
    private Integer pincodedays;
    @SerializedName("deliverytoday")
    @Expose
    private Integer deliverytoday;
    @SerializedName("module_pincodedays_extrafields_heading")
    @Expose
    private String modulePincodedaysExtrafieldsHeading;
    @SerializedName("module_pincodedays_extrafields")
    @Expose
    private String modulePincodedaysExtrafields;
    @SerializedName("blockdays")
    @Expose
    private String blockdays;
    @SerializedName("timeslots")
    @Expose
    private Timeslots timeslots;
    @SerializedName("dateformat")
    @Expose
    private String dateformat;
    @SerializedName("text_sdelivery_date")
    @Expose
    private String textSdeliveryDate;
    @SerializedName("text_enterdate")
    @Expose
    private String textEnterdate;
    @SerializedName("error_dmy_format")
    @Expose
    private String errorDmyFormat;
    @SerializedName("error_mdy_format")
    @Expose
    private String errorMdyFormat;
    @SerializedName("isitoptional")
    @Expose
    private String isitoptional;
    @SerializedName("pincodedays_deliverydate")
    @Expose
    private String pincodedaysDeliverydate;
    @SerializedName("pincodedays_timeslot")
    @Expose
    private Integer pincodedaysTimeslot;
    @SerializedName("pincodedays_daynumber")
    @Expose
    private String pincodedaysDaynumber;
    @SerializedName("displayslots")
    @Expose
    private Integer displayslots;
    @SerializedName("blockeddays")
    @Expose
    private List<Object> blockeddays = null;
    @SerializedName("individualslots")
    @Expose
    private Individualslots individualslots;
    @SerializedName("todayslots")
    @Expose
    private Integer todayslots;
    @SerializedName("timeslotcharges")
    @Expose
    private Integer timeslotcharges;
    @SerializedName("areslotsavailable")
    @Expose
    private Integer areslotsavailable;

    public List<Object> getError() {
        return error;
    }

    public void setError(List<Object> error) {
        this.error = error;
    }

//    public TimeslotsToday getTimeslotsToday() {
//        return timeslotsToday;
//    }
//
//    public void setTimeslotsToday(TimeslotsToday timeslotsToday) {
//        this.timeslotsToday = timeslotsToday;
//    }

    public Integer getPincodedays() {
        return pincodedays;
    }

    public void setPincodedays(Integer pincodedays) {
        this.pincodedays = pincodedays;
    }

    public Integer getDeliverytoday() {
        return deliverytoday;
    }

    public void setDeliverytoday(Integer deliverytoday) {
        this.deliverytoday = deliverytoday;
    }

    public String getModulePincodedaysExtrafieldsHeading() {
        return modulePincodedaysExtrafieldsHeading;
    }

    public void setModulePincodedaysExtrafieldsHeading(String modulePincodedaysExtrafieldsHeading) {
        this.modulePincodedaysExtrafieldsHeading = modulePincodedaysExtrafieldsHeading;
    }

    public String getModulePincodedaysExtrafields() {
        return modulePincodedaysExtrafields;
    }

    public void setModulePincodedaysExtrafields(String modulePincodedaysExtrafields) {
        this.modulePincodedaysExtrafields = modulePincodedaysExtrafields;
    }

    public String getBlockdays() {
        return blockdays;
    }

    public void setBlockdays(String blockdays) {
        this.blockdays = blockdays;
    }

    public Timeslots getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(Timeslots timeslots) {
        this.timeslots = timeslots;
    }

    public String getDateformat() {
        return dateformat;
    }

    public void setDateformat(String dateformat) {
        this.dateformat = dateformat;
    }

    public String getTextSdeliveryDate() {
        return textSdeliveryDate;
    }

    public void setTextSdeliveryDate(String textSdeliveryDate) {
        this.textSdeliveryDate = textSdeliveryDate;
    }

    public String getTextEnterdate() {
        return textEnterdate;
    }

    public void setTextEnterdate(String textEnterdate) {
        this.textEnterdate = textEnterdate;
    }

    public String getErrorDmyFormat() {
        return errorDmyFormat;
    }

    public void setErrorDmyFormat(String errorDmyFormat) {
        this.errorDmyFormat = errorDmyFormat;
    }

    public String getErrorMdyFormat() {
        return errorMdyFormat;
    }

    public void setErrorMdyFormat(String errorMdyFormat) {
        this.errorMdyFormat = errorMdyFormat;
    }

    public String getIsitoptional() {
        return isitoptional;
    }

    public void setIsitoptional(String isitoptional) {
        this.isitoptional = isitoptional;
    }

    public String getPincodedaysDeliverydate() {
        return pincodedaysDeliverydate;
    }

    public void setPincodedaysDeliverydate(String pincodedaysDeliverydate) {
        this.pincodedaysDeliverydate = pincodedaysDeliverydate;
    }

    public Integer getPincodedaysTimeslot() {
        return pincodedaysTimeslot;
    }

    public void setPincodedaysTimeslot(Integer pincodedaysTimeslot) {
        this.pincodedaysTimeslot = pincodedaysTimeslot;
    }

    public String getPincodedaysDaynumber() {
        return pincodedaysDaynumber;
    }

    public void setPincodedaysDaynumber(String pincodedaysDaynumber) {
        this.pincodedaysDaynumber = pincodedaysDaynumber;
    }

    public Integer getDisplayslots() {
        return displayslots;
    }

    public void setDisplayslots(Integer displayslots) {
        this.displayslots = displayslots;
    }

    public List<Object> getBlockeddays() {
        return blockeddays;
    }

    public void setBlockeddays(List<Object> blockeddays) {
        this.blockeddays = blockeddays;
    }

    public Individualslots getIndividualslots() {
        return individualslots;
    }

    public void setIndividualslots(Individualslots individualslots) {
        this.individualslots = individualslots;
    }

    public Integer getTodayslots() {
        return todayslots;
    }

    public void setTodayslots(Integer todayslots) {
        this.todayslots = todayslots;
    }

    public Integer getTimeslotcharges() {
        return timeslotcharges;
    }

    public void setTimeslotcharges(Integer timeslotcharges) {
        this.timeslotcharges = timeslotcharges;
    }

    public Integer getAreslotsavailable() {
        return areslotsavailable;
    }

    public void setAreslotsavailable(Integer areslotsavailable) {
        this.areslotsavailable = areslotsavailable;
    }

}
