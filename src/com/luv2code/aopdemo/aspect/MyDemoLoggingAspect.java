package com.luv2code.aopdemo.aspect;

import java.util.List;
import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.luv2code.aopdemo.Account;

@Component
@Aspect
@Order(1)
public class MyDemoLoggingAspect {
	private static Logger logger = Logger.getLogger(MyDemoLoggingAspect.class.getName());
	@Around("execution(* com.luv2code.aopdemo.service.*.getFortune(..))")
	public Object aroundGetFortune(ProceedingJoinPoint theProceedingJoinPoint) throws Throwable {
		// print out the method we are advising on
		String method = theProceedingJoinPoint.getSignature().toShortString();
		logger.info("\n====>>>Executing @Around on method: " + method);
		// get begin TimeStamp
		long begin = System.currentTimeMillis();
		// now let's execute the method
		Object result = theProceedingJoinPoint.proceed();
		// now end TimeStamp
		long end = System.currentTimeMillis();
		// compute duration and display it
		long duration = end - begin;
		logger.info("\n=======>>>>>Duration: " + duration/1000.00 + " seconds");
		return result;
		
	}
	@After("execution(* com.luv2code.aopdemo.dao.AccountDAO.findAccounts(..))")
	public void afterFinallyFindAccountAdvice(JoinPoint theJoinPoint) {
		// print out which method we are advising on
		String method = theJoinPoint.getSignature().toShortString();
		logger.info("\n==============>Executing @After (finally) on method: " + method);
	}
	@AfterThrowing(pointcut = "execution(* com.luv2code.aopdemo.dao.AccountDAO.findAccounts(..))", throwing = "theExc")
	public void afterThrowingFindAccountAdvice(JoinPoint theJoinPoint, Throwable theExc) {
		// print out which method we are advising on
			String method = theJoinPoint.getSignature().toShortString();
			logger.info("\n=====>>Executing @AfterThrowing on method:  " + method);
		// log the exception here
			logger.info("\n=====>>The exception is: " + theExc);
	}

	@AfterReturning(pointcut = "execution(* com.luv2code.aopdemo.dao.AccountDAO.findAccounts(..))", returning = "result")
	public void afterReturningFindAccountsAdvice(JoinPoint theJoinPoint, List<Account> result) {
		// print out which method we are advising on
		String method = theJoinPoint.getSignature().toShortString();
		logger.info("\n==============>Executing After Returning on method: " + method);

		// print out the results of the method call
		logger.info("\n==============>The Result is: " + result);

		// let's post process the data, let's modify it :--
		// covert the account names to uppercase
		convertNamesToUpperCase(result);
		logger.info("\n==============>The Result is: " + result);
	}

	private void convertNamesToUpperCase(List<Account> result) {
		// Loop through the result list
		for (Account account : result) {

			// get the upper case version of the result
			String upperName = account.getName().toUpperCase();
			account.setName(upperName);
		}

	}

	// If you don't want to catch the return value of method
	/*
	 * @AfterReturning("execution(* com.luv2code.aopdemo.dao.AccountDAO.findAccounts(..))"
	 * ) public void afterReturningFindAccountsAdviceNotCatchReturnValue(JoinPoint
	 * theJoinPoint) { // print out which method we are advising on String method =
	 * theJoinPoint.getSignature().toShortString(); System.out.
	 * println("\n==============>Executing After Returning on method NotCatchReturnValue: "
	 * + method); }
	 */
	@Before("com.luv2code.aopdemo.aspect.AopExpressions.forDAOPackageNoGeterSetter()") // give fully qualified name if
																						// you want to access pointcut
																						// present in different class
	public void beforeAddAccountAdvice(JoinPoint theJoinPoint) {
		logger.info("\n==========>>> Executing @Before Advice on addAccount()");

		// display the method signature
		MethodSignature methodsig = (MethodSignature) theJoinPoint.getSignature();
		logger.info("Method: " + methodsig);
		// display the method arguments
		// get the arguments
		for (Object arg : theJoinPoint.getArgs()) {
			logger.info("Argument is: " + arg);

			if (arg instanceof Account) {

				// downcast and print the actual stuff

				Account account = (Account) arg;

				logger.info("Account Name is: " + account.getName());
				logger.info("Account Level is: " + account.getLevel());
			}
		}

	}

}
