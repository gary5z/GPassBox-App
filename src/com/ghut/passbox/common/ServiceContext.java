package com.ghut.passbox.common;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ghut.passbox.common.db.DBManager;
import com.ghut.passbox.model.Account;
import com.ghut.passbox.model.Category;
import com.ghut.passbox.model.CategoryCount;
import com.ghut.passbox.service.AccountService;
import com.ghut.passbox.service.CategoryService;
import com.ghut.passbox.service.SystemService;
import com.ghut.passbox.service.impl.AccountServiceImpl;
import com.ghut.passbox.service.impl.CategoryServiceImpl;
import com.ghut.passbox.service.impl.SystemServiceImpl;
import com.ghut.passbox.util.AppUtils;
import com.ghut.passbox.util.ClientHelper;
import com.ghut.passbox.util.DateTimeUtils;
import com.ghut.passbox.util.FileUtils;
import com.ghut.passbox.util.LogUtils;
import com.ghut.passbox.util.StringUtils;

/**
 * @author Gary
 * 
 */
public class ServiceContext {

	private static ServiceContext serviceContext;

	private SystemService systemService;
	private CategoryService categoryService;
	private AccountService accountService;

	private ServiceContext() {
		DBManager dbMgr = new DBManager(AppUtils.getDBHelper().getSQLiteDB());
		this.systemService = new SystemServiceImpl(dbMgr);
		this.categoryService = new CategoryServiceImpl(dbMgr);
		this.accountService = new AccountServiceImpl(dbMgr);
	}

	public static ServiceContext getInstance() {
		if (serviceContext == null) {
			serviceContext = new ServiceContext();
		}
		return serviceContext;
	}

	/**
	 * 还原数据文件
	 * 
	 * @param fileName
	 * @return
	 */
	public String restore(String fileName, String input) {
		String res = "ok";
		SQLiteDatabase restoreDB = null;

		try {
			if (!FileUtils.isFile(fileName)) {
				return "文件不存在，请确认！" + fileName;
			}

			restoreDB = SQLiteDatabase.openDatabase(fileName, null,
					SQLiteDatabase.OPEN_READONLY);
			DBManager dbMgr = new DBManager(restoreDB);
			SystemService systemSrv = new SystemServiceImpl(dbMgr);
			if (systemSrv.verifyPwd(input)) {
				File dataFile = AppUtils.getDatabaseFile();
				FileUtils.copy(new File(fileName), dataFile);
				AppUtils.getDBHelper().checkAndUpgrade();
				AppUtils.setEncryptKey(input);
			} else {
				res = "密码错误！";
			}
		} catch (Exception e) {
			LogUtils.error(ServiceContext.class, e);
			res = e.getMessage();
		} finally {
			if (restoreDB != null) {
				restoreDB.close();
			}
		}

		return res;
	}

	/**
	 * 备份数据文件
	 * 
	 * @param backupPath
	 * @param dataFile
	 * @return
	 */
	public String backup(String backupPath) {
		String res = "ok";

		try {
			File dataFile = AppUtils.getDatabaseFile();

			File targetFile = new File(backupPath, getBackupName(dataFile));

			FileUtils.copy(dataFile, targetFile);
			res += targetFile.getCanonicalPath();
		} catch (Exception e) {
			LogUtils.error(ServiceContext.class, e);
			res = e.getMessage();
		}

		return res;
	}

	private String getBackupName(File dataFile) {
		String fileName = dataFile.getName();
		if (StringUtils.isEmpty(fileName)) {
			fileName = Constants.DATABASE_NAME;
		}

		if (fileName.indexOf(".") != -1) {
			fileName = fileName.substring(0, fileName.indexOf("."));
		}

		StringBuilder sb = new StringBuilder();
		sb.append(fileName);
		sb.append("_");
		sb.append(DateTimeUtils.currentTimeString());
		sb.append(".bak");
		return sb.toString();
	}

	public String backupToEmail(Context context, String email) {
		String res = "ok";

		try {
			if (FileUtils.checkSDcard()) {
				File dataFile = AppUtils.getDatabaseFile();

				// 因为外部应用没有访问AccountBox的权限，所以这里先将数据文件复制到Cache文件夹
				// 临时文件可以在程序退出时清理
				File cacheDir = context.getExternalCacheDir();
				File targetFile = new File(cacheDir, getBackupName(dataFile));
				FileUtils.copy(dataFile, targetFile);

				String title = "Backup for AccountBox";
				ClientHelper.sendEmail(context, email, title, targetFile);
			} else {
				res = "SD卡不存在，系统不支持此功能！";
			}
		} catch (Exception e) {
			LogUtils.error(ServiceContext.class, e);
			res = e.getMessage();
		}

		return res;
	}

	public String bindEmail(String email) {
		String res = "";

		try {
			res = systemService.bindEmail(email);
		} catch (Exception e) {
			LogUtils.error(ServiceContext.class, e);
			res = e.getMessage();
		}

		return res;
	}

	public String getEmail() {
		String res = "";

		try {
			res = systemService.getEmail();
		} catch (Exception e) {
			LogUtils.error(ServiceContext.class, e);
			res = e.getMessage();
		}

		return res;
	}

	/**
	 * 是否已经注册了
	 * 
	 * @param pwd
	 * @return
	 */
	public boolean hasRegistered() {
		boolean result = false;
		try {
			result = systemService.hasRegistered();
		} catch (Exception e) {
			LogUtils.error(ServiceContext.class, e);
		}
		return result;
	}

	/**
	 * @param name
	 * @param pwd
	 * @return
	 */
	public String login(String pwd) {
		String result = "";
		try {
			result = systemService.login(pwd);

			if ("ok".equals(result)) {
				AppUtils.setEncryptKey(pwd);
			}

		} catch (Exception e) {
			LogUtils.error(ServiceContext.class, e);
			result = e.getMessage();
		}

		LogUtils.info(ServiceContext.class, result);
		return result;
	}

	public String modifyPwd(String oldpwd, String newpwd) {
		String res = "ok";

		try {
			res = systemService.modifyPwd(oldpwd, newpwd);

			if ("ok".equals(res)) {
				List<Account> accountList = accountService.getAll();
				AppUtils.setEncryptKey(newpwd);
				for (Account account : accountList) {
					account.setLoginName(account.getLoginName());
					account.setLoginPwd(account.getLoginPwd());
					accountService.modify(account);
				}
			}
		} catch (Exception e) {
			res = e.getMessage();
			LogUtils.error(ServiceContext.class, e);
		}

		LogUtils.info(ServiceContext.class, res);
		return res;
	}

	public String addAccount(Account account) {
		String res = "ok";
		try {
			res = accountService.addNew(account);
		} catch (Exception e) {
			res = e.getMessage();
			LogUtils.error(ServiceContext.class, e);
		}
		return res;
	}

	public String updateAccount(Account account) {
		String res = "ok";
		try {
			res = accountService.modify(account);
		} catch (Exception e) {
			e.printStackTrace();
			res = e.getMessage();
		}
		return res;
	}

	public String removeAccount(Account account) {
		String res = "ok";
		try {
			res = accountService.remove(account);
		} catch (Exception e) {
			e.printStackTrace();
			res = e.getMessage();
		}
		return res;
	}

	public String removeAccount(int acctId) {
		String res = "ok";
		try {
			res = accountService.remove(acctId);
		} catch (Exception e) {
			e.printStackTrace();
			res = e.getMessage();
		}
		return res;
	}

	public String removeAccount(Category category) {
		String res = "ok";
		try {
			res = accountService.remove(category);
		} catch (Exception e) {
			e.printStackTrace();
			res = e.getMessage();
		}
		return res;
	}

	public List<Account> getAllAccount() {
		List<Account> res = null;
		try {
			res = accountService.getAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public List<Account> getAccountByCategory(int catId) {
		List<Account> res = null;
		try {
			res = accountService.getListByCategory(catId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public List<Account> getAccountByCategory(Category category) {
		List<Account> res = null;
		try {
			res = accountService.getListByCategory(category);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public List<CategoryCount> getCategoryCounts() {
		List<CategoryCount> res = null;
		try {
			res = accountService.getCategoryCounts();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public String addCategory(Category category) {
		String res = "ok";
		try {
			res = categoryService.addNew(category);
		} catch (Exception e) {
			e.printStackTrace();
			res = e.getMessage();
		}
		return res;
	}

	public String updateCategory(Category category) {
		String res = "ok";
		try {
			res = categoryService.modify(category);
		} catch (Exception e) {
			e.printStackTrace();
			res = e.getMessage();
		}
		return res;
	}

	public String removeCategory(Category category) {
		String res = "ok";
		try {
			res = categoryService.remove(category);
		} catch (Exception e) {
			e.printStackTrace();
			res = e.getMessage();
		}
		return res;
	}

	public String removeCategory(int catId) {
		String res = "ok";
		try {
			res = categoryService.remove(catId);
		} catch (Exception e) {
			e.printStackTrace();
			res = e.getMessage();
		}
		return res;
	}

	public Category getCategory(int catId) {
		Category res = null;
		try {
			res = categoryService.get(catId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public List<Category> getAllCategory() {
		List<Category> res = null;
		try {
			res = categoryService.getAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

}
