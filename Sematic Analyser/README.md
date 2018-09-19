Semantic Analyzer


ES14BTECH11001 & CS14BTECH11018


public semantic consists of Inheritance where the classes are checked for the multiple declarations and if the parent class is not defined .try and catch statement is used . Attributes and Methods are overrided .Naming and scope is done.

expression_ here , all the types of expressions are assigned.

CycleCheck checks wheather the cycles are present or not.

Errors:-
These are the few errors after compilation
fib.cl:-

fib.cl:31: Variable not declared
fib.cl:31: The predicate in loop must be of bool type
fib.cl:39: Variable not declared
fib.cl:28: Type mismatch in let 

testing.cl:-

testing.cl:30: The two operands must be of type int
testing.cl:30: The predicate in cond must be bool type
testing.cl:36: The predicate in cond must be bool type
testing.cl:65: Variable not declared
testing.cl:65: The predicate in loop must be of bool type
testing.cl:78: Variable not declared

casecheck.cl:-

casecheck.cl:31: Variable not declared
casecheck.cl:24: Type mismatch in let 

prob.cl:-

prob.cl:18: The two operands are not of same type
prob.cl:18: The predicate in loop must be of bool type
prob.cl:24: Variable not declared
prob.cl:17: Type mismatch in let 

bad.cl:-

bad.cl:1: Graph contains cycle.
bad.cl:30: The two operands must be of type int
bad.cl:30: The predicate in cond must be bool type


Tree after correct code compilation:-

#5
_program
  #5
  _class
    A
    IO
    "good.cl"
    (
    #6
    _attr
      x
      Int
      #6
      _no_expr
      : _no_type
    #7
    _attr
      y
      Int
      #7
      _no_expr
      : _no_type
    #8
    _method
      init
      A
      #9
      _block
        #10
        _assign
          x
          #10
          _int
            2
          : Int
        : Int
        #11
        _assign
          y
          #11
          _int
            3
          : Int
        : Int
      : Int
    #15
    _method
      swap
      A
      #16
      _let
        p
        Int
        #16
        _no_expr
        : _no_type
        #17
        _block
          #18
          _assign
            p
            #18
            _object
              x
            : Int
          : Int
          #19
          _assign
            x
            #19
            _object
              y
            : Int
          : Int
          #20
          _assign
            y
            #20
            _object
              p
            : Int
          : Int
        : Int
      : Int
    #24
    _method
      print
      A
      #25
      _block
        #26
        _dispatch
          #26
          _object
            self
          : _no_type
          out_string
          (
          #26
          _string
            "The value of a is :- "
          : _no_type
          )
        : _no_type
        #27
        _dispatch
          #27
          _object
            self
          : _no_type
          out_int
          (
          #27
          _object
            x
          : _no_type
          )
        : _no_type
        #28
        _dispatch
          #28
          _object
            self
          : _no_type
          out_string
          (
          #28
          _string
            "\nThe value of b is :- "
          : _no_type
          )
        : _no_type
        #29
        _dispatch
          #29
          _object
            self
          : _no_type
          out_int
          (
          #29
          _object
            y
          : _no_type
          )
        : _no_type
      : _no_type
    )
  #34
  _class
    Main
    A
    "good.cl"
    (
    #35
    _method
      main
      Object
      #36
      _block
        #37
        _dispatch
          #37
          _object
            self
          : _no_type
          out_string
          (
          #37
          _string
            "Enter the values of 'a' and 'b' :- \n"
          : _no_type
          )
        : _no_type
        #38
        _dispatch
          #38
          _dispatch
            #38
            _dispatch
              #38
              _new
                Main
              : _no_type
              init
              (
              )
            : _no_type
            swap
            (
            )
          : _no_type
          print
          (
          )
        : _no_type
        #39
        _dispatch
          #39
          _object
            self
          : _no_type
          out_string
          (
          #39
          _string
            "\n"
          : _no_type
          )
        : _no_type
      : _no_type
    )



