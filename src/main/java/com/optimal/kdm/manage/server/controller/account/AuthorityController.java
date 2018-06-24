package com.optimal.kdm.manage.server.controller.account;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.optimal.kdm.common.utils.CommonUtil;
import com.optimal.kdm.common.utils.Constants;
import com.optimal.kdm.common.utils.Groups;
import com.optimal.kdm.common.utils.PageObj;
import com.optimal.kdm.common.utils.PropertyFilter.MatchType;
import com.optimal.kdm.common.utils.ResultResponse;
import com.optimal.kdm.common.utils.ZtreeVo;
import com.optimal.kdm.manage.server.utils.AuthorityUtil;
import com.optimal.kdm.manage.server.utils.PageVo;
import com.optimal.kdm.module.manage.api.vo.account.AuthorityVo;
import com.optimal.kdm.module.manage.biz.entity.account.Authority;
import com.optimal.kdm.module.manage.biz.service.AccountService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("authority")
@Controller
public class AuthorityController {
	
	@Autowired
	private AccountService accountService;
	
	@GetMapping("/")
	public String index() {
		
		return "account/authority";
	}
	
	
	@RequestMapping(value = "loadAuthority")
	@ResponseBody
	public PageVo loadAuthority(@Valid PageVo pageVo,Long id, String params){
		Groups groups = new Groups();
		PageObj<Authority> page = new PageObj<Authority>(pageVo.getPageSize(),pageVo.getPageNo());
		if(!CommonUtil.isNull(params)){
			groups = CommonUtil.filterGroup(params);
		}
		if(id != null){
			groups.Add("parent.id",id);
		}else{
			groups.Add("parent.id","",MatchType.NULL);
		}
//		groups.Add("method",Constants.AUTHORITY_MENU);
		if(!CommonUtil.isNull(pageVo.getSortname())){
			groups.setOrder(pageVo.getOrder());
			groups.setOrderby(pageVo.getSortname());
		}else{
			groups.setOrder("asc");
			groups.setOrderby("sort");
		}
		accountService.findAuthorityPageByGroups(groups, page);
		pageVo.setItems(page.getItems());
		pageVo.setTotalCount(page.getTotalCount());
		pageVo.setTotalPageCount(page.getTotalPageCount());
		
		return pageVo;
	}
	
	@RequestMapping(value = "findAuthorityForTree")
	@ResponseBody
	public List<ZtreeVo> findAuthorityForTree(Long id)
	{
		List<ZtreeVo> ztreeVos = new ArrayList<ZtreeVo>();
		Groups groups = new Groups();
		groups.Add("enable",true);
		if(id == null || id == 0)
		{
			groups.Add("parent","",MatchType.NULL);
		}
		else
		{
			groups.Add("parent.id", id);
		}
		groups.setOrder("asc");
		groups.setOrderby("sort");
		List<AuthorityVo> authorityVos = accountService.findAuthorityByGroups(groups);
		for(AuthorityVo authorityVo : authorityVos)
		{
			ZtreeVo ztree = new ZtreeVo();
			ztree.setId(authorityVo.getId());
			ztree.setName(authorityVo.getName());
			if(authorityVo.getParentId() == null){
				ztree.setParent(true);
			}else{
				ztree.setParent(false);
			}
			ztreeVos.add(ztree);
		}
		
		return ztreeVos;
	}
	
	
	@RequestMapping(value = "findAuthorityByTree")
	@ResponseBody
	public List<ZtreeVo> findAuthorityByTree(Long id)
	{
		List<ZtreeVo> ztreeVos = new ArrayList<ZtreeVo>();
		Groups groups = new Groups();
		groups.Add("enable",true);
		if(id == null || id == 0)
		{
			groups.Add("parent","",MatchType.NULL);
		}
		else
		{
			groups.Add("parent.id", id);
		}
		groups.setOrder("asc");
		groups.setOrderby("sort");
		List<AuthorityVo> authorityVos = accountService.findAuthorityByGroups(groups);
		for(AuthorityVo authorityVo : authorityVos)
		{
			ZtreeVo ztree = new ZtreeVo();
			ztree.setId(authorityVo.getId());
			ztree.setName(authorityVo.getName());
			if(authorityVo.getMethod() == Constants.AUTHORITY_MENU){
				ztree.setParent(true);
			}else{
				ztree.setParent(false);
			}
			ztreeVos.add(ztree);
		}
		
		return ztreeVos;
	}
	
	@RequestMapping(value = "findAuthorityAllTree")
	@ResponseBody
	public List<ZtreeVo> findAuthorityAllTree(Long id)
	{
		List<ZtreeVo> ztreeVos = new ArrayList<ZtreeVo>();
		Groups groups = new Groups();
		groups.Add("enable",true);
		if(id == null || id == 0)
		{
			groups.Add("parent","",MatchType.NULL);
		}
		else
		{
			groups.Add("parent.id", id);
		}
		groups.setOrder("asc");
		groups.setOrderby("sort");
		List<AuthorityVo> authorityVos = accountService.findAuthorityByGroups(groups);
		for(AuthorityVo authorityVo : authorityVos)
		{
			ZtreeVo ztree = new ZtreeVo();
			ztree.setId(authorityVo.getId());
			ztree.setName(authorityVo.getName());
			if(authorityVo.getMethod() == Constants.AUTHORITY_MENU){
				ztree.setParent(true);
			}else{
				ztree.setParent(false);
			}
			List<ZtreeVo> children = findAuthorityAllTree(ztree.getId());
			ztree.setChildren(children);
			ztreeVos.add(ztree);
		}
		
		return ztreeVos;
	}
	
	
	
	@PostMapping("add")
	@ResponseBody
	public ResultResponse addSubmit(@Valid AuthorityVo authorityVo, BindingResult result){
		String errorMsg = AuthorityUtil.checkParams(result);
		if(CommonUtil.isNull(errorMsg)) {
			return ResultResponse.fail(errorMsg);
		}
		
		try {
			authorityVo.setId(null);
			accountService.saveAuthority(authorityVo);
		} catch (Exception e) {
			log.error("添加权限失败",e);
			return ResultResponse.fail("保存失败："+e.getMessage());
		}
		return ResultResponse.ok("保存成功");
	}
	
	@PostMapping("edit")
	@ResponseBody
	public ResultResponse editSubmit(@Valid AuthorityVo authorityVo, BindingResult result){
		String errorMsg = AuthorityUtil.checkParams(result);
		if(CommonUtil.isNull(errorMsg)) {
			return ResultResponse.fail(errorMsg);
		}
		
		try {
			accountService.updateAuthority(authorityVo);
		} catch (Exception e) {
			log.error("修改权限失败",e);
			return ResultResponse.fail("保存失败："+e.getMessage());
		}
		return ResultResponse.ok("保存成功");
	}
	
	@PostMapping("delete")
	@ResponseBody
	public ResultResponse delete(Long id){
		try {
			accountService.deleteAuthority(id);
		} catch (Exception e) {
			log.error("删除权限失败",e);
			return ResultResponse.fail("删除失败："+e.getMessage());
		}
		return ResultResponse.ok("删除成功");
	}
	
	

}
