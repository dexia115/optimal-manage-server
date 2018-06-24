package com.optimal.kdm.manage.server.utils;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

public class AuthorityUtil {
	Logger logger=LoggerFactory.getLogger(AuthorityUtil.class);
	
	public static String getLoginUsername(){
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	/**
	 * 判断当前用户是否拥有某个权限
	 * @param authority
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Boolean hasAuthority(String authority,String authority2){
		Boolean result = false;
		List<GrantedAuthority> auths = (List<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		for(GrantedAuthority auth : auths){
			if(authority.equals(auth.getAuthority()) || authority2.equals(auth.getAuthority())){
				result = true;
				break;
			}
		}
		return result;
	}
	
	/**
	 * 检查请求参数是否满足校验规则
	 * @param result
	 * @return
	 */
	public static String checkParams(BindingResult result) {
		String checkResult = "";
		if (result.hasErrors()) {
			StringBuffer errorMsg = new StringBuffer();
			List<ObjectError> errors = result.getAllErrors();
			for(ObjectError error : errors){
				errorMsg.append(error.getDefaultMessage()+"<br/>");
			}
			checkResult = errorMsg.toString();
		}
		return checkResult;
	}
	
}
