package com.cxqm.xiaoerke.modules.plan.entity;

import java.util.Date;

public class NutritionEvaluate {
    private Long id;

    private String oilSalt;//油盐类

    private String vegetables;//蔬菜类

    private String millet;//谷类

    private String potato;//薯类

    private String milk;//奶类

    private String meat;//肉类

    private String fishEggs;//鱼禽肉蛋类

    private String water;//水

    private Date createTime;
    
    private Date updateTime;
    
    private String createBy;
    
    private String updateBy;
    
    private String userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOilSalt() {
		return oilSalt;
	}

	public void setOilSalt(String oilSalt) {
		this.oilSalt = oilSalt;
	}

	public String getVegetables() {
		return vegetables;
	}

	public void setVegetables(String vegetables) {
		this.vegetables = vegetables;
	}

	public String getMillet() {
		return millet;
	}

	public void setMillet(String millet) {
		this.millet = millet;
	}

	public String getPotato() {
		return potato;
	}

	public void setPotato(String potato) {
		this.potato = potato;
	}

	public String getMilk() {
		return milk;
	}

	public void setMilk(String milk) {
		this.milk = milk;
	}

	public String getMeat() {
		return meat;
	}

	public void setMeat(String meat) {
		this.meat = meat;
	}

	public String getFishEggs() {
		return fishEggs;
	}

	public void setFishEggs(String fishEggs) {
		this.fishEggs = fishEggs;
	}

	public String getWater() {
		return water;
	}

	public void setWater(String water) {
		this.water = water;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
    
}