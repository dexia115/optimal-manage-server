package com.optimal.kdm.manage.server.controller.account;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.optimal.kdm.common.utils.CommonUtil;
import com.optimal.kdm.common.utils.Groups;
import com.optimal.kdm.common.utils.PageObj;
import com.optimal.kdm.common.utils.ResultResponse;
import com.optimal.kdm.manage.server.utils.AuthorityUtil;
import com.optimal.kdm.manage.server.utils.TableVo;
import com.optimal.kdm.module.manage.api.vo.account.RoleVo;
import com.optimal.kdm.module.manage.biz.entity.account.Role;
import com.optimal.kdm.module.manage.biz.service.AccountService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("role")
@Controller
public class RoleController {
	
	@Autowired
	private AccountService accountService;
	
	
	@GetMapping("/")
	@PreAuthorize("hasAnyAuthority('ACCOUNT_ROLE')")
	public String index() {
		
		return "account/role";
	}
	
	@RequestMapping(value = "loadRole")
	@ResponseBody
	public TableVo loadRole(@Valid TableVo tableVo, BindingResult result, HttpServletRequest request){
		if (result.hasErrors()) {
			tableVo.setAaData(new ArrayList<>());
			tableVo.setiTotalDisplayRecords(0);
			tableVo.setiTotalRecords(0);
			return tableVo;
		}
		int pageSize = tableVo.getiDisplayLength();
		int index = tableVo.getiDisplayStart();
		int currentPage = index / pageSize + 1;
		String params = tableVo.getsSearch();
		int col = tableVo.getiSortCol_0();
		String dir = tableVo.getsSortDir_0();
		String colname = request.getParameter("mDataProp_" + col);
		Groups groups = CommonUtil.filterGroup(params);
		groups.setOrderby(colname);
		groups.setOrder(dir);
		PageObj<Role> page = new PageObj<Role>(pageSize,currentPage);
		accountService.findRolePageByGroups(groups, page);
		long total = page.getTotalCount();
		tableVo.setAaData(page.getItems());
		tableVo.setiTotalDisplayRecords(total);
		tableVo.setiTotalRecords(total);
		return tableVo;
	}
	
	@GetMapping("/add")
	@PreAuthorize("hasAnyAuthority('ACCOUNT_ROLE_ADD')")
	public String addForm() {
		return "account/role_add";
	}
	
	@PostMapping("/add")
	@PreAuthorize("hasAnyAuthority('ACCOUNT_ROLE_ADD')")
	@ResponseBody
	public ResultResponse addSubmit(@Valid RoleVo roleVo, BindingResult result){
		String errorMsg = AuthorityUtil.checkParams(result);
		if(CommonUtil.isNull(errorMsg)) {
			return ResultResponse.fail(errorMsg);
		}
		try {
			accountService.saveRole(roleVo);
		} catch (Exception e) {
			log.error("添加角色失败",e);
			return ResultResponse.fail("保存失败："+e.getMessage());
		}
		return ResultResponse.ok("保存成功");
	}
	
	
	@GetMapping("/edit")
	@PreAuthorize("hasAnyAuthority('ACCOUNT_ROLE_EDIT')")
	public String editForm(Long id,Model model) {
		RoleVo roleVo = accountService.findRole(id);
		model.addAttribute("roleVo", roleVo);
		
		return "account/role_edit";
	}
	
	@PostMapping("/edit")
	@PreAuthorize("hasAnyAuthority('ACCOUNT_ROLE_EDIT')")
	@ResponseBody
	public ResultResponse editSubmit(@Valid RoleVo roleVo, BindingResult result){
		String errorMsg = AuthorityUtil.checkParams(result);
		if(CommonUtil.isNull(errorMsg)) {
			return ResultResponse.fail(errorMsg);
		}
		
		try {
			accountService.updateRole(roleVo);
		} catch (Exception e) {
			log.error("编辑角色失败",e);
			return ResultResponse.ok("保存失败："+e.getMessage());
		}
		
		return ResultResponse.ok("保存成功");
	}

}
