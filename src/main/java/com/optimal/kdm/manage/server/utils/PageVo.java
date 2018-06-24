package com.optimal.kdm.manage.server.utils;

import java.util.List;
import javax.validation.constraints.Max;
import lombok.Data;

@Data
public class PageVo {
	
	public PageVo(){
		
	}
	

	public PageVo(Integer pageSize, Integer pageNo){
		this.pageSize = pageSize;
		this.pageNo = pageNo;
	}
	
	private List items;

	private Long totalCount;// 总记录数

	private Integer totalPageCount;// 总页数

	// 每页显示数量
	@Max(value = 200, message = "每页显示最大不能查过200")
	private Integer pageSize = 10;// 每页记录个数

	private Integer pageNo = 1;// 当前页数
	
	//查询索引位置
	private Integer fromIndex = -1;
	
	private String sortname;
	
	private String order;


}
