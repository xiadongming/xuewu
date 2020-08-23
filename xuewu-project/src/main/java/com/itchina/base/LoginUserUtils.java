package com.itchina.base;

import com.itchina.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.regex.Pattern;

/***
 *  @auther xiadongming
 *  @date 2020/8/9
 **/
public class LoginUserUtils {
    private static final String PHONE_REGEX = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    public static User getLoginUserInfo(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(null != principal && principal instanceof User){
            return (User) principal;
        }
        return null;
    }
    public static boolean checkTelephone(String target) {
        return PHONE_PATTERN.matcher(target).matches();
    }

}
