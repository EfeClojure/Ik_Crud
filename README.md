
A friend Ikechukwu Ezugworie started writing
a version of this CRUD(hence the name ik_crud) 
program in python. He was having some trouble with 
some parts of it so I decided to help by showing him 
how I would do it in Clojure. On a whim I ended up writing 
the entire program in Clojure. I feel some may learn from 
my experience because as I later found out - the spec may seem
easy but the implementation may not be. 


The spec for the program is in the PROGRAM_SPEC.txt file.
The spec is more like a pseudo code. It's kinda long so I had to 
put it in a separate file.

To run the program. Run the following from your command line


lein deps

That's for getting the following dependencies
[jline "2.11"]
[org.clojure/core.match "0.3.0-alpha4"]



lein trampoline repl

That's for making it possible to get input from the user
and also have the repl be responsive to the user input.


