package cn.crap.service.thirdly;

import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.crap.dto.thirdly.GitHubAccessToken;
import cn.crap.dto.thirdly.GitHubUser;
import cn.crap.framework.MyException;
import cn.crap.springbeans.Config;
import cn.crap.utils.HttpPostGet;
import cn.crap.utils.Tools;

@Service
public class GitHubService {
	@Autowired
	private Config config;
	
	   public GitHubAccessToken getAccessToken(String code,String redirect_uri) throws Exception{
	        String url ="https://github.com/login/oauth/access_token";
	        Map<String,Object> params = Tools.getStrMap("client_id",config.getClientID(),
	        		"client_secret",config.getClientSecret(),"code",code,"redirect_uri",redirect_uri);
	        
	        String rs = HttpPostGet.post(url, params, Tools.getStrMap("Accept","application/json"),null);
	        GitHubAccessToken accessToken = (GitHubAccessToken) JSONObject.toBean(JSONObject.fromObject(rs),GitHubAccessToken.class);
	        if(accessToken == null || accessToken.getAccess_token() == null)
	            throw new MyException("000026");
	        return accessToken;
	    }

	    public GitHubUser getUser(String accessToken) throws Exception{
	        String url = "https://api.github.com/user?access_token="+accessToken;
	        String rs = HttpPostGet.get(url, null, null);
	        if(rs.contains("message"))
	        	throw new MyException("000026",rs);
	        return (GitHubUser) JSONObject.toBean(JSONObject.fromObject(rs),GitHubUser.class);
	    }

}
