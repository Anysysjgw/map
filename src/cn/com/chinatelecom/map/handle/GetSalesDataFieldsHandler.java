/**
 * 
 */
package cn.com.chinatelecom.map.handle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;

import cn.com.chinatelecom.map.common.Config;
import cn.com.chinatelecom.map.entity.Data;

/**
 * @author Shelwin
 *
 */
public class GetSalesDataFieldsHandler implements IHandler {

	/* (non-Javadoc)
	 * @see cn.com.chinatelecom.map.handle.IHandler#handle(java.util.List)
	 */
	@Override
	public Map<String, Object> handle(List<FileItem> items) {
		// TODO Auto-generated method stub
		Map<String, Object> result = new HashMap<String, Object>();
		int status = 0;
		
		for (FileItem item : items) {
			if (item.isFormField()) {
				String fieldName = item.getFieldName();
				try {
					String string = item.getString(Config.getInstance().getValue("charset").toString());
					
					switch(fieldName) {
					case "status":
						try {
							status = Integer.parseInt(string);
						} catch (Exception e) {
							logger.error("获取营销字段数据失败：" + e.getMessage());
						}
						break;
					}
				} catch (java.io.UnsupportedEncodingException e) {
					logger.error("获取营销字段数据失败：" + e.getMessage());
				}
			}
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		String[] namesOfMemVar = Data.getNameOfMemberVariables();
		for (int i = 0; i < namesOfMemVar.length; i++) {
			if (namesOfMemVar[i].equalsIgnoreCase("calculatedDate") || namesOfMemVar[i].equalsIgnoreCase("gridCode") || namesOfMemVar[i].equalsIgnoreCase("uploadDate")) {
				continue;
			}
			if (status > 0) { //Field is in use
				if (Data.fieldIsOnUse(namesOfMemVar[i])) {
					sb.append("{\"field\":\"" + namesOfMemVar[i] + "\",");
					sb.append("\"description\":\"" + Data.getFieldDesc(namesOfMemVar[i]) + "\",");
					sb.append("\"onlyDay\":" + Data.getOnlyDay(namesOfMemVar[i]) + ",");
					sb.append("\"jueduizhiThreshold\":\"" + Data.getJueduizhiThreshold(namesOfMemVar[i]) + "\",");
					sb.append("\"huanbiThreshold\":\"" + Data.getHuanbiThreshold(namesOfMemVar[i]) + "\",");
					sb.append("\"tongbiThreshold\":\"" + Data.getTongbiThreshold(namesOfMemVar[i]) + "\",");
					sb.append("\"status\":" + Data.getStatus(namesOfMemVar[i]) + ",");
					sb.append("\"category\":" + Data.getCategory(namesOfMemVar[i]) + "},");
				}
			} else if (status < 0) { //Field isn't in use
				if (!Data.fieldIsOnUse(namesOfMemVar[i])) {
					sb.append("{\"field\":\"" + namesOfMemVar[i] + "\",");
					sb.append("\"description\":\"" + Data.getFieldDesc(namesOfMemVar[i]) + "\",");
					sb.append("\"onlyDay\":" + Data.getOnlyDay(namesOfMemVar[i]) + ",");
					sb.append("\"jueduizhiThreshold\":\"" + Data.getJueduizhiThreshold(namesOfMemVar[i]) + "\",");
					sb.append("\"huanbiThreshold\":\"" + Data.getHuanbiThreshold(namesOfMemVar[i]) + "\",");
					sb.append("\"tongbiThreshold\":\"" + Data.getTongbiThreshold(namesOfMemVar[i]) + "\",");
					sb.append("\"status\":" + Data.getStatus(namesOfMemVar[i]) + ",");
					sb.append("\"category\":" + Data.getCategory(namesOfMemVar[i]) + "},");
				}
			} else { // Both in use and not in use
				sb.append("{\"field\":\"" + namesOfMemVar[i] + "\",");
				sb.append("\"description\":\"" + Data.getFieldDesc(namesOfMemVar[i]) + "\",");
				sb.append("\"onlyDay\":" + Data.getOnlyDay(namesOfMemVar[i]) + ",");
				sb.append("\"jueduizhiThreshold\":\"" + Data.getJueduizhiThreshold(namesOfMemVar[i]) + "\",");
				sb.append("\"huanbiThreshold\":\"" + Data.getHuanbiThreshold(namesOfMemVar[i]) + "\",");
				sb.append("\"tongbiThreshold\":\"" + Data.getTongbiThreshold(namesOfMemVar[i]) + "\",");
				sb.append("\"status\":" + Data.getStatus(namesOfMemVar[i]) + ",");
				sb.append("\"category\":" + Data.getCategory(namesOfMemVar[i]) + "},");
			}
			
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		result.put("SalesDataFields", sb.toString());
		
		return result;
	}

}
