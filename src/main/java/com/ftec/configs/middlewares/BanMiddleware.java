package com.ftec.configs.middlewares;

import com.ftec.utils.FileUtils;
import com.ftec.utils.RequestsMapper;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class BanMiddleware implements HandlerInterceptor {
    private final String banFileName = "/BitBotFiles/bannedUsers/banned.txt";
    private Map<String, List<Date>> accesses = new HashMap<>();
    private static List<String> banned = null;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(banned==null) init();
        String ip = RequestsMapper.getClientIp(request);
        if(banned.contains(ip)){
            if(request.getMethod().toUpperCase().equals("GET")) {
                response.sendRedirect("/error/banned");
                return false;
            }
            response.sendError(423, "Your ip range was banned. Contact admin@coinbot.club for more info.");
            return false;
        }
        if(request.getMethod().toUpperCase().equals("GET")) return true;
        List<Date> access = accesses.get(ip);
        if(access==null){
            accesses.put(ip, new ArrayList<Date>(){{add(new Date());}});
        } else{
            access.add(new Date());
            if(access.size()==10){
                long difference = access.get(access.size()-1).getTime() - access.get(0).getTime();
                if(difference>=2*1000){
                    access.remove(0);
                    return true;
                } else{
                    banUser(ip);
                    accesses.remove(ip);
                    response.sendError(423, "Your ip range was banned. Contact admin@coinbot.club for more info.");
                    return false;
                }
            }
        }
        return true;
    }

    private void init(){
        banned = new ArrayList<>();
        banned.addAll(Arrays.asList(FileUtils.loadAsString(banFileName).split(",")));
    }

    private void banUser(String ip){
        System.out.println("Banning ip "+ip);
        banned.add(ip);
        FileUtils.appendToFile(banFileName, ip+",");
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}
