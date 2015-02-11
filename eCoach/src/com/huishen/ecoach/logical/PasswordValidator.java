package com.huishen.ecoach.logical;

import android.content.Context;

/**
 * 提供应用的密码强度验证。
 * @author Muyangmin
 * @create 2015-2-11
 */
public final class PasswordValidator {
	/**
	 * 验证一个字符串是否可以作为密码。
	 */
	public static final ValidateResult isValidPassword(Context context, String pwd){
		return ValidateResult.createValidResult();
	}
	/**
	 * 存放验证结果的实体类。
	 * @author Muyangmin
	 * @create 2015-2-11
	 */
	public static final class ValidateResult{
		/**
		 * 验证结果。
		 */
		public final boolean ISVALID;
		/**
		 * 错误提示信息。该字段仅在{@link #ISVALID}字段为<code>false</code>时才有意义。
		 */
		public final String ERROR_MSG;
		/**
		 * 错误提示代码。该字段仅在{@link #ISVALID}字段为<code>false</code>时才有意义。
		 */
		public final int ERROR_CODE;
		
		/**
		 * 创建表示验证通过的Result实体。
		 */
		protected static final ValidateResult createValidResult(){
			return new ValidateResult(true, null, 0);
		}
		
		/**
		 * 创建表示验证未通过的Result实体。
		 * @param emsg 错误信息
		 * @param ecode 错误代码
		 */
		protected static final ValidateResult createInvalidResult(String emsg, int ecode){
			return new ValidateResult(false, emsg, ecode);
		}
		
		protected ValidateResult(boolean result, String emsg, int ecode) {
			ISVALID = result;
			ERROR_MSG = emsg;
			ERROR_CODE = ecode;
		}
	}
}
