package com.jacky.strive.oauth2.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class User {

	//decimal这种标准数据类型。	其区别在于，float，double等非标准类型，在DB中保存的是近似值，而Decimal则以字符串的形式保存数值
	private String userid;

	private String username;

	private String password;

	private String telphone;

	private String email;

	private String image;

	private String country;

	private String address;

	private Date lastlogin;

	private Date addtime;

	private Date modifytime;

	private Date expiredtime;

	private Byte isactivated;

	private String permissioncode;

	private String remark;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid == null ? null : userid.trim();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username == null ? null : username.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone == null ? null : telphone.trim();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image == null ? null : image.trim();
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country == null ? null : country.trim();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address == null ? null : address.trim();
	}

	public Date getLastlogin() {
		return lastlogin;
	}

	public void setLastlogin(Date lastlogin) {
		this.lastlogin = lastlogin;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Date getModifytime() {
		return modifytime;
	}

	public void setModifytime(Date modifytime) {
		this.modifytime = modifytime;
	}

	public Date getExpiredtime() {
		return expiredtime;
	}

	public void setExpiredtime(Date expiredtime) {
		this.expiredtime = expiredtime;
	}

	public Byte getIsactivated() {
		return isactivated;
	}

	public void setIsactivated(Byte isactivated) {
		this.isactivated = isactivated;
	}

	public String getPermissioncode() {
		return permissioncode;
	}

	public void setPermissioncode(String permissioncode) {
		this.permissioncode = permissioncode == null ? null : permissioncode.trim();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}
}