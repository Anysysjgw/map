package cn.com.chinatelecom.map.handle;

import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;

import cn.com.chinatelecom.map.entity.Grid;

/**
 * @author joseph
 *
 */
public class ReshapeHandler implements IHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.com.chinatelecom.map.handle.IHandler#handle(java.util.List)
	 */
	@Override
	public Map<String, Object> handle(List<FileItem> items) {
		if (items == null) {
			return null;
		}

		String code = null;
		String coordinates = null;
		for (FileItem item : items) {
			if (item.isFormField()) {
				String name = item.getFieldName();
				String string = item.getString();
				switch (name) {
				case "GRID_CODE":
					code = string;
					break;
				case "GRID_COORDINATES":
					coordinates = string;
					break;
				default:
					break;
				}
			}
		}

		Grid origin = new Grid("{GRID_CODE:'" + code + "'}");
		Grid target = Grid.findOne(origin.toString());
		StringBuffer sb = new StringBuffer("{GRID_CODE:'" + code + "'");
		if (target.getName() != null) {
			sb.append(",GRID_NAME:'" + target.getName() + "'");
		}
		if (target.getManager() != null) {
			sb.append(",GRID_MANAGER:'" + target.getManager() + "'");
		}
		if (target.getAddress() != null) {
			sb.append(",GRID_ADDRESS:'" + target.getAddress() + "'");
		}
		sb.append(",GRID_COORDINATES:" + coordinates + "}");
		origin.update(sb.toString());

		return null;
	}

}