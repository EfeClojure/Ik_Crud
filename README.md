
A friend Ikechukwu Ezugworie started writing
a version of this CRUD(hence the name ik_crud) 
program in python. He was having some trouble with 
some parts of it so I decided to help by showing him 
how I would do it in Clojure. I ended up writing the entire 
program in Clojure on a whim. I feel some may learn 
from my experience because as I later found out - the spec may seem
easy but the implementation may not be. 


The spec for the program is below:

CMD-CRUD using basic python DS and functions
	Features:
		Show Records
			Pretty print all existing records in
			the agreed format
		Add Record
			Request inputs (first, last, role)
			Test Validity of inputs
			Check if input already exists
				Print message(Record already exist)
				Reload program
			Add New Record
				Print message
				Show All Records
				reload_app
		Edit Record
			Request inputs (first, last)
			Test Validity of inputs
			check if inputs exists in a record
				print_record
				ask for input replacements
				test validity of input replacements
				insert input replacements
				print success
				print_records
				reload_app // can't figure out how to reload app
		Find Record
			display_sub_menu = {
				'f' : 'First Name'
				'l' : 'Last Name'
				'r' : 'Role'
				'v' : 'Full Name'
			}
			request menu value
			test if input is valid
				request input
					test if input[s] is/are valid
						check if input exists
						print record[s]
				Print input doesn't exist
		Delete Record
			request inputs (first, last)
			test validity of input
			check existence of input
				Delete record
				print success message
				Show all class mates
				reload_app
			Print Class mate doesn't exist
				reload_app
		Exit:
			return sys.exit(0)


	Main Program order
		Display main menu
		Ask for input (menu option)
		Test input validity with respect to main menu
		Execute selected path way

