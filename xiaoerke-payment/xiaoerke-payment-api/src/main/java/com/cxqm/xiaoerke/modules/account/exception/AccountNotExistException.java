package com.cxqm.xiaoerke.modules.account.exception;

/**
 * 余额不足异常
 * @author frank
 *
 */
public class AccountNotExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccountNotExistException(String message) {
		super(message);
	}

}
