/*
*			A.Rohan (ES14BTECH11001)
*			K.Rohith Reddy (CS14BTECH11018)
*
*/




package cool;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Map.Entry;

public class Codegen{
	ArrayList < ArrayList <Integer> > classGraph;
	HashMap <Integer, String> idxName;
	HashMap <String, AST.class_> idxCont;
	int strCount = 0;
	public String name;
	public String parent = null;
	public HashMap <String, AST.attr> alist;
	public HashMap <String, AST.method> mlist;
	public HashMap <String, Integer> attrOffset;
	public HashMap <String, Integer> methodOffset;
	public HashMap <String, String> IRname;
	public ArrayList <AST.method> methodList;
	public ArrayList <AST.attr> attrList;
	public void IRClassPlus(String nm, String pr, HashMap<String, AST.attr> al, HashMap<String, AST.method> ml, HashMap<String, Integer> ao, HashMap <String, Integer> mo, ArrayList <AST.attr> pa, ArrayList <AST.method> pm, HashMap <String, String> irn) {
			name = new String(nm);
			if(pr != null) parent = new String(pr);
			alist = new HashMap <String, AST.attr>();
			alist.putAll(al);
			mlist = new HashMap <String, AST.method>();
			mlist.putAll(ml);
			attrOffset = new HashMap <String, Integer>();
			attrOffset.putAll(ao);
			methodOffset = new HashMap <String, Integer>();
			methodOffset.putAll(mo);
			attrList = new ArrayList <AST.attr>();
			attrList.addAll(pa);
			methodList = new ArrayList <AST.method>();
			methodList.addAll(pm);
			IRname = new HashMap <String, String>();
			IRname.putAll(irn);
	}

	public Codegen(AST.program program, PrintWriter out){
        CodegenInit(out);
        ProcessGraph(program.classes, out);
        AST.class_ classtemp = idxCont.get("Main");
        List<AST.feature> ftrs = new ArrayList<AST.feature>();
        ftrs = classtemp.features;
        for(int i = 0; i < ftrs.size(); ++i)
        {
        	AST.feature feat = new AST.feature();
        	feat = ftrs.get(i);
        	if(feat.getClass() == AST.attr.class)
        	{
        		AST.expression expr = new AST.expression();
        		AST.attr temp = (AST.attr)feat;
        		expr = temp.value;
        		ProcessStr(expr,out);
        	}
        	else if(feat.getClass() == AST.method.class)
        	{
        		AST.expression expr = new AST.expression();
        		AST.method temp = (AST.method)feat;
        		expr = temp.body;
        		ProcessStr(expr,out);
        	}
        }
        for(AST.class_ e : program.classes)
        {
					if(!(e.name.equals("Main")))
					{
			    		ftrs = classtemp.features;
							for(int i = 0; i < ftrs.size(); ++i)
				      {
				      	AST.feature feat = new AST.feature();
				      	feat = ftrs.get(i);
				      	if(feat.getClass() == AST.attr.class)
			        	{
			        		AST.expression expr = new AST.expression();
			        		AST.attr temp = (AST.attr)feat;
			        		expr = temp.value;
			        		ProcessStr(expr,out);
				        }
				      	else if(feat.getClass() == AST.method.class)
				      	{
				      		AST.expression expr = new AST.expression();
				      		AST.method temp = (AST.method)feat;
			        		expr = temp.body;
			        		ProcessStr(expr,out);
				        }
				      }
					}
				}
	}

	private void printStringConst(AST.expression expr_str, PrintWriter out)
	{
		AST.string_const str_obj = (AST.string_const)expr_str;
  		if(strCount==0){
  			out.println("@.str = private unnamed_addr constant ["+ (str_obj.value.length()+1) + " x i8] c\""+str_obj.value+"\\00\", align 1");
  		}
  		else{
  			out.println("@.str."+ strCount+ " = private unnamed_addr constant ["+ (str_obj.value.length()+1) + " x i8] c\""+str_obj.value+"\\00\", align 1");
  		}
  		strCount++;
	}

	private void ProcessStr(AST.expression expr_str, PrintWriter out)
	{
		if(expr_str.getClass() == AST.assign.class)						// assign
		{
			AST.assign str = (AST.assign)expr_str;
			AST.expression exp = str.e1;
			ProcessStr(exp,out);
		}
		else if(expr_str.getClass() == AST.cond.class)				// if-then-else
		{
			AST.cond str = (AST.cond)expr_str;
			AST.expression e1 = str.predicate;
			AST.expression e2 = str.ifbody;
			AST.expression e3 = str.elsebody;
			if(e1.getClass() == AST.string_const.class)
			{
				printStringConst(e1, out);
				return;
			}
			else
				ProcessStr(e1,out);
			if(e2.getClass() == AST.string_const.class)
			{
				printStringConst(e2, out);
				return;
			}
			else
				ProcessStr(e2,out);
			if(e3.getClass() == AST.string_const.class)
			{
				printStringConst(e3, out);
				return;
			}
			else
				ProcessStr(e3,out);
		}
		else if(expr_str.getClass() == AST.let.class)						// let
		{
			AST.let str = (AST.let)expr_str;
			AST.expression e1 = str.value;

			if(e1.getClass() == AST.string_const.class)
			{
				printStringConst(str.value,out);
				return;
			}
			else
				ProcessStr(e1,out);

			AST.expression e2 = str.body;
			if(e2.getClass() == AST.string_const.class)
			{
				printStringConst(e2,out);
				return;
			}
			else
				ProcessStr(e2,out);
		}
		else if(expr_str.getClass() == AST.loop.class)						// while loop
		{
			AST.loop str = (AST.loop)expr_str;
			AST.expression e1 = str.predicate;
			AST.expression e2 = str.body;
			if(e1.getClass() == AST.string_const.class)
        	{
        		AST.string_const str2 = (AST.string_const)e1;
        		printStringConst(str2,out);
        		return;
        	}
        	else
				ProcessStr(e1,out);
			if(e2.getClass() == AST.string_const.class)
        	{
        		AST.string_const str3 = (AST.string_const)e2;
        		printStringConst(str3,out);
        		return;
        	}
        	else
				ProcessStr(e2,out);
		}
		else if(expr_str.getClass() == AST.string_const.class)					// string constant
		{
			AST.string_const str = (AST.string_const)expr_str;
			printStringConst(str,out);
		}
	}
	static final String DATA_LAYOUT = "target datalayout = \"e-m:e-i64:64-f80:128-n8:16:32:64-S128\"";
	static final String TARGET_TRIPLE = "target triple = \"x86_64-unknown-macos-gnu\"";
	static final String CMETHOD_HELPERS = "@strformatstr = private unnamed_addr constant [3 x i8] c\"%s\\00\", align 1\n"
			+ "@intformatstr = private unnamed_addr constant [3 x i8] c\"%d\\00\", align 1\n";
	static final String ERRORS = "@Abortdivby0 = private unnamed_addr constant [22 x i8] c\"Error: Division by 0\\0A\\00\", align 1\n"
			+ "@Abortdispvoid = private unnamed_addr constant [25 x i8] c\"Error: Dispatch on void\\0A\\00\", align 1\n";
	static final String CMETHODS = "declare i32 @printf(i8*, ...)\n"
			+ "declare i32 @scanf(i8*, ...)\n"
			+ "declare i32 @strcmp(i8*, i8*)\n"
			+ "declare i8* @strcat(i8*, i8*)\n"
			+ "declare i8* @strcpy(i8*, i8*)\n"
			+ "declare i8* @strncpy(i8*, i8*, i32)\n"
			+ "declare i64 @strlen(i8*)\n"
			+ "declare i8* @malloc(i64)\n"
			+ "declare void @exit(i32)";
	static final String OBJECT_TYPENAME = "define [1024 x i8]* @_ZN6Object9type_name( %class.Object* %this ) {\n"
			+ "entry:\n"
			+ "%0 = getelementptr inbounds %classObject, %classObject* %this, i32 0, i32 0\n"
			+ "%1 = load i32, i32* %0\n"
			+ "%2 = getelementptr inbounds [8 x [1024 x i8]], [8 x [1024 x i8]]* @classnames, i32 0, i32 %1\n"
			+ "%retval = call [1024 x i8]* @_ZN6String4copy( [1024 x i8]* %2 )\n"
			+ "ret [1024 x i8]* %retval\n"
			+ "}\n";
	static final String IO_OUTSTRING = "define %classIO* @_ZN2IO10out_string( %class.IO* %this, [1024 x i8]* %str ) {\n"
			+ "entry:\n"
			+ "%0 = call i32 (i8*, ...) @printf( i8* bitcast ( [3 x i8]* @strformatstr to i8* ), [1024 x i8]* %str )\n"
			+ "ret %classIO* %this\n"
			+ "}\n";
	static final String IO_OUTINT = "define %classIO* @_ZN2IO7out_int( %class.IO* %this, i32 %int ) {\n"
			+ "entry:\n"
			+ "%0 = call i32 (i8*, ...) @printf( i8* bitcast ( [3 x i8]* @intformatstr to i8* ), i32 %int )\n"
			+ "ret %classIO* %this\n"
			+ "}\n";
	static final String IO_INSTRING = "define [1024 x i8]* @_ZN2IO9in_string( %class.IO* %this ) {\n"
			+ "entry:\n"
			+ "%0 = call i8* @malloc( i64 1024 )\n"
			+ "%retval = bitcast i8* %0 to [1024 x i8]*\n"
			+ "%1 = call i32 (i8*, ...) @scanf( i8* bitcast ( [3 x i8]* @strformatstr to i8* ), [1024 x i8]* %retval )\n"
			+ "ret [1024 x i8]* %retval\n"
			+ "}\n";
	static final String IO_ININT = "define i32 @_ZN2IO9in_int( %class.IO* %this ) {\n"
			+ "entry:\n"
			+ "%0 = call i8* @malloc( i64 4 )\n"
			+ "%1 = bitcast i8* %0 to i32*\n"
			+ "%2 = call i32 (i8*, ...) @scanf( i8* bitcast ( [3 x i8]* @intformatstr to i8* ), i32* %1 )\n"
			+ "%retval = load i32, i32* %1\n"
			+ "ret i32 %retval\n"
			+ "}\n";
	static final String OBJECT_BASE = "define void @_Z6ObjectBaseC ( %class.Object.Base ) {\n"
			+ "entry:\n"
			+ "ret void\n"
			+ "}\n";
	private void CodegenInit(PrintWriter out) {
		out.println(DATA_LAYOUT);
		out.println(TARGET_TRIPLE);
		out.println(ERRORS);
		out.println(CMETHODS);
		out.println(CMETHOD_HELPERS);
	}

	private String baseClassName(String className) {
		return "class." + className + ".Base";
	}

	private void EmitBaseConstructor(IRClassPlus irclass, PrintWriter out) {
		out.print("define void @_Z" + irclass.name.length() + irclass.name + "Base" + "C");
		out.print(" ( %" + irclass.name + ".Base*" + "%this" + " ) { \n");
		out.println("entry: ");


		int ptr = 0;
		for(int i = 0; i < irclass.attrList.size(); ++i) {
			AST.attr at = irclass.attrList.get(i);
			out.println("%" + ptr + " = getelementptr inbouds %" + baseClassName(at.typeid) + ", " + baseClassName(at.typeid) + "* %this, i32 0, i32 " + i);
			out.println("call void " + "@_Z" + at.typeid.length() + at.typeid + "Base"+ "C" + "( %" + baseClassName(at.typeid) + "*" + "%" + i);
			out.println("return void");
		}
	}

	private void ProcessGraph(List <AST.class_> classes, PrintWriter out) {
		Integer classno = 0;
		idxCont = new HashMap <String, AST.class_> ();
		HashMap <String, Integer> classIdx = new HashMap <String, Integer> ();
		idxName = new HashMap <Integer, String>();
		classGraph = new ArrayList < ArrayList <Integer> >();
		classIdx.put("Object", 0);										//Fundamentals
		idxName.put(0, "Object");
		classIdx.put("IO", 1);
		idxName.put(0, "IO");

		classGraph.add(new ArrayList <Integer> (Arrays.asList(1)));
		classGraph.add(new ArrayList <Integer>());
		idxName.put(0, "Object");
		idxName.put(1, "IO");
		classno = classno + 2;															// IO and Object are other 2
		for(AST.class_ e : classes) {
			idxName.put(classno, e.name);
			classIdx.put(e.name, classno++);
			idxCont.put(e.name, e);
			classGraph.add(new ArrayList <Integer> ());
		}
	}
}
