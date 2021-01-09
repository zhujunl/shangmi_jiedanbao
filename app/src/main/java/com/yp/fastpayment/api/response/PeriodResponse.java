package com.yp.fastpayment.api.response;

import java.util.List;

public class PeriodResponse {
    private int code;
    private String message;
    private List<Data> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data{
        private String id;

        private String mealHourName;

        private String startTime;

        private String endTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMealHourName() {
            return mealHourName;
        }

        public void setMealHourName(String mealHourName) {
            this.mealHourName = mealHourName;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }
}
