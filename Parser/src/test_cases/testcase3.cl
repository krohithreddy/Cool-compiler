class A inherits IO {
	r : Int ;
	init() : A {
		{
			r <- in_int();
			self;
		}
	};
	area() : SELF_TYPE {
		(let x : Int in
		{
			out_string("Surface area of the cube :- ");
			x <- 6 * r * r;
			out_int(x);
			out_string("\n");
		}
			)

	};
	volume() : SELF_TYPE {
		(let y : Int in
		{
			out_string("volume of the cube :- ");
			y <- r * r *;                                -- i missed a multiplication symbol bitwwen which will return an error 
			out_int(y);
			out_string("\n");
		}

			)
	};

};
class Main inherits A {
	main() : Object {
		{
			out_string("Enter the side length of the cube.\n");
			(new A).init().area().volume();
		}
	};
};