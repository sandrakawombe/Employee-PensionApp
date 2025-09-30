package edu.miu.cs.cs489.lab2a;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class EmployeePensionApp {

    public static void main(String[] args) {
        List<Employee> employees = loadData();

        // Feature 1: Print all employees (JSON-like)
        printAllEmployees(employees);

        // Feature 2: Quarterly Upcoming Enrollees report
        printQuarterlyUpcomingEnrollees(employees);
    }

    private static List<Employee> loadData() {
        return Arrays.asList(
                new Employee(1, "Daniel", "Benard", LocalDate.parse("2018-01-17"), 105945.50,
                        new PensionPlan("EX1089", LocalDate.parse("2023-01-17"), 0)),
                new Employee(2, "Shaw", "Agar", LocalDate.parse("2022-09-03"), 197750.00,
                        new PensionPlan("EX2090", LocalDate.parse("2025-09-03"), 100.00)),
                new Employee(3, "Carly", "Agar", LocalDate.parse("2014-05-16"), 842000.75,
                        new PensionPlan("SM2307", LocalDate.parse("2017-05-17"), 1555.50)),
                new Employee(4, "Wesley", "Schneider", LocalDate.parse("2023-07-21"), 74500.00, null),
                new Employee(5, "Anna", "Wiltord", LocalDate.parse("2023-03-15"), 85750.00, null),
                new Employee(6, "Yosef", "Tesfalem", LocalDate.parse("2024-10-31"), 100000.00, null)
        );
    }

    private static void printAllEmployees(List<Employee> employees) {
        System.out.println("=== All Employees in JSON ===");
        employees.stream()
                .sorted(Comparator.comparing(Employee::getYearlySalary).reversed()
                        .thenComparing(Employee::getLastName))
                .forEach(emp -> {
                    System.out.println("{");
                    System.out.println("  \"employeeId\": " + emp.getEmployeeId() + ",");
                    System.out.println("  \"firstName\": \"" + emp.getFirstName() + "\",");
                    System.out.println("  \"lastName\": \"" + emp.getLastName() + "\",");
                    System.out.println("  \"employmentDate\": \"" + emp.getEmploymentDate() + "\",");
                    System.out.println("  \"yearlySalary\": " + emp.getYearlySalary() + ",");
                    if (emp.getPensionPlan() != null) {
                        System.out.println("  \"pensionPlan\": { \"planReferenceNumber\": \"" +
                                emp.getPensionPlan().getPlanReferenceNumber() + "\", " +
                                "\"enrollmentDate\": \"" + emp.getPensionPlan().getEnrollmentDate() + "\", " +
                                "\"monthlyContribution\": " + emp.getPensionPlan().getMonthlyContribution() + " }");
                    } else {
                        System.out.println("  \"pensionPlan\": null");
                    }
                    System.out.println("}");
                });
    }

    private static void printQuarterlyUpcomingEnrollees(List<Employee> employees) {
        LocalDate now = LocalDate.now();
        int currentQuarter = (now.getMonthValue() - 1) / 3 + 1;
        int nextQuarter = currentQuarter == 4 ? 1 : currentQuarter + 1;
        int year = currentQuarter == 4 ? now.getYear() + 1 : now.getYear();

        Month startMonth = Month.of((nextQuarter - 1) * 3 + 1);
        Month endMonth = Month.of((nextQuarter - 1) * 3 + 3);
        LocalDate startDate = LocalDate.of(year, startMonth, 1);
        LocalDate endDate = LocalDate.of(year, endMonth, endMonth.length(false));

        System.out.println("\n=== Quarterly Upcoming Enrollees Report (JSON) ===");
        employees.stream()
                .filter(e -> e.getPensionPlan() == null) // no pension yet
                .filter(e -> {
                    LocalDate eligibleDate = e.getEmploymentDate().plusYears(3);
                    return (eligibleDate.isEqual(startDate) || eligibleDate.isEqual(endDate)
                            || (eligibleDate.isAfter(startDate) && eligibleDate.isBefore(endDate)));
                })
                .sorted(Comparator.comparing(Employee::getEmploymentDate).reversed())
                .forEach(e -> {
                    System.out.println("{");
                    System.out.println("  \"employeeId\": " + e.getEmployeeId() + ",");
                    System.out.println("  \"firstName\": \"" + e.getFirstName() + "\",");
                    System.out.println("  \"lastName\": \"" + e.getLastName() + "\",");
                    System.out.println("  \"employmentDate\": \"" + e.getEmploymentDate() + "\",");
                    System.out.println("  \"yearlySalary\": " + e.getYearlySalary());
                    System.out.println("}");
                });
    }

}
