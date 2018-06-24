package com.optimal.kdm.manage.server.controller.account;

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
import com.optimal.kdm.module.manage.api.vo.account.UserVo;
import com.optimal.kdm.module.manage.biz.entity.account.User;
import com.optimal.kdm.module.manage.biz.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequestMapping("user")
@Controller
public class UserController {
	
	@Autowired
	private AccountService accountService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@RequestMapping("/")
	@PreAuthorize("hasAnyAuthority('ACCOUNT_USER')")
	public String index(Model model) {
		Groups groups = new Groups();
		List<RoleVo> roleList = accountService.findRoleByGroups(groups);
		model.addAttribute("roleList", roleList);
		return "account/user";
	}

    @RequestMapping("loadUser")
    @ResponseBody
    public TableVo loadUser(@Valid TableVo tableVo, BindingResult result, HttpServletRequest request) {
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
		if (CommonUtil.isNull(colname)) {
			colname = "id";
			dir = "desc";
		}
        
        groups.setOrderby(colname);
        groups.setOrder(dir);
        PageObj<User> page = new PageObj<User>(pageSize,currentPage);
        accountService.findUserPageByGroups(groups, page);
        Long total = page.getTotalCount();
        tableVo.setAaData(page.getItems());
        tableVo.setiTotalDisplayRecords(total);
        tableVo.setiTotalRecords(total);
        return tableVo;
    }
    
    @RequestMapping(value = "checkMobile")
	@ResponseBody
	public Boolean checkMobile(Long id, String mobile)
	{
		Boolean valid = true;
		UserVo user = accountService.findUserByField("mobile", mobile);
		if (user != null)
		{
			if (user.getId().equals(id)) {//修改时
				valid = true;
			}else{
				valid = false;
			}
		}

		return valid;
	}
    
    
    
    
	@GetMapping("/add")
	@PreAuthorize("hasAnyAuthority('ACCOUNT_USER_ADD')")
	public String addForm(Model model) {
		Groups groups = new Groups();
		groups.Add("enable", true);
		List<RoleVo> roleList = accountService.findRoleByGroups(groups);
		model.addAttribute("roleList", roleList);

		return "account/user_add";
	}

	@PostMapping("/add")
	@PreAuthorize("hasAnyAuthority('ACCOUNT_USER_ADD')")
	@ResponseBody
	public ResultResponse addSubmit(@Valid UserVo userVo, BindingResult result, Model model) {
		String errorMsg = AuthorityUtil.checkParams(result);
		if(CommonUtil.isNull(errorMsg)) {
			return ResultResponse.fail(errorMsg);
		}
		try {
			String newPassword = passwordEncoder.encode(userVo.getPassword());
			userVo.setPassword(newPassword);
			userVo.setId(null);
			accountService.saveUser(userVo);
		} catch (Exception e) {
			log.error("新增用户失败",e);
			return ResultResponse.fail("保存失败："+e.getMessage());
		}
		return ResultResponse.ok("保存成功");
	}

	
	@GetMapping("/edit")
	@PreAuthorize("hasAnyAuthority('ACCOUNT_USER_EDIT')")
	public String editForm(Long id,Model model) {
		UserVo userVo = accountService.findUser(id);
		Groups groups = new Groups();
		groups.Add("enable",true);
		List<RoleVo> roles = accountService.findRoleByGroups(groups);
		model.addAttribute("allRoles", roles);
		model.addAttribute("userVo", userVo);
		
		return "account/user_edit";
	}
	
	@PostMapping("/edit")
	@PreAuthorize("hasAnyAuthority('ACCOUNT_USER_EDIT')")
	@ResponseBody
	public ResultResponse editSubmit(@Valid UserVo userVo, BindingResult result,Model model) {
		String errorMsg = AuthorityUtil.checkParams(result);
		if(CommonUtil.isNull(errorMsg)) {
			return ResultResponse.fail(errorMsg);
		}
		try {
			accountService.updateUser(userVo);
		} catch (Exception e) {
			log.error("修改用户失败",e);
			return ResultResponse.fail("保存失败："+e.getMessage());
		}
		return ResultResponse.ok("保存成功");
	}
	
	@RequestMapping(value = "updatePassword")
	@ResponseBody
	public ResultResponse updatePassword(@Valid UserVo userVo, BindingResult result){
		String errorMsg = AuthorityUtil.checkParams(result);
		if(CommonUtil.isNull(errorMsg)) {
			return ResultResponse.fail(errorMsg);
		}
		
		try {
			Long userId = userVo.getId();
			if(userId == null){
				userVo = accountService.findUserByField("login", AuthorityUtil.getLoginUsername());
				userId = userVo.getId();
			}
			String newPassword = passwordEncoder.encode(userVo.getPassword());
			
			accountService.updatePassword(userId, newPassword);
			log.info("修改用户ID={}的密码",userId);
			return ResultResponse.ok("修改成功");
		} catch (Exception e) {
			log.error("修改用户密码失败",e);
			return ResultResponse.fail("修改失败");
		}
	}
	
}