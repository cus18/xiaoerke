package com.cxqm.xiaoerke.modules.consult.entity;


/**
 * 送心意优惠卡vo
 * Created by sunxiao on 2016-10-31.
 */
public class SendMindCouponVo {

        private Integer id ;

        private String name;

        private String link ;

        private String image;

        public Integer getId() {
                return id;
        }

        public String getImage() {
                return image;
        }

        public void setImage(String image) {
                this.image = image;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getLink() {
                return link;
        }

        public void setLink(String link) {
                this.link = link;
        }
}
