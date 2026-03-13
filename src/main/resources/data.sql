-- Initial Data for RevWorkforce
-- Idempotent SQL for H2 (uses MERGE INTO to avoid PK/Unique violations on restart)

-- Departments
MERGE INTO department (department_name) KEY (department_name) VALUES ('IT');
MERGE INTO department (department_name) KEY (department_name) VALUES ('HR');
MERGE INTO department (department_name) KEY (department_name) VALUES ('Finance');
MERGE INTO department (department_name) KEY (department_name) VALUES ('Marketing');
MERGE INTO department (department_name) KEY (department_name) VALUES ('Operations');

-- Designations
MERGE INTO designation (designation_name) KEY (designation_name) VALUES ('System Administrator');
MERGE INTO designation (designation_name) KEY (designation_name) VALUES ('IT Manager');
MERGE INTO designation (designation_name) KEY (designation_name) VALUES ('Java Developer');
MERGE INTO designation (designation_name) KEY (designation_name) VALUES ('HR Specialist');
MERGE INTO designation (designation_name) KEY (designation_name) VALUES ('Financial Analyst');

-- Users
-- Admin (password: admin123)
MERGE INTO users (email, password, role, is_active) KEY (email)
VALUES ('admin@rev.com', '$2a$10$aXrK7pBzrYFUHHeGSkV6/en2c2YzJ4saIe7rLnXm6M83Ugu/w1Ttu', 'ROLE_ADMIN', 1);
-- Manager (password: manager123)
MERGE INTO users (email, password, role, is_active) KEY (email)
VALUES ('manager@rev.com', '$2a$10$wE6TuNF37r4SZX/.AB8DYuEj6cLp3sVDzThcd5OJb6cj1nve/P5O2', 'ROLE_MANAGER', 1);
-- Employee (password: password)
MERGE INTO users (email, password, role, is_active) KEY (email)
VALUES ('employee@rev.com', '$2a$12$ejziqZALRVLSAK8h.Y.XKu7J5Pb9CYHR/iiCPnfDOBOJqaxrCSx/6', 'ROLE_EMPLOYEE', 1);
-- System User (password: 2454)
MERGE INTO users (email, password, role, is_active) KEY (email)
VALUES ('system@rev.com', '$2a$12$iZBWZKrAAEY3wyj7uSgur.4PpRJsMwFB14NZ0XTUnJq0eM3OyMv4C', 'ROLE_EMPLOYEE', 1);

-- Employees
-- RW001: Admin
MERGE INTO employee (emp_id, user_id, first_name, last_name, joining_date, department_id, designation_id, salary) 
KEY (emp_id)
VALUES ('RW001', (SELECT user_id FROM users WHERE email='admin@rev.com'), 'Harsha', 'Munjala', CURRENT_DATE, 
        (SELECT department_id FROM department WHERE department_name='IT'), 
        (SELECT designation_id FROM designation WHERE designation_name='System Administrator'), 100000.00);

-- RW002: Manager
MERGE INTO employee (emp_id, user_id, first_name, last_name, joining_date, department_id, designation_id, manager_id, salary) 
KEY (emp_id)
VALUES ('RW002', (SELECT user_id FROM users WHERE email='manager@rev.com'), 'John', 'Smith', CURRENT_DATE, 
        (SELECT department_id FROM department WHERE department_name='IT'), 
        (SELECT designation_id FROM designation WHERE designation_name='IT Manager'), 'RW001', 85000.00);

-- RW003: Employee
MERGE INTO employee (emp_id, user_id, first_name, last_name, joining_date, department_id, designation_id, manager_id, salary) 
KEY (emp_id)
VALUES ('RW003', (SELECT user_id FROM users WHERE email='employee@rev.com'), 'Jack', 'Wilson', CURRENT_DATE, 
        (SELECT department_id FROM department WHERE department_name='IT'), 
        (SELECT designation_id FROM designation WHERE designation_name='Java Developer'), 'RW002', 60000.00);

-- RW004: System Employee
MERGE INTO employee (emp_id, user_id, first_name, last_name, joining_date, department_id, designation_id, manager_id, salary) 
KEY (emp_id)
VALUES ('RW004', (SELECT user_id FROM users WHERE email='system@rev.com'), 'System', 'User', CURRENT_DATE, 
        (SELECT department_id FROM department WHERE department_name='IT'), 
        (SELECT designation_id FROM designation WHERE designation_name='Java Developer'), 'RW002', 50000.00);

-- Leave Types
MERGE INTO leave_type (leave_name, max_per_year) KEY (leave_name) VALUES ('Sick Leave', 12);
MERGE INTO leave_type (leave_name, max_per_year) KEY (leave_name) VALUES ('Casual Leave', 12);
MERGE INTO leave_type (leave_name, max_per_year) KEY (leave_name) VALUES ('Privilege Leave', 15);

-- Auto-assign managers (Final verification)
UPDATE employee SET manager_id = 'RW002'
    WHERE manager_id IS NULL
    AND emp_id != 'RW001'
    AND emp_id != 'RW002'
    AND emp_id IN (SELECT e.emp_id FROM employee e JOIN users u ON e.user_id = u.user_id WHERE u.role = 'ROLE_EMPLOYEE');

UPDATE employee SET manager_id = 'RW001'
    WHERE manager_id IS NULL
    AND emp_id != 'RW001'
    AND emp_id IN (SELECT e.emp_id FROM employee e JOIN users u ON e.user_id = u.user_id WHERE u.role = 'ROLE_MANAGER');
