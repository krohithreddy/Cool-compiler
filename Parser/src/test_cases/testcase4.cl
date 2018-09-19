class List inherits IO{
   -- Define operations on empty lists.

   isNil() : Bool { true };

   head()  : String { { abort(); ""; } };

   tail()  : List { { abort(); self; } };

   push(i : String) : List {
      (new Cons).init(i, self)
   };

};

class Cons inherits List {

   car : String;	-- The element in this list cell

   cdr : List;	-- The rest of the list

   isNil() : Bool { false };

   head()  : String { car };

   tail()  : List { cdr };

   init(i : String, rest : List) : List {
      {
	 car <- i;
	 cdr <- rest;
	 self;
      }
   };

};
class A2I {

     c2i(char : String) : Int {
	if char = "0" then 0 else
	if char = "1" then 1 else
	if char = "2" then 2 else
        if char = "3" then 3 else
        if char = "4" then 4 else
        if char = "5" then 5 else
        if char = "6" then 6 else
        if char = "7" then 7 else
        if char = "8" then 8 else
        if char = "9" then 9 else
        { abort(); 0; }  -- the 0 is needed to satisfy the typchecker
        fi fi fi fi fi fi fi fi fi fi
     };

(*
   i2c is the inverse of c2i.
*)
     i2c(i : Int) : String {
	if i = 0 then "0" else
	if i = 1 then "1" else
	if i = 2 then "2" else
	if i = 3 then "3" else
	if i = 4 then "4" else
	if i = 5 then "5" else
	if i = 6 then "6" else
	if i = 7 then "7" else
	if i = 8 then "8" else
	if i = 9 then "9" else
	{ abort(); ""; }  -- the "" is needed to satisfy the typchecker
        fi fi fi fi fi fi fi fi fi fi
     };

(*
   a2i converts an ASCII string into an integer.  The empty string
is converted to 0.  Signed and unsigned strings are handled.  The
method aborts if the string does not represent an integer.  Very
long strings of digits produce strange answers because of arithmetic 
overflow.

*)
     a2i(s : String) : Int {
        if s.length() = 0 then 0 else
	if s.substr(0,1) = "-" then ~a2i_aux(s.substr(1,s.length()-1)) else
        if s.substr(0,1) = "+" then a2i_aux(s.substr(1,s.length()-1)) else
           a2i_aux(s)
        fi fi fi
     };

(*
  a2i_aux converts the usigned portion of the string.  As a programming
example, this method is written iteratively.
*)
     a2i_aux(s : String) : Int {
	(let int : Int <- 0 in	
           {	
               (let j : Int <- s.length() in
	          (let i : Int <- 0 in
		    while i < j loop
			{
			    int <- int * 10 + c2i(s.substr(i,1));
			    i <- i + 1;
			}
		    pool
		  )
	       );
              int;
	    }
        )
     };

(*
    i2a converts an integer to a string.  Positive and negative 
numbers are handled correctly.  
*)
    i2a(i : Int) : String {
	if i = 0 then "0" else 
        if 0 < i then i2a_aux(i) else
          "-".concat(i2a_aux(i * ~1)) 
        fi fi
    };
	
(*
    i2a_aux is an example using recursion.
*)		
    i2a_aux(i : Int) : String {
        if i = 0 then "" else 
	    (let next : Int <- i / 10 in
		i2a_aux(next).concat(i2c(i - next * 10))
	    )
        fi
    };

};



class main inherits Cons{
   mylist:List;
   input :String;
   a:Int;
   b:Int;
   c:Int;
   d:String;
   e:String;
   terminate:Int<-0;
                        --the program will terminate when termiante is equal to one
     print_list(l : List) : Object {
      if l.isNil() then out_string("")
                   else {
			   out_string(l.head());
			   out_string("\n");
			   print_list(l.tail());
		        }
      fi
   };
     main(): Object{
      {
      	 mylist<-new List;
         out_string("enter the input\n");
         while terminate=0 
         loop
         {
         	
             out_string(">");
             input<-in_string(); --taking the input in input
          if input="+"
             then mylist<-mylist.push(input)  --just take the input and push into the list
          else if input="s"
              then  mylist<-mylist.push(input)
          else if input="d"
              --print the contents of the list
              then    print_list(mylist)
          else if input="e"
               then {                               --we have to excute the depending upon on the head element
                     if mylist.head()="+"
                     then {
                           mylist<-mylist.tail(); 
                           a<-(new A2I).a2i_aux(mylist.head());          --I have to write convert
                           mylist<-mylist.tail();
                           b<-(new A2I).a2i_aux(mylist.head());
                           mylist<-mylist.tail();
                           c<-a+b;
                           mylist<-mylist.push((new A2I).i2a_aux(c));
                          }
                     else if mylist.head()="s"
                       then {
                            mylist<-mylist.tail();    
                            d<-mylist.head();
                            mylist<-mylist.tail();
                            e<-mylist.head();
                            mylist<-mylist.tail();
                            mylist<-mylist.push(d);
                            mylist<-mylist.push(e);
                            }
                      else 
                            mylist<-mylist
                        fi fi;
               }
          else if input="x"
              then    terminate<-1
          else
               mylist<-mylist.push(input)  -- now we got the integer 
               --now there is no need to convert the string into the integer just push the string into the list
               fi fi fi fi fi;
        }
         pool;
        
      }
   };
}; 