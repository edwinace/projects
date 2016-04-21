package seamshop.interceptor.transaction;

import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionInvocation;

@Component
@SuppressWarnings("serial")
public class WriteTransactionInterceptor extends TransactionInterceptor
{
	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception
	{
		return invokeWithinTransaction(TransactionType.WRITE);
	}
}