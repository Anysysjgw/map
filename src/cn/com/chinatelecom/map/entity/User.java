package cn.com.chinatelecom.map.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import cn.com.chinatelecom.map.common.MongoDB;
import cn.com.chinatelecom.map.utils.StringUtils;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * @author Shelwin
 * 
 */
public class User {
	
	private String userName;
	private String password;
	private int role; /* 1 - System Administrator; 2 - Grid Data Administrator; 3 - Sales Data Administrator; 4 - Normal User */
	private String realName;
	private String department;
	private Date createDate;
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setRole(int role) {
		this.role = role;
	}
	
	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	public void setDepartment(String department) {
		this.department = department;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public int getRole() {
		return role;
	}
	
	public String getRealName() {
		return realName;
	}
	
	public String getDepartment() {
		return department;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	
	public User() {
		
	}
	
	public User(User user) {
		this.userName = user.userName;
		this.password = user.password;
		this.role = user.role;
		this.realName = user.realName;
		this.department = user.department;
		this.createDate = user.createDate;
	}

	public User(String json) {
		DBObject dbo = (DBObject) JSON.parse(json);
		setUser(dbo);
	}

	public User(DBObject dbo) {
		setUser(dbo);
	}
	
	private void setUser(DBObject dbo) {
		if (dbo.get("username") != null) {
			userName = dbo.get("username").toString();
		} else {
			return;
		}
		if (dbo.get("password") != null) {
			password = dbo.get("password").toString();
		}
		if (dbo.get("role") != null) {
			try {
				role = Integer.parseInt(dbo.get("role").toString());
			} catch (Exception e) {
				@SuppressWarnings("deprecation")
				String log = StringUtils.getLogPrefix(Level.SEVERE);
				System.out.println("\n" + log + "\n" + e.getClass()
						+ "\t:\t" + e.getMessage());
			}
		}
		if (dbo.get("realname") != null) {
			realName = dbo.get("realname").toString();
		}
		if (dbo.get("department") != null) {
			department = dbo.get("department").toString();
		}
		if (dbo.get("createdate") != null) {
			createDate = new Date((long) dbo.get("createdate"));
		}
	}
	
	@SuppressWarnings("deprecation")
	public boolean exist() {
		DBObject dbo = MongoDB.getInstance().findOne("user", toString());
		if (dbo == null) {
			return false;
		} else {
			return true;
		}
	}
	
	@SuppressWarnings("deprecation")
	public boolean insert() {
		return MongoDB.getInstance().insert("user", toString());
	}
	
	@SuppressWarnings("deprecation")
	public boolean delete() {
		return MongoDB.getInstance().delete("user", toString());
	}
	
	@SuppressWarnings("deprecation")
	public boolean update(String json) {
		return MongoDB.getInstance().update("user", toString(), json);
	}
	
	@SuppressWarnings("deprecation")
	public static User findOne(String json) {
		return new User(MongoDB.getInstance().findOne("user", json));
	}
	
	@SuppressWarnings("deprecation")
	public static List<User> findList(String json) {
		List<User> ul = new ArrayList<User>();
		List<DBObject> dbl = MongoDB.getInstance().findList("user", json);
		for (DBObject dbo : dbl) {
			ul.add(new User(dbo));
		}
		
		return ul;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		if (userName != null) {
			sb.append("'username':'" + userName + "',");
		}
		if (password != null) {
			sb.append("'password':'" + password + "',");
		}
		if (role > 0) {
			sb.append("'role':" + role + ",");
		}
		if (realName != null) {
			sb.append("'realname':'" + realName + "',");
		}
		if (department != null) {
			sb.append("'department':'" + department + "',");
		}
		if (createDate != null) {
			sb.append("'createdate':" + createDate.getTime());
		}
		if (sb.charAt(sb.length() - 1) == ',') {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("}");
		return sb.toString();
	}

}
