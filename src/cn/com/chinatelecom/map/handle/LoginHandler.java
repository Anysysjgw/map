package cn.com.chinatelecom.map.handle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;

import cn.com.chinatelecom.map.common.Config;
import cn.com.chinatelecom.map.entity.User;

public class LoginHandler implements IHandler {
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.com.chinatelecom.map.handle.IHandler#handle(java.util.List)
	 */
	@Override
	public Map<String, Object> handle(List<FileItem> items) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		User user = new User();
		
		for (FileItem item : items) {
			if (item.isFormField()) {
				String fieldName = item.getFieldName();
				try {
					String string = item.getString(Config.getInstance().getValue("charset").toString());
				
					switch(fieldName) {
					case "username":
						user.setUserName(string);
						break;
					case "password":
						user.setPassword(string);
						break;
//					case "role":
//						user.setRole(Integer.parseInt(string));
//						break;
					}
				} catch (java.io.UnsupportedEncodingException e) {
					logger.error("获取用户登录数据失败：" + e.getMessage());
				}
			}
		}
		
		if (user.getUserName() == null || user.getPassword() == null || user.getUserName().equals("") || user.getPassword().equals("")) {
			result.put("loginstate", "F");
		} else {
			if(user.exist()) {
				User userTmp = User.findOne(user.toString());
				result.put("loginstate", "S");
				result.put("username", userTmp.getUserName());
				result.put("role", Integer.toString(userTmp.getRole()));
			} else {
				result.put("loginstate", "F");
			}
		}
		
		return result;
		
	}
	
}
