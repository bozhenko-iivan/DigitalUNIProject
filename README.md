A console-based information system for managing university entities (Students, Teachers, Faculties, and Departments). Developed for the Advanced Java course at NaUKMA.

Our system has following hierarchy:

<pre>
  
								University
								    ||
								   /  \
								  /    \
					 ...   Faculty		Faculty  ...
			 _____________/  / \
		   /  				/   \
		  /   ...  Departament  Departament ...
	   Groups   		|
		 |			 Teachers
	  Students
  
</pre>

Система дозволяє керувати і редагувати користувачів, студентів, вчителів та складових університету: кафедри, факультети та групи. 
Розроблені ролі користувачів, адмін, менеджер та юзер мають різні рівні доступу та захищені бітовими масками. 
На рівні університету доступні повні звіти про: університет, учнів, вчителів. 
Реалізовано додавання факультетів та сортування студентів конкретного факультету за курсами.
Доступний рівень студента з усіма операціями керування, переводу та пошуку. Також реалізовано механізм додавання та редагування оцінок, розрахунок середнього балу.
