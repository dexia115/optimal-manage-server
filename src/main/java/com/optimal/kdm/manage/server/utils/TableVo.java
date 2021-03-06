package com.optimal.kdm.manage.server.utils;

import java.util.List;
import javax.validation.constraints.Max;

@SuppressWarnings("rawtypes")
public class TableVo {

	private long iTotalRecords;

	private long iTotalDisplayRecords;

	// 每页显示数量
	@Max(value = 200, message = "每页显示最大不能查过200")
	private int iDisplayLength = 10;
	
	private int pageNo = 1;

	// 分页时每页跨度数量
	private int iDisplayStart = 10;

	private List aaData;

	// DataTable请求服务器端次数
	private String sEcho;

	private String sSortDir_0;

	private int iSortCol_0;

	private String sColumns;

	private int iSortingCols;

	// 列数
	private int iColumns;

	// 搜索条件
	private String sSearch;

	private Long unreadNum;

	public long getiTotalRecords() {
		return iTotalRecords;
	}

	public void setiTotalRecords(long iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}

	public long getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	public void setiTotalDisplayRecords(long iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

	public int getiDisplayLength() {
		return iDisplayLength;
	}

	public void setiDisplayLength(int iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getiDisplayStart() {
		return iDisplayStart;
	}

	public void setiDisplayStart(int iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}

	public List getAaData() {
		return aaData;
	}

	public void setAaData(List aaData) {
		this.aaData = aaData;
	}

	public String getsEcho() {
		return sEcho;
	}

	public void setsEcho(String sEcho) {
		this.sEcho = sEcho;
	}

	public String getsSortDir_0() {
		return sSortDir_0;
	}

	public void setsSortDir_0(String sSortDir_0) {
		this.sSortDir_0 = sSortDir_0;
	}

	public int getiSortCol_0() {
		return iSortCol_0;
	}

	public void setiSortCol_0(int iSortCol_0) {
		this.iSortCol_0 = iSortCol_0;
	}

	public String getsColumns() {
		return sColumns;
	}

	public void setsColumns(String sColumns) {
		this.sColumns = sColumns;
	}

	public int getiSortingCols() {
		return iSortingCols;
	}

	public void setiSortingCols(int iSortingCols) {
		this.iSortingCols = iSortingCols;
	}

	public int getiColumns() {
		return iColumns;
	}

	public void setiColumns(int iColumns) {
		this.iColumns = iColumns;
	}

	public String getsSearch() {
		return sSearch;
	}

	public void setsSearch(String sSearch) {
		this.sSearch = sSearch;
	}

	public Long getUnreadNum() {
		return unreadNum;
	}

	public void setUnreadNum(Long unreadNum) {
		this.unreadNum = unreadNum;
	}


}
