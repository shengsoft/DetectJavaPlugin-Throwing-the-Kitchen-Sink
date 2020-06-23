package tutorial691online.patterns;
import java.util.HashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaModelException;

import tutorial691online.handlers.SampleHandler;
import tutorial691online.visitors.CatchClauseVisitor;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

public class ExceptionFinder {
	HashMap<MethodDeclaration, String> suspectMethods = new HashMap<>();
	
	public void findExceptions(IProject project) throws JavaModelException {
		IPackageFragment[] packages = JavaCore.create(project).getPackageFragments();
		for(IPackageFragment mypackage : packages){

			findTargetCatchClauses(mypackage);
		}
	}
	
	private void findTargetCatchClauses(IPackageFragment packageFragment) throws JavaModelException {
		int eol = -1;
		int n_throw = -1;
		String output = null;
		String ss = null;
		for (ICompilationUnit unit : packageFragment.getCompilationUnits()) {
			CompilationUnit parsedCompilationUnit = parse(unit);
			
			String sss = String.format("%s", parsedCompilationUnit);
			ss = sss;
			do {
				eol = ss.indexOf("\n");
				output = ss.substring(0, eol);
				ss = ss.substring(eol + 1);
				
				n_throw = output.indexOf(") throws");
				if (n_throw > 0 && output.substring(n_throw + 1).indexOf(",") > 0) {
					SampleHandler.printMessage("The following method suffers from the Throwing the Kitchen Sink pattern");
					SampleHandler.printMessage(" ");
					SampleHandler.printMessage(output);
				}
			} while (eol >= 0 && ss.isEmpty() == false);
			CatchClauseVisitor exceptionVisitor = new CatchClauseVisitor();
			parsedCompilationUnit.accept(exceptionVisitor);
		}
	} 
	
	public HashMap<MethodDeclaration, String> getSuspectMethods() {
		return suspectMethods;
	}
	
	public void printExceptions() {
		for(MethodDeclaration declaration : suspectMethods.keySet()) {
			String type = suspectMethods.get(declaration);
			
			SampleHandler.printMessage(String.format("The following method suffers from the %s pattern", type));
			SampleHandler.printMessage(declaration.toString());
		}
	}
	
	public static CompilationUnit parse(ICompilationUnit unit) {
		@SuppressWarnings("deprecation")
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);
		return (CompilationUnit) parser.createAST(null); // parse
	}
}
