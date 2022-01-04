package com.example.winteq.model.wms;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WmsData {

	@Expose
	@SerializedName("wms_id")
	private String wms_id;

	@Expose
	@SerializedName("no_tag")
	private String no_tag;

	@Expose
	@SerializedName("date")
	private String date;

	@Expose
	@SerializedName("item_name")
	private String item_name;

	@Expose
	@SerializedName("type")
	private String type;

	@Expose
	@SerializedName("lifetime_wms")
	private String lifetime_wms;

	@Expose
	@SerializedName("qty")
	private String qty;

	@Expose
	@SerializedName("category")
	private String category;

	@Expose
	@SerializedName("copro")
	private String copro;

	@Expose
	@SerializedName("area")
	private String area;

	@Expose
	@SerializedName("cabinet")
	private String cabinet;

	@Expose
	@SerializedName("shelf")
	private String shelf;

	@Expose
	@SerializedName("image")
	private String image;

	@Expose
	@SerializedName("description")
	private String description;

	@Expose
	@SerializedName("elc_tag")
	private String elc_tag;

	@Expose
	@SerializedName("mec_tag")
	private String mec_tag;


	@Expose
	@SerializedName("status")
	private boolean status;

	@Expose
	@SerializedName("message")
	private String[] message;

	public String getWms_id() {
		return wms_id;
	}

	public void setWms_id(String wms_id) {
		this.wms_id = wms_id;
	}

	public String getNo_tag() {
		return no_tag;
	}

	public void setNo_tag(String no_tag) {
		this.no_tag = no_tag;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLifetime_wms() {
		return lifetime_wms;
	}

	public void setLifetime_wms(String lifetime_wms) {
		this.lifetime_wms = lifetime_wms;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCopro() {
		return copro;
	}

	public void setCopro(String copro) {
		this.copro = copro;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCabinet() {
		return cabinet;
	}

	public void setCabinet(String cabinet) {
		this.cabinet = cabinet;
	}

	public String getShelf() {
		return shelf;
	}

	public void setShelf(String shelf) {
		this.shelf = shelf;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getElc_tag() {
		return elc_tag;
	}

	public void setElc_tag(String elc_tag) {
		this.elc_tag = elc_tag;
	}

	public String getMec_tag() {
		return mec_tag;
	}

	public void setMec_tag(String mec_tag) {
		this.mec_tag = mec_tag;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String[] getMessage() {
		return message;
	}

	public void setMessage(String[] message) {
		this.message = message;
	}
}