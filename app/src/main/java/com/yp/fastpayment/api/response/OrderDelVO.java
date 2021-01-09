package com.yp.fastpayment.api.response;


import java.util.List;

// "results": [
//         {
//         "order_no": "1609297576284100179",
//         "meal_hour_name": "晚餐",
//         "meal_hour_id": 5,
//         "target_date": "2020-12-30",
//         "reserve_status": 11,
//         "create_time": "2020-12-30T03:06:16.000+00:00",
//         "shop_id": 23,
//         "update_time": "2020-12-30T03:07:19.000+00:00",
//         "branch_id": 9,
//         "meal_taking_num": "7457",
//         "branch_name": "一分店",
//         "id": 964,
//         "day": 3,
//         "reserve_type": 3
//         }
//         ],
//         "page_num": 1,
//         "page_size": 9999,
//         "all_page_num": 1,
//         "record_count": 1,
//         "sql_name": "查询取消的预定单单号",
//         "sql_id": "GET-CANCLE-RESERVER-ORDERNOS"
public class OrderDelVO {
    private List<Data> results;

    public List<Data> getResults() {
        return results;
    }

    public void setResults(List<Data> results) {
        this.results = results;
    }

    public static class Data{
        private String order_no;
        private String meal_hour_name;
        private int meal_hour_id;
        private String target_date;
        private int reserve_status;
        private int shop_id;
        private int branch_id;
        private String create_time;
        private String update_time;
        private int meal_taking_num;
        private int reserve_type;

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public String getMeal_hour_name() {
            return meal_hour_name;
        }

        public void setMeal_hour_name(String meal_hour_name) {
            this.meal_hour_name = meal_hour_name;
        }

        public int getMeal_hour_id() {
            return meal_hour_id;
        }

        public void setMeal_hour_id(int meal_hour_id) {
            this.meal_hour_id = meal_hour_id;
        }

        public String getTarget_date() {
            return target_date;
        }

        public void setTarget_date(String target_date) {
            this.target_date = target_date;
        }

        public int getReserve_status() {
            return reserve_status;
        }

        public void setReserve_status(int reserve_status) {
            this.reserve_status = reserve_status;
        }

        public int getShop_id() {
            return shop_id;
        }

        public void setShop_id(int shop_id) {
            this.shop_id = shop_id;
        }

        public int getBranch_id() {
            return branch_id;
        }

        public void setBranch_id(int branch_id) {
            this.branch_id = branch_id;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public int getMeal_taking_num() {
            return meal_taking_num;
        }

        public void setMeal_taking_num(int meal_taking_num) {
            this.meal_taking_num = meal_taking_num;
        }

        public int getReserve_type() {
            return reserve_type;
        }

        public void setReserve_type(int reserve_type) {
            this.reserve_type = reserve_type;
        }
    }
}
