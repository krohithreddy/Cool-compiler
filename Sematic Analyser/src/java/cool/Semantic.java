package cool;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Stack;

public class Semantic
{
	private boolean errorFlag = false;
	public void reportError(String filename, int lineNo, String error){
		errorFlag = true;
		System.err.println(filename+":"+lineNo+": "+error);
	}
	public boolean getErrorFlag(){
		return errorFlag;
	}

/*
	Don't change code above this line
*/
    ScopeTable<String> s;

	public final String BOOL = "Bool";
	public final String INT = "Int";
	public final String STRING = "String";
	public final String OBJECT = "Object";


	public Semantic(AST.program program)
	{
			//Write Semantic analyzer code here
	        // Inheritance
		try
	    {
	        int nodes=program.classes.size(),k=0;
	      	int y[]=new int[nodes + 1];
	        int adjmat[][]=new int[nodes + 1][nodes + 1];	// Checking whether main class is defined or not
			for (int i=1;i<=nodes;i++)
	        {
	        	if(program.classes.get(i-1).name.equals("Main")==true)
	        	{
					k=1;
					break;
	        	}

				if(k!=1 && i==nodes)
					reportError(program.classes.get(i-1).filename,program.classes.get(i-1).lineNo,"Class 'Main' is not defined in program.");
			}
			for (int i=1;i<=nodes;i++)		// checking wheather a class is defined too many times
	        {
	            for (int j=1;j<=nodes;j++)
	            {
	            	if(program.classes.get(i-1).name.equals(program.classes.get(j-1).name)==true && i!=j)
	                {
						reportError(program.classes.get(i-1).filename,program.classes.get(i-1).lineNo,"class is defined many times.");
	                	break;
	                }
	            }
	        }
	        for (int i=1;i<=nodes;i++)			// Checking whether parent class is defined or not
	        {
				if(program.classes.get(i-1).parent.equals("IO")==true)
					y[i]=2;
	        	else if(program.classes.get(i-1).parent.equals(OBJECT)==false)
	        	{
	                for (int j=1;j<=nodes;j++)
	                {
	                    if(program.classes.get(i-1).parent.equals(program.classes.get(j-1).name)==true)
	                        y[i]=1;
	                }
	            }
	            else if (program.classes.get(i-1).parent.equals(OBJECT)==true)
	            {
					y[i]=2;
	        	}
			}
			for (int i=1;i<=nodes;i++)
	        {
	            if(y[i]!=1 && y[i]!=2)
					reportError(program.classes.get(i-1).filename,program.classes.get(i-1).lineNo,"Parent class not defined.");
			}
	    	for (int i=1;i<=nodes;i++)				// Creating Adjacency Matrix
	        {
	        	for (int j=1;j<=nodes;j++)
	    	    {
					if(program.classes.get(i-1).parent.equals(program.classes.get(j-1).name)==true)
	                    adjmat[i][j]=1;
	                else
	                {
	                	adjmat[i][j]=0;
	                }
	            }
	        }
			CycleCheck CycleCheck = new CycleCheck();				// Checking whether it contains Circle
	        if(CycleCheck.dfs(adjmat, 1)!=0)
	     	{
	        	reportError(program.classes.get(CycleCheck.dfs(adjmat, 1)-1).filename,program.classes.get(CycleCheck.dfs(adjmat, 1)-1).lineNo,"Graph contains cycle.");
	        }

		}
		catch(InputMismatchException inputMismatch)
		{
			System.out.println("Wrong Input format");
		}
	    s = new ScopeTable<String>();		//Naming and Scoping
	    int i,j,k,l;
		s.enterScope();						//Entering scope
		for(j=0;j<program.classes.size();j++)
		{
			for(k=0;k<program.classes.get(j).features.size();k++)
		    {
			    if(program.classes.get(j).features.get(k)  instanceof AST.method)		//if feature is method
			    {		//calling expression_ function
				    expression_(program,((AST.method)(program.classes.get(j).features.get(k))).body,program.classes.get(j));
				    for(l=0;l<((AST.method)(program.classes.get(j).features.get(k))).formals.size();l++)
				        s.insert(((AST.method)(program.classes.get(j).features.get(k))).formals.get(l).name,((AST.method)(program.classes.get(j).features.get(k))).formals.get(l).typeid);
			            //inserting name and type
				}
		    	if(program.classes.get(j).features.get(k) instanceof AST.attr)			//if feature is attribute
		    	{
		    		s.insert(((AST.attr)(program.classes.get(j).features.get(k))).name,((AST.attr)(program.classes.get(j).features.get(k))).typeid);
					expression_(program,((AST.attr)(program.classes.get(j).features.get(k))).value,program.classes.get(j));
		    	}
	    	}
		}
		s.exitScope();			//exiting scope
	}

	public String parenttype(AST.program program,String T1 )	//finding the parent of a given type using the program list
	{
		for(int i=0;i<program.classes.size();i++)
			if(program.classes.get(i).name.equals(T1))
				return program.classes.get(i).parent;
	    return OBJECT;
	}

	public boolean implies(AST.program program,String T1,String T2)  //verifying wheather T1<=T2
	{
	    String T=T1;
		if(T1.equals(T2))
            return true;
        while(!(T.equals(T2)) && T!=OBJECT)
        {
			T=parenttype(program,T1);
            if(T.equals(T2))
                return true;
        }
        return false;
	}

	public String findType(String name)				//given a name find its type by looking up in symbol table
	{
		String localtype = s.lookUpLocal(name);
		if(localtype!=null)
			return localtype;
		else
		{
			String globaltype = s.lookUpGlobal(name);			//lookup in global
			if(globaltype!=null)
				return globaltype;
			else
				return null;
		}
	}

	public String joinTypes(AST.program program,String T1,String T2)			// Function for finding Type for T1 union T2
	{
	    if(T1.equals(T2))
	        return T1;
	    if(parenttype(program,T1).equals(T2))
	    return T2;
	    if(parenttype(program,T2).equals(T1))
	    return T1;
	    return joinTypes(program,parenttype(program,T1),parenttype(program,T2));
	}

	public AST.expression  expression_(AST.program program,AST.expression expr,AST.class_ cls)			//Returning expression with  type assigned
	{
	    int i;
	    if(expr instanceof AST.string_const)		// Declaring expression type such as string,bool,int,object
	        expr.type=STRING;
	    if(expr instanceof AST.bool_const)
	        expr.type=BOOL;
	    if(expr instanceof AST.int_const)
			expr.type=INT;
		if(expr instanceof AST.object)
		{
			expr.type =  findType(((AST.object)expr).name);
			if(expr.type==null)
			{
				expr.type = OBJECT;
				reportError(cls.filename, ((AST.object)expr).lineNo, "Variable not declared");
			}
		}
		if(expr instanceof AST.comp)
			expr.type = expression_( program,((AST.comp)expr).e1, cls).type;
		if(expr instanceof AST.neg)
		{
			AST.expression expr1 = expression_(program,((AST.neg)expr).e1, cls);
			if(expr1.type == INT)
				expr.type = INT;
			else
			{
				expr.type = OBJECT;
				reportError(cls.filename, expr.lineNo, "Negation must have int type");
			}
		}
		if(expr instanceof AST.isvoid)
	    	expr.type=BOOL;
		if(expr instanceof AST.assign)
		{
			AST.expression expr1 = expression_( program,((AST.assign)expr).e1, cls);
			expr.type = findType(((AST.assign)expr).name);
			if(expr.type==null)
			{
				expr.type = OBJECT;
				reportError(cls.filename, ((AST.assign)expr).lineNo, "Variable not declared");
			}

			if(implies(program, expr.type, expr1.type)==false)
			{
				reportError(cls.filename, expr.lineNo, "Type Mismatch");
			}
		}
		if(expr instanceof AST.eq)
		{
	    	expr.type = BOOL;
			if((expression_( program,((AST.eq)expr).e1, cls).type.equals(INT) || expression_( program,((AST.eq)expr).e1, cls).type.equals(BOOL) || expression_( program,((AST.eq)expr).e1, cls).type.equals(STRING) ||
	                expression_( program,((AST.eq)expr).e2, cls).type.equals(INT) || expression_( program,((AST.eq)expr).e2, cls).type.equals(BOOL) || expression_( program,((AST.eq)expr).e2, cls).type.equals(STRING) ) && !expression_( program,((AST.eq)expr).e1, cls).type.equals(expression_( program,((AST.eq)expr).e2, cls).type))
	        {
	                expr.type = OBJECT;
	                reportError(cls.filename, expr.lineNo, "The two operands are not of same type");
	        }
	    }
		if(expr instanceof AST.leq)
		{
	        if(expression_(program,((AST.leq)expr).e1, cls).type.equals(expression_( program,((AST.leq)expr).e2, cls).type) && expression_( program,((AST.leq)expr).e2, cls).type.equals(INT))
	            expr.type = BOOL;
	            else
	            {
					expr.type = OBJECT;
					reportError(cls.filename, expr.lineNo, "The two operands must be of type int");
	            }
	    }
		if(expr instanceof AST.lt)
		{
	        if(expression_(program,((AST.lt)expr).e1, cls).type.equals(expression_( program,((AST.lt)expr).e2, cls).type) && expression_( program,((AST.lt)expr).e2, cls).type.equals(INT))
	            expr.type = BOOL;
	        else
		    {
	            expr.type = OBJECT;
	            reportError(cls.filename, expr.lineNo, "The two operands must be of type int");
	        }
	    }
		if(expr instanceof AST.divide)
		{
	        if(expression_(program,((AST.divide)expr).e1, cls).type.equals(expression_( program,((AST.divide)expr).e2, cls).type) && expression_( program,((AST.divide)expr).e2, cls).type.equals(INT))
	            expr.type = INT;
	        else
	        {
	            expr.type = OBJECT;
	            reportError(cls.filename, expr.lineNo, "The two operands must be of type int");
	        }
	    }
		if(expr instanceof AST.mul)
		{
	        if(expression_(program,((AST.mul)expr).e1, cls).type.equals(expression_( program,((AST.mul)expr).e2, cls).type) && expression_( program,((AST.mul)expr).e2, cls).type.equals(INT))
	            expr.type = INT;
	        else
	        {
	            expr.type = OBJECT;
	            reportError(cls.filename, expr.lineNo, "The two operands must be of type int");
	        }
	    }
		if(expr instanceof AST.sub)
		{
	        if(expression_(program,((AST.sub)expr).e1, cls).type.equals(expression_( program,((AST.sub)expr).e2, cls).type) && expression_( program,((AST.sub)expr).e2, cls).type.equals(INT))
	            expr.type = INT;
	        else
	        {
	            expr.type = OBJECT;
	            reportError(cls.filename, expr.lineNo, "The two operands must be of type int");
	        }
	    }
		if(expr instanceof AST.plus)
		{
	        if(expression_(program,((AST.plus)expr).e1, cls).type.equals(expression_( program,((AST.plus)expr).e2, cls).type) && expression_( program,((AST.plus)expr).e2, cls).type.equals(INT))
	            expr.type = INT;
	        else
	        {
	            expr.type = OBJECT;
	            reportError(cls.filename, expr.lineNo, "The two operands must be of type int");
	        }
	    }
		if(expr instanceof AST.new_)
		{
	    	if(((AST.new_)expr).typeid.equals("SELF_TYPE"))
	        	expr.type=cls.name;
	        else
	        	expr.type= ((AST.new_)expr).typeid;
		}
		if(expr instanceof AST.block)
		{
			int numexprs = ((AST.block)expr).l1.size();
			for(int j=0; j<numexprs; j++)
			{
				AST.expression expr1 = expression_(program,((AST.block)expr).l1.get(j),  cls);
				if(j==numexprs-1) expr.type = expr1.type;
			}
		}
		if(expr instanceof AST.loop)
		{
	        expr.type = OBJECT;
	        if(!expression_(program,((AST.loop)expr).predicate,  cls).type.equals(BOOL))
			{
	            reportError(cls.filename, expr.lineNo, "The predicate in loop must be of bool type");
	        }
	    }
		if(expr instanceof AST.cond)
		{
	        if(expression_(program,((AST.cond)expr).predicate,cls).type == BOOL)
	            expr.type = joinTypes(program, expression_(program,((AST.cond)expr).ifbody,cls).type, expression_(program,((AST.cond)expr).elsebody,cls).type);
	        else
			{
	            expr.type = OBJECT;
	            reportError(cls.filename, expr.lineNo, "The predicate in cond must be bool type");
	        }
	    }
		if(expr instanceof AST.let)
		{
	    	s.enterScope();
	    	String T;
	    	if(((AST.let)expr).name.equals("SELF_TYPE"))
	        	T=cls.name;
	    	else
	        	T=expression_(program,((AST.let)expr).value,cls).type;
	    	if(expression_(program,((AST.let)expr).value,cls).type.equals("_no_type"))
	    	{
	        	s.insert(((AST.let)expr).name,((AST.let)expr).typeid);
	        	expr.type=expression_(program,((AST.let)expr).body,cls).type;
	    	}
	    	else if(implies(program,expression_(program,((AST.let)expr).body,cls).type,T))
	    	{
	            s.insert(((AST.let)expr).name,((AST.let)expr).typeid);
	        	expr.type=expression_(program,((AST.let)expr).body,cls).type;
	    	}
	    	else
	    	{
	        	expr.type=OBJECT;
	        	reportError(cls.filename, expr.lineNo, "Type mismatch in let ");
	    	}
	    	s.exitScope();
	    }
		if((expr instanceof AST.typcase)==true)
		{
	    	s.enterScope();
	 		i=0;
	    	String x =expression_(program,((AST.typcase)expr).branches.get(0).value,cls).type ;
	    	s.insert(((AST.typcase)expr).branches.get(0).name,((AST.typcase)expr).branches.get(0).type);
	    	while(i<=((AST.typcase)expr).branches.size()-2)
			{
	            s.insert(((AST.typcase)expr).branches.get(i+1).name,((AST.typcase)expr).branches.get(i+1).type);
				x= joinTypes(program,x,expression_(program,((AST.typcase)expr).branches.get(i+1).value,cls).type);
	        	i++;
	    	}
	    s.exitScope();
		}

		return expr;
	}

	public class CycleCheck					// Checking whether cycles are present or not
	{
	    private Stack<Integer> stack;
	    private int adjmat1[][];
	    public CycleCheck()
	    {
	        stack=new Stack<Integer>();
	    }
	    public int dfs(int adjmat[][], int src)
		{
	        int nodes=adjmat[src].length - 1;
	        adjmat1=new int[nodes + 1][nodes + 1];
	        for (int i=1;i<=nodes;i++)
	        {
	            for (int j=1;j<=nodes;j++)
	            {
	                adjmat1[i][j]=adjmat[i][j];
	            }
	        }
	        int read[]=new int[nodes + 1];
	        int elem=src;
	        int end=src;
	        read[src]=1;
	        stack.push(src);
	        while (!stack.isEmpty())
	        {
	        	elem=stack.peek();
				end=elem;
				while (end<=nodes)
			    {
		            if (adjmat1[elem][end]==1 && read[end]==1 && stack.contains(end))
		            {
						return end;
		            }
		          	if (adjmat1[elem][end]==1 && read[end]==0)
		        	{
		                stack.push(end);
		                read[end]=1;
		                adjmat1[elem][end]=0;
		                elem=end;
		                end=1;
		    	        continue;
		            }
		            end++;
		    	}
	        stack.pop();
	        }
	        return 0;
	    }
	}
}
