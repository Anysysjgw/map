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

/**
 * @author swei019
 *
 */
public class DeleteUserHandler implements IHandler {

	/* (non-Javadoc)
	 * @see cn.com.chinatelecom.map.handle.IHandler#handle(java.util.List)
	 */
	@Override
	public Map<String, Object> handle(List<FileItem> items) {
		// TODO Auto-generated method stub
		Map<String, Object> result = new HashMap<String, Object>();
		User user = new User();
		String userName = "";
		
		for (FileItem item : items) {
			if (item.isFormField()) {
				String fieldName = item.getFieldName();
				try {
					String string = item.getString(Config.getInstance().getValue("charset").toString());
					
					switch(fieldName) {
					case "username":
						userName = string;
						break;
					}
				} catch (java.io.UnsupportedEncodingException e) {}
			}
		}
		
		user.setUserName(userName);
		if(user.exist()) {
			if (user.delete()) {
				result.put("statusCode", "200");
				result.put("message", "用户删除成功！");
				result.put("navTabId", "");
				result.put("rel", "");
				result.put("callbackType", "");
				result.put("forwardUrl", "");
			} else {
				result.put("statusCode", "300");
				result.put("message", "用户删除失败，请重新操作！");
				result.put("navTabId", "");
				result.put("rel", "");
				result.put("callbackType", "");
				result.put("forwardUrl", "");
			}
		} else {
			result.put("statusCode", "300");
			result.put("message", "用户名 " + userName + " 不存在！");
			result.put("navTabId", "");
			result.put("rel", "");
			result.put("callbackType", "");
			result.put("forwardUrl", "");
		}
		
		return result;
	}

}