package com.example.helloword.contactList;

import android.graphics.drawable.Drawable;

public class SortModel {

	private String name;   //显示的数据
	private String sortLetters;  //显示数据拼音的首字母
	private Drawable image;

	public SortModel(){

	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
	public Drawable getImage() {
		return image;
	}
	public void setImage(Drawable image) {
		this.image = image;
	}
}
