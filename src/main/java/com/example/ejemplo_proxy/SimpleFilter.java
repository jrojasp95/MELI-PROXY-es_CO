/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ejemplo_proxy;

import javax.servlet.http.HttpServletRequest;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.ZuulFilter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import org.springframework.http.HttpStatus;

/**
 *
 * @author Jair
 */
public class SimpleFilter extends ZuulFilter {
    
    @Autowired
    private RequestControlConditions controlConditions;

    private static Logger log = LoggerFactory.getLogger(SimpleFilter.class);
    
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        //return 1;
        return FilterConstants.PRE_DECORATION_FILTER_ORDER+1; 
    }

    @Override
    public boolean shouldFilter() {
        return RequestContext.getCurrentContext().getRequest().getRequestURI().startsWith("/meli");
    }

    @Value("${filtes.max_request_ip}")
    private long max_req_ip;
    
    
    @Value("${filtes.max_request_url_path}")
    private long max_req_url_path;
    
    @Value("${filtes.max_request_ip_path}")
    private long max_request_ip_path;
    
    
    
    
    @Override
    public Object run() {
        /**
         * 
         * 
         */
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
         StringBuffer strLog=new StringBuffer();
        //final long prueba = querycontroller.getNDocs();
       //
        //log.info(String.format("%s hola mundo request %s", request.getMethod(),request.getRequestURL().toString()));
        
       final String client_ip = request.getRemoteAddr();
       final String url_path = request.getRequestURI();
       final boolean control_ip = controlConditions.MaxRequestOneFieldFilter("client.ip", client_ip,max_req_ip);
       final boolean control_path = controlConditions.MaxRequestOneFieldFilter("url.path", url_path, max_req_url_path);
       final boolean control_ip_path = controlConditions.MaxRequestTwoFieldFilter("client.ip", client_ip, "url.path", url_path, max_request_ip_path);
       //final long control_ip_path = controlConditions.MaxRequestTwoFieldFilter("client.ip", client_ip, "url.path", url_path, max_request_ip_path);
       
       if (control_ip==false)
       {
           String msgError="Se ha superado la cantidad de request por ip"
                   + "";
	   strLog.append("\n"+msgError+"\n");	  
	   ctx.setResponseBody(msgError);
	   ctx.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
           ctx.setSendZuulResponse(false); 
	   log.info(strLog.toString());	    	    	
	   return null;
       }
       log.info(String.format("%s control_ip", control_ip));
       if(control_path==false)
       {
           String msgError="Se ha superado la cantidad de request por path de destino"
                   + "";
	   strLog.append("\n"+msgError+"\n");	  
	   ctx.setResponseBody(msgError);
	   ctx.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
           ctx.setSendZuulResponse(false); 
	   log.info(strLog.toString());	    	    	
	   return null;
       }
       log.info(String.format("%s control_path", control_path));
       if(control_ip_path==false)
       {
           String msgError="Se ha superado la cantidad de request por ip de origen y path de destino"
                   + "";
	   strLog.append("\n"+msgError+"\n");	  
	   ctx.setResponseBody(msgError);
	   ctx.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
           ctx.setSendZuulResponse(false); 
	   log.info(strLog.toString());	    	    	
	   return null;
       }
           
       //log.info(String.format("%s control_ip_path", control_ip_path));

        return null;
    }
}
