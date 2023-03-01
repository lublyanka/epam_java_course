package com.epam.rd.tasks.sqlqueries;

/**
 * Implement sql queries like described
 */
public class SqlQueries {
    //Select all employees sorted by last name in ascending order
    //language=HSQLDB
    String select01 = "SELECT * FROM employee ORDER BY lastname ASC";

    //Select employees having no more than 5 characters in last name sorted by last name in ascending order
    //language=HSQLDB
    String select02 = "SELECT * FROM employee WHERE LENGTH(lastname) <= 5 ORDER BY lastname ASC";

    //Select employees having salary no less than 2000 and no more than 3000
    //language=HSQLDB
    String select03 = "SELECT * FROM employee WHERE salary >= '2000' AND SALARY <= '3000'";

    //Select employees having salary no more than 2000 or no less than 3000
    //language=HSQLDB
    String select04 = "SELECT * FROM employee WHERE salary <= '2000' OR salary >= '3000'";

    //Select all employees assigned to departments and corresponding department
    //language=HSQLDB
    String select05 = "SELECT * FROM employee e JOIN department d ON e.department=d.ID";

    //Select all employees and corresponding department name if there is one.
    //Name column containing name of the department "depname".
    //language=HSQLDB
    String select06 = "SELECT e.id, " +
            "e.firstname, " +
            "e.lastname, " +
            "e.middlename, " +
            "e.position, " +
            "e.manager, " +
            "e.hiredate, " +
            "e.salary, " +
            "e.department, " +
            "d.name AS depname " +
            "FROM employee AS e LEFT JOIN DEPARTMENT AS d ON e.DEPARTMENT=d.ID";

    //Select total salary pf all employees. Name it "total".
    //language=HSQLDB
    String select07 = "SELECT SUM(SALARY) as total FROM EMPLOYEE";

    //Select all departments and amount of employees assigned per department
    //Name column containing name of the department "depname".
    //Name column containing employee amount "staff_size".
    //language=HSQLDB
    String select08 = "SELECT d.id, " +
            "d.name as depname, " +
            "d.location, " +
            "COUNT(e.id) AS staff_size " +
            "FROM DEPARTMENT AS d JOIN EMPLOYEE AS e ON d.id = e.department " +
            "GROUP BY d.id";

    //Select all departments and values of total and average salary per department
    //Name column containing name of the department "depname".
    //language=HSQLDB
    String select09 = "SELECT d.id, " +
            "d.name as depname, " +
            "d.location, " +
            "SUM(e.salary) AS total, " +
            "AVG(e.salary) AS average " +
            "FROM DEPARTMENT AS d JOIN EMPLOYEE AS e ON d.id = e.department " +
            "GROUP BY d.id";

    //Select lastnames of all employees and lastnames of their managers if an employee has a manager.
    //Name column containing employee's lastname "employee".
    //Name column containing manager's lastname "manager".
    //language=HSQLDB
    String select10 = "SELECT lastname AS e1.employee, " +
            "(SELECT e2.lastname " +
            "FROM employee e2 " +
            "WHERE e2.id = e1.manager) AS manager " +
            "FROM employee e1" ;


}
