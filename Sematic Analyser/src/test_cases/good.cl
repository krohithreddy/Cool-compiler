(*
The following code swaps the given 2 numbers.

*)
class A inherits IO {
	x : Int ;
	y : Int ;
	init() : A {
		{
			x <- 2;
			y <- 3;
			--self;
		}
	};
	swap() : A {      ---swapping of numbers.
		(let p : Int in 
		{
			p <- x;
			x <- y;
			y <- p;
			--self;
		})
	};
	print() : A {
		{
		out_string("The value of a is :- ");
		out_int(x);
		out_string("\nThe value of b is :- ");
		out_int(y);
		}
	};
};

class Main inherits A {
	main() : Object {
		{
			out_string("Enter the values of 'a' and 'b' :- \n");
			(new Main).init().swap().print();
			out_string("\n");
		}
	};
};