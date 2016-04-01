package com.cxqm.xiaoerke.modules.account.exception;

/**
 * 余额不足异常
 * @author frank
 *
 */
public class BalanceNotEnoughException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BalanceNotEnoughException() {
	}

	public BalanceNotEnoughException(String message) {
		super(message);
	}
}
