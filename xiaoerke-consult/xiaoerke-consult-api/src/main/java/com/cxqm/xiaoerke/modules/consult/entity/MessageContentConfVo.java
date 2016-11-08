package com.cxqm.xiaoerke.modules.consult.entity;


/**
 * 文案配置vo
 * Created by sunxiao on 2016-10-31.
 */
public class MessageContentConfVo {

        private Integer id ;

        private String scene;

        private String week ;

        private String startTime ;

        private String endTime;

        private String priority;

        private String content;

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public String getScene() {
                return scene;
        }

        public void setScene(String scene) {
                this.scene = scene;
        }

        public String getWeek() {
                return week;
        }

        public void setWeek(String week) {
                this.week = week;
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

        public String getPriority() {
                return priority;
        }

        public void setPriority(String priority) {
                this.priority = priority;
        }

        public String getContent() {
                return content;
        }

        public void setContent(String content) {
                this.content = content;
        }
}
