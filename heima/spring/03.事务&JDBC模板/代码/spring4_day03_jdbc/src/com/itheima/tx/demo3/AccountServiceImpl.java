package com.itheima.tx.demo3;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 转账的业务层的实现类
 * @author jt
 *
 */
@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED)
public class AccountServiceImpl implements AccountService {

	// 注入DAO:
	private AccountDao accountDao;
	
	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}
	
	@Override
	/**
	 * from：转出账号
	 * to：转入账号
	 * money：转账金额
	 */
	public void transfer( String from,  String to,  Double money) {
		
			accountDao.outMoney(from, money);
			int d = 1/0;
			accountDao.inMoney(to, money);
		
	}

}
