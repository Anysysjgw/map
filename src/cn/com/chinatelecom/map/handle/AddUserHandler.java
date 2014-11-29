/**
 * 
 */
package cn.com.chinatelecom.map.handle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;

import cn.com.chinatelecom.map.common.Config;
import cn.com.chinatelecom.map.entity.User;
import cn.com.chinatelecom.map.utils.DateUtils;

/**
 * @author swei019
 *
 */
public class AddUserHandler implements IHandler {

	@Override
	public Map<String, Object> handle(List<FileItem> items) {
		// TODO Auto-generated method stub
		Map<String, Object> result = new HashMap<String, Object>();
		User user = new User();
		String userName = "";
		String password = "";
		String role = "";
		String realName = "";
		String department = "";
		
		for (FileItem item : items) {
			if (item.isFormField()) {
				String fieldName = item.getFieldName();
				try {
					String string = item.getString(Config.getInstance().getValue("charset").toString());
					
					switch(fieldName) {
					case "username":
						userName = string;
						break;
					case "password":
						password = string;
						break;
					case "role":
						role = string;
						break;
					case "realname":
						realName = string;
						break;
					case "department":
						department = string;
						break;
					}
				} catch (java.io.UnsupportedEncodingException e) {}
			}
		}
		
		user.setUserName(userName);
		if(user.exist()) {
			result.put("statusCode", "300");
			result.put("message", "用户名 " + userName + " 已存在！");
			result.put("navTabId", "");
			result.put("rel", "");
			result.put("callbackType", "");
			result.put("forwardUrl", "");
		} else {
			user.setPassword(password);
			user.setRole(role);
			user.setRealName(realName);
			user.setDepartment(department);
			user.setCreateDate(DateUtils.getCurrentDate());
			if (user.insert()) {
				result.put("statusCode", "200");
				result.put("message", "用户添加成功！");
				result.put("navTabId", "");
				result.put("rel", "");
				result.put("callbackType", "");
				result.put("forwardUrl", "");
			} else {
				result.put("statusCode", "300");
				result.put("message", "用户添加失败，请重新操作！");
				result.put("navTabId", "");
				result.put("rel", "");
				result.put("callbackType", "");
				result.put("forwardUrl", "");
			}
		}
		
		return result;
	}

}
