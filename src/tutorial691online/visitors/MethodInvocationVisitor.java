package tutorial691online.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class MethodInvocationVisitor extends ASTVisitor{
	
	private static String[] DefaultMethods = {"LOG", "throw", "log", "printStackTrace"}; // display statement
	private int logPrintDefaultStatements = 0;
	private String LogCatchSwitch;
	
	public MethodInvocationVisitor(String LogCatchSwitch) {
		this.LogCatchSwitch = LogCatchSwitch;
	}
	
	
	@Override
	public boolean visit(MethodInvocation node) {
		if(this.LogCatchSwitch == "LogCatchSwitch"){  // log statement inside catch
			String nodeName = node.getName().toString();
			if (IsDefaultStatement(nodeName))
				logPrintDefaultStatements += 1;
		}
		return super.visit(node);
	}
	
    /// To check whether an invocation is a default statement
	private static boolean IsDefaultStatement(String statement)
	{
        if (statement == null) return false;
        for (String defaultmethod : DefaultMethods)
        {
            if (statement.indexOf(defaultmethod) > -1)
            {
                return true;
            }
        }
        return false;
    }
	
	public int getLogPrintDefaultStatements() {
		
		return logPrintDefaultStatements;
	}
}
