package tk.zedlabs.sidb.POJO;

import com.google.gson.annotations.SerializedName;

    public class Data {

        @SerializedName("id")
        private Integer id;

        @SerializedName("email")
        private String email;

        public Integer getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

    }

