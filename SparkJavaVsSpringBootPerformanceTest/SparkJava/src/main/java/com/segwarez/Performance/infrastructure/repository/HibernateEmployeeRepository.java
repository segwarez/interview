package com.segwarez.Performance.infrastructure.repository;

import com.segwarez.Performance.infrastructure.repository.entity.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class HibernateEmployeeRepository implements EmployeeRepository {
    private static SessionFactory factory = new Configuration().configure().buildSessionFactory();

    @Override
    public Employee findById(Long id) {
        Session session = factory.openSession();
        Query query = session.createQuery("FROM Employee where id = :id");
        query.setParameter("id", id);
        List<Employee> employees = query.getResultList();
        session.close();

        if (employees == null || employees.isEmpty()) {
            return null;
        }
        return employees.get(0);
    }

    @Override
    public List<Employee> findAll() {
        Session session = factory.openSession();
        List<Employee> employees = session.createQuery("FROM Employee").getResultList();
        session.close();
        return employees;
    }

    @Override
    public Employee save(Employee employee) {
        Session session = factory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(employee);
        session.getTransaction().commit();
        session.close();
        return employee;
    }
}
